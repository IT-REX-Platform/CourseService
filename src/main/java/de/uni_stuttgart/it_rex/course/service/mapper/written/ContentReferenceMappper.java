package de.uni_stuttgart.it_rex.course.service.mapper.written;

import de.uni_stuttgart.it_rex.course.domain.written_entities.Chapter;
import de.uni_stuttgart.it_rex.course.domain.written_entities.ContentReference;
import de.uni_stuttgart.it_rex.course.domain.written_entities.TimePeriod;
import de.uni_stuttgart.it_rex.course.repository.written.ChapterRepository;
import de.uni_stuttgart.it_rex.course.repository.written.TimePeriodRepository;
import de.uni_stuttgart.it_rex.course.service.dto.written_dtos.ContentReferenceDTO;
import org.mapstruct.Mapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public abstract class ContentReferenceMappper {

    @Autowired
    ChapterRepository chapterRepository;

    @Autowired
    TimePeriodRepository timePeriodRepository;

    public List<ContentReference> toEntity(
        final List<ContentReferenceDTO> contentReferenceDTOS) {
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

    public ContentReferenceDTO toDto(final ContentReference contentReference) {
        ContentReferenceDTO contentReferenceDTO = new ContentReferenceDTO();

        contentReferenceDTO.setId(contentReference.getId());
        contentReferenceDTO.setStartDate(contentReference.getStartDate());
        contentReferenceDTO.setEndDate(contentReference.getEndDate());
        contentReferenceDTO.setContentId(contentReference.getContentId());

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
}
