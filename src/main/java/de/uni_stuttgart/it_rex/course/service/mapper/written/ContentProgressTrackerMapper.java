package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written.ContentProgressTracker;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentProgressTrackerDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ContentProgressTrackerMapper {

    @Autowired
    private ContentReferenceMapper contentReferenceMapper;

    /**
     * Converts an entity to a DTO.
     *
     * @param tracker the entity
     * @return the dto
     */
    public ContentProgressTrackerDTO toDTO(final ContentProgressTracker tracker) {
        return setBasicProperties(tracker);
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param trackers the entities
     * @return the DTOs
     */
    public List<ContentProgressTrackerDTO> toDTO(
        final List<ContentProgressTracker> trackers) {
        return trackers.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param tracker the entity
     * @return the dto
     */
    public Optional<ContentProgressTrackerDTO> toDTO(
        final Optional<ContentProgressTracker> tracker) {
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
    private ContentProgressTrackerDTO setBasicProperties(
        final ContentProgressTracker tracker) {
        final ContentProgressTrackerDTO trackerDTO =
            new ContentProgressTrackerDTO();

        trackerDTO.setId(tracker.getId());
        trackerDTO.setContentReference(contentReferenceMapper.toDTO(tracker.getContentReference()));
        trackerDTO.setState(tracker.getState());
        trackerDTO.setProgress(tracker.getProgress());
        return trackerDTO;
    }
}
