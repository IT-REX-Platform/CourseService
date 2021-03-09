package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.util.Objects;
import java.util.UUID;

public class ContentReferenceDTO {
    /**
     * Id.
     */
    private UUID id;

    /**
     * Content id.
     */
    private UUID contentId;

    /**
     * The Course Id.
     */
    private UUID chapterId;

    /**
     * The Time Period Ids.
     */
    private UUID timePeriodId;

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public UUID getContentId() {
        return contentId;
    }

    public void setContentId(UUID contentId) {
        this.contentId = contentId;
    }

    public UUID getChapterId() {
        return chapterId;
    }

    public void setChapterId(UUID chapterId) {
        this.chapterId = chapterId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentReferenceDTO)) {
            return false;
        }
        final ContentReferenceDTO that = (ContentReferenceDTO) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getContentId(), that.getContentId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(),
            getContentId(),
            getChapterId());
    }

    public UUID getTimePeriodId() {
        return timePeriodId;
    }

    public void setTimePeriodId(final UUID timePeriodId) {
        this.timePeriodId = timePeriodId;
    }
}
