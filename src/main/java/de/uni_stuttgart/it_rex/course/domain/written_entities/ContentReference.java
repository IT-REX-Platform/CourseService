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
@Table(name = "content_reference")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ContentReference implements Serializable {

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
   * Content id.
   */
  @Column(name = "content_id")
  private UUID contentId;

  /**
   * Chapter id.
   */
  @Column(name = "chapter_id")
  private UUID chapterId;

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
   * @return the content id
   */
  public UUID getContentId() {
    return contentId;
  }

  /**
   * Setter.
   *
   * @param newContentId the content id
   */
  public void setContentId(final UUID newContentId) {
    this.contentId = newContentId;
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
    if (!(o instanceof ContentReference)) {
      return false;
    }
    final ContentReference that = (ContentReference) o;
    return getIndex() == that.getIndex()
        && Objects.equals(getId(), that.getId())
        && Objects.equals(getContentId(), that.getContentId())
        && Objects.equals(getChapterId(), that.getChapterId());
  }

  /**
   * Hash code.
   *
   * @return the hash code.
   */
  @Override
  public int hashCode() {
    return Objects.hash(getId(), getIndex(), getContentId(), getChapterId());
  }

  /**
   * To string method.
   *
   * @return the string.
   */
  @Override
  public String toString() {
    return "ContentReference{"
        + "id=" + id
        + ", index=" + index
        + ", contentId=" + contentId
        + ", chapterId=" + chapterId + '}';
  }
}
