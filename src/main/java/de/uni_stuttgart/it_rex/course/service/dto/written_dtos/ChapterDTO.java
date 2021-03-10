package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class ChapterDTO {
  /**
   * Identifier.
   */
  private UUID id;

  /**
   * Name of the chapter.
   */
  private String name;

  /**
   * Course id.
   */
  private UUID courseId;

  /**
   * Content Reference items.
   */
  private List<ContentReferenceDTO> contentReferences;

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
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Setter.
   *
   * @param newTitle the name
   */
  public void setName(final String newTitle) {
    this.name = newTitle;
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
   * @return the ContentReferences
   */
  public List<ContentReferenceDTO> getContentReferences() {
    return contentReferences;
  }

  /**
   * Setter.
   *
   * @param newContentReferences the ContentReferences
   */
  public void setContentReferences(
      final List<ContentReferenceDTO> newContentReferences) {
    this.contentReferences = newContentReferences;
  }

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
    final ChapterDTO that = (ChapterDTO) o;
    return Objects.equals(getId(), that.getId())
        && Objects.equals(getName(), that.getName())
        && Objects.equals(getCourseId(), that.getCourseId())
        && Objects.equals(getContentReferences(), that.getContentReferences());
  }

  /**
   * Hash code method.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(
        getId(),
        getName(),
        getCourseId(),
        getContentReferences());
  }

  /**
   * To string method.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "ChapterDTO{"
        + "id=" + id
        + ", name='" + name + '\''
        + ", courseId=" + courseId
        + ", contentReferences=" + contentReferences + '}';
  }
}
