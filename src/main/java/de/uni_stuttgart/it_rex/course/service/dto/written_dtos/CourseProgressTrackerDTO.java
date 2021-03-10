package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;


import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentProgressTracker;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * The CourseProgressTrackerDTO serves to send the data of a @{@link de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker}
 * to the client and collect all @{@link de.uni_stuttgart.it_rex.course.domain.written_entities.ContentProgressTracker}-objects of a course.
 *
 * This DTO is not used to create {@link de.uni_stuttgart.it_rex.course.domain.written_entities.CourseProgressTracker}-objects.
 */
public class CourseProgressTrackerDTO {

    private UUID id;

    /**
     * Id of the course the represented tracker belongs to.
     */
    private UUID courseId;

    /**
     * Last accessed content ref.
     */
    private Optional<ContentReferenceDTO> lastContentReference;

    /**
     * A Map of Content Reference Id to respective Content Progress Trackers.
     */
    private Map<UUID, ContentProgressTrackerDTO> contentProgressTrackers;

    /**
     * Getter.
     *
     * @return the id.
     */
    public UUID getCourseId() {
        return courseId;
    }

    /**
     * Getter.
     *
     * @return the name.
     */
    public Optional<ContentReferenceDTO> getLastContentReference() {
        return lastContentReference;
    }

    /**
     * Getter.
     *
     * @return list containing content progress trackers.
     */
    public Map<UUID, ContentProgressTrackerDTO> getContentProgressTrackers() {
        return contentProgressTrackers;
    }

    /**
     * Setter.
     *
     * @param newCourseId the content ref id
     */
    public void setCourseId(final UUID newCourseId) {
        this.courseId = newCourseId;
    }

    /**
     * Setter.
     *
     * @param newLastContentReference the content ref id
     */
    public void setLastContentReference(final ContentReferenceDTO newLastContentReference) {
        this.lastContentReference = Optional.ofNullable(newLastContentReference);
    }

    public void setContentProgressTrackers(final Map<UUID, ContentProgressTrackerDTO> newContentProgressTrackers) {
        this.contentProgressTrackers = newContentProgressTrackers;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Equals method.
     *
     * @param o the other object.
     * @return if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseProgressTrackerDTO)) {
            return false;
        }
        CourseProgressTrackerDTO tracker = (CourseProgressTrackerDTO) o;
        return Objects.equals(getCourseId(), tracker.getCourseId())
            && Objects.equals(
            getLastContentReference(), tracker.getLastContentReference());
    }

    /**
     * Hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getCourseId(), getLastContentReference(), getContentProgressTrackers());
    }

    /**
     * Converts the instance to a string.
     *
     * @return the string.
     */
    @Override
    public String toString() {
        return "CourseProgressTrackerDTO{"
            + "courseId=" + courseId
            + ", lastContentRef='" + lastContentReference + '\''
            + '}';
    }
}
