package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class TimePeriodMapper {

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private TimePeriodRepository timePeriodRepository;

    /**
     * Updates an entity from another entity.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateTimePeriodFromTimePeriodDTO(
        final TimePeriodDTO update,
        @MappingTarget final TimePeriod toUpdate);

    public TimePeriod updateOrCreateFromDTO(final TimePeriodDTO update) {
        final Optional<TimePeriod> contentReferenceOptional
            = timePeriodRepository.findById(update.getId());

        TimePeriod toUpdate = new TimePeriod();
        if (contentReferenceOptional.isPresent()) {
            toUpdate = contentReferenceOptional.get();
        }
        updateTimePeriodFromTimePeriodDTO(update, toUpdate);
        return toUpdate;
    }

    public TimePeriodDTO toDTO(final TimePeriod timePeriod) {
        final TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setId(timePeriod.getId());
        timePeriodDTO.setStartDate(timePeriod.getStartDate());
        timePeriodDTO.setEndDate(timePeriod.getEndDate());

        if (timePeriod.getCourse() != null) {
            timePeriodDTO.setCourseId(timePeriod.getCourse().getId());
        }

        timePeriodDTO.setContentReferenceIds(
            timePeriod.getContentReferences().stream()
                .map(ContentReference::getId).collect(
                Collectors.toList())
        );

        return timePeriodDTO;
    }

    public List<TimePeriodDTO> toDTO
        (final Collection<TimePeriod> timePeriods) {
        return timePeriods.stream().map(this::toDTO)
            .collect(Collectors.toList());
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param timePeriod the entity
     * @return the dto
     */
    public Optional<TimePeriodDTO> toDTO(final Optional<TimePeriod> timePeriod) {
        if (timePeriod.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(timePeriod.get()));
    }

    public TimePeriod toEntity(final TimePeriodDTO timePeriodDTO) {
        TimePeriod timePeriod = new TimePeriod();
        timePeriod.setId(timePeriodDTO.getId());
        timePeriod.setStartDate(timePeriodDTO.getStartDate());
        timePeriod.setEndDate(timePeriodDTO.getEndDate());

        if (timePeriodDTO.getCourseId() != null) {
            courseRepository.findById(timePeriodDTO.getCourseId())
                .ifPresent(timePeriod::setCourse);
        }
        if (timePeriodDTO.getContentReferenceIds() != null) {
            final List<ContentReference> contentReferences =
                contentReferenceRepository.findAllById(
                    timePeriodDTO.getContentReferenceIds());
            timePeriod.setContentReferences(contentReferences);
        }

        return timePeriod;
    }

    public List<TimePeriod> toEntity(final List<TimePeriodDTO> timePeriodDTOs) {
        return timePeriodDTOs.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
}
