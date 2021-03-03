package de.uni_stuttgart.it_rex.course.domain.written_entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
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
        this.contentReferences = new HashSet<>();
    }

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Title of the chapter.
     */
    @Column(name = "title")
    private String title;

    /**
     * Course id.
     */
    @Column(name = "course_id")
    private UUID courseId;

    /**
     * Start date of the Chapter.
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * End date of the Chapter.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Content items.
     */
    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "chapter_content",
        joinColumns = {
            @JoinColumn(name = "chapter_id", referencedColumnName = "id")},
        inverseJoinColumns = {
            @JoinColumn(name = "content_id", referencedColumnName = "id")}
    )
    @OrderBy("startDate")
    protected final Set<ContentReference> contentReferences;

    /**
     * The Course.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    protected Course course;

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
     * @param newId
     */
    public void setId(final UUID newId) {
        this.id = newId;
    }

    /**
     * Getter.
     *
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Setter.
     *
     * @param newTitle the title
     */
    public void setTitle(final String newTitle) {
        this.title = newTitle;
    }

    /**
     * Getter.
     *
     * @return the course id.
     */
    public UUID getCourseId() {
        return courseId;
    }

    /**
     * Setter.
     *
     * @param newCourseId the course id
     */
    public void setCourseId(final UUID newCourseId) {
        this.courseId = newCourseId;
    }

    /**
     * Getter.
     *
     * @return the start date.
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter.
     *
     * @param newStartDate the start date.
     */
    public void setStartDate(final LocalDate newStartDate) {
        this.startDate = newStartDate;
    }

    /**
     * Getter.
     *
     * @return the end date.
     */
    public LocalDate getEndDate() {
        return endDate;
    }

    /**
     * Setter.
     *
     * @param newEndDate the end date.
     */
    public void setEndDate(final LocalDate newEndDate) {
        this.endDate = newEndDate;
    }

    /**
     * Getter.
     *
     * @return content references
     */
    public Collection<ContentReference> getContentReferences() {
        return contentReferences;
    }

    /**
     * Setter.
     *
     * @param newContentReferences the content references
     */
    public void setContentReferences(
        final Collection<ContentReference> newContentReferences) {
        getContentReferences().clear();
        addContentReferences(newContentReferences);
    }

    public void addContentReference(
        final ContentReference newContentReference) {
        newContentReference.chapters.add(this);
        this.contentReferences.add(newContentReference);
    }

    public void addContentReferences(
        final Collection<ContentReference> newContentReferences) {
        getContentReferences()
            .addAll(newContentReferences.stream().map(newContentReference -> {
                newContentReference.chapters.add(this);
                return newContentReference;
            }).collect(Collectors.toSet()));
    }

    public void removeContentReference(
        final ContentReference newContentReference) {
        newContentReference.chapters.remove(this);
        this.contentReferences.remove(newContentReference);
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
}
