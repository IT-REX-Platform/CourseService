package de.uni_stuttgart.it_rex.course.domain.written_entities;

import net.logstash.logback.encoder.org.apache.commons.lang3.builder.EqualsBuilder;
import net.logstash.logback.encoder.org.apache.commons.lang3.builder.HashCodeBuilder;

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
public class TimePeriod implements Serializable, Organizable {

    public TimePeriod() {
        this.contentReferences = new ArrayList<>();
    }

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

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(final LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(final LocalDate endDate) {
        this.endDate = endDate;
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
        contentReferences.clear();
        addContentReferences(newContentReferences);
    }

    public void addContentReference(
        final ContentReference newContentReference) {
        newContentReference.timePeriod = this;
        this.contentReferences.add(newContentReference);
    }

    public void addContentReferences(
        final Collection<ContentReference> newContentReferences) {
        contentReferences
            .addAll(newContentReferences.stream().map(newContentReference -> {
                newContentReference.timePeriod = this;
                return newContentReference;
            }).collect(Collectors.toSet()));
    }

    public void removeContentReference(
        final ContentReference newContentReference) {
        newContentReference.timePeriod = null;
        this.contentReferences.remove(newContentReference);
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(final Course newCourse) {
        if (course != null) {
            course.removeTimePeriod(this);
        }
        newCourse.getTimePeriods().add(this);
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
        if (!(o instanceof TimePeriod)) {
            return false;
        }
        final TimePeriod timePeriod = (TimePeriod) o;
        return new EqualsBuilder()
            .append(id, timePeriod.id)
            .append(course, timePeriod.course)
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
