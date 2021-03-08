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
   * Setter.
   *
   * @param newCourseRole the courseRole
   */
  public void setCourseRole(final COURSEROLE newCourseRole) {
    this.courseRole = newCourseRole;
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
     * @return the time periods
     */
    public List<TimePeriodDTO> getTimePeriods() {
        return timePeriods;
    }

    /**
     * Setter.
     *
     * @param newTimePeriods the time periods
     */
    public void setTimePeriods(final List<TimePeriodDTO> newTimePeriods) {
        this.timePeriods = newTimePeriods;
    }
    CourseDTO course = (CourseDTO) o;
    return Objects.equals(getId(), course.getId())
        && Objects.equals(getName(), course.getName())
        && Objects.equals(getCourseRole(), course.getCourseRole())
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
        getCourseRole(),
        getStartDate(),
        getEndDate(),
        getMaxFoodSum(),
        getCourseDescription(),
        getPublishState());
  }

  /**
   * Converts the dto to a string.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "CourseDTO{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", courseRole='" + courseRole + '\''
        + ", startDate=" + startDate
        + ", endDate=" + endDate
        + ", maxFoodSum=" + maxFoodSum
        + ", courseDescription='" + courseDescription + '\''
        + ", publishState=" + publishState
        + '}';
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
        CourseDTO course = (CourseDTO) o;
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
     * Converts the dto to a string.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "CourseDTO{"
            + "id=" + id
            + ", name='" + name + '\''
            + ", startDate=" + startDate
            + ", endDate=" + endDate
            + ", maxFoodSum=" + maxFoodSum
            + ", courseDescription='" + courseDescription + '\''
            + ", publishState=" + publishState
            + '}';
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
}
