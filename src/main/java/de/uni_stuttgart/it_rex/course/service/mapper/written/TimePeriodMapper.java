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

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

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

    @Mapping(target = "courseId", source = "course.id")
    @Mapping(target = "contentReferenceIds", ignore = true)
    public abstract TimePeriodDTO toDTO(final TimePeriod timePeriod);

    @AfterMapping
    protected void setContentReferenceIds(
        final TimePeriod timePeriod,
        @MappingTarget TimePeriodDTO timePeriodDTO) {
        timePeriodDTO.setContentReferenceIds(timePeriod.getContentReferences()
            .stream().map(ContentReference::getId)
            .collect(Collectors.toList()));
    }

    public abstract List<TimePeriodDTO> toDTO(
        final Collection<TimePeriod> timePeriods);

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

    @Mapping(target = "course", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    public abstract TimePeriod toEntity(final TimePeriodDTO timePeriodDTO);

    @AfterMapping
    protected void setMappings(
        TimePeriodDTO timePeriodDTO, @MappingTarget TimePeriod timePeriod) {
        if (timePeriodDTO.getCourseId() != null) {
            courseRepository.findById(timePeriodDTO.getCourseId())
                .ifPresent(timePeriod::setCourse);
        }
        if (timePeriodDTO.getContentReferenceIds() != null) {
            timePeriod.setContentReferences(contentReferenceRepository
                .findAllById(timePeriodDTO.getContentReferenceIds()));
        }
    }

    public abstract List<TimePeriod> toEntity(
        final List<TimePeriodDTO> timePeriodDTOs);
}
