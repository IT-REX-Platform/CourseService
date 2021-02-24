package de.uni_stuttgart.it_rex.course.repository.written;

import de.uni_stuttgart.it_rex.course.domain.ContentIndex;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * Spring Data repository for the ContentIndex entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ContentIndexRepository extends JpaRepository<ContentIndex, UUID> {
}
