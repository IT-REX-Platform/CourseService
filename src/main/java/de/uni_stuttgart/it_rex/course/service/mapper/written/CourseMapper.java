package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;
import java.util.Optional;
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
  public abstract void updateCourseFromCourse(Course update,
                                              @MappingTarget Course toUpdate);

  /**
   * Updates an entity from a DTO.
   *
   * @param update   the update
   * @param toUpdate the updated entity.
   */
  public void updateCourseFromCourseDTO(final CourseDTO update,
                                        final Course toUpdate) {
    if (update.getId() != null) {
      toUpdate.setId(update.getId());
    }
    if (update.getName() != null) {
      toUpdate.setName(update.getName());
    }
    if (update.getCourseDescription() != null) {
      toUpdate.setCourseDescription(update.getCourseDescription());
    }
    if (update.getMaxFoodSum() != null) {
      toUpdate.setMaxFoodSum(update.getMaxFoodSum());
    }
    if (update.getStartDate() != null) {
      toUpdate.setStartDate(update.getStartDate());
    }
    if (update.getEndDate() != null) {
      toUpdate.setEndDate(update.getEndDate());
    }
    if (update.getPublishState() != null) {
      toUpdate.setPublishState(update.getPublishState());
    }
    if (update.getChapters() != null) {
      toUpdate.setChapters(uUIDsToChapterIndices(toUpdate.getId(),
          update.getChapters()));
    }
  }

  /**
   * Converts an entity to a DTO.
   *
   * @param course the entity
   * @return the dto
   */
  public CourseDTO toDTO(final Course course) {
    final CourseDTO courseDTO = setBasicProperties(course);
    if (course.getChapters() != null) {
      final List<UUID> chapters = course.getChapters().stream()
          .map(ChapterIndex::getChapterId).collect(Collectors.toList());
      courseDTO.setChapters(chapters);
    }
    return courseDTO;
  }

  /**
   * Converts a list of entities to a list of DTOs.
   *
   * @param courses the entities
   * @return the DTOs
   */
  public List<CourseDTO> toDTO(final List<Course> courses) {
    return courses.stream().map(this::toDTO).collect(Collectors.toList());
  }

  /**
   * Converts an optional entity to a optional DTO.
   *
   * @param course the entity
   * @return the dto
   */
  public Optional<CourseDTO> toDTO(final Optional<Course> course) {
    if (course.isEmpty()) {
      return Optional.empty();
    }
    return Optional.of(toDTO(course.get()));
  }

  /**
   * Converts a DTO to an entity.
   *
   * @param courseDTO the dto
   * @return the entity
   */
  public Course toEntity(final CourseDTO courseDTO) {
    final Course course = setBasicProperties(courseDTO);
    if (courseDTO.getChapters() != null) {
      final List<UUID> chapterIds = courseDTO.getChapters();
      final List<ChapterIndex> chapters = uUIDsToChapterIndices(course.getId(),
          chapterIds);
      course.setChapters(chapters);
    }

    return course;
  }

  /**
   * Converts a list of DTOs to a list of entities.
   *
   * @param courseDTOs the entities
   * @return the entities
   */
  public List<Course> toEntity(final List<CourseDTO> courseDTOs) {
    return courseDTOs.stream().map(this::toEntity).collect(Collectors.toList());
  }

  /**
   * Creates a CourseDTO from a Course and sets all properties
   * except the chapters.
   *
   * @param course the entity
   * @return the dto
   */
  private CourseDTO setBasicProperties(final Course course) {
    final CourseDTO courseDTO = new CourseDTO();
    courseDTO.setId(course.getId());
    courseDTO.setName(course.getName());
    courseDTO.setMaxFoodSum(course.getMaxFoodSum());
    courseDTO.setCourseDescription(course.getCourseDescription());
    courseDTO.setRemainActiveOffset(course.getRemainActiveOffset());
    courseDTO.setPublishState(course.getPublishState());
    courseDTO.setStartDate(course.getStartDate());
    courseDTO.setEndDate(course.getEndDate());
    return courseDTO;
  }

  /**
   * Creates a Course from a CourseDTO and sets all properties
   * except the chapters.
   *
   * @param courseDTO the dto
   * @return the entity
   */
  private Course setBasicProperties(final CourseDTO courseDTO) {
    final Course course = new Course();
    course.setId(courseDTO.getId());
    course.setName(courseDTO.getName());
    course.setMaxFoodSum(courseDTO.getMaxFoodSum());
    course.setCourseDescription(courseDTO.getCourseDescription());
    course.setRemainActiveOffset(courseDTO.getRemainActiveOffset());
    course.setPublishState(courseDTO.getPublishState());
    course.setStartDate(courseDTO.getStartDate());
    course.setEndDate(courseDTO.getEndDate());
    return course;
  }

  /**
   * Calculates a list of ChapterIndices from a list of UUIDs.
   *
   * @param courseId   the course id
   * @param chapterIds the list of UUIDs
   * @return the list of ChapterIndices
   */
  private List<ChapterIndex> uUIDsToChapterIndices(
      final UUID courseId,
      final List<UUID> chapterIds) {
    return IntStream.range(0, chapterIds.size()).mapToObj(i -> {
      final UUID id = chapterIds.get(i);
      ChapterIndex chapterIndex = new ChapterIndex();
      chapterIndex.setIndex(i);
      chapterIndex.setChapterId(id);
      chapterIndex.setCourseId(courseId);
      return chapterIndex;
    }).collect(Collectors.toList());
  }
}
