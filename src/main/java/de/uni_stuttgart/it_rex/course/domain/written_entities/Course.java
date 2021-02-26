package de.uni_stuttgart.it_rex.course.domain.written_entities;

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
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    /**
     * Constructor.
     */
    public Course() {
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
     * Chapter items.
     */
    @OneToMany(cascade = CascadeType.ALL,
        fetch = FetchType.EAGER,
        orphanRemoval = true)
    @JoinColumn(name = "course_id", referencedColumnName = "id")
    @OrderBy("index")
    private List<ChapterIndex> chapters;

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
     * @return the chapters
     */
    public List<ChapterIndex> getChapters() {
        return chapters;
    }

    /**
     * Setter.
     *
     * @param newChapters the chapters
     */
    public void setChapters(final List<ChapterIndex> newChapters) {
        getChapters().clear();
        addChapterIndex(newChapters);
    }

    /**
     * Adds a ChapterIndex.
     *
     * @param chapterIndex the chapterIndex
     */
    public void addChapterIndex(final ChapterIndex chapterIndex) {
        chapterIndex.setCourseId(getId());
        getChapters().add(chapterIndex);
    }

    /**
     * Adds a list of ChapterIndexes.
     *
     * @param chapterIndexes the chapterIndexes
     */
    public void addChapterIndex(final List<ChapterIndex> chapterIndexes) {
        getChapters().addAll(chapterIndexes.stream().map(chapterIndex -> {
            chapterIndex.setCourseId(getId());
            return chapterIndex;
        }).collect(Collectors.toList()));
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
        if (!(o instanceof Course)) {
            return false;
        }
        Course course = (Course) o;
        return Objects.equals(getId(), course.getId())
            && Objects.equals(getName(), course.getName())
            && Objects.equals(getStartDate(), course.getStartDate())
            && Objects.equals(getEndDate(), course.getEndDate())
            && Objects.equals(getMaxFoodSum(), course.getMaxFoodSum())
            && Objects.equals(getCourseDescription(),
            course.getCourseDescription())
            && getPublishState() == course.getPublishState();
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId(),
            getName(),
            getStartDate(),
            getEndDate(),
            getMaxFoodSum(),
            getCourseDescription(),
            getPublishState());
    }

    /**
     * Converts the entity to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "Course{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", maxFoodSum=" + maxFoodSum
            + ", courseDescription='" + courseDescription + '\''
            + ", publishState=" + publishState
            + '}';
    }
}
