package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoslovnaGodinaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoslovnaGodina.class);
        PoslovnaGodina poslovnaGodina1 = new PoslovnaGodina();
        poslovnaGodina1.setId(1L);
        PoslovnaGodina poslovnaGodina2 = new PoslovnaGodina();
        poslovnaGodina2.setId(poslovnaGodina1.getId());
        assertThat(poslovnaGodina1).isEqualTo(poslovnaGodina2);
        poslovnaGodina2.setId(2L);
        assertThat(poslovnaGodina1).isNotEqualTo(poslovnaGodina2);
        poslovnaGodina1.setId(null);
        assertThat(poslovnaGodina1).isNotEqualTo(poslovnaGodina2);
    }
}
