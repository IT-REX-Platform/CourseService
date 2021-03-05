package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class ContentReferenceDTO {
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
     * Content id.
     */
    private UUID contentId;

    /**
     * The Chapter Ids.
     */
    private Set<UUID> chapterIds;

    /**
     * The Time Period Ids.
     */
    private Set<UUID> timePeriodIds;

    /**
     * The Course Id.
     */
    private UUID courseId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }

    public Set<UUID> getChapterIds() {
        return chapterIds;
    }

    public void setChapterIds(Set<UUID> chapterIds) {
        this.chapterIds = chapterIds;
    }

    public Set<UUID> getTimePeriodIds() {
        return timePeriodIds;
    }

    public void setTimePeriodIds(Set<UUID> timePeriodIds) {
        this.timePeriodIds = timePeriodIds;
    }

    public UUID getCourseId() {
        return courseId;
    }

    public void setCourseId(UUID courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {return true;}
        if (!(o instanceof ContentReferenceDTO)) {return false;}
        final ContentReferenceDTO that = (ContentReferenceDTO) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getStartDate(), that.getStartDate())
            && Objects.equals(getEndDate(), that.getEndDate())
            && Objects.equals(getContentId(), that.getContentId())
            && Objects.equals(getChapterIds(), that.getChapterIds())
            && Objects.equals(getTimePeriodIds(), that.getTimePeriodIds())
            && Objects.equals(getCourseId(), that.getCourseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
            getStartDate(),
            getEndDate(),
            getContentId(),
            getChapterIds(),
            getTimePeriodIds(),
            getCourseId());
    }
}
