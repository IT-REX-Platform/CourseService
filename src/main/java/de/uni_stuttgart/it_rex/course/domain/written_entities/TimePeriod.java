package de.uni_stuttgart.it_rex.course.domain.written_entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Entity
@Table(name = "time_period")
public class TimePeriod implements Serializable {

  public TimePeriod() {
    this.chapterIndices = new ArrayList<>();
  }

  @Id
  @GeneratedValue
  private UUID id;

  /**
   * Start date.
   */
  @Column(name = "start_date")
  private LocalDate startDate;

  /**
   * End date.
   */
  @Column(name = "end_date")
  private LocalDate endDate;

  /**
   * Course id
   */
  @ManyToOne
  @JoinColumn(name = "course_id", referencedColumnName = "id")
  protected Course course;

  /**
   * ChapterIndexes.
   */
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      mappedBy = "timePeriod")
  @OrderBy("index")
  private List<ChapterIndex> chapterIndices;

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

  public Course getCourse() {
    return course;
  }

  public void setCourse(final Course newCourse) {
    if (course != null) {
      course.removeTimePeriod(this);
    }
    newCourse.getTimePeriods().add(this);
    this.course = newCourse;
  }

  public List<ChapterIndex> getChapterIndices() {
    return chapterIndices;
  }

  public void setChapterIndices(final List<ChapterIndex> newChapterIndices) {
    this.chapterIndices.clear();
    addChapterIndices(newChapterIndices);
  }

  public void addChapterIndex(final ChapterIndex chapterIndex) {
    chapterIndex.timePeriod = this;
    this.chapterIndices.add(chapterIndex);
  }

  public void addChapterIndices(final List<ChapterIndex> newChapterIndices) {
    this.chapterIndices.addAll(newChapterIndices.stream().map(chapterIndex -> {
      chapterIndex.timePeriod = this;
      return chapterIndex;
    }).collect(Collectors.toList()));
  }

  public void removeChapterIndex(final ChapterIndex chapterIndex) {
    chapterIndex.timePeriod = null;
    getChapterIndices().remove(chapterIndex);
  }
}
