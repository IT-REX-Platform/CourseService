package de.uni_stuttgart.it_rex.course.written.web.rest;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.Course;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

    @Autowired
    private CourseRepository courseRepository;

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
}
