package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import de.uni_stuttgart.it_rex.course.domain.enumeration.CONTENTREFERENCETYPE;

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
     * The CONTENTREFERENCETYPE.
     */
    private CONTENTREFERENCETYPE contentReferenceType;

    /**
     * The Course Id.
     */
    private UUID chapterId;

    /**
     * The Time Period Ids.
     */
    private UUID timePeriodId;

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
     * @return the Content Id
     */
    public UUID getContentId() {
        return contentId;
    }

    /**
     * Setter.
     *
     * @param newContentId the Content Id
     */
    public void setContentId(final UUID newContentId) {
        this.contentId = newContentId;
    }

    /**
     * Getter.
     *
     * @return the CONTENTREFERENCETYPE
     */
    public CONTENTREFERENCETYPE getContentReferenceType() {
        return contentReferenceType;
    }

    /**
     * Setter.
     *
     * @param newContentReferenceType the CONTENTREFERENCETYPE
     */
    public void setContentReferenceType(
        final CONTENTREFERENCETYPE newContentReferenceType) {
        this.contentReferenceType = newContentReferenceType;
    }

    /**
     * Getter.
     *
     * @return the Chapter Id.
     */
    public UUID getChapterId() {
        return chapterId;
    }

    /**
     * Setter.
     *
     * @param newChapterId the Chapter Id.
     */
    public void setChapterId(final UUID newChapterId) {
        this.chapterId = newChapterId;
    }

    /**
     * Getter.
     *
     * @return the TimePeriod id.
     */
    public UUID getTimePeriodId() {
        return timePeriodId;
    }

    /**
     * Setter.
     *
     * @param newTimePeriodId the TimePeriod id.
     */
    public void setTimePeriodId(final UUID newTimePeriodId) {
        this.timePeriodId = newTimePeriodId;
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
        if (!(o instanceof ContentReferenceDTO)) {
            return false;
        }
        final ContentReferenceDTO that = (ContentReferenceDTO) o;
        return Objects.equals(getId(), that.getId())
            && Objects.equals(getContentId(), that.getContentId())
            && Objects.equals(getChapterId(), that.getChapterId())
            && Objects.equals(getTimePeriodId(), that.getTimePeriodId());
    }

    /**
     * Hash code.
     *
     * @return the Hash code
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getContentId(),
            getChapterId(),
            getTimePeriodId());
    }

    /**
     * To String method.
     *
     * @return the String
     */
    @Override
    public String toString() {
        return "ContentReferenceDTO{"
            + "id=" + id
            + ", contentId=" + contentId
            + ", chapterId=" + chapterId
            + ", timePeriodId=" + timePeriodId + '}';
    }
}
