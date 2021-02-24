package de.uni_stuttgart.it_rex.course.domain.written;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class ChapterTest {private static final UUID FIRST_ID = UUID.randomUUID();
  private static final UUID SECOND_ID = UUID.randomUUID();

  private static final String FIRST_NAME = "AAAAAAAAAA";
  private static final String SECOND_NAME = "BBBBBBBBBB";

  private static final LocalDate FIRST_START_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate SECOND_START_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final LocalDate FIRST_END_DATE = LocalDate.ofEpochDay(0L);
  private static final LocalDate SECOND_END_DATE = LocalDate.now(ZoneId.systemDefault());

  private static final UUID FIRST_COURSE_ID = UUID.randomUUID();
  private static final UUID SECOND_COURSE_ID = UUID.randomUUID();

  private static final UUID FIRST_NEXT = UUID.randomUUID();
  private static final UUID SECOND_NEXT = UUID.randomUUID();

  private static final UUID FIRST_PREVIOUS =UUID.randomUUID();
  private static final UUID SECOND_PREVIOUS = UUID.randomUUID();

  @Test
  void equalsVerifier() throws Exception {

    Chapter course1 = new Chapter();
    course1.setId(FIRST_ID);
    course1.setTitle(FIRST_NAME);
    course1.setStartDate(FIRST_START_DATE);
    course1.setEndDate(FIRST_END_DATE);
    course1.setCourseId(FIRST_COURSE_ID);

    Chapter course2 = new Chapter();
    course2.setId(SECOND_ID);
    course2.setTitle(SECOND_NAME);
    course2.setStartDate(SECOND_START_DATE);
    course2.setEndDate(SECOND_END_DATE);
    course2.setCourseId(SECOND_COURSE_ID);

    Chapter course3 = new Chapter();
    course3.setId(FIRST_ID);
    course3.setTitle(FIRST_NAME);
    course3.setStartDate(FIRST_START_DATE);
    course3.setEndDate(FIRST_END_DATE);
    course3.setCourseId(FIRST_COURSE_ID);

    assertEquals(course1.hashCode(), course1.hashCode());
    assertEquals(course1, course1);
    assertEquals(course1, course3);
    assertNotEquals(course1, course2);
    assertNotEquals(course1.hashCode(), course2.hashCode());

    assertNotEquals(SECOND_NEXT, course1);
  }
}
