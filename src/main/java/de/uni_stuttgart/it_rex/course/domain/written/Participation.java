package de.uni_stuttgart.it_rex.course.domain.written;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import de.uni_stuttgart.it_rex.course.domain.User;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PARTICIPATIONTYPE;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.UUID;

/**
 * A Participation.
 */
@Entity
@Table(name = "participation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Participation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "type")
    private PARTICIPATIONTYPE type;

    @ManyToOne
    @JsonIgnoreProperties(value = "participations", allowSetters = true)
    private User user;

    @ManyToOne
    @JsonIgnoreProperties(value = "participations", allowSetters = true)
    private Course course;

    // jhipster-needle-entity-add-field - JHipster will add fields here
    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public PARTICIPATIONTYPE getType() {
        return type;
    }

    public Participation type(PARTICIPATIONTYPE type) {
        this.type = type;
        return this;
    }

    public void setType(PARTICIPATIONTYPE type) {
        this.type = type;
    }

    public User getUser() {
        return user;
    }

    public Participation user(User user) {
        this.user = user;
        return this;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Course getCourse() {
        return course;
    }

    public Participation course(Course course) {
        this.course = course;
        return this;
    }

    public void setCourse(Course course) {
        this.course = course;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participation)) {
            return false;
        }
        return id != null && id.equals(((Participation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Participation{" +
            "id=" + getId() +
            ", type='" + getType() + "'" +
            "}";
    }
}
