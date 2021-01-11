package de.uni_stuttgart.it_rex.course.service.dto;

import java.io.Serializable;
import de.uni_stuttgart.it_rex.course.domain.enumeration.ROLE;

/**
 * A DTO for the {@link de.uni_stuttgart.it_rex.course.domain.Participation} entity.
 */
public class ParticipationDTO implements Serializable {
    
    private Long id;

    private ROLE status;


    private Long personId;

    private Long courseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ROLE getStatus() {
        return status;
    }

    public void setStatus(ROLE status) {
        this.status = status;
    }

    public Long getPersonId() {
        return personId;
    }

    public void setPersonId(Long personId) {
        this.personId = personId;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ParticipationDTO)) {
            return false;
        }

        return id != null && id.equals(((ParticipationDTO) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ParticipationDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", personId=" + getPersonId() +
            ", courseId=" + getCourseId() +
            "}";
    }
}
