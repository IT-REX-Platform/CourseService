package de.uni_stuttgart.it_rex.course.domain.written_entities;

import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;
import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * The ContentProgressTracker stores the progress a user achieves working with a content item.
 *
 * To achieve this, the object relates a {@link ContentReference} to a user and stores the respective {@link ContentProgressState}.
 */
@Entity
@Table(name = "content_progress_tracker")
public class ContentProgressTracker implements Serializable {

    @Id
    @GeneratedValue
    private UUID id;

    @ManyToOne()
    @JoinColumn(name="content_ref_id", referencedColumnName = "id")
    private ContentReference contentReference;

    @Column(name="user_id")
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(name="state")
    private ContentProgressState state;

    /**
     * A generic field for storing the progress of an individual content reference.
     * Might be a percentage for s.th., the seconds of a video, ...
     */
    @Column(name="progress")
    private float progress;

    /**
     * A reference to the course-level progress tracking object for faster accumulation of data.
     */
    @ManyToOne
    @JoinColumn(name="course_progress_tracker_id", referencedColumnName = "id")
    private CourseProgressTracker courseProgressTracker;

    /**
     * Empty contstructor for hibernate.
     */
    public ContentProgressTracker(){
    }

    /**
     * Initialize a ContentProgressTracker with state "STARTED" and progress 0.0 for the given contentReference and userId.
     * @param userId
     * @param contentReference
     */
    public ContentProgressTracker(final UUID userId, final ContentReference contentReference, final CourseProgressTracker courseProgressTracker){
        this.userId = userId;
        this.contentReference = contentReference;
        this.state = ContentProgressState.STARTED;
        this.progress = 0.0f;
        this.courseProgressTracker = courseProgressTracker;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public ContentReference getContentReference() {
        return contentReference;
    }

    public UUID getUserId() {
        return userId;
    }

    public ContentProgressState getState() {
        return state;
    }

    public float getProgress() { return progress; }

    public CourseProgressTracker getCourseProgressTracker() {
        return courseProgressTracker;
    }

    /**
     * Sets the Tracker State to COMPLETED ({@link ContentProgressState}).
     */
    public void complete() {
        this.state = ContentProgressState.COMPLETED;
    }

    /**
     * Setter.
     *
     * @param newProgress the progress to set.
     */
    public void setProgress(final float newProgress) {
        this.progress = newProgress;
    }
}
