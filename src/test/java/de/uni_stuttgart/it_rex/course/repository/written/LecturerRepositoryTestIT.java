package de.uni_stuttgart.it_rex.course.repository.written;


import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Lecturer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {TestSecurityConfiguration.class})
@Transactional
public class LecturerRepositoryTestIT {
  private static final String TEST_NAME = "Steffen";

  @Autowired
  LecturerRepository lecturerRepository;

  @Test
 public void saveAndFind() {
    Lecturer steffen = new Lecturer();
    steffen.setName(TEST_NAME);

    UUID id = lecturerRepository.save(steffen).getId();
    Optional<Lecturer> result = lecturerRepository.findById(id);
    assertTrue(result.isPresent());
    assertEquals(result.get().getName(), steffen.getName());
  }
}
