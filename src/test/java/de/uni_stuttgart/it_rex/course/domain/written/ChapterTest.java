package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class ChapterTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final String FIRST_NAME = "AAAAAAAAAA";
    private static final String SECOND_NAME = "BBBBBBBBBB";

    private static final UUID FIRST_COURSE_ID = UUID.randomUUID();
    private static final UUID SECOND_COURSE_ID = UUID.randomUUID();

    private static final UUID FIRST_NEXT = UUID.randomUUID();
    private static final UUID SECOND_NEXT = UUID.randomUUID();

    private static final UUID FIRST_PREVIOUS = UUID.randomUUID();
    private static final UUID SECOND_PREVIOUS = UUID.randomUUID();

    @Test
    void equalsVerifier() {

        Chapter chapter1 = new Chapter();
        chapter1.setId(FIRST_ID);
        chapter1.setName(FIRST_NAME);

        Chapter chapter2 = new Chapter();
        chapter2.setId(SECOND_ID);
        chapter2.setName(SECOND_NAME);

        Chapter chapter3 = new Chapter();
        chapter3.setId(FIRST_ID);
        chapter3.setName(FIRST_NAME);

        assertEquals(chapter1.hashCode(), chapter1.hashCode());
        assertEquals(chapter1, chapter1);

        ChapterUtil.equalsChapter(chapter1, chapter3);

        assertNotEquals(chapter1, chapter2);
    }
}
