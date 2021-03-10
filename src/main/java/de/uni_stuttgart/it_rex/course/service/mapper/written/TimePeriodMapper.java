package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import org.mapstruct.AfterMapping;
import org.mapstruct.BeanMapping;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring",
    uses = ContentReferenceMapper.class,
    injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public abstract class TimePeriodMapper {

    /**
     * The ContentReferenceRepository.
     */
    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    /**
     * The CourseRepository.
     */
    @Autowired
    private CourseRepository courseRepository;

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateTimePeriodFromTimePeriodDTO(
        TimePeriodDTO update,
        @MappingTarget TimePeriod toUpdate);

    /**
     * Converts an entity to a DTO.
     *
     * @param timePeriod the entity
     * @return the DTO
     */
    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "contentReferenceIds", ignore = true)
    public abstract TimePeriodDTO toDTO(TimePeriod timePeriod);

    /**
     * Sets the ContentReferenceIds when an entity is converted to a DTO.
     *
     * @param timePeriod    the entity
     * @param timePeriodDTO the DTO
     */
    @AfterMapping
    protected void setContentReferenceIds(
        final TimePeriod timePeriod,
        @MappingTarget final TimePeriodDTO timePeriodDTO) {
        timePeriodDTO.setContentReferenceIds(timePeriod.getContentReferences()
            .stream().map(ContentReference::getId)
            .collect(Collectors.toList()));
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param timePeriods the entities
     * @return the DTO
     */
    public abstract List<TimePeriodDTO> toDTO(
        Collection<TimePeriod> timePeriods);

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param timePeriod the optional
     * @return the dto
     */
    public Optional<TimePeriodDTO> toDTO(
        final Optional<TimePeriod> timePeriod) {
        if (timePeriod.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(timePeriod.get()));
    }

    /**
     * Converts a DTO to an entity.
     *
     * @param timePeriodDTO the DTO
     * @return the entity
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    public abstract TimePeriod toEntity(TimePeriodDTO timePeriodDTO);

    /**
     * Sets the relationships when a DTO is converted to an entity.
     *
     * @param timePeriodDTO the DTO
     * @param timePeriod    the entity
     */
    @AfterMapping
    protected void setRelationships(
        final TimePeriodDTO timePeriodDTO,
        @MappingTarget final TimePeriod timePeriod) {
        if (timePeriodDTO.getCourseId() != null) {
            courseRepository.findById(timePeriodDTO.getCourseId())
                .ifPresent(timePeriod::setCourse);
        }
        if (timePeriodDTO.getContentReferenceIds() != null) {
            timePeriod.setContentReferences(contentReferenceRepository
                .findAllById(timePeriodDTO.getContentReferenceIds()));
        }
    }

    /**
     * Converts a list of DTOs to a list of entities.
     *
     * @param timePeriodDTOs the DTOs
     * @return the entities
     */
    public abstract List<TimePeriod> toEntity(
        List<TimePeriodDTO> timePeriodDTOs);
}
