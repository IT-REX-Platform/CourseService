package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Mapper(componentModel = "spring")
public abstract class TimePeriodMapper {

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ChapterRepository chapterRepository;

  @Autowired
  private ChapterMapper chapterMapper;

  public TimePeriodDTO toDto(final TimePeriod timePeriod) {
    final TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
    timePeriodDTO.setId(timePeriod.getId());
    timePeriodDTO.setCourseId(timePeriod.getCourse().getId());
    timePeriodDTO.setStartDate(timePeriod.getStartDate());
    timePeriodDTO.setEndDate(timePeriod.getEndDate());

    List<ChapterDTO> chapterDTOs = timePeriod.getChapterIndices().stream()
        .map(chapterIndex -> chapterMapper.toDTO(chapterIndex.getChapter()))
        .collect(Collectors.toList());

    timePeriodDTO.setChapters(chapterDTOs);
    return timePeriodDTO;
  }

  public List<TimePeriodDTO> toDto(final List<TimePeriod> timePeriods) {
    return timePeriods.stream().map(timePeriod -> toDto(timePeriod))
        .collect(Collectors.toList());
  }

  public TimePeriod toEntity(final TimePeriodDTO timePeriodDTO) {
    TimePeriod timePeriod = new TimePeriod();
    timePeriod.setId(timePeriodDTO.getId());
    timePeriod.setStartDate(timePeriodDTO.getStartDate());
    timePeriod.setEndDate(timePeriodDTO.getEndDate());

    if (timePeriodDTO.getCourseId() != null) {
      final Optional<Course> courseOptional = courseRepository
          .findById(timePeriodDTO.getCourseId());
      courseOptional.ifPresent(course -> timePeriod.setCourse(course));
    }

    if (timePeriodDTO.getChapters() != null) {
      List<ChapterIndex> chapterIndices
          = toChapterIndices(timePeriodDTO.getChapters(), timePeriod);
      timePeriod.setChapterIndices(chapterIndices);
    }
    return timePeriod;
  }

  public List<TimePeriod> toEntity(final List<TimePeriodDTO> timePeriodDTOs) {
    return timePeriodDTOs.stream()
        .map(timePeriodDTO -> toEntity(timePeriodDTO))
        .collect(Collectors.toList());
  }

  private List<ChapterIndex> toChapterIndices(
      final List<ChapterDTO> chapterDTOS,
      final TimePeriod timePeriod) {
    return IntStream.range(0, chapterDTOS.size()).mapToObj(i -> {
      final ChapterIndex chapterIndex = new ChapterIndex();
      final Chapter chapter = chapterMapper.toEntity(chapterDTOS.get(i));
      chapterIndex.setIndex(i);
      chapterIndex.setTimePeriod(timePeriod);
      chapterIndex.setChapter(chapter);
      return chapterIndex;
    }).collect(Collectors.toList());
  }
}
