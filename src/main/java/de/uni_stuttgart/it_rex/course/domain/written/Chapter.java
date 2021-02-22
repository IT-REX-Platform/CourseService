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

  @Override
  public String toString() {
    return "Chapter{" +
        "id=" + id +
        ", title='" + title + '\'' +
        ", courseId=" + courseId +
        ", previousId=" + previousId +
        ", nextId=" + nextId +
        ", startDate=" + startDate +
        ", endDate=" + endDate +
        '}';
  }

  public void setId(UUID id) {
    this.id = id;
  }

  public UUID getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(final String title) {
    this.title = title;
  }

  public UUID getCourseId() {
    return courseId;
  }

  public void setCourseId(final UUID courseId) {
    this.courseId = courseId;
  }

  public UUID getPreviousId() {
    return previousId;
  }

  public void setPreviousId(final UUID previousId) {
    this.previousId = previousId;
  }

  public UUID getNextId() {
    return nextId;
  }

  public void setNextId(final UUID nextId) {
    this.nextId = nextId;
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
}
