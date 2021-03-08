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
        && Objects.equals(getName(), chapterDTO.getName())
        && Objects.equals(getCourseId(), chapterDTO.getCourseId());
  }

  /**
   * Hash code method.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(),
        getName(),
        getCourseId());
  }

  /**
   * To string method.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "ChapterDTO{" + "id=" + id
        + ", title='" + name + '\''
        + ", courseId=" + courseId + '}';
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

  public List<ContentReferenceDTO> getContentReferences() {
    return contentReferences;
  }

  public void setContentReferences(final List<ContentReferenceDTO> contentReferences) {
    this.contentReferences = contentReferences;
  }
}
