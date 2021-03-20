package de.uni_stuttgart.it_rex.course.domain.written;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ContentProgressTrackerTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final ContentReference firstContentReference = new ContentReference();
    private static final ContentReference secondContentReference = new ContentReference();

    private static final UUID FIRST_USER_ID = UUID.randomUUID();
    private static final UUID SECOND_USER_ID = UUID.randomUUID();

    private static final float firstProgress = 0.1f;
    private static final float secondProgress = 0.2f;

    private static final CourseProgressTracker firstCourseProgressTracker = new CourseProgressTracker();
    private static final CourseProgressTracker secondCourseProgressTracker = new CourseProgressTracker();

    @Test
    public void equalsVerifier(){

        ContentProgressTracker contentProgressTracker1
            = new ContentProgressTracker(FIRST_USER_ID, firstContentReference, firstCourseProgressTracker);
        contentProgressTracker1.setId(FIRST_ID);
        contentProgressTracker1.setProgress(firstProgress);

        ContentProgressTracker contentProgressTracker2
            = new ContentProgressTracker(SECOND_USER_ID, secondContentReference, secondCourseProgressTracker);
        contentProgressTracker2.setId(SECOND_ID);
        contentProgressTracker2.setProgress(secondProgress);

        ContentProgressTracker contentProgressTracker3
            = new ContentProgressTracker(FIRST_USER_ID, firstContentReference, firstCourseProgressTracker);
        contentProgressTracker3.setId(FIRST_ID);
        contentProgressTracker3.setProgress(firstProgress);

        assertEquals(contentProgressTracker1.hashCode(), contentProgressTracker1.hashCode());
        assertEquals(contentProgressTracker1, contentProgressTracker1);

        assertEquals(contentProgressTracker1, contentProgressTracker3);

        assertNotEquals(contentProgressTracker1, contentProgressTracker2);
    }
}
