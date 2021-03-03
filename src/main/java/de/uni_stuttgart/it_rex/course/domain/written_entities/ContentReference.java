package de.uni_stuttgart.it_rex.course.domain.written_entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "content_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContentReference implements Serializable {

    /**
     * Constructor.
     */
    public ContentReference() {
        this.chapters = new HashSet<>();
        this.timePeriods = new HashSet<>();
    }

    /**
     * Identifier.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Start date.
     */
    @Column(name = "start_date")
    private LocalDate startDate;

    /**
     * End date.
     */
    @Column(name = "end_date")
    private LocalDate endDate;

    /**
     * Content id.
     */
    @Column(name = "content_id")
    private UUID contentId;

    /**
     * The Chapters.
     */
    @ManyToMany(mappedBy = "contentReferences")
    protected final Set<Chapter> chapters;

    /**
     * The Time Periods.
     */
    @ManyToMany(mappedBy = "contentReferences")
    protected final Set<TimePeriod> timePeriods;

    /**
     * The Course.
     */
    @ManyToOne
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    protected Course course;

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
     * @return the start date
     */
    public LocalDate getStartDate() {
        return startDate;
    }

    /**
     * Setter.
     *
     * @param startDate the start date
     */
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
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
     * @param endDate the end date
     */
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
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
    public Set<Chapter> getChapters() {
        return chapters;
    }

    /**
     * Setter.
     *
     * @param newChapters the chapters
     */
    public void setChapters(final Collection<Chapter> newChapters) {
        chapters.clear();
        addChapters(newChapters);
    }

    public void addChapter(final Chapter newChapter) {
        newChapter.contentReferences.add(this);
        this.chapters.add(newChapter);
    }

    public void addChapters(final Collection<Chapter> newChapters) {
        chapters.addAll(newChapters.stream().map(newChapter -> {
            newChapter.contentReferences.add(this);
            return newChapter;
        }).collect(Collectors.toSet()));
    }

    public void removeChapter(final Chapter chapter) {
        chapter.contentReferences.remove(this);
        this.chapters.remove(chapter);
    }

    /**
     * Getter.
     *
     * @return time periods
     */
    public Set<TimePeriod> getTimePeriods() {
        return timePeriods;
    }

    /**
     * Setter.
     *
     * @param newTimePeriods the time periods
     */
    public void setTimePeriods(final Collection<TimePeriod> newTimePeriods) {
        timePeriods.clear();
        addTimePeriods(newTimePeriods);
    }

    public void addTimePeriod(final TimePeriod newTimePeriod) {
        newTimePeriod.contentReferences.add(this);
        this.timePeriods.add(newTimePeriod);
    }

    public void addTimePeriods(final Collection<TimePeriod> newTimePeriods) {
        timePeriods.addAll(newTimePeriods.stream().map(newTimePeriod -> {
            newTimePeriod.contentReferences.add(this);
            return newTimePeriod;
        }).collect(Collectors.toSet()));
    }

    public void removeTimePeriod(final TimePeriod timePeriod) {
        timePeriod.contentReferences.remove(this);
        this.timePeriods.remove(timePeriod);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(final Course newCourse) {
        if (course != null) {
            course.removeContentReference(this);
        }
        newCourse.getContentReferences().add(this);
        this.course = newCourse;
    }

}
