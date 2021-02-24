package de.uni_stuttgart.it_rex.course.service.written;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.GroupsResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.OAuth2Constants;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;

import de.uni_stuttgart.it_rex.course.service.written.RexAuthz.CourseRole;

@Service
public class KeycloakAdminService {
    // TODO: Extract those into a file.
    private static final String URL = "http://keycloak:9080/auth";
    private static final String REALM = "jhipster";
    private static final String USER = "admin";
    private static final String PASSWD = "admin";
    private static final String CLIENT_ID = "admin-cli";

    /**
     * The keycloak instance used by the communicator.
     */
    private Keycloak keycloak;

    /**
     * Creates a new communicator with an initialized {@link Keycloak} instance.
     */
    public KeycloakAdminService() {
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

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        List<RoleRepresentation> roles = new ArrayList<RoleRepresentation>(roleNames.length);
        for (String curRoleName : roleNames) {
            roles.add(keycloak.realm(REALM)
                .roles().get(curRoleName).toRepresentation());
        }

        keycloak.realm(REALM).groups().group(groupId).roles().realmLevel().add(roles);
    }

    /**
     * Adds a given user by id to a group by name.
     *
     * @param userId the id of the user to add to the group.
     * @param groupName the name of the group to add the user to.
     */
    public void addUserToGroup(String userId, String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(REALM).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        keycloak.realm(REALM).users().get(userId).joinGroup(groupId);
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
        List<GroupRepresentation> groups = keycloak.realm(REALM).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        keycloak.realm(REALM).groups().group(groupId).remove();
    }

    /**
     * Removes a user by id from a group by name.
     *
     * @param userId the user by id to remove from the group.
     * @param groupName the name of the group to remove the user from.
     */
    public void removeUserFromGroup(String userId, String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(REALM).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();
        keycloak.realm(REALM).users().get(userId).leaveGroup(groupId);
    }
}
