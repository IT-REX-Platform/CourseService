package de.uni_stuttgart.it_rex.course.domain.written_entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Models the progress of one user in one course.
 * <p>
 * The CourseProgressTracker stores the last content a user accessed and accumulates the progress of a user's {@link ContentProgressTracker}s of a course.
 * There is only one instance of this class per courseId-userId combination.
 */
@Entity
@Table(name = "course_progress_tracker")
public class CourseProgressTracker implements Serializable {

    /**
     * Intialize a CourseProgressTracker for a user in a course, specified by their respective Ids.
     *
     * @param courseId
     * @param userId
     */
    public CourseProgressTracker(final UUID courseId, final UUID userId) {
        this.courseId = courseId;
        this.userId = userId;
        this.contentProgressTrackers = new HashSet<>();

    }

    /**
     * Non-arg constructor for hibernate.
     */
    public CourseProgressTracker(){
        this.contentProgressTrackers = new HashSet<>();
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
    @ManyToOne
    @JoinColumn(name = "last_content_ref_id", referencedColumnName = "id", nullable = true)
    private ContentReference lastContentReference;

    /**
     * A reference to the content-level progress tracking objects for faster accumulation of data.
     */
    @OneToMany(mappedBy = "courseProgressTracker")
    private Set<ContentProgressTracker> contentProgressTrackers;

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
    public Optional<ContentReference> getLastContentReference() {
        return Optional.ofNullable(this.lastContentReference);
    }

    /**
     * Getter.
     *
     * @return the set of content progress trackers.
     */
    public Set<ContentProgressTracker> getContentProgressTrackers() {
        return this.contentProgressTrackers;
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
     * @param lastContentReference the content ref id
     */
    public void setLastContentReference(final ContentReference lastContentReference) {
        this.lastContentReference = lastContentReference;
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
        return Objects.hash(getId(), getCourseId(), getUserId(),
            getLastContentReference());
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
            + ", lastContentRef='" + lastContentReference + '\''
            + '}';
    }
}
