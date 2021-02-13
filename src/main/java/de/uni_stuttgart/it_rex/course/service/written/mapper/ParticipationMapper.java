package de.uni_stuttgart.it_rex.course.service.written.mapper;


import de.uni_stuttgart.it_rex.course.domain.written.Participation;
import de.uni_stuttgart.it_rex.course.service.written.dto.ParticipationDTO;
import de.uni_stuttgart.it_rex.course.service.mapper.EntityMapper;
import de.uni_stuttgart.it_rex.course.service.mapper.UserMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

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

    default Participation fromId(final UUID id) {
        if (id == null) {
            return null;
        }
        Participation participation = new Participation();
        participation.setId(id);
        return participation;
    }
}
