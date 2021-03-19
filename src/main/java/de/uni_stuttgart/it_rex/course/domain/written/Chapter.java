package de.uni_stuttgart.it_rex.course.domain.written;

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

/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chapter implements Serializable {

    public static final int HASH_CODE = 13;

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
     * Number of the chapter.
     */
    @Column(name = "chapter_number")
    private int chapterNumber;

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
     * @return the chapter number.
     */
    public int getChapterNumber() {
        return chapterNumber;
    }

    /**
     * Setter.
     *
     * @param newChapterNumber the chapter number.
     */
    public void setChapterNumber(final int newChapterNumber) {
        this.chapterNumber = newChapterNumber;
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
    public void setContentReferences(
        final Collection<ContentReference> newContentReferences) {
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
    public void addContentReference(
        final ContentReference newContentReference) {
        if (newContentReference == null) {
            return;
        }
        contentReferences.add(newContentReference);
        newContentReference.chapter = this;
        newContentReference.setIndex(this.contentReferences.size() - 1);
    }

    /**
     * Adds a set of Content References.
     *
     * @param newContentReferences the content references
     */
    public void addContentReferences(
        final Collection<ContentReference> newContentReferences) {
        if (newContentReferences == null) {
            return;
        }
        for (ContentReference c : newContentReferences) {
            addContentReference(c);
        }
    }

    /**
     * Removes a Content Reference.
     *
     * @param newContentReference the content references
     */
    public void removeContentReference(
        final ContentReference newContentReference) {
        newContentReference.chapter = null;
        getContentReferences().remove(newContentReference);
    }

    /**
     * Getter.
     *
     * @return the Course
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the Course and the appropriate relationship.
     *
     * @param newCourse the Course
     */
    public void setCourse(final Course newCourse) {
        if (course != null) {
            course.removeChapter(this);
        }
        newCourse.getChapters().add(this);
        this.course = newCourse;
    }

    /**
     * Equals method.
     * <p>
     * Only compare the Id (primary key) here as Hibernate handles the rest.
     *
     * @param o the other instance.
     * @return if they are equal.
     */
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Chapter chapter = (Chapter) o;
        return id != null && id.equals(chapter.getId());
    }

    /**
     * Hash code.
     * <p>
     * Use a constant value here because the Id is generated and set when
     * persisting and can be null before that.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return HASH_CODE;
    }

    /**
     * Converts the entity to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Chapter{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", chapterNumber=" + chapterNumber +
            ", course=" + course +
            ", contentReferences=" + contentReferences +
            '}';
    }
}
