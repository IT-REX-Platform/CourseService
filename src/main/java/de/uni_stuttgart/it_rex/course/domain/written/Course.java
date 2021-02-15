package de.uni_stuttgart.it_rex.course.domain.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "max_food_sum")
    private Integer maxFoodSum;

    @Lob
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "course_description")
    private String courseDescription;

    @Enumerated(EnumType.STRING)
    @Column(name = "publish_state")
    private PUBLISHSTATE publishState;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Course startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Course endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxFoodSum() {
        return maxFoodSum;
    }

    public Course maxFoodSum(Integer maxFoodSum) {
        this.maxFoodSum = maxFoodSum;
        return this;
    }

    public void setMaxFoodSum(Integer maxFoodSum) {
        this.maxFoodSum = maxFoodSum;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public Course courseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
        return this;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public PUBLISHSTATE getPublishState() {
        return publishState;
    }

    public Course publishState(PUBLISHSTATE publishState) {
        this.publishState = publishState;
        return this;
    }

    public void setPublishState(PUBLISHSTATE publishState) {
        this.publishState = publishState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", maxFoodSum=" + getMaxFoodSum() +
            ", courseDescription='" + getCourseDescription() + "'" +
            ", publishState='" + getPublishState() + "'" +
            "}";
    }
}
