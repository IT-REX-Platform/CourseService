package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class, TestSecurityConfiguration.class})
class ChapterMapperIT {
  private static final UUID UPDATE_ID = UUID.randomUUID();

  @Autowired
  private ChapterMapper chapterMapper;

  @Test
  void updateChapterFromChapterDTO() {
    Chapter toUpdate = ChapterUtil.createChapter();
    ChapterDTO update = new ChapterDTO();

    update.setId(UPDATE_ID);

    Chapter expected = new Chapter();
    expected.setId(update.getId());
    expected.setTitle(toUpdate.getTitle());

    chapterMapper.updateChapterFromChapterDTO(update, toUpdate);

    assertEquals(expected, toUpdate);
  }

  @Test
  void toDTO() {
    Chapter chapter = ChapterUtil.createChapter();
    ChapterDTO expected = new ChapterDTO();

    expected.setId(chapter.getId());
    expected.setTitle(chapter.getTitle());

    ChapterDTO result = chapterMapper.toDTO(chapter);
    ChapterUtil.equalsChapterDTO(expected, result);
  }

  @Test
  void toEntity() {
    ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    Chapter expected = new Chapter();

    expected.setId(chapterDTO.getId());
    expected.setTitle(chapterDTO.getTitle());

    Chapter result = chapterMapper.toEntity(chapterDTO);
    ChapterUtil.equalsChapter(expected, result);
  }
}
