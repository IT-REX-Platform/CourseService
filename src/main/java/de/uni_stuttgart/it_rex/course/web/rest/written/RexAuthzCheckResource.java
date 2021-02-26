package de.uni_stuttgart.it_rex.course.web.rest.written;

import de.uni_stuttgart.it_rex.course.domain.enumeration.REXROLE;
import de.uni_stuttgart.it_rex.course.service.written.RexAuthz;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * REST controller for managing {@link User}.
 */
@RestController
@RequestMapping("/api")
public class RexAuthzCheckResource {
    /**
     * Logger.
     */
    private final Logger log = LoggerFactory.getLogger(RexAuthzCheckResource.class);

    /**
     * Entity name.
     */
    private static final String ENTITY_NAME = "User";

    /**
     * Application name.
     */
    @Value("${jhipster.clientApp.name}")
    private String applicationName;


    @GetMapping("/rex-authz-check/")
    public ResponseEntity<Void> getUserInfo() {
        log.debug("REST request to get UserInfo");
        log.debug("getUserId: {}", RexAuthz.getUserId());
        log.debug("getCoursesOfUser: {}", RexAuthz.getCoursesOfUser());
        log.debug("getCoursesAndRolesOfUser: {}", RexAuthz.getCoursesAndRolesOfUser());
        log.debug("getRexRole: {}", RexAuthz.getRexRole());
        log.debug("userHasRexRole: {}", RexAuthz.userHasRexRole(REXROLE.STUDENT));
        log.debug("userIsRexAdmin: {}", RexAuthz.userIsRexAdmin());
        log.debug("userIsRexLecturer: {}", RexAuthz.userIsRexLecturer());
        log.debug("userIsRexStudent: {}", RexAuthz.userIsRexStudent());

        return ResponseEntity.ok().build();
    }

}

