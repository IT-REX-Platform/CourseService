package de.uni_stutgart.it_rex.course.service.dto;

import java.time.LocalDate;
import java.io.Serializable;
import javax.persistence.Lob;

/**
 * A DTO for the {@link de.uni_stutgart.it_rex.course.domain.Course} entity.
 */
public class CourseDTO implements Serializable {
    
    private Long id;

    private String name;

    private LocalDate startDate;

    private LocalDate endDate;

    private Integer maxFoodSum;

    @Lob
    private byte[] courseDescription;

    private String courseDescriptionContentType;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public byte[] getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(byte[] courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseDescriptionContentType() {
        return courseDescriptionContentType;
    }

    public void setCourseDescriptionContentType(String courseDescriptionContentType) {
        this.courseDescriptionContentType = courseDescriptionContentType;
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
            "}";
    }
}
