package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.util.Objects;
import java.util.UUID;

public class CourseProgressTrackerDTO {

    /**
     * Id of the tracker.
     */

    private UUID id;

    /**
     * Last accessed content ref.
     */

    private UUID lastContentRef;

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
    public UUID getLastContentRef() {
        return lastContentRef;
    }

    /**
     * Setter.
     *
     * @param newContentRef the content ref id
     */
    public void setLastContentRef(final UUID newContentRef) {
        this.lastContentRef = newContentRef;
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
            && Objects.equals(getLastContentRef(), tracker.getLastContentRef());
    }

    /**
     * Hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getLastContentRef());
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
            + ", lastContentRef='" + lastContentRef + '\''
            + '}';
    }
}
