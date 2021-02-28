package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentIndex;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ContentIndexUtil {
  public static ContentIndex createContentIndex(final int index) {
    ContentIndex contentIndex = new ContentIndex();
    contentIndex.setIndex(index);
    contentIndex.setContentId(UUID.randomUUID());
    return contentIndex;
  }

  public static List<ContentIndex> createContentIndexList(final int number) {
    return IntStream.range(0, number).mapToObj(i -> createContentIndex(i)).collect(Collectors.toList());
  }

  public static void equals(final ContentIndex first, final ContentIndex second) {
    assertEquals(first.getIndex(), second.getIndex());
    assertEquals(first.getChapterId(), second.getChapterId());
    assertEquals(first.getContentId(), second.getContentId());
  }
}
