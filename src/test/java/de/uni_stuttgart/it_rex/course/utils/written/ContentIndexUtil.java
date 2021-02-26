package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentIndex;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ContentIndexUtil {

  /**
   * Creates a random entity.
   *
   * @return the entity
   */
  public static ContentIndex createContentIndex(final int index) {
    ContentIndex contentIndex = new ContentIndex();
    contentIndex.setIndex(index);
    contentIndex.setContentId(UUID.randomUUID());
    return contentIndex;
  }

  /**
   * Creates a random list of entities.
   *
   * @param number number of entities
   * @return the list
   */
  public static List<ContentIndex> createContentIndexList(final int number) {
    return IntStream.range(0, number).mapToObj(i -> createContentIndex(i)).collect(Collectors.toList());
  }

  /**
   * Tests if two entities are equal but ignores their id.
   *
   * @param first
   * @param second
   */
  public static void equals(final ContentIndex first, final ContentIndex second) {
    assertEquals(first.getIndex(), second.getIndex());
    assertEquals(first.getChapterId(), second.getChapterId());
    assertEquals(first.getContentId(), second.getContentId());
  }
}
