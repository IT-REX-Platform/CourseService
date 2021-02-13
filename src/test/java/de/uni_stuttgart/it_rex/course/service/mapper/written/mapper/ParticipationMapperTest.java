package de.uni_stuttgart.it_rex.course.service.mapper.written.mapper;

import de.uni_stuttgart.it_rex.course.service.written.mapper.ParticipationMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class ParticipationMapperTest {

    private ParticipationMapper participationMapper;

    @BeforeEach
    public void setUp() {
        participationMapper = new ParticipationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        final UUID id = UUID.randomUUID();
        assertThat(participationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(participationMapper.fromId(null)).isNull();
    }
}
