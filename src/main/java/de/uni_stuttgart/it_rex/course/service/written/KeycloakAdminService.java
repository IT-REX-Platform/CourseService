package de.uni_stuttgart.it_rex.course.service.written;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.GroupRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class KeycloakAdminService {
    /**
     * Logger.
     */
    private static final Logger log = LoggerFactory.getLogger(KeycloakAdminService.class);

    final String serverUri;
    final String realm;
    final String clientId;
    final String clientSecret;

    /**
     * The keycloak instance used by the communicator.
     */
    private Keycloak keycloak;

    /**
     * Creates a new communicator with an initialized {@link Keycloak} instance.
     */
    public KeycloakAdminService(
        @Value("${it-rex.keycloak-admin-service.server-uri}")
        final String newServerUri,
        @Value("${it-rex.keycloak-admin-service.realm}")
        final String newRealm,
        @Value("${spring.security.oauth2.client.registration.oidc.client-id}")
        final String newClientId,
        @Value("${spring.security.oauth2.client.registration.oidc.client-secret}")
        final String newClientSecret
    ) {
        this.serverUri = newServerUri;
        this.realm = newRealm;
        this.clientId = newClientId;
        this.clientSecret = newClientSecret;

        log.debug("KeycloakAdminService ctor");
        log.debug("serverUri: {}", this.serverUri);
        log.debug("realm: {}", this.realm);
        log.debug("clientId: {}", this.clientId);
        log.debug("clientSecret: {}", this.clientSecret);

        //  keycloak = KeycloakBuilder.builder()
        //      .serverUrl(this.serverUri)
        //      .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
        //      .realm(this.realm)
        //      .clientId(this.clientId)
        //      .clientSecret(this.clientSecret)
        //      .build();

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
        keycloak.realm(this.realm).roles().create(newRoleRep);
    }

    /**
     * Adds a group to the main realm in Keycloak.
     *
     * @param groupName the name of the group to add.
     */
    public void addGroup(String groupName) {
        GroupRepresentation newGroupRep = new GroupRepresentation();
        newGroupRep.setName(groupName);
        keycloak.realm(this.realm).groups().add(newGroupRep);
    }

    /**
     * Adds the given roles to the given group.
     *
     * @param groupName the name of the group to add the role(s) to.
     * @param roleNames the name(s) of the role(s) to add to the group.
     */
    public void addRolesToGroup(String groupName, String... roleNames) {
        List<GroupRepresentation> groups = keycloak.realm(this.realm)
            .groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        List<RoleRepresentation> roles = new ArrayList<>(roleNames.length);
        for (String curRoleName : roleNames) {
            roles.add(keycloak.realm(this.realm)
                .roles().get(curRoleName).toRepresentation());
        }

        keycloak.realm(this.realm).groups().group(groupId).roles().realmLevel().add(roles);
    }

    /**
     * Adds a given user by id to a group by name.
     *
     * @param userId the id of the user to add to the group.
     * @param groupName the name of the group to add the user to.
     */
    public void addUserToGroup(String userId, String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(this.realm).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        keycloak.realm(this.realm).users().get(userId).joinGroup(groupId);
    }

    /**
     * Gets the users registered by Keycloak.
     *
     * @return the users in Keycloak.
     */
    public UsersResource getUsers() {
        return keycloak.realm(this.realm).users();
    }

    /**
     * Removes the role with the given name out of the main realm.
     *
     * @param roleName the name of the role to remove.
     */
    public void removeRole(String roleName) {
        keycloak.realm(this.realm).roles().deleteRole(roleName);
    }

    /**
     * Removes the group with the given name out of the main realm.
     *
     * @param groupName the name of the group to remove.
     */
    public void removeGroup(String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(this.realm).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();

        keycloak.realm(this.realm).groups().group(groupId).remove();
    }

    /**
     * Removes a user by id from a group by name.
     *
     * @param userId the user by id to remove from the group.
     * @param groupName the name of the group to remove the user from.
     */
    public void removeUserFromGroup(String userId, String groupName) {
        List<GroupRepresentation> groups = keycloak.realm(this.realm).groups().groups(groupName, 0, Integer.MAX_VALUE);

        if (groups.isEmpty()) {
            // TODO: Maybe throw exception?
            return;
        }
        String groupId = groups.get(0).getId();
        keycloak.realm(this.realm).users().get(userId).leaveGroup(groupId);
    }
}
