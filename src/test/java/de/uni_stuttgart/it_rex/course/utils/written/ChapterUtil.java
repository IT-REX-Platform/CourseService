package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.Chapter;

import java.time.LocalDate;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ChapterUtil {
  public static Chapter createChapter() {
    Chapter chapter = new Chapter();
    chapter.setTitle(StringUtil.generateRandomString(10, 50));
    chapter.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
    chapter.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
    chapter.setContents(ContentIndexUtil.createContentIndexList(NumbersUtil.generateRandomInteger(4, 50)));
    return chapter;
  }

  public static void equals(final Chapter first, final Chapter second) {
    assertEquals(first.getTitle(), second.getTitle());
    assertEquals(first.getCourseId(), second.getCourseId());
    assertEquals(first.getStartDate(), second.getStartDate());
    assertEquals(first.getEndDate(), second.getEndDate());

    assertEquals(first.getContents().size(), second.getContents().size());
    IntStream.range(0, first.getContents().size()).forEach(i -> ContentIndexUtil.equals(first.getContents().get(i), second.getContents().get(i)));
  }
}
