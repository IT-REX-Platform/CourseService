package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import de.uni_stuttgart.it_rex.course.service.written.KeycloakAdminService;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.utils.written.StringUtil;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ChapterResource} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
public class CourseResourceIT {

  private static UUID NON_EXISTING_ID = UUID.randomUUID();

  private static String COURSE_NAME = "MAGIC";
  private static String COURSE_DESCRIPTION;
  private static String UPDATED_NAME = "The coolest name";
  private static int UPDATED_MAX_FOOD_SUM = 9999;
  private static LocalDate OLD_START_DATE = LocalDate.now().minusDays(400);
  private static LocalDate OLD_END_DATE = LocalDate.now().minusDays(200);
  private static LocalDate NEW_END_DATE = LocalDate.now().plusDays(400);

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private TimePeriodRepository timePeriodRepository;

  @Autowired
  private ContentReferenceRepository contentReferenceRepository;

  @Autowired
  private CourseResource courseResource;

  @Autowired
  private MockMvc restCourseMockMvc;

  @Autowired
  private CourseMapper courseMapper;

  @MockBean
  private KeycloakAdminService keycloakAdminService;

  @BeforeAll
  static void setup() throws IOException {
    COURSE_DESCRIPTION = StringUtil.loadLongString();
  }

  @AfterEach
  void cleanUp() {
    contentReferenceRepository.deleteAll();
    chapterRepository.deleteAll();
    timePeriodRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  @Transactional
  public void createCourse() throws Exception {
    int databaseSizeBeforeCreate = courseRepository.findAll().size();
    CourseDTO createdCourseDTO = CourseUtil.createCourseDTO();
    // Create the Course
    restCourseMockMvc.perform(post("/api/courses").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(createdCourseDTO)))
      .andExpect(status().isCreated());

    // Validate the Course in the database
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
    CourseDTO testCourseDTO = courseMapper.toDTO(courseList.get(courseList.size() - 1));
    CourseUtil.equals(createdCourseDTO, testCourseDTO);
  }

  @Test
  @Transactional
  public void createCourseWithExistingId() throws Exception {
    int databaseSizeBeforeCreate = courseRepository.findAll().size();
    Course createdCourse = CourseUtil.createCourse();

    // Create the Course with an existing ID
    createdCourse.setId(UUID.randomUUID());

    // An entity with an existing ID cannot be created, so this API call must fail
    restCourseMockMvc.perform(post("/api/courses").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(createdCourse)))
      .andExpect(status().isBadRequest());

    // Validate the Course in the database
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeCreate);
  }


  @Test
  @Transactional
  public void getAllCourses() throws Exception {
    // Initialize the database
    final Course course = CourseUtil.createCourse();
    course.setName(COURSE_NAME);
    course.setCourseDescription(COURSE_DESCRIPTION);
    courseRepository.saveAndFlush(course);

    // Get all the courseList
    restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(course.getName())))
      .andExpect(jsonPath("$.[*].startDate").value(hasItem(course.getStartDate().toString())))
      .andExpect(jsonPath("$.[*].endDate").value(hasItem(course.getEndDate().toString())))
      .andExpect(jsonPath("$.[*].remainActiveOffset").value(hasItem(course.getRemainActiveOffset())))
      .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(course.getMaxFoodSum())))
      .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(course.getCourseDescription())))
      .andExpect(jsonPath("$.[*].publishState").value(hasItem(course.getPublishState().toString())));
  }

  @Test
  @Transactional
  public void getCourse() throws Exception {
    // Initialize the database
    final Course course = CourseUtil.createCourse();
    course.setName(COURSE_NAME);
    course.setCourseDescription(COURSE_DESCRIPTION);
    courseRepository.saveAndFlush(course);

    // Get the course
    restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(course.getId().toString()))
      .andExpect(jsonPath("$.name").value(course.getName()))
      .andExpect(jsonPath("$.startDate").value(course.getStartDate().toString()))
      .andExpect(jsonPath("$.endDate").value(course.getEndDate().toString()))
      .andExpect(jsonPath("$.remainActiveOffset").value(course.getRemainActiveOffset()))
      .andExpect(jsonPath("$.maxFoodSum").value(course.getMaxFoodSum()))
      .andExpect(jsonPath("$.courseDescription").value(course.getCourseDescription()))
      .andExpect(jsonPath("$.publishState").value(course.getPublishState().toString()));
  }

  @Test
  @Transactional
  public void getNonExistingCourse() throws Exception {
    // Get the course
    restCourseMockMvc.perform(get("/api/courses/{id}", NON_EXISTING_ID))
      .andExpect(status().isNotFound());
  }

  @Test
  @Transactional
  public void updateCourse() throws Exception {
    // Initialize the database
    final Course course = CourseUtil.createCourse();
    UUID id = courseRepository.saveAndFlush(course).getId();

    int databaseSizeBeforeUpdate = courseRepository.findAll().size();

    // Update the course
    CourseDTO update = new CourseDTO();
    update.setId(id);
    update.setName(UPDATED_NAME);
    update.setStartDate(course.getStartDate());
    update.setEndDate(course.getEndDate());
    update.setRemainActiveOffset(course.getRemainActiveOffset());
    update.setMaxFoodSum(UPDATED_MAX_FOOD_SUM);
    update.setCourseDescription(COURSE_DESCRIPTION);
    update.setPublishState(course.getPublishState());

    restCourseMockMvc.perform(put("/api/courses").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(update)))
      .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(update.getId().toString()))
      .andExpect(jsonPath("$.name").value(update.getName()))
      .andExpect(jsonPath("$.startDate").value(update.getStartDate().toString()))
      .andExpect(jsonPath("$.endDate").value(update.getEndDate().toString()))
      .andExpect(jsonPath("$.remainActiveOffset").value(update.getRemainActiveOffset()))
      .andExpect(jsonPath("$.maxFoodSum").value(update.getMaxFoodSum()))
      .andExpect(jsonPath("$.courseDescription").value(update.getCourseDescription()))
      .andExpect(jsonPath("$.publishState").value(update.getPublishState().toString()));

    // Validate the Course in the database
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void patchCourse() throws Exception {
    // Initialize the database
    final Course course = CourseUtil.createCourse();
    UUID id = courseRepository.saveAndFlush(course).getId();

    int databaseSizeBeforeUpdate = courseRepository.findAll().size();

    // Update the course
    CourseDTO patch = new CourseDTO();
    patch.setId(id);
    patch.setName(UPDATED_NAME);
    patch.setMaxFoodSum(UPDATED_MAX_FOOD_SUM);
    patch.setCourseDescription(COURSE_DESCRIPTION);

    // Expected
    CourseDTO expected = new CourseDTO();
    expected.setId(id);
    expected.setName(UPDATED_NAME);
    expected.setStartDate(course.getStartDate());
    expected.setEndDate(course.getEndDate());
    expected.setRemainActiveOffset(course.getRemainActiveOffset());
    expected.setMaxFoodSum(UPDATED_MAX_FOOD_SUM);
    expected.setCourseDescription(COURSE_DESCRIPTION);
    expected.setPublishState(course.getPublishState());

    restCourseMockMvc.perform(patch("/api/courses").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(patch)))
      .andExpect(status().isOk()).andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.id").value(expected.getId().toString()))
      .andExpect(jsonPath("$.name").value(expected.getName()))
      .andExpect(jsonPath("$.startDate").value(expected.getStartDate().toString()))
      .andExpect(jsonPath("$.endDate").value(expected.getEndDate().toString()))
      .andExpect(jsonPath("$.remainActiveOffset").value(expected.getRemainActiveOffset()))
      .andExpect(jsonPath("$.maxFoodSum").value(expected.getMaxFoodSum()))
      .andExpect(jsonPath("$.courseDescription").value(expected.getCourseDescription()))
      .andExpect(jsonPath("$.publishState").value(expected.getPublishState().toString()));

    // Validate the Course in the database
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void updateNonExistingCourse() throws Exception {
    CourseDTO courseDTO = CourseUtil.createCourseDTO();
    int databaseSizeBeforeUpdate = courseRepository.findAll().size();

    // If the entity doesn't have an ID, it will throw BadRequestAlertException
    restCourseMockMvc.perform(put("/api/courses").with(csrf())
      .contentType(MediaType.APPLICATION_JSON)
      .content(TestUtil.convertObjectToJsonBytes(courseDTO)))
      .andExpect(status().isBadRequest());

    // Validate the Course in the database
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
  }

  @Test
  @Transactional
  public void deleteCourse() throws Exception {
    // Initialize the database
    Course course = CourseUtil.createCourse();
    courseRepository.saveAndFlush(course);

    int databaseSizeBeforeDelete = courseRepository.findAll().size();

    // Delete the course
    restCourseMockMvc.perform(delete("/api/courses/{id}", course.getId()).with(csrf())
      .accept(MediaType.APPLICATION_JSON))
      .andExpect(status().isNoContent());

    // Validate the database contains one less item
    List<Course> courseList = courseRepository.findAll();
    assertThat(courseList).hasSize(databaseSizeBeforeDelete - 1);
  }

  @Test
  @Transactional
  void getFilteredCourses() throws Exception {
    String publishState = "PUBLISHED";
    final Course course = CourseUtil.createCourse();
    course.setPublishState(PUBLISHSTATE.PUBLISHED);
    course.setName(COURSE_NAME);
    course.setCourseDescription(COURSE_DESCRIPTION);

    // Initialize the database
    courseRepository.saveAndFlush(course);

    // Get all the courseList
    restCourseMockMvc
      .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=false")
        .param("publishState", publishState)).andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(course.getName())))
      .andExpect(jsonPath("$.[*].startDate").value(hasItem(course.getStartDate().toString())))
      .andExpect(jsonPath("$.[*].endDate").value(hasItem(course.getEndDate().toString())))
      .andExpect(jsonPath("$.[*].remainActiveOffset").value(hasItem(course.getRemainActiveOffset())))
      .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(course.getMaxFoodSum())))
      .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(course.getCourseDescription())))
      .andExpect(jsonPath("$.[*].publishState").value(hasItem(course.getPublishState().toString())));
  }

  @Test
  @Transactional
  void getFilteredActiveCourses() throws Exception {
    String publishState = "PUBLISHED";
    final Course course = CourseUtil.createCourse();
    course.setPublishState(PUBLISHSTATE.PUBLISHED);
    course.setName(COURSE_NAME);
    course.setCourseDescription(COURSE_DESCRIPTION);
    course.setStartDate(OLD_START_DATE);
    course.setEndDate(OLD_END_DATE);

    // Initialize the database
    courseRepository.saveAndFlush(course);

    // Get all the courseList
    restCourseMockMvc
      .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=true")
        .param("publishState", publishState))
      .andExpect(status().isOk())
      .andExpect(content().string("[]"));

    course.setEndDate(NEW_END_DATE);
    courseRepository.saveAndFlush(course);

    restCourseMockMvc
      .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=true")
        .param("publishState", publishState))
      .andExpect(status().isOk())
      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
      .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
      .andExpect(jsonPath("$.[*].name").value(hasItem(course.getName())))
      .andExpect(jsonPath("$.[*].startDate").value(hasItem(course.getStartDate().toString())))
      .andExpect(jsonPath("$.[*].endDate").value(hasItem(course.getEndDate().toString())))
      .andExpect(jsonPath("$.[*].remainActiveOffset").value(hasItem(course.getRemainActiveOffset())))
      .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(course.getMaxFoodSum())))
      .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(course.getCourseDescription())))
      .andExpect(jsonPath("$.[*].publishState").value(hasItem(course.getPublishState().toString())));
  }

  @Test
  @Transactional
  void patchCourseWithoutId() {
    Course toUpdate = new Course();
    toUpdate.setName("Herr der Ringe schauen");
    toUpdate.setCourseDescription("Cool Course");
    toUpdate.setMaxFoodSum(Integer.MAX_VALUE);
    assertThrows(BadRequestAlertException.class, () -> courseResource.patchCourse(courseMapper.toDTO(toUpdate)));
  }
}
