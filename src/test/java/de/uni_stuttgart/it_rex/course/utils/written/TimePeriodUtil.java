package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;


public final class TimePeriodUtil {

    /**
     * Creates a random entity.
     *
     * @return the entity
     */
    public static TimePeriod createTimePeriod() {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setStartDate(LocalDate.now()
            .minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        timePeriod.setEndDate(LocalDate.now()
            .plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return timePeriod;
    }

    /**
     * Tests if two entities are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsTimePeriod(final TimePeriod first, final TimePeriod second) {
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());

        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getContentReferences(), second.getContentReferences());
         */
    }

    /**
     * Creates a random DTO.
     *
     * @return the DTO
     */
    public static TimePeriodDTO createTimePeriodDTO() {
        TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setStartDate(LocalDate.now()
            .minusDays(NumbersUtil.generateRandomInteger(20, 200)));
        timePeriodDTO.setEndDate(LocalDate.now()
            .plusDays(NumbersUtil.generateRandomInteger(20, 200)));
        return timePeriodDTO;
    }

    /**
     * Tests if two DTOs are equal but ignores their id.
     *
     * @param first
     * @param second
     */
    public static void equalsTimePeriodDTO(final TimePeriodDTO first, final TimePeriodDTO second) {
        assertEquals(first.getStartDate(), second.getStartDate());
        assertEquals(first.getEndDate(), second.getEndDate());

        /* Todo: replace with equals methods
        assertEquals(first.getCourse(), second.getCourse());
        assertEquals(first.getContentReferences(), second.getContentReferences());
         */
    }

}
