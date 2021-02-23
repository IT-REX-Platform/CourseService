package de.uni_stuttgart.it_rex.course.written;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    // TODO: Extract those into a file.
    private static final String URL = "http://keycloak:9080/auth";
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
        keycloak = KeycloakBuilder.builder()
            .serverUrl(URL)
            .grantType(OAuth2Constants.PASSWORD)
            .realm("master")
            .clientId(CLIENT_ID)
            .username(USER)
            .password(PASSWD)
            .build();

        // TODO: Resteasy client for pooling, for handling multiple requests at the same time.
        // .resteasyClient(
        //     new ResteasyClientBuilder()
        //         .connectionPoolSize(10).build())
    }

    /**
     * Adds a role in the main realm of Keycloak.
     *
     * @param roleName the name of the role to add.
     */
    public void addRole(String roleName) {
        addRole(roleName, "Automatically created role.");
    }

    /**
     * Adds a role with a given description in the main realm of Keycloak.
     *
     * @param roleName the name of the role to add.
     * @param description the description the role has.
     */
    public void addRole(String roleName, String description) {
        RoleRepresentation newRoleRep = new RoleRepresentation();
        newRoleRep.setName(roleName);
        newRoleRep.setDescription(description);
        keycloak.realm(REALM).roles().create(newRoleRep);
    }

    /**
     * Adds a group to the main realm in Keycloak.
     *
     * @param groupName the name of the group to add.
     */
    public void addGroup(String groupName) {
        GroupRepresentation newGroupRep = new GroupRepresentation();
        newGroupRep.setName(groupName);
        keycloak.realm(REALM).groups().add(newGroupRep);
    }

    /**
     * Adds the given roles to the given group.
     *
     * @param groupName the name of the group to add the role(s) to.
     * @param roleNames the name(s) of the role(s) to add to the group.
     */
    public void addRolesToGroup(String groupName, String... roleNames) {
        List<GroupRepresentation> groups = keycloak.realm(REALM)
            .groups().groups(groupName, 0, Integer.MAX_VALUE);
        String groupId = groups.get(0).getId();

        List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>(roleNames.length);
        for (String curRoleName : roleNames) {
            roles.add(keycloak.realm(REALM)
                .roles().get(curRoleName).toRepresentation());
        }

        keycloak.realm(REALM).groups().group(groupId)
            .roles().realmLevel().add(roles);
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
            if (curGroup.getName().equals(groupName)) {
                groupIdToRemove = curGroup.getId();
                break;
            }
        }

        if (groupIdToRemove.isEmpty()) {
            return;
        }

        groups.group(groupIdToRemove).remove();
    }
}
