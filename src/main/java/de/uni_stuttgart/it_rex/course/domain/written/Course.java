package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    public static final int HASH_CODE = 69;

    /**
     * Constructor.
     */
    public Course() {
        this.timePeriods = new ArrayList<>();
        this.chapters = new ArrayList<>();
    }

    /**
     * Version.
     */
    private static final long serialVersionUID = 1L;

    /**
     * Id.
     */
    @Id
    @GeneratedValue
    private UUID id;

    /**
     * Name.
     */
    @Column(name = "name")
    private String name;

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
     * How long to remain active for after end date.
     */
    @Column(name = "remain_active_offset")
    private Integer remainActiveOffset;

    /**
     * Max food sum for the course.
     */
    @Column(name = "max_food_sum")
    private Integer maxFoodSum;

    /**
     * The course description.
     */
    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "course_description")
    private String courseDescription;

    /**
     * The publish state.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "publish_state")
    private PUBLISHSTATE publishState;

    /**
     * Time period items.
     */
    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "course")
    @OrderBy("startDate")
    private final List<TimePeriod> timePeriods;

    /**
     * Chapters.
     */
    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY,
        orphanRemoval = true,
        mappedBy = "course")
    @OrderBy("chapterNumber")
    private final List<Chapter> chapters;

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
     * @param newName the name
     */
    public void setName(final String newName) {
        this.name = newName;
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
     * @return how long to remain active for after end date.
     */
    public Integer getRemainActiveOffset() {
        return remainActiveOffset;
    }

    /**
     * Setter.
     *
     * @param newRemainActiveOffset the new offset for remaining active after
     *                              end date.
     */
    public void setRemainActiveOffset(final Integer newRemainActiveOffset) {
        this.remainActiveOffset = newRemainActiveOffset;
    }

    /**
     * Getter.
     *
     * @return the maximum food sum.
     */
    public Integer getMaxFoodSum() {
        return maxFoodSum;
    }

    /**
     * Setter.
     *
     * @param newMaxFoodSum the maximum food sum
     */
    public void setMaxFoodSum(final Integer newMaxFoodSum) {
        this.maxFoodSum = newMaxFoodSum;
    }

    /**
     * Getter.
     *
     * @return the course description.
     */
    public String getCourseDescription() {
        return courseDescription;
    }

    /**
     * Setter.
     *
     * @param newCourseDescription the course description
     */
    public void setCourseDescription(final String newCourseDescription) {
        this.courseDescription = newCourseDescription;
    }

    /**
     * Getter.
     *
     * @return the publish state
     */
    public PUBLISHSTATE getPublishState() {
        return publishState;
    }

    /**
     * Setter.
     *
     * @param newPublishState the publish state
     */
    public void setPublishState(final PUBLISHSTATE newPublishState) {
        this.publishState = newPublishState;
    }

    /**
     * Getter.
     *
     * @return the time periods
     */
    public List<TimePeriod> getTimePeriods() {
        return timePeriods;
    }

    /**
     * Setter.
     *
     * @param newTimePeriods the time periods
     */
    public void setTimePeriods(final Collection<TimePeriod> newTimePeriods) {
        if (newTimePeriods == null) {
            return;
        }
        timePeriods.clear();
        addTimePeriods(newTimePeriods);
    }

    /**
     * Adds a TimePeriod.
     *
     * @param newTimePeriod the time period
     */
    public void addTimePeriod(final TimePeriod newTimePeriod) {
        if (newTimePeriod == null) {
            return;
        }
        timePeriods.add(newTimePeriod);
        newTimePeriod.course = this;
    }

    /**
     * Adds a set of TimePeriods.
     *
     * @param newTimePeriods the chapterIndexes
     */
    public void addTimePeriods(final Collection<TimePeriod> newTimePeriods) {
        if (newTimePeriods == null) {
            return;
        }
        timePeriods.addAll(newTimePeriods.stream().map(newTimePeriod -> {
            newTimePeriod.course = this;
            return newTimePeriod;
        }).collect(Collectors.toList()));
    }

    /**
     * Removes a TimePeriod.
     *
     * @param newTimePeriod the time period
     */
    public void removeTimePeriod(final TimePeriod newTimePeriod) {
        newTimePeriod.course = null;
        timePeriods.remove(newTimePeriod);
    }

    /**
     * Getter.
     *
     * @return the chapters
     */
    public List<Chapter> getChapters() {
        return chapters;
    }

    /**
     * Setter.
     *
     * @param newChapters the chapters
     */
    public void setChapters(final Collection<Chapter> newChapters) {
        if (newChapters == null) {
            return;
        }
        chapters.clear();
        addChapters(newChapters);
    }

    /**
     * Adds a Chapter.
     *
     * @param newChapter the chapter
     */
    public void addChapter(final Chapter newChapter) {
        if (newChapter == null) {
            return;
        }
        chapters.add(newChapter);
        newChapter.course = this;
    }

    /**
     * Adds a set of Chapters.
     *
     * @param newChapters the chapters
     */
    public void addChapters(final Collection<Chapter> newChapters) {
        if (newChapters == null) {
            return;
        }
        chapters.addAll(newChapters.stream().map(newChapter -> {
            newChapter.course = this;
            return newChapter;
        }).collect(Collectors.toList()));
    }

    /**
     * Removes a Chapter.
     *
     * @param newChapter the chapter
     */
    public void removeChapter(final Chapter newChapter) {
        newChapter.course = null;
        chapters.remove(newChapter);
    }

    /**
     * Equals method.
     *
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
        Course course = (Course) o;
        return id != null && id.equals(course.getId());
    }

    /**
     * Hash code.
     *
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
        return "Course{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", remainActiveOffset=" + remainActiveOffset +
            ", maxFoodSum=" + maxFoodSum +
            ", courseDescription='" + courseDescription + '\'' +
            ", publishState=" + publishState +
            ", timePeriods=" + timePeriods +
            ", chapters=" + chapters +
            '}';
    }
}
