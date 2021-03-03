package de.uni_stuttgart.it_rex.course.utils.written;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class ChapterIndexUtil {

  /**
   * Creates a random entity.
   *
   * @param courseId
   * @param index
   * @return the entity
   */
  public static TpChapterRelation createChapterIndex(final UUID courseId, final int index) {
    TpChapterRelation tpChapterRelation = new TpChapterRelation();
    tpChapterRelation.setIndex(index);
  //  chapterIndex.setChapter(UUID.randomUUID());
  //  chapterIndex.setTimePeriod(courseId);
    return tpChapterRelation;
  }

  /**
   * Creates a random entity.
   *
   * @param index
   * @return the entity
   */
  public static TpChapterRelation createChapterIndex(final int index) {
    return createChapterIndex(null, index);
  }

  /**
   * Creates a random list of entities.
   *
   * @param courseId the course id
   * @param number   number of entities
   * @return the list
   */
  public static List<TpChapterRelation> createChapterIndexList(final UUID courseId, final int number) {
    return IntStream.range(0, number).mapToObj(i -> createChapterIndex(courseId, i)).collect(Collectors.toList());
  }

  /**
   * Creates a random list of entities.
   *
   * @param number number of entities
   * @return the list
   */
  public static List<TpChapterRelation> createChapterIndexList(final int number) {
    return createChapterIndexList(null, number);
  }

  /**
   * Tests if two entities are equal but ignores their id.
   *
   * @param first
   * @param second
   */
  public static void equals(final TpChapterRelation first, final TpChapterRelation second) {
    assertEquals(first.getIndex(), second.getIndex());
    assertEquals(first.getChapter(), second.getChapter());
    assertEquals(first.getTimePeriod(), second.getTimePeriod());
  }
}
