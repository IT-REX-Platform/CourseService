package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class CourseProgressTrackerMapper {

    @Autowired
    private ContentReferenceMapper contentReferenceMapper;

    @Autowired
    private ContentProgressTrackerMapper contentProgressTrackerMapper;

    /**
     * Converts an entity to a DTO.
     *
     * @param tracker the entity
     * @return the dto
     */
    public CourseProgressTrackerDTO toDTO(final CourseProgressTracker tracker) {
        return setBasicProperties(tracker);
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param trackers the entities
     * @return the DTOs
     */
    public List<CourseProgressTrackerDTO> toDTO(
        final List<CourseProgressTracker> trackers) {
        return trackers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param tracker the entity
     * @return the dto
     */
    public Optional<CourseProgressTrackerDTO> toDTO(
        final Optional<CourseProgressTracker> tracker) {
        if (tracker.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(tracker.get()));
    }

    /**
     * Creates a CourseProgressTrackerDTO from a CourseProgressTracker
     * and sets all properties except the chapters.
     *
     * @param tracker the entity
     * @return the dto
     */
    private CourseProgressTrackerDTO setBasicProperties(
        final CourseProgressTracker tracker) {
        final CourseProgressTrackerDTO trackerDTO =
            new CourseProgressTrackerDTO();
        Optional<ContentReference> optionalContentReference = tracker.getLastContentReference();
        ContentReferenceDTO lastContentReference = optionalContentReference.map(
            contentReference -> contentReferenceMapper.toDTO(contentReference))
            .orElse(null);
        trackerDTO.setId(tracker.getId());
        trackerDTO.setCourseId(tracker.getCourseId());
        trackerDTO.setLastContentReference(lastContentReference);

        Map<UUID, ContentProgressTrackerDTO> contentProgressTrackers = new HashMap<>();
        for (ContentProgressTracker contentProgressTracker : tracker.getContentProgressTrackers()){
            contentProgressTrackers.put(
                contentProgressTracker.getContentReference().getId(),
                contentProgressTrackerMapper.toDTO(contentProgressTracker)
            );
        }

        trackerDTO.setContentProgressTrackers(contentProgressTrackers);

        return trackerDTO;
    }
}
