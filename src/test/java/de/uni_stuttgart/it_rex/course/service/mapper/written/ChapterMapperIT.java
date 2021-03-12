package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.CourseServiceApp;
import de.uni_stuttgart.it_rex.course.config.TestSecurityConfiguration;
import de.uni_stuttgart.it_rex.course.domain.written.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written.Course;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import de.uni_stuttgart.it_rex.course.utils.written.ChapterUtil;
import de.uni_stuttgart.it_rex.course.utils.written.ContentReferenceUtil;
import de.uni_stuttgart.it_rex.course.utils.written.CourseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(classes = {CourseServiceApp.class,
    TestSecurityConfiguration.class})
class ChapterMapperIT {
    private static final UUID UPDATE_ID = UUID.randomUUID();

    @Autowired
    private ChapterMapper chapterMapper;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    private static Course THE_COURSE;

    @BeforeEach
    void init() {
        THE_COURSE = CourseUtil.createCourse();
        courseRepository.save(THE_COURSE);
    }


    @Test
    void updateChapterFromChapterDTO() {
        final Chapter toUpdate = ChapterUtil.createChapter();
        toUpdate.setCourse(THE_COURSE);

        final ChapterDTO update = new ChapterDTO();
        update.setId(UPDATE_ID);

        final Chapter expected = new Chapter();
        expected.setId(update.getId());
        expected.setName(toUpdate.getName());
        expected.setCourse(THE_COURSE);

        chapterMapper.updateChapterFromChapterDTO(update, toUpdate);

        assertEquals(expected, toUpdate);
    }

    @Test
    void toDTO() {
        final Chapter chapter = ChapterUtil.createChapter();
        final ChapterDTO expected = new ChapterDTO();

        expected.setId(chapter.getId());
        expected.setName(chapter.getName());

        final ChapterDTO result = chapterMapper.toDTO(chapter);
        ChapterUtil.equalsChapterDTO(expected, result);
    }

    @Test
    void withContentReferencesToDTO() {
        final Chapter chapter = ChapterUtil.createChapter();
        final ContentReference contentReference1 =
            ContentReferenceUtil.createContentReference();
        final ContentReference contentReference2 =
            ContentReferenceUtil.createContentReference();
        chapter.addContentReference(contentReference1);
        chapter.addContentReference(contentReference2);

        final ContentReferenceDTO contentReference1DTO =
            new ContentReferenceDTO();
        contentReference1DTO.setId(contentReference1.getId());
        contentReference1DTO.setContentId(contentReference1.getContentId());
        contentReference1DTO
            .setChapterId(contentReference1.getChapter().getId());
        contentReference1DTO.setContentReferenceType(
            contentReference1.getContentReferenceType());

        final ContentReferenceDTO contentReference2DTO =
            new ContentReferenceDTO();
        contentReference2DTO.setId(contentReference2.getId());
        contentReference2DTO.setContentId(contentReference2.getContentId());
        contentReference2DTO
            .setChapterId(contentReference2.getChapter().getId());
        contentReference2DTO.setContentReferenceType(
            contentReference2.getContentReferenceType());

        final ChapterDTO expected = new ChapterDTO();
        expected.setId(chapter.getId());
        expected.setName(chapter.getName());
        expected.setContentReferences(
            Arrays.asList(contentReference1DTO, contentReference2DTO));

        final ChapterDTO result = chapterMapper.toDTO(chapter);
        ChapterUtil.equalsChapterDTO(expected, result);
    }

    @Test
    void toEntity() {
        final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
        final Chapter expected = new Chapter();

        expected.setId(chapterDTO.getId());
        expected.setName(chapterDTO.getName());

        final Chapter result = chapterMapper.toEntity(chapterDTO);
        ChapterUtil.equalsChapter(expected, result);
    }

    @Test
    @Transactional
    void withContentReferencesToEntity() {
        final ChapterDTO chapterDTO = ChapterUtil.createChapterDTO();
        final ContentReferenceDTO contentReference1DTO =
            ContentReferenceUtil.createContentReferenceDTO();
        final ContentReferenceDTO contentReference2DTO =
            ContentReferenceUtil.createContentReferenceDTO();
        chapterDTO.setContentReferences(
            Arrays.asList(contentReference1DTO, contentReference2DTO));

        final Chapter result = chapterMapper.toEntity(chapterDTO);

        final Chapter expected = new Chapter();
        expected.setId(chapterDTO.getId());
        expected.setName(chapterDTO.getName());
        chapterRepository.save(expected);

        chapterDTO.setId(expected.getId());

        final ContentReference contentReference1 = new ContentReference();
        contentReference1.setId(contentReference1.getId());
        contentReference1.setContentId(contentReference1.getContentId());
        contentReference1.setChapter(expected);
        contentReference1.setContentReferenceType(
            contentReference1.getContentReferenceType());

        final ContentReference contentReference2 = new ContentReference();
        contentReference2.setId(contentReference2DTO.getId());
        contentReference2.setContentId(contentReference2DTO.getContentId());
        contentReference2.setChapter(expected);
        contentReference2.setContentReferenceType(
            contentReference2DTO.getContentReferenceType());

        ChapterUtil.equalsChapter(expected, result);
    }
}
