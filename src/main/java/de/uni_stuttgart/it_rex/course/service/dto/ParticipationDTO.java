package de.uni_stuttgart.it_rex.course.service.dto;

import java.io.Serializable;
import java.util.UUID;
import de.uni_stuttgart.it_rex.course.domain.enumeration.PARTICIPATIONTYPE;

/**
 * A DTO for the {@link de.uni_stuttgart.it_rex.course.domain.Participation} entity.
 */
public class ParticipationDTO implements Serializable {
    
    private Long id;

    private UUID uuid;

    private PARTICIPATIONTYPE type;


    private String userId;

    private String userLogin;

    private Long courseId;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UUID getUuid() {
        return uuid;
    }

    public void setUuid(UUID uuid) {
        this.uuid = uuid;
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
            "id=" + getId() +
            ", uuid='" + getUuid() + "'" +
            ", type='" + getType() + "'" +
            ", userId='" + getUserId() + "'" +
            ", userLogin='" + getUserLogin() + "'" +
            ", courseId=" + getCourseId() +
            "}";
    }
}
