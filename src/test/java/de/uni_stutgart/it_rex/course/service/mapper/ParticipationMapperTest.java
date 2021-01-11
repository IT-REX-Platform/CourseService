package de.uni_stutgart.it_rex.course.service.mapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

public class ParticipationMapperTest {

    private ParticipationMapper participationMapper;

    @BeforeEach
    public void setUp() {
        participationMapper = new ParticipationMapperImpl();
    }

    @Test
    public void testEntityFromId() {
        Long id = 1L;
        assertThat(participationMapper.fromId(id).getId()).isEqualTo(id);
        assertThat(participationMapper.fromId(null)).isNull();
    }
}
