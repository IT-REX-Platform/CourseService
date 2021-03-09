package de.uni_stuttgart.it_rex.course.web.rest.written;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.uni_stuttgart.it_rex.course.domain.enumeration.REXROLE;
import de.uni_stuttgart.it_rex.course.security.written.RexAuthz;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.UserDTO;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class UserInfoResource {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(UserInfoResource.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "UserInfo";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @GetMapping("/user-info")
    public UserDTO getUserInfo() {

        UserDTO userDto = new UserDTO();
        userDto.setId(RexAuthz.getUserId());
        userDto.setUserName(RexAuthz.getUserName().orElse(null));
        userDto.setName(RexAuthz.getName().orElse(null));
        userDto.setGivenName(RexAuthz.getGivenName().orElse(null));
        userDto.setFamilyName(RexAuthz.getFamilyName().orElse(null));
        userDto.setEmail(RexAuthz.getEmail().orElse(null));
        userDto.setRexRole(RexAuthz.getRexRole());
        userDto.setCourses(RexAuthz.getCoursesAndRolesOfUser());

        return userDto;
    }
}
