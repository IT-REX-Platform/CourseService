package de.uni_stuttgart.it_rex.course.domain;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * A Course.
 */
@Entity
@Table(name = "course")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Course implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "max_food_sum")
    private Integer maxFoodSum;

    @Lob
    @Column(name = "course_description")
    private byte[] courseDescription;

    @Column(name = "course_description_content_type")
    private String courseDescriptionContentType;

    @OneToMany(mappedBy = "course")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<Participation> participations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Course name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public Course startDate(LocalDate startDate) {
        this.startDate = startDate;
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public Course endDate(LocalDate endDate) {
        this.endDate = endDate;
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Integer getMaxFoodSum() {
        return maxFoodSum;
    }

    public Course maxFoodSum(Integer maxFoodSum) {
        this.maxFoodSum = maxFoodSum;
        return this;
    }

    public void setMaxFoodSum(Integer maxFoodSum) {
        this.maxFoodSum = maxFoodSum;
    }

    public byte[] getCourseDescription() {
        return courseDescription;
    }

    public Course courseDescription(byte[] courseDescription) {
        this.courseDescription = courseDescription;
        return this;
    }

    public void setCourseDescription(byte[] courseDescription) {
        this.courseDescription = courseDescription;
    }

    public String getCourseDescriptionContentType() {
        return courseDescriptionContentType;
    }

    public Course courseDescriptionContentType(String courseDescriptionContentType) {
        this.courseDescriptionContentType = courseDescriptionContentType;
        return this;
    }

    public void setCourseDescriptionContentType(String courseDescriptionContentType) {
        this.courseDescriptionContentType = courseDescriptionContentType;
    }

    public Set<Participation> getParticipations() {
        return participations;
    }

    public Course participations(Set<Participation> participations) {
        this.participations = participations;
        return this;
    }

    public Course addParticipation(Participation participation) {
        this.participations.add(participation);
        participation.setCourse(this);
        return this;
    }

    public Course removeParticipation(Participation participation) {
        this.participations.remove(participation);
        participation.setCourse(null);
        return this;
    }

    public void setParticipations(Set<Participation> participations) {
        this.participations = participations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Course)) {
            return false;
        }
        return id != null && id.equals(((Course) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Course{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", maxFoodSum=" + getMaxFoodSum() +
            ", courseDescription='" + getCourseDescription() + "'" +
            ", courseDescriptionContentType='" + getCourseDescriptionContentType() + "'" +
            "}";
    }
}
