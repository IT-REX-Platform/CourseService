package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
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

    Chapter chapter1 = new Chapter();
    chapter1.setId(FIRST_ID);
    chapter1.setName(FIRST_NAME);
  // chapter1.setStartDate(FIRST_START_DATE);
  // chapter1.setEndDate(FIRST_END_DATE);

    Chapter chapter2 = new Chapter();
    chapter2.setId(SECOND_ID);
    chapter2.setName(SECOND_NAME);
   // chapter2.setStartDate(SECOND_START_DATE);
   // chapter2.setEndDate(SECOND_END_DATE);

    Chapter chapter3 = new Chapter();
    chapter3.setId(FIRST_ID);
    chapter3.setName(FIRST_NAME);
 //   chapter3.setStartDate(FIRST_START_DATE);
 //   chapter3.setEndDate(FIRST_END_DATE);

    assertEquals(chapter1.hashCode(), chapter1.hashCode());
    assertEquals(chapter1, chapter1);
    ChapterUtil.equalsChapter(chapter1, chapter3);
    assertNotEquals(chapter1, chapter2);
    assertNotEquals(chapter1.hashCode(), chapter2.hashCode());

    assertNotEquals(SECOND_NEXT, chapter1);
  }
}
