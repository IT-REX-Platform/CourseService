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

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(final UUID courseId) {
        this.courseId = courseId;
    }

    public List<UUID> getContentReferenceIds() {
        return contentReferenceIds;
    }

    public void setContentReferenceIds(
        List<UUID> contentReferenceIds) {
        this.contentReferenceIds = contentReferenceIds;
    }

}
