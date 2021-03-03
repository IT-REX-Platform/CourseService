package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChapterDTO {
  /**
   * Identifier.
   */
  private UUID id;

  /**
   * Title of the chapter.
   */
  private String title;

  /**
   * Course id.
   */
  private UUID courseId;

  /**
   * Start date of the Chapter.
   */
  private LocalDate startDate;

  /**
   * End date of the Chapter.
   */
  private LocalDate endDate;

  /**
   * Content Reference items.
   */
  private List<UUID> contentReferenceIds;

  /**
   * Equals method.
   *
   * @param o the other object.
   * @return if they are equal
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChapterDTO)) {
      return false;
    }
    final ChapterDTO chapterDTO = (ChapterDTO) o;
    return Objects.equals(getId(), chapterDTO.getId())
        && Objects.equals(getTitle(), chapterDTO.getTitle())
        && Objects.equals(getCourseId(), chapterDTO.getCourseId())
        && Objects.equals(getStartDate(), chapterDTO.getStartDate())
        && Objects.equals(getEndDate(), chapterDTO.getEndDate())
        && Objects.equals(
        getContentReferenceIds(), chapterDTO.getContentReferenceIds());
  }

  /**
   * Hash code method.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(),
        getTitle(),
        getCourseId(),
        getStartDate(),
        getEndDate(),
        getContentReferenceIds());
  }

  /**
   * To string method.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "ChapterDTO{" + "id=" + id
        + ", title='" + title + '\''
        + ", courseId=" + courseId
        + ", startDate=" + startDate
        + ", endDate=" + endDate + '}';
  }

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
   * @param newId
   */
  public void setId(final UUID newId) {
    this.id = newId;
  }

  /**
   * Getter.
   *
   * @return the title
   */
  public String getTitle() {
    return title;
  }

  /**
   * Setter.
   *
   * @param newTitle the title
   */
  public void setTitle(final String newTitle) {
    this.title = newTitle;
  }

  /**
   * Getter.
   *
   * @return the course id.
   */
  public UUID getCourseId() {
    return courseId;
  }

  /**
   * Setter.
   *
   * @param newCourseId the course id
   */
  public void setCourseId(final UUID newCourseId) {
    this.courseId = newCourseId;
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
   * @return the content ids.
   */
  public List<UUID> getContentReferenceIds() {
    return contentReferenceIds;
  }

  /**
   * Setter.
   *
   * @param newContents the content ids.
   */
  public void setContentReferenceIds(final List<UUID> newContents) {
    this.contentReferenceIds = newContents;
  }
}
