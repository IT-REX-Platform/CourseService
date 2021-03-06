package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ContentReferenceUtil {

    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static ContentReference createContentReference() {
        ContentReference contentReference = new ContentReference();
        contentReference.setContentId(UUID.randomUUID());
        contentReference.setStartDate(LocalDate.now()
            .minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        contentReference.setEndDate(LocalDate.now()
            .plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return contentReference;
    }

    /**
     * Creates a random list of entities.
     *
     * @param number number of entities
     * @return the list
     */
    public static List<ContentReference> createContentReferenceList(
        final int number) {
        return IntStream.range(0, number)
            .mapToObj(i -> createContentReference())
            .collect(Collectors.toList());
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsContentReference(final ContentReference first,
                                              final ContentReference second) {
        assertEquals(first.getContentId(), second.getContentId());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getChapters(), second.getChapters());
        assertEquals(first.getTimePeriods(), second.getTimePeriods());
         */
    }

    /**
     * Creates a random dto.
     *
     * @return the dto
     */
    public static ContentReferenceDTO createContentReferenceDTO() {
        ContentReferenceDTO contentReferenceDTO = new ContentReferenceDTO();
        contentReferenceDTO.setContentId(UUID.randomUUID());
        contentReferenceDTO.setStartDate(LocalDate.now()
            .minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        contentReferenceDTO.setEndDate(LocalDate.now()
            .plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return contentReferenceDTO;
    }

    /**
     * Creates a List of random DTOs.
     *
     * @param number the length of the list
     * @return the DTOs
     */
    public static List<ContentReferenceDTO> createContentReferenceDTOs(final int number) {
        return IntStream.range(0, number).mapToObj(i -> createContentReferenceDTO()).collect(Collectors.toList());
    }

    /**
     * Tests if two dtos are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsContentReferenceDTO(
        final ContentReferenceDTO first,
        final ContentReferenceDTO second) {
        assertEquals(first.getContentId(), second.getContentId());
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getChapters(), second.getChapters());
        assertEquals(first.getTimePeriods(), second.getTimePeriods());
         */
    }
}
