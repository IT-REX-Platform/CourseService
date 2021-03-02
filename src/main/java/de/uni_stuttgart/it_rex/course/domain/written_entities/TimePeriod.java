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
    this.tPChapterRelation = new ArrayList<>();
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
   * TpChapterRelation.
   */
  @OneToMany(cascade = CascadeType.ALL,
      fetch = FetchType.LAZY,
      orphanRemoval = true,
      mappedBy = "timePeriod")
  @OrderBy("index")
  private List<TpChapterRelation> tPChapterRelation;

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

  public List<TpChapterRelation> getTPChapterRelation() {
    return tPChapterRelation;
  }

  public void setTPChapterRelation(
      final List<TpChapterRelation> newChapterIndices) {
    this.tPChapterRelation.clear();
    addTPChapterRelations(newChapterIndices);
  }

  public void addTPChapterRelation(final TpChapterRelation tpChapterRelation) {
    tpChapterRelation.timePeriod = this;
    this.tPChapterRelation.add(tpChapterRelation);
  }

  public void addTPChapterRelations(
      final List<TpChapterRelation> newTPChapterRelation) {
    this.tPChapterRelation.addAll(newTPChapterRelation.stream()
        .map(tPChapterRelation -> {
          tPChapterRelation.timePeriod = this;
          return tPChapterRelation;
        }).collect(Collectors.toList()));
  }

  public void removeTPChapterRelation(
      final TpChapterRelation tpChapterRelation) {
    tpChapterRelation.timePeriod = null;
    getTPChapterRelation().remove(tpChapterRelation);
  }
}
