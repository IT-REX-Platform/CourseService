package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class CourseProgressTrackerDTO {

    /**
     * Id of the tracker.
     */
    private UUID id;

    /**
     * Last accessed content ref.
     */
    private Optional<ContentReferenceDTO> lastContentReference;

    /**
     * Getter.
     *
     * @return the id.
     */
    public UUID getId() {
        return id;
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
     * Setter.
     *
     * @param lastContentReference the content ref id
     */
    public void setLastContentReference(final ContentReferenceDTO lastContentReference) {
        this.lastContentReference = Optional.ofNullable(lastContentReference);
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
        return Objects.equals(getId(), tracker.getId())
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
        return Objects.hash(getId(), getLastContentReference());
    }

    /**
     * Converts the instance to a string.
     *
     * @return the string.
     */
    @Override
    public String toString() {
        return "CourseProgressTrackerDTO{"
            + "id=" + id
            + ", lastContentRef='" + lastContentReference + '\''
            + '}';
    }
}
