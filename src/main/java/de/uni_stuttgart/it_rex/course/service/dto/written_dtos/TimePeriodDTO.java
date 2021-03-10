package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TimePeriodDTO {

    /**
     * Id.
     */
    private UUID id;

    /**
     * Start date.
     */
    private LocalDate startDate;

    /**
     * End date.
     */
    private LocalDate endDate;

    /**
     * Course id.
     */
    private UUID courseId;

    /**
     * Content Reference ids.
     */
    private List<UUID> contentReferenceIds;

    /**
     * Getter.
     *
     * @return the id
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter.
     *
     * @param newId the id
     */
    public void setId(final UUID newId) {
        this.id = newId;
    }

    /**
     * Getter.
     *
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter.
     *
     * @param newStartDate the start date
     */
    public void setStartDate(final LocalDate newStartDate) {
        this.startDate = newStartDate;
    }

    /**
     * Getter.
     *
     * @return the end date
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter.
     *
     * @param newEndDate the end date
     */
    public void setEndDate(final LocalDate newEndDate) {
        this.endDate = newEndDate;
    }

    /**
     * Getter.
     *
     * @return the Course Id
     */
    public UUID getCourseId() {
        return courseId;
    }

    /**
     * Setter.
     *
     * @param newCourseId the Course Id
     */
    public void setCourseId(final UUID newCourseId) {
        this.courseId = newCourseId;
    }

    /**
     * Getter.
     *
     * @return the ContentReference Ids
     */
    public List<UUID> getContentReferenceIds() {
        return contentReferenceIds;
    }

    /**
     * Setter.
     *
     * @param newContentReferenceIds the ContentReference Ids
     */
    public void setContentReferenceIds(
        final List<UUID> newContentReferenceIds) {
        this.contentReferenceIds = newContentReferenceIds;
    }

    /**
     * Equals method.
     *
     * @param o the other object
     * @return if they are equal
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof TimePeriodDTO)) {
            return false;
        }
        final TimePeriodDTO that = (TimePeriodDTO) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getStartDate(), that.getStartDate())
            && Objects.equals(getEndDate(), that.getEndDate())
            && Objects.equals(getCourseId(), that.getCourseId())
            && Objects.equals(getContentReferenceIds(),
            that.getContentReferenceIds());
    }

    /**
     * Hash code.
     *
     * @return the hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getStartDate(),
            getEndDate(),
            getCourseId(),
            getContentReferenceIds());
    }

    /**
     * To String method.
     *
     * @return the String
     */
    @Override
    public String toString() {
        return "TimePeriodDTO{"
            + "id=" + id
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", courseId=" + courseId
            + ", contentReferenceIds=" + contentReferenceIds
            + '}';
    }
}
