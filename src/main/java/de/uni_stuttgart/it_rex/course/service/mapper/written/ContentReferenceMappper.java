package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.ContentReferenceRepository;
import de.uni_stuttgart.it_rex.course.repository.written.CourseRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ContentReferenceMappper {

    @Autowired
    private ChapterRepository chapterRepository;

    @Autowired
    private CourseRepository courseRepository;

    @Autowired
    private ContentReferenceRepository contentReferenceRepository;

    @Autowired
    private TimePeriodRepository timePeriodRepository;

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

    public void updateContentReferenceFromContentReferenceDTO(
        final ContentReferenceDTO update,
        final ContentReference toUpdate) {

        if (update.getId() != null) {
            toUpdate.setId(update.getId());
        }
        if (update.getStartDate() != null) {
            toUpdate.setStartDate(update.getStartDate());
        }
        if (update.getEndDate() != null) {
            toUpdate.setEndDate(update.getEndDate());
        }
        if (update.getContentId() != null) {
            toUpdate.setContentId(update.getContentId());
        }
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
}
