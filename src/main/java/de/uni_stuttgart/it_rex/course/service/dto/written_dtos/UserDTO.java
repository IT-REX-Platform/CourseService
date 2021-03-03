package de.uni_stuttgart.it_rex.course.service.dto.written_dtos;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import de.uni_stuttgart.it_rex.course.domain.enumeration.COURSEROLE;
import de.uni_stuttgart.it_rex.course.domain.enumeration.REXROLE;

/**
 * The DTO for user information
 */
public class UserDTO implements Serializable {

    /**
     * id of user.
     */
    private UUID id;

    /**
     * user name of user.
     */
    private String userName;

    /**
     * name of user.
     */
    private String name;

    /**
     * given name of user.
     */
    private String givenName;

    /**
     * given name of user.
     */
    private String familyName;

    /**
     * email of user.
     */
    private String email;

    /**
     * Rexrole of user.
     */
    private REXROLE rexRole;

    /**
     * courses with roles of user.
     */
    private Map<UUID, COURSEROLE> courses;

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
     * @param newId the id
     */
    public void setId(final UUID newId) {
        this.id = newId;
    }

    /**
     * Getter.
     *
     * @return the userName.
     */
    public String getUserName() {
        return userName;
    }

    /**
     * Setter.
     *
     * @param newUserName the userName
     */
    public void setUserName(final String newUserName) {
        this.userName = newUserName;
    }

    /**
     * Getter.
     *
     * @return the givenName.
     */
    public String getGivenName() {
        return givenName;
    }

    /**
     * Setter.
     *
     * @param newGivenName the givenName
     */
    public void setGivenName(final String newGivenName) {
        this.givenName = newGivenName;
    }

    /**
     * Getter.
     *
     * @return the familyName.
     */
    public String getFamilyName() {
        return familyName;
    }

    /**
     * Setter.
     *
     * @param newFamilyName the familyName
     */
    public void setFamilyName(final String newFamilyName) {
        this.familyName = newFamilyName;
    }

    /**
     * Getter.
     *
     * @return the email.
     */
    public String getEmail() {
        return email;
    }

    /**
     * Setter.
     *
     * @param newEmail the email
     */
    public void setEmail(final String newEmail) {
        this.email = newEmail;
    }

    /**
     * Getter.
     *
     * @return the name.
     */
    public String getName() {
        return name;
    }

    /**
     * Setter.
     *
     * @param newName the name
     */
    public void setName(final String newName) {
        this.name = newName;
    }

    /**
     * Getter.
     *
     * @return the RexRole.
     */
    public REXROLE getRexRole() {
        return rexRole;
    }

    /**
     * Setter.
     *
     * @param newRexRole the RexRole
     */
    public void setRexRole(final REXROLE newRexRole) {
        this.rexRole = newRexRole;
    }

    /**
     * Getter.
     *
     * @return the courses.
     */
    public Map<UUID, COURSEROLE> getCourses() {
        return courses;
    }

    /**
     * Setter.
     *
     * @param newCourses the Courses
     */
    public void setCourses(final Map<UUID, COURSEROLE> newCourses) {
        this.courses = newCourses;
    }

    /**
     * Equals method.
     *
     * @param o the other object.
     * @return if they are equal
     */
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(getId(), userDTO.getId()) 
                && Objects.equals(getUserName(), userDTO.getUserName())
                && Objects.equals(getName(), userDTO.getName())
                && Objects.equals(getGivenName(), userDTO.getGivenName())
                && Objects.equals(getFamilyName(), userDTO.getFamilyName())
                && Objects.equals(getEmail(), userDTO.getEmail()) 
                && getRexRole() == userDTO.getRexRole()
                && Objects.equals(getCourses(), userDTO.getCourses());
    }

    /**
     * Hash code method.
     *
     * @return the hash code.
     */
    @Override
    public int hashCode() {
        return Objects.hash(
                getId(),
                getUserName(),
                getName(),
                getGivenName(),
                getFamilyName(),
                getEmail(),
                getRexRole(),
                getCourses());
    }

    /**
     * To string method.
     *
     * @return the string
     */
    @Override
    public String toString() {
        return "UserDTO{" +
            "id=" + id +
            ", userName='" + userName + '\'' +
            ", name='" + name + '\'' +
            ", givenName='" + givenName + '\'' +
            ", familyName='" + familyName + '\'' +
            ", email='" + email + '\'' +
            ", rexRole=" + rexRole +
            ", courses=" + courses +
            '}';
    }
}
