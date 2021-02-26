package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseMapperImpl.class})
class CourseMapperIT {
  private static final UUID UPDATE_ID = UUID.randomUUID();
  private static final int MAX_FOOD_SUM = 42;

  @Autowired
  private CourseMapper courseMapper;

  @Test
  void updateCourseFromCourse() {
    Course toUpdate = CourseUtil.createCourse();
    Course update = new Course();

    update.setId(UPDATE_ID);
    update.setName(null);
    update.setMaxFoodSum(MAX_FOOD_SUM);

    Course expected = new Course();
    expected.setId(update.getId());
    expected.setName(toUpdate.getName());
    expected.setMaxFoodSum(update.getMaxFoodSum());
    expected.setCourseDescription(toUpdate.getCourseDescription());
    expected.setChapters(toUpdate.getChapters());
    expected.setPublishState(toUpdate.getPublishState());
    expected.setStartDate(toUpdate.getStartDate());
    expected.setEndDate(toUpdate.getEndDate());

    courseMapper.updateCourseFromCourse(update, toUpdate);

    assertEquals(expected, toUpdate);
  }

  @Test
  void toDTO() {
    Course course = CourseUtil.createCourse();
    CourseDTO expected = new CourseDTO();
    expected.setId(course.getId());
    expected.setName(course.getName());
    expected.setMaxFoodSum(course.getMaxFoodSum());
    expected.setCourseDescription(course.getCourseDescription());
    expected.setPublishState(course.getPublishState());
    expected.setStartDate(course.getStartDate());
    expected.setEndDate(course.getEndDate());

    List<UUID> chapters = course.getChapters().stream()
        .map(ChapterIndex::getChapterId).collect(Collectors.toList());
    expected.setChapters(chapters);

    CourseDTO result = courseMapper.toDTO(course);
    assertEquals(expected, result);
  }

  @Test
  void toEntity() {
    CourseDTO courseDTO = CourseUtil.createCourseDTO();
    Course expected = new Course();
    expected.setId(courseDTO.getId());
    expected.setName(courseDTO.getName());
    expected.setMaxFoodSum(courseDTO.getMaxFoodSum());
    expected.setCourseDescription(courseDTO.getCourseDescription());
    expected.setPublishState(courseDTO.getPublishState());
    expected.setStartDate(courseDTO.getStartDate());
    expected.setEndDate(courseDTO.getEndDate());

    final List<UUID> chapterIds = courseDTO.getChapters();
    List<ChapterIndex> chapters =
        IntStream.range(0, chapterIds.size()).mapToObj(i -> {
          final UUID id = chapterIds.get(i);
          ChapterIndex chapterIndex = new ChapterIndex();
          chapterIndex.setId(UUID.randomUUID());
          chapterIndex.setIndex(i);
          chapterIndex.setChapterId(id);
          chapterIndex.setCourseId(expected.getId());
          return chapterIndex;
        }).collect(Collectors.toList());

    expected.setChapters(chapters);

    Course result = courseMapper.toEntity(courseDTO);
    assertEquals(expected, result);
  }
}