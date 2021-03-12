package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


class CourseTest {

    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final String FIRST_NAME = "AAAAAAAAAA";
    private static final String SECOND_NAME = "BBBBBBBBBB";

    private static final LocalDate FIRST_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_START_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate FIRST_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_END_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final Integer FIRST_REMAIN_ACTIVE_OFFSET = 0;
    private static final Integer SECOND_REMAIN_ACTIVE_OFFSET = 1;

    private static final Integer FIRST_MAX_FOOD_SUM = 1;
    private static final Integer SECOND_MAX_FOOD_SUM = 2;

    private static final String FIRST_COURSE_DESCRIPTION = "AAAAAAAAAA";
    private static final String SECOND_COURSE_DESCRIPTION = "BBBBBBBBBB";

    private static final PUBLISHSTATE FIRST_PUBLISH_STATE = PUBLISHSTATE.PUBLISHED;
    private static final PUBLISHSTATE SECOND_PUBLISH_STATE = PUBLISHSTATE.UNPUBLISHED;

    @Test
    void equalsVerifier() throws Exception {

        Course course1 = new Course();
        course1.setId(FIRST_ID);
        course1.setName(FIRST_NAME);
        course1.setStartDate(FIRST_START_DATE);
        course1.setEndDate(FIRST_END_DATE);
        course1.setRemainActiveOffset(FIRST_REMAIN_ACTIVE_OFFSET);
        course1.setMaxFoodSum(FIRST_MAX_FOOD_SUM);
        course1.setCourseDescription(FIRST_COURSE_DESCRIPTION);
        course1.setPublishState(FIRST_PUBLISH_STATE);

        Course course2 = new Course();
        course2.setId(SECOND_ID);
        course2.setName(SECOND_NAME);
        course2.setStartDate(SECOND_START_DATE);
        course2.setEndDate(SECOND_END_DATE);
        course2.setRemainActiveOffset(SECOND_REMAIN_ACTIVE_OFFSET);
        course2.setMaxFoodSum(SECOND_MAX_FOOD_SUM);
        course2.setCourseDescription(SECOND_COURSE_DESCRIPTION);
        course2.setPublishState(SECOND_PUBLISH_STATE);

        Course course3 = new Course();
        course3.setId(FIRST_ID);
        course3.setName(FIRST_NAME);
        course3.setStartDate(FIRST_START_DATE);
        course3.setEndDate(FIRST_END_DATE);
        course3.setRemainActiveOffset(FIRST_REMAIN_ACTIVE_OFFSET);
        course3.setMaxFoodSum(FIRST_MAX_FOOD_SUM);
        course3.setCourseDescription(FIRST_COURSE_DESCRIPTION);
        course3.setPublishState(FIRST_PUBLISH_STATE);

        assertEquals(course1.hashCode(), course1.hashCode());
        assertEquals(course1, course1);
        assertEquals(course1, course3);
        assertNotEquals(course1, course2);
        assertNotEquals(course1.hashCode(), course2.hashCode());

        assertNotEquals(SECOND_COURSE_DESCRIPTION, course1);
    }
}
