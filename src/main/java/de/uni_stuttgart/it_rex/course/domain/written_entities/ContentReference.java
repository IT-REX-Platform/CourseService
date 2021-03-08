package de.uni_stuttgart.it_rex.course.domain.written_entities;

import net.logstash.logback.encoder.org.apache.commons.lang3.builder.EqualsBuilder;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
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

    public int getIndex() {
        return index;
    }

    public void setIndex(final int index) {
        this.index = index;
    }

    /**
     * Equals method.
     *
     * @param o the other instance.
     * @return if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ContentReference)) {
            return false;
        }
        final ContentReference contentReference = (ContentReference) o;
        return new EqualsBuilder()
            .append(id, contentReference.id)
            .isEquals();
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).toHashCode();
    }

}
