package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.utils.written.TimePeriodUtil;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class TimePeriodTest {
    private static final UUID FIRST_ID = UUID.randomUUID();
    private static final UUID SECOND_ID = UUID.randomUUID();

    private static final LocalDate FIRST_START_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_START_DATE =
        LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate FIRST_END_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate SECOND_END_DATE =
        LocalDate.now(ZoneId.systemDefault());

    @Test
    void equalsVerifier() {

        TimePeriod timePeriod1 = new TimePeriod();
        timePeriod1.setId(FIRST_ID);
        timePeriod1.setStartDate(FIRST_START_DATE);
        timePeriod1.setEndDate(FIRST_END_DATE);

        TimePeriod timePeriod2 = new TimePeriod();
        timePeriod2.setId(SECOND_ID);
        timePeriod2.setStartDate(SECOND_START_DATE);
        timePeriod2.setEndDate(SECOND_END_DATE);

        TimePeriod timePeriod3 = new TimePeriod();
        timePeriod3.setId(FIRST_ID);
        timePeriod3.setStartDate(FIRST_START_DATE);
        timePeriod3.setEndDate(FIRST_END_DATE);

        assertEquals(timePeriod1.hashCode(), timePeriod1.hashCode());
        assertEquals(timePeriod1, timePeriod1);

        TimePeriodUtil.equalsTimePeriod(timePeriod1, timePeriod3);

        assertNotEquals(timePeriod1, timePeriod2);
    }
}
