package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class TimePeriodDTO {

  /**
   * Id.
   */
  private UUID id;

  /**
   * Start date.
   */
  private LocalDate startDate;

  /**
   * End date.
   */
  private LocalDate endDate;

  /**
   * Course id.
   */
  private UUID courseId;

  /**
   * Chapter ids.
   */
  private List<ChapterDTO> chapters;

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

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(final UUID courseId) {
    this.courseId = courseId;
  }

  public List<ChapterDTO> getChapters() {
    return chapters;
  }

  public void setChapters(final List<ChapterDTO> newChapters) {
    this.chapters = newChapters;
  }

  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof TimePeriodDTO)) {
      return false;
    }
    final TimePeriodDTO that = (TimePeriodDTO) o;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getStartDate(), that.getStartDate())
        && Objects.equals(getEndDate(), that.getEndDate())
        && Objects.equals(getCourseId(), that.getCourseId())
        && Objects.equals(getChapters(), that.getChapters());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(),
        getStartDate(),
        getEndDate(),
        getCourseId(),
        getChapters());
  }

  @Override
  public String toString() {
    return "TimePeriodDTO{"
        + "id=" + id + ", "
        + "startDate=" + startDate
        + ", endDate=" + endDate
        + ", courseId=" + courseId
        + ", chapterIds=" + chapters + '}';
  }
}
