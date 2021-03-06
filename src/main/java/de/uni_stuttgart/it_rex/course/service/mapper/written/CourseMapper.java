package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CourseMapper {

    @Autowired
    private TimePeriodMapper timePeriodMapper;

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private ContentReferenceMapper contentReferenceMapper;


    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "timePeriods", ignore = true)
    @Mapping(target = "chapters", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateCourseFromCourseDTO(
        final CourseDTO update,
        @MappingTarget final Course toUpdate);

    /**
     * Converts an entity to a DTO.
     *
     * @param course the entity
     * @return the dto
     */
    public CourseDTO toDTO(final Course course) {
        final CourseDTO courseDTO = setBasicProperties(course);
        if (course.getContentReferences() != null) {
            final List<ContentReferenceDTO> contentReferenceDTOs =
                contentReferenceMapper.toDTO(course.getContentReferences()).
                    stream().collect(Collectors.toList());
            courseDTO.setContentReferences(contentReferenceDTOs);
        }
        if (course.getTimePeriods() != null) {
            final List<TimePeriodDTO> timePeriods = timePeriodMapper
                .toDTO(course.getTimePeriods()).
                    stream().collect(Collectors.toList());
            courseDTO.setTimePeriods(timePeriods);
        }
        if (course.getChapters() != null) {
            final List<ChapterDTO> chapters = chapterMapper
                .toDTO(course.getChapters()).
                    stream().collect(Collectors.toList());
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
    public List<CourseDTO> toDTO(final Collection<Course> courses) {
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
        setMappingProperties(courseDTO, course);
        return course;
    }

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param courseDTOs the entities
     * @return the entities
     */
    public List<Course> toEntity(final List<CourseDTO> courseDTOs) {
        return courseDTOs.stream().map(this::toEntity)
            .collect(Collectors.toList());
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

    private void setMappingProperties(final CourseDTO courseDTO,
                                      final Course course) {
        if (courseDTO.getContentReferences() != null) {
            course.setContentReferences(contentReferenceMapper
                .toEntity(courseDTO.getContentReferences()));
        }
        if (courseDTO.getChapters() != null) {
            course.setChapters(chapterMapper
                .toEntity(courseDTO.getChapters()));
        }
        if (courseDTO.getTimePeriods() != null) {
            course.setTimePeriods(timePeriodMapper
                .toEntity(courseDTO.getTimePeriods()));
        }
    }
}
