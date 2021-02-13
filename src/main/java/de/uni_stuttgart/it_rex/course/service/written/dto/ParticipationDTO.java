package de.uni_stuttgart.it_rex.course.service.written.dto;

import de.uni_stuttgart.it_rex.course.domain.enumeration.PARTICIPATIONTYPE;
import de.uni_stuttgart.it_rex.course.domain.written.Participation;

import java.io.Serializable;
import java.util.UUID;

/**
 * A DTO for the {@link Participation} entity.
 */
public class ParticipationDTO implements Serializable {

    private UUID id;

    private PARTICIPATIONTYPE type;

    private String userId;

    private String userLogin;

    private Long courseId;

    public UUID getId() {
        return id;
    }

    public void setId(final UUID id) {
        this.id = id;
    }

    public PARTICIPATIONTYPE getType() {
        return type;
    }

    public void setType(PARTICIPATIONTYPE type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserLogin() {
        return userLogin;
    }

    public void setUserLogin(String userLogin) {
        this.userLogin = userLogin;
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
            "id=" + getId().toString() +
            ", type='" + getType() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", courseId=" + getCourseId() +
            "}";
    }
}
