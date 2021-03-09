package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class ContentReferenceMapperIT {
  private static final UUID UPDATE_ID = UUID.randomUUID();

  @Autowired
  private ContentReferenceMapper contentReferenceMapper;

  @Test
  @Disabled
  void updateContentReferenceFromContentReferenceDTO() {
    ContentReference toUpdate = ContentReferenceUtil.createContentReference();
    ContentReferenceDTO update = new ContentReferenceDTO();

    update.setId(UPDATE_ID);
    update.setContentId(null);

    ContentReference expected = new ContentReference();
    expected.setId(update.getId());
    expected.setContentId(toUpdate.getContentId());

    contentReferenceMapper.updateContentReferenceFromContentReferenceDTO(update, toUpdate);

    ContentReferenceUtil.equalsContentReference(expected, toUpdate);
  }

  @Test
  void toDTO() {
    ContentReference contentReference = ContentReferenceUtil.createContentReference();
    ContentReferenceDTO expected = new ContentReferenceDTO();

    expected.setId(contentReference.getId());
    expected.setContentId(contentReference.getContentId());

    ContentReferenceDTO result = contentReferenceMapper.toDTO(contentReference);
    ContentReferenceUtil.equalsContentReferenceDTO(expected, result);
  }

  @Test
  @Disabled
  void toEntity() {
    ContentReferenceDTO contentReferenceDTO
        = ContentReferenceUtil.createContentReferenceDTO();
    ContentReference expected = new ContentReference();

    expected.setId(contentReferenceDTO.getId());
    expected.setContentId(contentReferenceDTO.getContentId());

    ContentReference result = contentReferenceMapper.toEntity(contentReferenceDTO);
    ContentReferenceUtil.equalsContentReference(expected, result);
  }
}
