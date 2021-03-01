package de.uni_stuttgart.it_rex.course.domain.written_entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "chapter_index")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ChapterIndex implements Serializable {

  /**
   * Identifier.
   */
  @Id
  @GeneratedValue
  private UUID id;

  /**
   * Index in the ordering.
   */
  @Column(name = "index")
  private int index;

  /**
   * Chapter id.
   */
  @Column(name = "chapter_id")
  private UUID chapterId;

  /**
   * Course id.
   */
  @Column(name = "course_id")
  private UUID courseId;

  /**
   * Getter.
   *
   * @return the id.
   */
  public UUID getId() {
    return id;
  }

  /**
   * Setter.
   *
   * @param newId the id.
   */
  public void setId(final UUID newId) {
    this.id = newId;
  }

  /**
   * Getter.
   *
   * @return the index
   */
  public int getIndex() {
    return index;
  }

  /**
   * Setter.
   *
   * @param newIndex the index
   */
  public void setIndex(final int newIndex) {
    this.index = newIndex;
  }

  /**
   * Getter.
   *
   * @return the course id
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
   * @return chapter id
   */
  public UUID getChapterId() {
    return chapterId;
  }

  /**
   * Setter.
   *
   * @param newChapterId the chapter id
   */
  public void setChapterId(final UUID newChapterId) {
    this.chapterId = newChapterId;
  }

  /**
   * Equals method.
   *
   * @param o the other one.
   * @return if they are equal
   */
  @Override
  public boolean equals(final Object o) {
    if (this == o) {
      return true;
    }
    if (!(o instanceof ChapterIndex)) {
      return false;
    }
    final ChapterIndex that = (ChapterIndex) o;
    return getIndex() == that.getIndex()
        && Objects.equals(getId(), that.getId())
        && Objects.equals(getChapterId(), that.getChapterId())
        && Objects.equals(getCourseId(), that.getCourseId());
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
        getIndex(),
        getChapterId(),
        getCourseId());
  }

  /**
   * To string method.
   *
   * @return the string.
   */
  @Override
  public String toString() {
    return "ChapterIndex{"
        + "id=" + id
        + ", index=" + index
        + ", chapterId=" + chapterId
        + ", courseId=" + courseId + '}';
  }
}
