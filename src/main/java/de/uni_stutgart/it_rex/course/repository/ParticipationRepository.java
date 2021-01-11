package de.uni_stutgart.it_rex.course.repository;

import de.uni_stutgart.it_rex.course.domain.Participation;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data  repository for the Participation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ParticipationRepository extends JpaRepository<Participation, Long> {
}
