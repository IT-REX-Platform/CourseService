package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written.TimePeriod;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;


public final class TimePeriodUtil {

    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static TimePeriod createTimePeriod() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        timePeriod.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return timePeriod;
    }

    /**
     * Creates a List of random entities.
     *
     * @param number the length of the list
     * @return the entities
     */
    public static List<TimePeriod> createTimePeriods(final int number) {
        return IntStream.range(0, number).mapToObj(i -> createTimePeriod()).collect(Collectors.toList());
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsTimePeriod(final TimePeriod first,
                                        final TimePeriod second) {
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
    }

    /**
     * Creates a random DTO.
     *
     * @return the DTO
     */
    public static TimePeriodDTO createTimePeriodDTO() {
        TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        timePeriodDTO.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return timePeriodDTO;
    }

    /**
     * Creates a DTO from given values.
     *
     * @return the DTO
     */
    public static TimePeriodDTO createTimePeriodDTO(
        final LocalDate startDate,
        final LocalDate endDate,
        final UUID courseId,
        final List<UUID> contentReferenceIds) {
        TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setStartDate(startDate);
        timePeriodDTO.setEndDate(endDate);
        timePeriodDTO.setCourseId(courseId);
        timePeriodDTO.setContentReferenceIds(contentReferenceIds);
        return timePeriodDTO;
    }

    /**
     * Creates a List of random DTOs.
     *
     * @param number the length of the list
     * @return the DTOs
     */
    public static List<TimePeriodDTO> createTimePeriodDTOs(final int number) {
        return IntStream.range(0, number).mapToObj(i -> createTimePeriodDTO()).collect(Collectors.toList());
    }


    /**
     * Tests if two DTOs are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsTimePeriodDTO(final TimePeriodDTO first,
                                           final TimePeriodDTO second) {
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());
        assertEquals(first.getCourseId(), second.getCourseId());
        assertEquals(first.getContentReferenceIds(), second.getContentReferenceIds());
    }

    /**
     * Tests if two Lists of DTOs are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static boolean equalsTimePeriodDTOs(final List<TimePeriodDTO> first,
                                           final List<TimePeriodDTO> second) {
        assertEquals(first.size(), second.size());
        for(int i = 0; i < first.size(); i++) {
            equalsTimePeriodDTO(first.get(i), second.get(i));
        }
        return true;
    }
}
