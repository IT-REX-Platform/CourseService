package de.uni_stuttgart.it_rex.course.domain.written_entities;

import net.logstash.logback.encoder.org.apache.commons.lang3.builder.EqualsBuilder;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.HashCodeBuilder;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chapter implements Serializable {

    /**
     * Constructor.
     */
    public Chapter() {
        this.contentReferences = new ArrayList<>();
    }

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Name of the chapter.
     */
    @Column(name = "name")
    private String name;

    /**
     * The Course.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    protected Course course;

    /**
     * Content items.
     */
    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "chapter")
    @OrderBy("index")
    protected final List<ContentReference> contentReferences;

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
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Setter.
     *
     * @param newTitle the name
     */
    public void setName(final String newTitle) {
        this.name = newTitle;
    }

    /**
     * Getter.
     *
     * @return the content references
     */
    public List<ContentReference> getContentReferences() {
        return contentReferences;
    }

    /**
     * Setter.
     *
     * @param newContentReferences the content references
     */
    public void setContentReferences(final Collection<ContentReference> newContentReferences) {
        if (newContentReferences == null) {
            return;
        }
        contentReferences.clear();
        addContentReferences(newContentReferences);
    }

    /**
     * Adds a Content Reference.
     *
     * @param newContentReference the content reference
     */
    public void addContentReference(final ContentReference newContentReference) {
        if (newContentReference == null) {
            return;
        }
        newContentReference.chapter = this;
        contentReferences.add(newContentReference);
    }

    /**
     * Adds a set of Content References.
     *
     * @param newContentReferences the content references
     */
    public void addContentReferences(final Collection<ContentReference> newContentReferences) {
        if (newContentReferences == null) {
            return;
        }
        contentReferences.addAll(newContentReferences.stream().map(newContentReference -> {
            newContentReference.chapter = this;
            return newContentReference;
        }).collect(Collectors.toList()));
    }

    /**
     * Removes a Content Reference.
     *
     * @param newContentReference the content references
     */
    public void removeContentReference(final ContentReference newContentReference) {
        newContentReference.chapter = null;
        getContentReferences().remove(newContentReference);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(final Course newCourse) {
        if (course != null) {
            course.removeChapter(this);
        }
        newCourse.getChapters().add(this);
        this.course = newCourse;
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
        if (!(o instanceof Chapter)) {
            return false;
        }
        final Chapter chapter = (Chapter) o;
        return new EqualsBuilder()
            .append(id, chapter.id)
            .append(course, chapter.course)
            .isEquals();
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return new HashCodeBuilder().append(id).append(course).toHashCode();
    }
}
