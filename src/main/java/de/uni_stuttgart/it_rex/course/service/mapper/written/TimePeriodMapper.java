package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.TimePeriodDTO;
import org.mapstruct.Mapper;
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

    public TimePeriodDTO toDto(final TimePeriod timePeriod) {
        final TimePeriodDTO timePeriodDTO = new TimePeriodDTO();
        timePeriodDTO.setId(timePeriod.getId());
        timePeriodDTO.setCourseId(timePeriod.getCourse().getId());
        timePeriodDTO.setStartDate(timePeriod.getStartDate());
        timePeriodDTO.setEndDate(timePeriod.getEndDate());

        timePeriodDTO.setContentReferenceIds(
            timePeriod.getContentReferences().stream()
                .map(ContentReference::getId).collect(
                Collectors.toList())
        );

        return timePeriodDTO;
    }

    public Collection<TimePeriodDTO> toDto(final Collection<TimePeriod> timePeriods) {
        return timePeriods.stream().map(this::toDto)
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
            courseOptional.ifPresent(timePeriod::setCourse);
        }
        if (timePeriodDTO.getContentReferenceIds() != null) {
            timePeriod.setContentReferences(
                contentReferenceRepository
                    .findAllById(
                        timePeriodDTO.getContentReferenceIds()));
        }

        return timePeriod;
    }

    public List<TimePeriod> toEntity(final List<TimePeriodDTO> timePeriodDTOs) {
        return timePeriodDTOs.stream()
            .map(this::toEntity)
            .collect(Collectors.toList());
    }
}
