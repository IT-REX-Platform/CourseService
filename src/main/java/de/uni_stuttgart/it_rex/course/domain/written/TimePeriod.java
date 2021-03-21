package de.uni_stuttgart.it_rex.course.domain.written;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "time_period")
public class TimePeriod implements Serializable {

    /**
     * Constant hash code.
     */
    public static final int HASH_CODE = 42;

    /**
     * Constructor.
     */
    public TimePeriod() {
        this.contentReferences = new ArrayList<>();
    }

    /**
     * The id.
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
     * Content items.
     */
    @OneToMany(mappedBy = "timePeriod", fetch = FetchType.LAZY)
    protected final List<ContentReference> contentReferences;

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
     * @param newEndDate the end date
     */
    public void setEndDate(final LocalDate newEndDate) {
        this.endDate = newEndDate;
    }

    /**
     * Getter.
     *
     * @return the course.
     */
    public Course getCourse() {
        return course;
    }

    /**
     * Sets the course and the relationships.
     *
     * @param newCourse the course
     */
    public void setCourse(final Course newCourse) {
        if (course != null) {
            course.removeTimePeriod(this);
        }
        newCourse.getTimePeriods().add(this);
        this.course = newCourse;
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
        final List<ContentReference> newContentReferences) {
        if (newContentReferences == null) {
            return;
        }
        contentReferences.clear();
        addContentReferences(newContentReferences);
    }

    /**
     * Adds a ContentReference and sets the relationships appropriately.
     *
     * @param newContentReference the ContentReference
     */
    public void addContentReference(
        final ContentReference newContentReference) {
        if (newContentReference == null) {
            return;
        }
        newContentReference.timePeriod = this;
        this.contentReferences.add(newContentReference);
    }

    /**
     * Adds ContentReferences and sets the relationships appropriately.
     *
     * @param newContentReferences the ContentReference
     */
    public void addContentReferences(
        final Collection<ContentReference> newContentReferences) {
        if (newContentReferences == null) {
            return;
        }
        contentReferences
            .addAll(newContentReferences.stream().map(newContentReference -> {
                newContentReference.timePeriod = this;
                return newContentReference;
            }).collect(Collectors.toSet()));
    }

    /**
     * Removes a ContentReferences and removes the relationships.
     *
     * @param toRemove the ContentReference
     */
    public void removeContentReference(
        final ContentReference toRemove) {
        toRemove.timePeriod = null;
        this.contentReferences.remove(toRemove);
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
        TimePeriod that = (TimePeriod) o;
        return id != null && id.equals(that.getId());
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
     * To String method.
     *
     * @return the String.
     */
    @Override
    public String toString() {
        return "TimePeriod{" +
            "id=" + id +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", contentReferences=" + contentReferences +
            ", course=" + course +
            '}';
    }
}
