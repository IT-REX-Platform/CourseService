package de.uni_stuttgart.it_rex.course.domain.written_entities;

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
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "time_period")
public class TimePeriod implements Serializable {

    public TimePeriod() {
        this.contentReferences = new HashSet<>();
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
    @ManyToMany(cascade = CascadeType.ALL,
        fetch = FetchType.LAZY)
    @JoinTable(
        name = "time_period_content",
        joinColumns = {
            @JoinColumn(name = "time_period_id", referencedColumnName = "id")},
        inverseJoinColumns = {
            @JoinColumn(name = "content_id", referencedColumnName = "id")}
    )
    @OrderBy("startDate")
    protected final Set<ContentReference> contentReferences;

    /**
     * Course id
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
        final Collection<ContentReference> newContentReferences) {
        contentReferences.clear();
        addContentReferences(newContentReferences);
    }

    public void addContentReference(
        final ContentReference newContentReference) {
        newContentReference.timePeriods.add(this);
        this.contentReferences.add(newContentReference);
    }

    public void addContentReferences(
        final Collection<ContentReference> newContentReferences) {
        contentReferences
            .addAll(newContentReferences.stream().map(newContentReference -> {
                newContentReference.timePeriods.add(this);
                return newContentReference;
            }).collect(Collectors.toSet()));
    }

    public void removeContentReference(
        final ContentReference newContentReference) {
        newContentReference.timePeriods.remove(this);
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


}
