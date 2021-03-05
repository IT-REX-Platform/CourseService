package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ChapterUtil {

    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static Chapter createChapter() {
        Chapter chapter = new Chapter();
        chapter.setTitle(StringUtil.generateRandomString(10, 50));
        return chapter;
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsChapter(final Chapter first, final Chapter second) {
        assertEquals(first.getTitle(), second.getTitle());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());

        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getContentReferences(), second.getContentReferences());
        */
    }

    /**
     * Creates a random dto.
     *
     * @return the dto
     */
    public static ChapterDTO createChapterDTO() {
        ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setTitle(StringUtil.generateRandomString(10, 50));
        return chapterDTO;
    }

    /**
     * Tests if two dtos are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsChapterDTO(final ChapterDTO first, final ChapterDTO second) {
        assertEquals(first.getTitle(), second.getTitle());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());

        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getContentReferences(), second.getContentReferences());
        */
    }

}
