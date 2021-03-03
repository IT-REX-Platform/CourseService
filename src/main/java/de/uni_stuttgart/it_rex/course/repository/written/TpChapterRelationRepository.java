package de.uni_stuttgart.it_rex.course.repository.written;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data repository for the TpChapterRelation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface TpChapterRelationRepository
    extends JpaRepository<TpChapterRelation, UUID> {
}
