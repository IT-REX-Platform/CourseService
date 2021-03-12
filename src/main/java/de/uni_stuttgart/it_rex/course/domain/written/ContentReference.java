package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.CONTENTREFERENCETYPE;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

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
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "content_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContentReference implements Serializable {

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Index of the content in the chapter.
     */
    @Column(name = "index")
    private int index;

    /**
     * Content id.
     */
    @Column(name = "content_id")
    private UUID contentId;

    /**
     * The CONTENTREFERENCETYPE.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "content_reference_type_state")
    private CONTENTREFERENCETYPE contentReferenceType;

    /**
     * The Chapters.
     */
    @ManyToOne
    @JoinColumn(name = "chapter_id", referencedColumnName = "id")
    protected Chapter chapter;

    /**
     * The TimePeriod.
     */
    @ManyToOne
    @JoinColumn(name = "time_period_id", referencedColumnName = "id")
    protected TimePeriod timePeriod;

    /**
     * Getter.
     *
     * @return the id.
     */
    public UUID getId() {
        return id;
    }

    /**
     * Setter.
     *
     * @param newId the id.
     */
    public void setId(final UUID newId) {
        this.id = newId;
    }

    /**
     * Getter.
     *
     * @return the content id
     */
    public UUID getContentId() {
        return contentId;
    }

    /**
     * Setter.
     *
     * @param newContentId the content id
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
     * @return chapters
     */
    public Chapter getChapter() {
        return chapter;
    }

    /**
     * Setter.
     *
     * @param newChapter the chapters
     */
    public void setChapter(final Chapter newChapter) {
        chapter = newChapter;
        newChapter.contentReferences.add(this);
        index = newChapter.contentReferences.size() - 1;
    }

    /**
     * Getter.
     *
     * @return time periods
     */
    public TimePeriod getTimePeriod() {
        return timePeriod;
    }

    /**
     * Setter.
     *
     * @param newTimePeriod the time periods
     */
    public void setTimePeriod(final TimePeriod newTimePeriod) {
        timePeriod = newTimePeriod;
        newTimePeriod.contentReferences.add(this);
    }

    /**
     * Getter.
     *
     * @return the index
     */
    public int getIndex() {
        return index;
    }

    /**
     * Setter.
     *
     * @param newIndex the index
     */
    public void setIndex(final int newIndex) {
        this.index = newIndex;
    }

    /**
     * Equals method.
     *
     * @param o the other instance.
     * @return if they are equal.
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ContentReference that = (ContentReference) o;
        return index == that.index && id.equals(that.id) &&
            contentId.equals(that.contentId) &&
            contentReferenceType == that.contentReferenceType &&
            chapter.equals(that.chapter) &&
            Objects.equals(timePeriod, that.timePeriod);
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(id, index, contentId, contentReferenceType, chapter,
            timePeriod);
    }

    /**
     * Converts the entity to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "ContentReference{" +
            "id=" + id +
            ", index=" + index +
            ", contentId=" + contentId +
            ", contentReferenceType=" + contentReferenceType +
            ", chapter=" + chapter +
            ", timePeriod=" + timePeriod +
            '}';
    }
}
