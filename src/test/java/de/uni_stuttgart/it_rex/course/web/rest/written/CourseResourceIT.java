package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.web.rest.TestUtil;
import de.uni_stuttgart.it_rex.course.web.rest.errors.BadRequestAlertException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Integration tests for the {@link CourseResource} REST controller.
 */
@SpringBootTest(classes = { CourseServiceApp.class, TestSecurityConfiguration.class })
@AutoConfigureMockMvc
@WithMockUser
public class CourseResourceIT {

    private static final UUID NON_EXISTING_ID = UUID.randomUUID();

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer DEFAULT_MAX_FOOD_SUM = 1;
    private static final Integer UPDATED_MAX_FOOD_SUM = 2;

    private static final String DEFAULT_COURSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_COURSE_DESCRIPTION = "BBBBBBBBBB";

    private static final PUBLISHSTATE DEFAULT_PUBLISH_STATE = PUBLISHSTATE.PUBLISHED;
    private static final PUBLISHSTATE UPDATED_PUBLISH_STATE = PUBLISHSTATE.UNPUBLISHED;


    private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

    private static final String OLD_NAME = "Herr der Ringe schauen";
    private static final String OLD_DESCRIPTION = "Cool Course";
    private static final Integer OLD_MAX_FOOD_SUM = Integer.MAX_VALUE;

    private static final String NEW_DESCRIPTION = "Really cool Course";
    private static final PUBLISHSTATE NEW_PUBLISHED_STATE = PUBLISHSTATE.PUBLISHED;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseResource courseResource;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity(EntityManager em) {
        Course course = new Course()
            .name(DEFAULT_NAME)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .maxFoodSum(DEFAULT_MAX_FOOD_SUM)
            .courseDescription(DEFAULT_COURSE_DESCRIPTION)
            .publishState(DEFAULT_PUBLISH_STATE);
        return course;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createUpdatedEntity(EntityManager em) {
        Course course = new Course()
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .maxFoodSum(UPDATED_MAX_FOOD_SUM)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .publishState(UPDATED_PUBLISH_STATE);
        return course;
    }

    @BeforeEach
    public void initTest() {
        course = createEntity(em);
    }

    @Test
    @Transactional
    public void createCourse() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();
        // Create the Course
        restCourseMockMvc.perform(post("/api/courses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isCreated());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate + 1);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testCourse.getStartDate()).isEqualTo(DEFAULT_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(DEFAULT_END_DATE);
        assertThat(testCourse.getMaxFoodSum()).isEqualTo(DEFAULT_MAX_FOOD_SUM);
        assertThat(testCourse.getCourseDescription()).isEqualTo(DEFAULT_COURSE_DESCRIPTION);
        assertThat(testCourse.getPublishState()).isEqualTo(DEFAULT_PUBLISH_STATE);
    }

    @Test
    @Transactional
    public void createCourseWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = courseRepository.findAll().size();

        // Create the Course with an existing ID
        course.setId(UUID.randomUUID());

        // An entity with an existing ID cannot be created, so this API call must fail
        restCourseMockMvc.perform(post("/api/courses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllCourses() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc.perform(get("/api/courses?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(DEFAULT_MAX_FOOD_SUM)))
            .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION.toString())))
            .andExpect(jsonPath("$.[*].publishState").value(hasItem(DEFAULT_PUBLISH_STATE.toString())));
    }
    
    @Test
    @Transactional
    public void getCourse() throws Exception {
        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get the course
        restCourseMockMvc.perform(get("/api/courses/{id}", course.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(course.getId().toString()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.maxFoodSum").value(DEFAULT_MAX_FOOD_SUM))
            .andExpect(jsonPath("$.courseDescription").value(DEFAULT_COURSE_DESCRIPTION.toString()))
            .andExpect(jsonPath("$.publishState").value(DEFAULT_PUBLISH_STATE.toString()));
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
        courseRepository.saveAndFlush(course);

        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // Update the course
        Course updatedCourse = courseRepository.findById(course.getId()).get();
        // Disconnect from session so that the updates on updatedCourse are not directly saved in db
        em.detach(updatedCourse);
        updatedCourse
            .name(UPDATED_NAME)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .maxFoodSum(UPDATED_MAX_FOOD_SUM)
            .courseDescription(UPDATED_COURSE_DESCRIPTION)
            .publishState(UPDATED_PUBLISH_STATE);

        restCourseMockMvc.perform(put("/api/courses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(updatedCourse)))
            .andExpect(status().isOk());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
        Course testCourse = courseList.get(courseList.size() - 1);
        assertThat(testCourse.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testCourse.getStartDate()).isEqualTo(UPDATED_START_DATE);
        assertThat(testCourse.getEndDate()).isEqualTo(UPDATED_END_DATE);
        assertThat(testCourse.getMaxFoodSum()).isEqualTo(UPDATED_MAX_FOOD_SUM);
        assertThat(testCourse.getCourseDescription()).isEqualTo(UPDATED_COURSE_DESCRIPTION);
        assertThat(testCourse.getPublishState()).isEqualTo(UPDATED_PUBLISH_STATE);
    }

    @Test
    @Transactional
    public void updateNonExistingCourse() throws Exception {
        int databaseSizeBeforeUpdate = courseRepository.findAll().size();

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCourseMockMvc.perform(put("/api/courses").with(csrf())
            .contentType(MediaType.APPLICATION_JSON)
            .content(TestUtil.convertObjectToJsonBytes(course)))
            .andExpect(status().isBadRequest());

        // Validate the Course in the database
        List<Course> courseList = courseRepository.findAll();
        assertThat(courseList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteCourse() throws Exception {
        // Initialize the database
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

        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc
            .perform(get("/api/courses?publishState=PUBLISHED")
                .param("publishState", publishState))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().toString())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(DEFAULT_MAX_FOOD_SUM)))
            .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].publishState").value(hasItem(DEFAULT_PUBLISH_STATE.toString())));
    }

    @Test
    @Transactional
    void patchCourse() throws URISyntaxException {
        Course toUpdate = new Course();
        toUpdate.setName(OLD_NAME);
        toUpdate.setCourseDescription(OLD_DESCRIPTION);
        toUpdate.setMaxFoodSum(OLD_MAX_FOOD_SUM);

        UUID id = courseResource.createCourse(toUpdate).getBody().getId();
        Course update = new Course();
        update.setId(id);
        update.setCourseDescription(NEW_DESCRIPTION);
        update.setPublishState(NEW_PUBLISHED_STATE);

        Course result = courseResource.patchCourse(update).getBody();

        Course expected = new Course();
        expected.setId(id);
        expected.setName(OLD_NAME);
        expected.setCourseDescription(NEW_DESCRIPTION);
        expected.setMaxFoodSum(OLD_MAX_FOOD_SUM);
        expected.setPublishState(NEW_PUBLISHED_STATE);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @Transactional
    void patchCourseWithoutId() {
        Course toUpdate = new Course();
        toUpdate.setName("Herr der Ringe schauen");
        toUpdate.setCourseDescription("Cool Course");
        toUpdate.setMaxFoodSum(Integer.MAX_VALUE);

        Exception e = assertThrows(BadRequestAlertException.class, () -> courseResource.patchCourse(toUpdate));
        assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
    }
}
