package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.ChapterIndex;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ChapterIndexUtil {
  public static ChapterIndex createChapterIndex(final UUID courseId, final int index) {
    ChapterIndex chapterIndex = new ChapterIndex();
    chapterIndex.setIndex(index);
    chapterIndex.setChapterId(UUID.randomUUID());
    chapterIndex.setCourseId(courseId);
    return chapterIndex;
  }

  public static ChapterIndex createChapterIndex(final int index) {
    return createChapterIndex(null, index);
  }

  public static List<ChapterIndex> createChapterIndexList(final UUID courseId, final int number) {
    return IntStream.range(0, number).mapToObj(i -> createChapterIndex(courseId, i)).collect(Collectors.toList());
  }

  public static List<ChapterIndex> createChapterIndexList(final int number) {
    return createChapterIndexList(null, number);
  }

  public static void equals(final ChapterIndex first, final ChapterIndex second) {
    assertEquals(first.getIndex(), second.getIndex());
    assertEquals(first.getChapterId(), second.getChapterId());
    assertEquals(first.getCourseId(), second.getCourseId());
  }
}
