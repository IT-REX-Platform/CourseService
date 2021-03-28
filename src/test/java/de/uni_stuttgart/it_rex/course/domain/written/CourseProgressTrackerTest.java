package de.uni_stuttgart.it_rex.course.domain.written;

import liquibase.pro.packaged.C;
import org.junit.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class CourseProgressTrackerTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final UUID FIRST_COURSE_ID = UUID.randomUUID();
    private static final UUID SECOND_COURSE_ID = UUID.randomUUID();

    private static final UUID FIRST_USER_ID = UUID.randomUUID();
    private static final UUID SECOND_USER_ID = UUID.randomUUID();

    private static final ContentReference lastContentReference1 = new ContentReference();
    private static final ContentReference lastContentReference2 = new ContentReference();

    @Test
    public void equalsVerifier(){
        CourseProgressTracker courseProgressTracker1 = new CourseProgressTracker();
        courseProgressTracker1.setId(FIRST_ID);
        courseProgressTracker1.setCourseId(FIRST_COURSE_ID);
        courseProgressTracker1.setUserId(FIRST_USER_ID);
        courseProgressTracker1.setLastContentReference(lastContentReference1);

        CourseProgressTracker courseProgressTracker2 = new CourseProgressTracker();
        courseProgressTracker2.setId(SECOND_ID);
        courseProgressTracker2.setCourseId(SECOND_COURSE_ID);
        courseProgressTracker2.setUserId(SECOND_USER_ID);
        courseProgressTracker2.setLastContentReference(lastContentReference2);

        CourseProgressTracker courseProgressTracker3 = new CourseProgressTracker();
        courseProgressTracker3.setId(FIRST_ID);
        courseProgressTracker3.setCourseId(FIRST_COURSE_ID);
        courseProgressTracker3.setUserId(FIRST_USER_ID);
        courseProgressTracker3.setLastContentReference(lastContentReference1);

        assertEquals(courseProgressTracker1.hashCode(), courseProgressTracker1.hashCode());
        assertEquals(courseProgressTracker1, courseProgressTracker1);

        assertEquals(courseProgressTracker1, courseProgressTracker3);

        assertNotEquals(courseProgressTracker1, courseProgressTracker2);
    }
}
