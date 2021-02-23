package de.uni_stuttgart.it_rex.course.written;

import javax.ws.rs.core.Response;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;

import de.uni_stuttgart.it_rex.course.written.web.rest.CourseResourceExtended.CourseRole;

public class KeycloakCommunicator {
    // Extract those into a file maybe?
    private static final String URL = "https://keycloak:9080/auth";
    private static final String REALM = "jhipster";
    private static final String USER = "admin";
    private static final String PASSWD = "admin";
    private static final String CLIENT_ID = "admin-cli";

    /**
     * The string template for a role created by the course manager.
     * The first parameter is the role, the second one is the course ID.
     */
    public static final String ROLE_COURSE_TEMPLATE = "ROLE_COURSE_%1$s_%2$s";

    /**
     * The string template for a group created by the course manager.
     * The first parameter is the role, the second one is the course ID.
     */
    public static final String GROUP_COURSE_TEMPLATE = "COURSE_%2$s_COURSE_%1$s";

    /**
     * The keycloak instance used by the communicator.
     */
    private Keycloak keycloak;

    /**
     * Returns a name given a template and course id and role.
     *
     * @param template the format template which will be filled in.
     * @param courseID the id of the course to fill in.
     * @param role the role to make the name for.
     * @return a string containing the filled-in name.
     */
    public static String makeNameForCourse(String template, Long courseID, CourseRole role) {
        return String.format(template, role.toString(), courseID);
    }

    /**
     * Returns the id from a given, previously generated role or group name.
     *
     * @param roleOrGroupName the name to extract the course id from.
     * @return the course id in the given role or group name string.
     */
    public static Long getCourseIdFromName(String roleOrGroupName) {
        String[] splitStr = roleOrGroupName.split("_");
        if (splitStr.length != 4) {
            return -1L;
        }

        if (splitStr[0] == "ROLE") {
            return Long.parseLong(splitStr[4]);
        } else {
            return Long.parseLong(splitStr[2]);
        }
    }


    /**
     * Creates a new communicator with an initialized {@link Keycloak} instance.
     */
    public KeycloakCommunicator() {
        keycloak = KeycloakBuilder.builder().
            serverUrl("http://keycloak:9080/auth")
            .grantType(OAuth2Constants.PASSWORD)
            .realm("master")
            .clientId("admin-cli")
            .username("admin")
            .password("admin")
            .build();
            // .resteasyClient(
            //     new ResteasyClientBuilder()
            //         .connectionPoolSize(10).build())
        // keycloak.tokenManager().getAccessToken();
        // RealmResource realmResource = keycloak.realm("realm-name");
    }

    /**
     * Creates a new communicator with an initialized {@link Keycloak} instance
     * with a custom uri to connect to.
     *
     * @param url the URL of the Keycloak to connect to.
     */
    // public KeycloakCommunicator(String url) {
    //     keycloak = Keycloak.getInstance(url, REALM, USER, PASSWD, CLIENT_ID);
    // }

    /**
     * Adds a role in the main realm of Keycloak.
     *
     * @param role the representation of the role to add.
     */
    public void addRole(RoleRepresentation role) {
        keycloak.realm(REALM).roles().create(role);
    }

    /**
     * Adds a group given by a presentation to the main realm in Keycloak.
     *
     * @param group the representation of the group to add.
     * @return the response by the operation given by Keycloak.
     */
    public Response addGroup(GroupRepresentation group) {
        return keycloak.realm(REALM).groups().add(group);
    }

    /**
     * Gets the users registered by Keycloak.
     *
     * @return the users in Keycloak.
     */
    public UsersResource getUsers() {
        return keycloak.realm(REALM).users();
    }

    /**
     * Removes the role with the given name out of the main realm.
     *
     * @param roleName the name of the role to remove.
     */
    public void removeRole(String roleName) {
        keycloak.realm(REALM).roles().deleteRole(roleName);
    }

    /**
     * Removes the group with the given name out of the main realm.
     *
     * @param groupName the name of the group to remove.
     */
    public void removeGroup(String groupName) {
        GroupsResource groups = keycloak.realm(REALM).groups();
        String groupIdToRemove = "";
        for (GroupRepresentation curGroup : groups.groups()) {
            if (curGroup.getName() == groupName) {
                groupIdToRemove = curGroup.getId();
                break;
            }
        }

        if (groupIdToRemove == "") {
            return;
        }

        groups.group(groupIdToRemove).remove();
    }
}
