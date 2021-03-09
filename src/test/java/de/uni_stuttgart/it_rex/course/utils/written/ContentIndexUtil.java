package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;

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
  public static ContentReference createContentIndex(final int index) {
    ContentReference contentReference = new ContentReference();
    contentReference.setIndex(index);
    contentReference.setContentId(UUID.randomUUID());
    return contentReference;
  }

  /**
   * Creates a random list of entities.
   *
   * @param number number of entities
   * @return the list
   */
  public static List<ContentReference> createContentIndexList(final int number) {
    return IntStream.range(0, number).mapToObj(i -> createContentIndex(i)).collect(Collectors.toList());
  }

  /**
   * Tests if two entities are equal but ignores their id.
   *
   * @param first
   * @param second
   */
  public static void equals(final ContentReference first, final ContentReference second) {
    assertEquals(first.getIndex(), second.getIndex());
    assertEquals(first.getChapterId(), second.getChapterId());
    assertEquals(first.getContentId(), second.getContentId());
  }
}