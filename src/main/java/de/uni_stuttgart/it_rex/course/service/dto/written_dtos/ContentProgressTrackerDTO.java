package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import de.uni_stuttgart.it_rex.course.domain.enumeration.ContentProgressState;
import java.util.Objects;
import java.util.UUID;

public class ContentProgressTrackerDTO {

    private UUID id;

    /**
     * The content reference the represented tracker belongs to.
     */
    private ContentReferenceDTO contentReference;

    /**
     * Content progress state
     */
    private ContentProgressState state;

    /**
     * Tracked Progress
     */
    private float progress;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    /**
     * Getter.
     *
     * @return the id.
     */
    public ContentReferenceDTO getContentReference() {
        return contentReference;
    }

    /**
     * Getter.
     *
     * @return the state.
     */
    public ContentProgressState getState() {
        return state;
    }

    /**
     * Getter.
     *
     * @return the progress.
     */
    public float getProgress() {
        return progress;
    }

    /**
     * Setter.
     *
     * @param newContentReference the new reference
     */
    public void setContentReference(final ContentReferenceDTO newContentReference) {
        this.contentReference = newContentReference;
    }

    /**
     * Setter.
     *
     * @param newState new completion state
     */
    public void setState(final ContentProgressState newState) {
        this.state = newState;
    }

    /**
     * Setter.
     *
     * @param newProgress progress
     */
    public void setProgress(final float newProgress) {
        this.progress = newProgress;
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
        ContentProgressTrackerDTO tracker = (ContentProgressTrackerDTO) o;
        return Objects.equals(getContentReference(),
            tracker.getContentReference())
            && Objects.equals(getState(), tracker.getState())
            && (getProgress() == tracker.getProgress());
    }

    /**
     * Hash code.
     *
     * @return
     */
    @Override
    public int hashCode() {
        return Objects.hash(getContentReference(), getState(), getProgress());
    }

    /**
     * Converts the instance to a string.
     *
     * @return the string.
     */
    @Override
    public String toString() {
        return "ContentProgressTrackerDTO{"
            + ", id='" + id + '\''
            + ", state='" + state + '\''
            + ", progress='" + progress + '\''
            + '}';
    }
}
