package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Course;
import de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.CourseProgressTrackerDTO;
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
public abstract class CourseProgressTrackerMapper {

    /**
     * Updates an entity from another entity.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateCourseProgressTrackerFromCourseProgressTracker(
        CourseProgressTracker update,
        @MappingTarget CourseProgressTracker toUpdate);


    /**
     * Converts an entity to a DTO.
     *
     * @param tracker the entity
     * @return the dto
     */
    public CourseProgressTrackerDTO toDTO(final CourseProgressTracker tracker) {
        final CourseProgressTrackerDTO trackerDTO = setBasicProperties(tracker);
        /*if (tracker.getChapters() != null) {
            final List<UUID> chapters = tracker.getChapters().stream()
                .map(ChapterIndex::getChapterId).collect(Collectors.toList());
            trackerDTO.setChapters(chapters);
        }*/
        return trackerDTO;
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
        trackerDTO.setLastContentRef(tracker.getLastContentRef());
        return trackerDTO;
    }
}
