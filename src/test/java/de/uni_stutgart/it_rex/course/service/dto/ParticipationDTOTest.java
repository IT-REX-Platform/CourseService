package de.uni_stutgart.it_rex.course.service.dto;

import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;
import de.uni_stutgart.it_rex.course.web.rest.TestUtil;

public class ParticipationDTOTest {

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipationDTO.class);
        ParticipationDTO participationDTO1 = new ParticipationDTO();
        participationDTO1.setId(1L);
        ParticipationDTO participationDTO2 = new ParticipationDTO();
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
        participationDTO2.setId(participationDTO1.getId());
        assertThat(participationDTO1).isEqualTo(participationDTO2);
        participationDTO2.setId(2L);
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
        participationDTO1.setId(null);
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
    }
}
