package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;


import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

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
     * Content Progress Trackers.
     */
    private Set<ContentProgressTrackerDTO> contentProgressTrackers;

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
    public Set<ContentProgressTrackerDTO> getContentProgressTrackers() {
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

    /**
     * Setter.
     *
     * @param newContentProgressTrackers the content ref id
     */
    public void setContentProgressTrackers(final Set<ContentProgressTrackerDTO> newContentProgressTrackers) {
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
