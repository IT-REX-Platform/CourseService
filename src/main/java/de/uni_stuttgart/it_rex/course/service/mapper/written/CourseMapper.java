package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {
  /**
   * Updates an entity from another entity.
   *
   * @param update   the update
   * @param toUpdate the updated entity.
   */
  @BeanMapping(nullValuePropertyMappingStrategy =
      NullValuePropertyMappingStrategy.IGNORE)
  public abstract void updateCourseFromCourse(Course update, @MappingTarget Course toUpdate);

  /**
   * Converts an entity to a DTO.
   *
   * @param course the entity
   * @return the dto
   */
  public CourseDTO toDTO(Course course) {
    /*
     * The test does basically the same thing because the utility functions
     * to create DTOs was used;
     */
    final CourseDTO courseDTO = new CourseDTO();
    courseDTO.setId(course.getId());
    courseDTO.setName(course.getName());
    courseDTO.setMaxFoodSum(course.getMaxFoodSum());
    courseDTO.setCourseDescription(course.getCourseDescription());
    courseDTO.setPublishState(course.getPublishState());
    courseDTO.setStartDate(course.getStartDate());
    courseDTO.setEndDate(course.getEndDate());

    final List<UUID> chapters = course.getChapters().stream()
        .map(ChapterIndex::getChapterId).collect(Collectors.toList());
    courseDTO.setChapters(chapters);

    return courseDTO;
  }

  /**
   * Converts a DTO to an entity.
   *
   * @param courseDTO the dto
   * @return the entity
   */
  public Course toEntity(CourseDTO courseDTO) {
    /*
     * The test does basically the same thing because the utility functions
     * to create entities was used;
     */
    final Course course = new Course();
    course.setId(courseDTO.getId());
    course.setName(courseDTO.getName());
    course.setMaxFoodSum(courseDTO.getMaxFoodSum());
    course.setCourseDescription(courseDTO.getCourseDescription());
    course.setPublishState(courseDTO.getPublishState());
    course.setStartDate(courseDTO.getStartDate());
    course.setEndDate(courseDTO.getEndDate());

    final List<UUID> chapterIds = courseDTO.getChapters();
    final List<ChapterIndex> chapters =
        IntStream.range(0, chapterIds.size()).mapToObj(i -> {
          final UUID id = chapterIds.get(i);
          ChapterIndex chapterIndex = new ChapterIndex();
          chapterIndex.setId(UUID.randomUUID());
          chapterIndex.setIndex(i);
          chapterIndex.setChapterId(id);
          chapterIndex.setCourseId(course.getId());
          return chapterIndex;
        }).collect(Collectors.toList());

    course.setChapters(chapters);
    return course;
  }
}
