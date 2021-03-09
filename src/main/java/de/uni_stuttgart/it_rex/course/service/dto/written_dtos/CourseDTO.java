package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import de.uni_stuttgart.it_rex.course.domain.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PUBLISHSTATE;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class CourseDTO {
    /**
     * Id.
     */
    private UUID id;
    /**
     * Name.
     */
    private String name;

    /**
     * courseRole.
     */
    private COURSEROLE courseRole;

    /**
     * Start date.
     */
    private LocalDate startDate;

    /**
     * End date.
     */
    private LocalDate endDate;

    /**
     * Max food sum for the course.
     */
    private Integer maxFoodSum;

    /**
     * How long to remain active for after end date.
     */
    private Integer remainActiveOffset;

    /**
     * The course description.
     */
    private String courseDescription;

    /**
     * The publish state.
     */
    private PUBLISHSTATE publishState;

    /**
     * Time Period items.
     */
    private List<TimePeriodDTO> timePeriods;

    /**
     * Time Period items.
     */
    private List<ChapterDTO> chapters;

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
     * @return the courseRole
     */
    public COURSEROLE getCourseRole() {
        return courseRole;
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

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public void setCourseRole(final COURSEROLE courseRole) {
        this.courseRole = courseRole;
    }

    /**
     * Getter.
     *
     * @return the active offset.
     */
    public Integer getRemainActiveOffset() {
        return remainActiveOffset;
    }

    /**
     * Setter.
     *
     * @param remainActiveOffset the active offset.
     */
    public void setRemainActiveOffset(final Integer remainActiveOffset) {
        this.remainActiveOffset = remainActiveOffset;
    }

    /**
     * Getter.
     *
     * @return the chapters
     */
    public List<ChapterDTO> getChapters() {
        return chapters;
    }

    /**
     * Setter.
     *
     * @param newChapters the chapters
     */
    public void setChapters(final List<ChapterDTO> newChapters) {
        this.chapters = newChapters;
    }

    /**
     * Getter.
     *
     * @return the timePeriods
     */
    public List<TimePeriodDTO> getTimePeriods() {
        return timePeriods;
    }

    /**
     * Setter.
     *
     * @param newTimePeriods the timePeriods
     */
    public void setTimePeriods(final List<TimePeriodDTO> newTimePeriods) {
        this.timePeriods = newTimePeriods;
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
        if (!(o instanceof CourseDTO)) {
            return false;
        }
        final CourseDTO courseDTO = (CourseDTO) o;
        return Objects.equals(
            getId(), courseDTO.getId())
            && Objects.equals(getName(), courseDTO.getName())
            && getCourseRole() == courseDTO.getCourseRole()
            && Objects.equals(getStartDate(), courseDTO.getStartDate())
            && Objects.equals(getEndDate(), courseDTO.getEndDate())
            && Objects.equals(getMaxFoodSum(), courseDTO.getMaxFoodSum())
            && Objects.equals(getRemainActiveOffset(),
            courseDTO.getRemainActiveOffset())
            && Objects.equals(getCourseDescription(),
            courseDTO.getCourseDescription())
            && getPublishState() == courseDTO.getPublishState()
            && Objects.equals(timePeriods, courseDTO.timePeriods)
            && Objects.equals(getChapters(), courseDTO.getChapters());
    }

    /**
     * Hash code.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
            getId(),
            getName(),
            getCourseRole(),
            getStartDate(),
            getEndDate(),
            getMaxFoodSum(),
            getRemainActiveOffset(),
            getCourseDescription(),
            getPublishState(),
            timePeriods,
            getChapters());
    }

    /**
     * Converts the dto to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "CourseDTO{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", courseRole=" + courseRole +
            ", startDate=" + startDate +
            ", endDate=" + endDate +
            ", maxFoodSum=" + maxFoodSum +
            ", remainActiveOffset=" + remainActiveOffset +
            ", courseDescription='" + courseDescription + '\'' +
            ", publishState=" + publishState +
            ", timePeriods=" + timePeriods +
            ", chapters=" + chapters +
            '}';
    }
}
