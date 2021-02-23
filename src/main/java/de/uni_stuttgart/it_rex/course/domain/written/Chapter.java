package de.uni_stuttgart.it_rex.course.domain.written;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chapter implements Serializable {

  /**
   * Identifier.
   */
  @Id
  @GeneratedValue
  private UUID id;

  /**
   * Title of the chapter.
   */
  @Column(name = "title")
  private String title;

  /**
   * Course id.
   */
  @Column(name = "course_id")
  private UUID courseId;

  /**
   * Previous Chapter id.
   */
  @Column(name = "previous_id")
  private UUID previousId;

  /**
   * Next Chapter id.
   */
  @Column(name = "next_id")
  private UUID nextId;

  /**
   * Start date of the Chapter.
   */
  @Column(name = "start_date")
  private LocalDate startDate;

  /**
   * End date of the Chapter.
   */
  @Column(name = "end_date")
  private LocalDate endDate;

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
    if (!(o instanceof Chapter)) {
      return false;
    }
    Chapter chapter = (Chapter) o;
    return Objects.equals(getId(), chapter.getId())
        && Objects.equals(getTitle(), chapter.getTitle())
        && Objects.equals(getCourseId(), chapter.getCourseId())
        && Objects.equals(getPreviousId(), chapter.getPreviousId())
        && Objects.equals(getNextId(), chapter.getNextId())
        && Objects.equals(getStartDate(), chapter.getStartDate())
        && Objects.equals(getEndDate(), chapter.getEndDate());
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
        getTitle(),
        getCourseId(),
        getPreviousId(),
        getNextId(),
        getStartDate(),
        getEndDate());
  }

  /**
   * To string method.
   *
   * @return the string
   */
  @Override
  public String toString() {
    return "Chapter{" + "id=" + id
        + ", title='" + title + '\''
        + ", courseId=" + courseId
        + ", previousId=" + previousId
        + ", nextId=" + nextId
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
   * @return the previous id.
   */
  public UUID getPreviousId() {
    return previousId;
  }

  /**
   * Setter.
   *
   * @param newPreviousId the previous id.
   */
  public void setPreviousId(final UUID newPreviousId) {
    this.previousId = newPreviousId;
  }

  /**
   * Getter.
   *
   * @return the next id
   */
  public UUID getNextId() {
    return nextId;
  }

  /**
   * Setter.
   *
   * @param newNextId the next id.
   */
  public void setNextId(final UUID newNextId) {
    this.nextId = newNextId;
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
}
