package de.uni_stuttgart.it_rex.course.service.mapper;


import de.uni_stuttgart.it_rex.course.domain.*;
import de.uni_stuttgart.it_rex.course.service.dto.ParticipationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participation} and its DTO {@link ParticipationDTO}.
 */
@Mapper(componentModel = "spring", uses = {UserMapper.class, CourseMapper.class})
public interface ParticipationMapper extends EntityMapper<ParticipationDTO, Participation> {

    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.login", target = "userLogin")
    @Mapping(source = "course.id", target = "courseId")
    ParticipationDTO toDto(Participation participation);

    @Mapping(source = "userId", target = "user")
    @Mapping(source = "courseId", target = "course")
    Participation toEntity(ParticipationDTO participationDTO);

    default Participation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participation participation = new Participation();
        participation.setId(id);
        return participation;
    }
}
