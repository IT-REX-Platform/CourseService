package de.uni_stuttgart.it_rex.course.utils.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;

import java.time.LocalDate;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class CourseUtil {

  /**
   * Creates a random entity.
   *
   * @return the entity
   */
  public static final Course createCourse() {
    Course course = new Course();
    course.setName(StringUtil.generateRandomString(10, 50));
    course.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
    course.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
    course.setMaxFoodSum(NumbersUtil.generateRandomInteger(1000, 200000));
    course.setCourseDescription(StringUtil.generateRandomString(300, 600));
    course.setPublishState(PublishStateUtil.generateRandomPublishState());
    course.setChapters(ChapterIndexUtil.createChapterIndexList(course.getId(), NumbersUtil.generateRandomInteger(1, 22)));
    return course;
  }

  /**
   * Creates a random DTO.
   *
   * @return the entity
   */
  public static final CourseDTO createCourseDTO() {
    CourseDTO courseDTO = new CourseDTO();
    courseDTO.setName(StringUtil.generateRandomString(10, 50));
    courseDTO.setStartDate(LocalDate.now().minusDays(NumbersUtil.generateRandomInteger(20, 200)));
    courseDTO.setEndDate(LocalDate.now().plusDays(NumbersUtil.generateRandomInteger(20, 200)));
    courseDTO.setMaxFoodSum(NumbersUtil.generateRandomInteger(1000, 200000));
    courseDTO.setCourseDescription(StringUtil.generateRandomString(300, 600));
    courseDTO.setPublishState(PublishStateUtil.generateRandomPublishState());
    courseDTO.setChapters(IntStream.range(0, NumbersUtil.generateRandomInteger(1, 22)).mapToObj(i -> UUID.randomUUID()).collect(Collectors.toList()));
    return courseDTO;
  }

  /**
   * Tests if two entities are equal but ignores their id.
   *
   * @param first
   * @param second
   */
  public static void equals(final Course first, final Course second) {
    assertEquals(first.getName(), second.getName());
    assertEquals(first.getStartDate(), second.getStartDate());
    assertEquals(first.getEndDate(), second.getEndDate());
    assertEquals(first.getMaxFoodSum(), second.getMaxFoodSum());
    assertEquals(first.getPublishState(), second.getPublishState());
    assertEquals(first.getCourseDescription(), second.getCourseDescription());

    assertEquals(first.getChapters().size(), second.getChapters().size());
    IntStream.range(0, first.getChapters().size()).forEach(i -> ChapterIndexUtil.equals(first.getChapters().get(i), second.getChapters().get(i)));
  }

  /**
   * Tests if two DTOs are equal but ignores their id.
   *
   * @param first
   * @param second
   */
  public static void equals(final CourseDTO first, final CourseDTO second) {
    assertEquals(first.getName(), second.getName());
    assertEquals(first.getStartDate(), second.getStartDate());
    assertEquals(first.getEndDate(), second.getEndDate());
    assertEquals(first.getMaxFoodSum(), second.getMaxFoodSum());
    assertEquals(first.getPublishState(), second.getPublishState());
    assertEquals(first.getCourseDescription(), second.getCourseDescription());
    assertEquals(first.getChapters(), second.getChapters());
  }
}
