package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.Collection;
import java.util.List;
import java.util.Optional;


@Mapper(componentModel = "spring",
    uses = {ChapterMapper.class, TimePeriodMapper.class},
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class CourseMapper {

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "timePeriods", source = "timePeriods",
        dependsOn = "chapters")
    public abstract void updateCourseFromCourseDTO(
        CourseDTO update,
        @MappingTarget Course toUpdate);

    /**
     * Converts an entity to a DTO.
     *
     * @param course the entity
     * @return the dto
     */
    @Mapping(target = "courseRole", ignore = true)
    public abstract CourseDTO toDTO(final Course course);

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param courses the entities
     * @return the DTOs
     */
    public abstract List<CourseDTO> toDTO(final Collection<Course> courses);

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
    @Mapping(target = "timePeriods", source = "timePeriods",
        dependsOn = "chapters")
    public abstract Course toEntity(final CourseDTO courseDTO);

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param courseDTOs the entities
     * @return the entities
     */
    public abstract List<Course> toEntity(final List<CourseDTO> courseDTOs);
}
