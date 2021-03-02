package de.uni_stuttgart.it_rex.course.domain.written_entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "tp_chapter_relation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class TpChapterRelation implements Serializable {

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
   * Chapter.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "chapter_id", referencedColumnName = "id")
  protected Chapter chapter;

  /**
   * TimePeriod.
   */
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "time_period_id", referencedColumnName = "id")
  protected TimePeriod timePeriod;

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
   * @return the time period
   */
  public TimePeriod getTimePeriod() {
    return timePeriod;
  }

  /**
   * Setter.
   *
   * @param newTimePeriod the time period
   */
  public void setTimePeriod(final TimePeriod newTimePeriod) {
    if (timePeriod != null) {
      timePeriod.removeTPChapterRelation(this);
    }
    this.timePeriod = newTimePeriod;
    newTimePeriod.getTPChapterRelation().add(this);
  }

  /**
   * Getter.
   *
   * @return the chapter
   */
  public Chapter getChapter() {
    return chapter;
  }

  /**
   * Setter.
   *
   * @param newChapter the chapter
   */
  public void setChapter(final Chapter newChapter) {
    newChapter.tpChapterRelation.add(this);
    this.chapter = newChapter;
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
    if (!(o instanceof TpChapterRelation)) {
      return false;
    }
    final TpChapterRelation that = (TpChapterRelation) o;
    return getIndex() == that.getIndex()
        && Objects.equals(getId(), that.getId())
        && Objects.equals(getChapter(), that.getChapter())
        && Objects.equals(getTimePeriod(), that.getTimePeriod());
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
        getChapter(),
        getTimePeriod());
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
        + ", chapterId=" + chapter
        + ", courseId=" + timePeriod + '}';
  }
}
