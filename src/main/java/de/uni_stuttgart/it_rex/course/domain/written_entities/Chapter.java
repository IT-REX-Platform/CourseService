package de.uni_stuttgart.it_rex.course.domain.written_entities;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * A Chapter.
 */
@Entity
@Table(name = "chapter")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Chapter implements Serializable {

  /**
   * Constructor.
   */
  public Chapter() {
    this.chapterIndices = new HashSet<>();
    this.contents = new ArrayList<>();
  }

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
   * ChapterIndex items.
   */
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      mappedBy = "chapter")
  protected Set<ChapterIndex> chapterIndices;

  /**
   * Content items.
   */
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true)
  @JoinColumn(name = "chapter_id", referencedColumnName = "id")
  @OrderBy("index")
  private List<ContentIndex> contents;

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
    final Chapter chapter = (Chapter) o;
    return Objects.equals(getId(), chapter.getId())
        && Objects.equals(getTitle(), chapter.getTitle())
        && Objects.equals(getCourseId(), chapter.getCourseId())
        && Objects.equals(getStartDate(), chapter.getStartDate())
        && Objects.equals(getEndDate(), chapter.getEndDate())
        && Objects.equals(getContents(), chapter.getContents());
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
        getContents());
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
  public List<ContentIndex> getContents() {
    return contents;
  }

  /**
   * Setter.
   *
   * @param newContents the content ids.
   */
  public void setContents(final List<ContentIndex> newContents) {
    getContents().clear();
    addContentIndex(newContents);
  }

  /**
   * Adds a ContentIndex.
   *
   * @param contentIndex the contentIndex
   */
  public void addContentIndex(final ContentIndex contentIndex) {
    contentIndex.setChapterId(getId());
    getContents().add(contentIndex);
  }

  /**
   * Adds a list of ContentIndexes.
   *
   * @param contentIndexes the contentIndexes
   */
  public void addContentIndex(final List<ContentIndex> contentIndexes) {
    getContents().addAll(contentIndexes.stream().map(contentIndex -> {
      contentIndex.setChapterId(getId());
      return contentIndex;
    }).collect(Collectors.toList()));
  }

  public Set<ChapterIndex> getChapterIndices() {
    return chapterIndices;
  }

  public void setChapterIndices(final Set<ChapterIndex> newChapterIndices) {
    this.chapterIndices.clear();
    addChapterIndices(newChapterIndices);
  }

  public void addChapterIndex(final ChapterIndex newChapterIndex) {
    chapterIndices.add(newChapterIndex);
    newChapterIndex.chapter = this;
  }

  public void addChapterIndices(final Set<ChapterIndex> newChapterIndices) {
    chapterIndices.addAll(newChapterIndices.stream().map(chapterIndex -> {
      chapterIndex.chapter = this;
      return chapterIndex;
    }).collect(Collectors.toList()));
  }
}
