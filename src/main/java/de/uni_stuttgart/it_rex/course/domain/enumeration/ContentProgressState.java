package de.uni_stuttgart.it_rex.course.domain.enumeration;

/**
 * The progress state of any content in IT-REX.
 *
 * The progress is tracked per content reference entry (i.e. @{@link de.uni_stuttgart.it_rex.course.domain.written_entities.ContentIndex} as part of a @{@link de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter}.
 * Automatic progress changes should only happen in the order of this enumeration, so currently that is: STARTED -> COMPLETED
 * A state UNTOUCHED is currently not needed, as trackers for content items are created on first user access to a content.
 */
public enum ContentProgressState {
    /**
     * This state indicates: a user has accessed a content at least once.
     */
    STARTED,
    /**
     * This states indicates: a user has completed a content item, e.g., watched a video completely (at least one time).
     * This state is the final progress state.
     */
    COMPLETED
}
