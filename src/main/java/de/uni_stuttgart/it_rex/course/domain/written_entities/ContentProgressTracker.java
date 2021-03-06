package de.uni_stuttgart.it_rex.course.domain.written_entities;

import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;

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
    @Column(name="progress_state")
    private ContentProgressState progressState;

    /**
     * A reference to the course-level progress tracking object for faster accumulation of data.
     */
    @ManyToOne
    @JoinColumn(name="course_progress_tracker_id", referencedColumnName = "id")
    private CourseProgressTracker courseProgressTracker;

    public UUID getId() {
        return id;
    }

    public ContentReference getContentReference() {
        return contentReference;
    }

    public UUID getUserId() {
        return userId;
    }

    public ContentProgressState getProgressState() {
        return progressState;
    }

    public CourseProgressTracker getCourseProgressTracker() {
        return courseProgressTracker;
    }

    /**
     * Sets the Tracker State to COMPLETED ({@link ContentProgressState})
     */
    public void complete(){
        this.progressState = ContentProgressState.COMPLETED;
    }
}
