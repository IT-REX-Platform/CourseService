package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ContentReferenceMapper {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private TimePeriodRepository timePeriodRepository;

    /**
     * Updates an entity from a DTO.
     *
     * @param update   the update
     * @param toUpdate the updated entity.
     */
    @Mapping(target = "course", ignore = true)
    @Mapping(target = "chapters", ignore = true)
    @Mapping(target = "timePeriods", ignore = true)
    @BeanMapping(nullValuePropertyMappingStrategy =
        NullValuePropertyMappingStrategy.IGNORE)
    public abstract void updateContentReferenceFromContentReferenceDTO(
        final ContentReferenceDTO update,
        @MappingTarget final ContentReference toUpdate);

    public ContentReference updateOrCreateFromDTO(
        final ContentReferenceDTO update) {
        final Optional<ContentReference> contentReferenceOptional
            = contentReferenceRepository.findById(update.getId());

        ContentReference toUpdate = new ContentReference();
        if (contentReferenceOptional.isPresent()) {
            toUpdate = contentReferenceOptional.get();
        }
        updateContentReferenceFromContentReferenceDTO(update, toUpdate);
        return toUpdate;
    }

    public List<ContentReference> toEntity(
        final Collection<ContentReferenceDTO> contentReferenceDTOS) {
        return contentReferenceDTOS.stream().map(this::toEntity).collect(
            Collectors.toList());
    }

    public ContentReference toEntity(
        final ContentReferenceDTO contentReferenceDTO) {
        ContentReference contentReference = new ContentReference();

        contentReference.setId(contentReferenceDTO.getId());
        contentReference.setStartDate(contentReferenceDTO.getStartDate());
        contentReference.setEndDate(contentReferenceDTO.getEndDate());
        contentReference.setContentId(contentReferenceDTO.getContentId());

        if (contentReferenceDTO.getCourseId() != null) {
            courseRepository.findById(contentReferenceDTO.getCourseId())
                .ifPresent(contentReference::setCourse);
        }
        if (contentReferenceDTO.getChapterIds() != null) {
            contentReference.setChapters(
                chapterRepository
                    .findAllById(contentReferenceDTO.getChapterIds()));
        }
        if (contentReferenceDTO.getTimePeriodIds() != null) {
            contentReference.setTimePeriods(
                timePeriodRepository
                    .findAllById(contentReferenceDTO.getTimePeriodIds()));
        }

        return contentReference;
    }

    public ContentReferenceDTO toDTO(final ContentReference contentReference) {
        ContentReferenceDTO contentReferenceDTO = new ContentReferenceDTO();

        contentReferenceDTO.setId(contentReference.getId());
        contentReferenceDTO.setStartDate(contentReference.getStartDate());
        contentReferenceDTO.setEndDate(contentReference.getEndDate());
        contentReferenceDTO.setContentId(contentReference.getContentId());

        if (contentReference.getCourse() != null) {
            contentReferenceDTO
                .setCourseId(contentReference.getCourse().getId());
        }

        contentReferenceDTO.setChapterIds(
            contentReference.getChapters().stream().map(Chapter::getId).collect(
                Collectors.toSet())
        );
        contentReferenceDTO.setTimePeriodIds(
            contentReference.getTimePeriods().stream().map(TimePeriod::getId)
                .collect(
                    Collectors.toSet())
        );

        return contentReferenceDTO;
    }

    public List<ContentReferenceDTO> toDTO(final Collection<ContentReference> contentReferences) {
        return contentReferences.stream().map(this::toDTO).collect(Collectors.toList());
    }

    /**
     * Converts an optional entity to a optional DTO.
     *
     * @param contentReference the optional
     * @return the dto
     */
    public Optional<ContentReferenceDTO> toDTO(final Optional<ContentReference> contentReference) {
        if (contentReference.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(toDTO(contentReference.get()));
    }
}
