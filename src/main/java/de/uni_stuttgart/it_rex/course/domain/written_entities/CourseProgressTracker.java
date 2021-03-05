package de.uni_stuttgart.it_rex.course.domain.written_entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "courseprogresstracker")
public class CourseProgressTracker implements Serializable {

    /**
     * Constructor.
     */
    public CourseProgressTracker() {
        /*this.contents = new ArrayList<>();*/
    }

    /**
     * Id of the tracker.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * ID of the course that is being tracked.
     */
    @Column(name = "course_id")
    private UUID courseId;

    /**
     * ID of the user this tracker belongs to.
     */
    @Column(name = "user_id")
    private UUID userId;

    /**
     * Last accessed content ref.
     */
    @Column(name = "last_content_ref_id")
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
     * @return the id.
     */
    public UUID getCourseId() {
        return courseId;
    }

    /**
     * Getter.
     *
     * @return the id.
     */
    public UUID getUserId() {
        return userId;
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
     * @param newId
     */
    public void setId(final UUID newId) {
        this.id = newId;
    }

    /**
     * Setter.
     *
     * @param newCourseId
     */
    public void setCourseId(final UUID newCourseId) {
        this.courseId = newCourseId;
    }

    /**
     * Setter.
     *
     * @param newUserId the id
     */
    public void setUserId(final UUID newUserId) {
        this.userId = newUserId;
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
        if (!(o instanceof CourseProgressTracker)) {
            return false;
        }
        CourseProgressTracker tracker = (CourseProgressTracker) o;
        return Objects.equals(getId(), tracker.getId())
            && Objects.equals(getCourseId(), tracker.getCourseId())
            && Objects.equals(getUserId(), tracker.getUserId())
            && Objects.equals(getLastContentRef(), tracker.getLastContentRef());
    }

    /**
     * Hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(), getCourseId(), getUserId(),
            getLastContentRef());
    }

    /**
     * Converts the instance to a string.
     *
     * @return the string.
     */
    @Override
    public String toString() {
        return "CourseProgressTracker{"
            + "id=" + id
            + ", courseId='" + courseId + '\''
            + ", userId='" + userId + '\''
            + ", lastContentRef='" + lastContentRef + '\''
            + '}';
    }
}
