package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ChapterDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ChapterMapper {

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ChapterRepository chapterRepository;

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "contentReferences", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateChapterFromChapterDTO
    (ChapterDTO update, @MappingTarget Chapter toUpdate);

    public Chapter updateOrCreateFromDTO(final ChapterDTO update) {
        final Optional<Chapter> chapterOptional
            = chapterRepository.findById(update.getId());

        Chapter toUpdate = new Chapter();
        if (chapterOptional.isPresent()) {
            toUpdate = chapterOptional.get();
        }
        updateChapterFromChapterDTO(update, toUpdate);
        return toUpdate;
    }

    /**
     * Converts an entity to a DTO.
     *
     * @param chapter the entity
     * @return the dto
     */
    public ChapterDTO toDTO(final Chapter chapter) {
        final ChapterDTO chapterDTO = setBasicProperties(chapter);
        if (chapter.getCourse() != null) {
            chapterDTO.setCourseId(chapter.getCourse().getId());
        }
        if (chapter.getContentReferences() != null) {
            final List<UUID> chapters = chapter.getContentReferences().stream()
                .map(ContentReference::getContentId)
                .collect(Collectors.toList());
            chapterDTO.setContentReferenceIds(chapters);
        }
        return chapterDTO;
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param chapter the entity
     * @return the dto
     */
    public Optional<ChapterDTO> toDTO(final Optional<Chapter> chapter) {
        if (chapter.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(chapter.get()));
    }

    /**
     * Converts a list of entities to a list of DTOs.
     *
     * @param chapters the entities
     * @return the DTOs
     */
    public List<ChapterDTO> toDTO(final Collection<Chapter> chapters) {
        return chapters.stream().map(this::toDTO).collect(Collectors.toList());
    }

    public List<Chapter> toEntity(final List<ChapterDTO> chapterDTOS) {
        return chapterDTOS.stream().map(this::toEntity)
            .collect(Collectors.toList());
    }

    /**
     * Converts a DTO to an entity.
     *
     * @param chapterDTO the dto
     * @return the entity
     */
    public Chapter toEntity(final ChapterDTO chapterDTO) {
        final Chapter chapter = setBasicProperties(chapterDTO);
        if (chapterDTO.getCourseId() != null) {
            courseRepository.findById(chapterDTO.getCourseId())
                .ifPresent(chapter::setCourse);
        }
        if (chapterDTO.getContentReferenceIds() != null) {
            final List<ContentReference> contentReferences =
                contentReferenceRepository.findAllById(
                    chapterDTO.getContentReferenceIds());
            chapter.setContentReferences(contentReferences);
        }
        return chapter;
    }

    /**
     * Creates a ChapterDTO from a Chapter and sets all properties
     * except the contents.
     *
     * @param chapter the entity
     * @return the dto
     */
    private ChapterDTO setBasicProperties(final Chapter chapter) {
        final ChapterDTO chapterDTO = new ChapterDTO();
        chapterDTO.setId(chapter.getId());
        chapterDTO.setTitle(chapter.getTitle());
        chapterDTO.setStartDate(chapter.getStartDate());
        chapterDTO.setEndDate(chapter.getEndDate());
        return chapterDTO;
    }

    /**
     * Creates a Chapter from a ChapterDTO and sets all properties
     * except the contents.
     *
     * @param chapterDTO the dto
     * @return the entity
     */
    private Chapter setBasicProperties(final ChapterDTO chapterDTO) {
        final Chapter chapter = new Chapter();
        chapter.setId(chapterDTO.getId());
        chapter.setTitle(chapterDTO.getTitle());
        chapter.setStartDate(chapterDTO.getStartDate());
        chapter.setEndDate(chapterDTO.getEndDate());
        return chapter;
    }

}
