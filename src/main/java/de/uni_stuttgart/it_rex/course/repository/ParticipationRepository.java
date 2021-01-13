package de.uni_stuttgart.it_rex.course.repository;

import de.uni_stuttgart.it_rex.course.domain.Participation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data  repository for the Participation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {

    @Query("select participation from Participation participation where participation.user.login = ?#{principal.preferredUsername}")
    List<Participation> findByUserIsCurrentUser();
}
