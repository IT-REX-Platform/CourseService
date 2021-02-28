package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.ChapterIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data repository for the ContentIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ChapterIndexRepository extends JpaRepository<ChapterIndex, UUID> {
}
