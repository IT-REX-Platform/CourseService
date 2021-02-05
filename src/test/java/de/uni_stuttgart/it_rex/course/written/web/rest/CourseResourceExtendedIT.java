package de.uni_stuttgart.it_rex.course.written.web.rest;


import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.Course;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.repository.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.CourseDTO;
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

import java.net.URISyntaxException;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link CourseResourceExtended} REST controller.
 */
@SpringBootTest(classes = {CourseServiceApp.class,
        TestSecurityConfiguration.class})
@AutoConfigureMockMvc
@WithMockUser
class CourseResourceExtendedIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";

    private static final LocalDate DEFAULT_START_DATE =
            LocalDate.ofEpochDay(0L);

    private static final LocalDate DEFAULT_END_DATE = LocalDate.ofEpochDay(0L);

    private static final Integer DEFAULT_MAX_FOOD_SUM = 1;

    private static final String DEFAULT_COURSE_DESCRIPTION = "AAAAAAAAAA";

    private static final PUBLISHSTATE DEFAULT_PUBLISH_STATE =
        PUBLISHSTATE.PUBLISHED;

    private static final String EXPECTED_EXCEPTION_MESSAGE = "Invalid id";

    private static final String OLD_NAME = "Herr der Ringe schauen";

    private static final String OLD_DESCRIPTION = "Cool Course";

    private static final Integer OLD_MAX_FOOD_SUM = Integer.MAX_VALUE;

    private static final String NEW_DESCRIPTION = "Really cool Course";

    private static final PUBLISHSTATE NEW_PUBLISHED_STATE = PUBLISHSTATE.PUBLISHED;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private CourseResourceExtended courseResourceExtended;

    @Autowired
    private MockMvc restCourseMockMvc;

    private Course course;

    /**
     * Create an entity for this test.
     * <p>
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Course createEntity() {
        return new Course()
                .name(DEFAULT_NAME)
                .startDate(DEFAULT_START_DATE)
                .endDate(DEFAULT_END_DATE)
                .maxFoodSum(DEFAULT_MAX_FOOD_SUM)
                .courseDescription(DEFAULT_COURSE_DESCRIPTION)
                .publishState(DEFAULT_PUBLISH_STATE);
    }

    @BeforeEach
    public void initTest() {
        course = createEntity();
    }

    @Test
    @Transactional
    void getFilteredCourses() throws Exception {
        String publishState = "PUBLISHED";

        // Initialize the database
        courseRepository.saveAndFlush(course);

        // Get all the courseList
        restCourseMockMvc
            .perform(get("/api/extended/courses?publishState=PUBLISHED")
                .param("publishState", publishState))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(course.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].maxFoodSum").value(hasItem(DEFAULT_MAX_FOOD_SUM)))
            .andExpect(jsonPath("$.[*].courseDescription").value(hasItem(DEFAULT_COURSE_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].publishState").value(hasItem(DEFAULT_PUBLISH_STATE.toString())));
    }

    @Test
    @Transactional
    void updateCourse() throws URISyntaxException {
        CourseDTO toUpdate = new CourseDTO();
        toUpdate.setName(OLD_NAME);
        toUpdate.setCourseDescription(OLD_DESCRIPTION);
        toUpdate.setMaxFoodSum(OLD_MAX_FOOD_SUM);

        Long id = courseResourceExtended.createCourse(toUpdate).getBody().getId();
        CourseDTO update = new CourseDTO();
        update.setId(id);
        update.setCourseDescription(NEW_DESCRIPTION);
        update.setPublishState(NEW_PUBLISHED_STATE);

        CourseDTO result = courseResourceExtended.updateCourse(update).getBody();

        CourseDTO expected = new CourseDTO();
        expected.setId(id);
        expected.setName(OLD_NAME);
        expected.setCourseDescription(NEW_DESCRIPTION);
        expected.setMaxFoodSum(OLD_MAX_FOOD_SUM);
        expected.setPublishState(NEW_PUBLISHED_STATE);

        assertThat(result).isEqualTo(expected);
    }

    @Test
    @Transactional
    void updateCourseWithoutId() {
        CourseDTO toUpdate = new CourseDTO();
        toUpdate.setName("Herr der Ringe schauen");
        toUpdate.setCourseDescription("Cool Course");
        toUpdate.setMaxFoodSum(Integer.MAX_VALUE);

        Exception e = assertThrows(BadRequestAlertException.class, () -> courseResourceExtended.updateCourse(toUpdate));
        assertThat(e.getMessage()).isEqualTo(EXPECTED_EXCEPTION_MESSAGE);
    }
}
