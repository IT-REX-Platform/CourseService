package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.written.CourseMapper;
import de.uni_stuttgart.it_rex.course.service.written.KeycloakAdminService;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseResource courseResource;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    @Autowired
    private CourseMapper courseMapper;

    @MockBean
    private KeycloakAdminService keycloakAdminService;

    private Course cour1se;

    @BeforeAll
    static void setup() throws IOException {
        COURSE_DESCRIPTION =
            Files.lines(Path.of("src/test/resources/CourseDescription.txt")).collect(Collectors.joining("\n"));
    }

    @AfterEach
    void cleanUp() {
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
    @Disabled
    @Transactional
    public void updateCourse() throws Exception {
        //      // Initialize the database
        //      final Course course = CourseUtil.createCourse();
        //      UUID id = courseRepository.saveAndFlush(course).getId();

        //      int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        //      // Update the course
        //      CourseDTO updatedCourse = new CourseDTO();
        //      updatedCourse.setId(id);
        //      updatedCourse.setName(UPDATED_NAME);
        //      updatedCourse.setStartDate(UPDATED_START_DATE);
        //      updatedCourse.setEndDate(UPDATED_END_DATE);
        //      updatedCourse.setRemainActiveOffset(UPDATED_REMAIN_ACTIVE_OFFSET);
        //      updatedCourse.setMaxFoodSum(UPDATED_MAX_FOOD_SUM);
        //      updatedCourse.setCourseDescription(UPDATED_COURSE_DESCRIPTION);
        //      updatedCourse.setPublishState(UPDATED_PUBLISH_STATE);

        //      restCourseMockMvc.perform(put("/api/courses").with(csrf())
        //          .contentType(MediaType.APPLICATION_JSON)
        //          .content(TestUtil.convertObjectToJsonBytes(courseMapper.toDTO(updatedCourse))))
        //          .andExpect(status().isOk());

        //      // Validate the Course in the database
        //      List<Course> courseList = courseRepository.findAll();
        //      assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
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
    @Disabled
    @Transactional
    void getFilteredCourses() throws Exception {
        String publishState = "PUBLISHED";

        // Initialize the database
        courseRepository.saveAndFlush(cour1se);

        // Get all the courseList
        //   restCourseMockMvc
        //       .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=false")
        //           .param("publishState", publishState))
        //       .andExpect(status().isOk())
        //       .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        //       .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
        //      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        //      .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
        //      .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
        //      .andExpect(jsonPath("$.[*].remainActiveOffset").value(hasItem(DEFAULT_REMAIN_ACTIVE_OFFSET)))
        //      .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(DEFAULT_MAX_FOOD_SUM)))
        //      .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION)))
        //      .andExpect(jsonPath("$.[*].publishState").value(hasItem(DEFAULT_PUBLISH_STATE.toString())));
    }

    @Test
    @Disabled
    @Transactional
    void getFilteredActiveCourses() throws Exception {
        String publishState = "PUBLISHED";

        // Initialize the database
        courseRepository.saveAndFlush(cour1se);

        // Get all the courseList
        restCourseMockMvc
            .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=true")
                .param("publishState", publishState))
            .andExpect(status().isOk())
            .andExpect(content().string("[]"));

        //  course.setEndDate(NEW_END_DATE);
        courseRepository.saveAndFlush(cour1se);

        //  restCourseMockMvc
        //      .perform(get("/api/courses?publishState=PUBLISHED&activeOnly=true")
        //          .param("publishState", publishState))
        //      .andExpect(status().isOk())
        //      .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
        //      .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
        //      .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
        //      .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
        //      .andExpect(jsonPath("$.[*].endDate").value(hasItem(NEW_END_DATE.toString())))
        //      .andExpect(jsonPath("$.[*].remainActiveOffset").value(hasItem(DEFAULT_REMAIN_ACTIVE_OFFSET)))
        //      .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(DEFAULT_MAX_FOOD_SUM)))
        //      .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION)))
        //      .andExpect(jsonPath("$.[*].publishState").value(hasItem(DEFAULT_PUBLISH_STATE.toString())));
    }

    @Test
    @Transactional
    void patchCourse() throws URISyntaxException {
        //    Course toUpdate = new Course();
        //    toUpdate.setName(OLD_NAME);
        //    toUpdate.setCourseDescription(OLD_DESCRIPTION);
        //    toUpdate.setMaxFoodSum(OLD_MAX_FOOD_SUM);

        //    UUID id = courseResource.createCourse(courseMapper.toDTO(toUpdate)).getBody().getId();
        //    Course update = new Course();
        //    update.setId(id);
        //    update.setCourseDescription(NEW_DESCRIPTION);
        //    update.setPublishState(NEW_PUBLISHED_STATE);

        //    Course result = courseMapper.toEntity(courseResource.patchCourse(courseMapper.toDTO(update)).getBody());

        //    Course expected = new Course();
        //    expected.setId(id);
        //    expected.setName(OLD_NAME);
        //    expected.setCourseDescription(NEW_DESCRIPTION);
        //    expected.setMaxFoodSum(OLD_MAX_FOOD_SUM);
        //    expected.setPublishState(NEW_PUBLISHED_STATE);

        //    Course updated = courseMapper.toEntity(courseResource.getCourse(id).getBody());

        //    assertThat(updated).isEqualTo(expected);
    }

    @Test
    @Transactional
    void patchCourseWithoutId() {
        Course toUpdate = new Course();
        toUpdate.setName("Herr der Ringe schauen");
        toUpdate.setCourseDescription("Cool Course");
        toUpdate.setMaxFoodSum(Integer.MAX_VALUE);

        Exception e = assertThrows(BadRequestAlertException.class, () -> courseResource.patchCourse(courseMapper.toDTO(toUpdate)));
    }
}
