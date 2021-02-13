package de.uni_stuttgart.it_rex.course.service.written.dto;

import java.time.LocalDate;
import java.io.Serializable;
import java.util.UUID;
import javax.persistence.Lob;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;
import de.uni_stuttgart.it_rex.course.domain.written.Course;

/**
 * A DTO for the {@link Course} entity.
 */
public class CourseDTO implements Serializable {
    
    private UUID id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxFoodSum;

    @Lob
    private String courseDescription;

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

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxFoodSum() {
        return maxFoodSum;
    }

    public void setMaxFoodSum(Integer maxFoodSum) {
        this.maxFoodSum = maxFoodSum;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public PUBLISHSTATE getPublishState() {
        return publishState;
    }

    public void setPublishState(PUBLISHSTATE publishState) {
        this.publishState = publishState;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseDTO)) {
            return false;
        }

        return id != null && id.equals(((CourseDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseDTO{" +
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
