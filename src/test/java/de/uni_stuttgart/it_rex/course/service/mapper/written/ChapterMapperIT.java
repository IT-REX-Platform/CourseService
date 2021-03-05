package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {ChapterMapperImpl.class})
class ChapterMapperIT {
  private static final UUID UPDATE_ID = UUID.randomUUID();

  @Autowired
  private ChapterMapper chapterMapper;

  @Test
  void updateChapterFromChapter() {
    Chapter toUpdate = ChapterUtil.createChapter();
    ChapterDTO update = new ChapterDTO();

    update.setId(UPDATE_ID);
    update.setTitle(null);

    Chapter expected = new Chapter();
    expected.setId(update.getId());
    expected.setTitle(toUpdate.getTitle());
    expected.setStartDate(toUpdate.getStartDate());
    expected.setEndDate(toUpdate.getEndDate());

    chapterMapper.updateChapterFromChapterDTO(update, toUpdate);

    assertEquals(expected, toUpdate);
  }

  @Test
  void toDTO() {
    Chapter chapter = ChapterUtil.createChapter();
    ChapterDTO expected = new ChapterDTO();
    expected.setId(chapter.getId());
    expected.setTitle(chapter.getTitle());
    expected.setStartDate(chapter.getStartDate());
    expected.setEndDate(chapter.getEndDate());

    ChapterDTO result = chapterMapper.toDTO(chapter);
    assertEquals(expected, result);
  }

  @Test
  void toEntity() {
    ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
    Chapter expected = new Chapter();
    expected.setId(chapterDTO.getId());
    expected.setTitle(chapterDTO.getTitle());
    expected.setStartDate(chapterDTO.getStartDate());
    expected.setEndDate(chapterDTO.getEndDate());

    final List<UUID> contentIds = chapterDTO.getContentReferenceIds();
    final List<ContentReference> contents =
        contentIds.stream().map(contentId -> {
            final UUID id = contentId;
            ContentReference contentReference = new ContentReference();
            //     contentReference.setIndex(i);
            contentReference.setContentId(id);
            //     contentReference.setChapterId(expected.getId());
            return contentReference;
        }).collect(Collectors.toList());

    // expected.setContents(contents);

    Chapter result = chapterMapper.toEntity(chapterDTO);
    ChapterUtil.equalsChapter(expected, result);
  }
}
