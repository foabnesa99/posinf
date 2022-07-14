package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoreskaStopaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoreskaStopa.class);
        PoreskaStopa poreskaStopa1 = new PoreskaStopa();
        poreskaStopa1.setId(1L);
        PoreskaStopa poreskaStopa2 = new PoreskaStopa();
        poreskaStopa2.setId(poreskaStopa1.getId());
        assertThat(poreskaStopa1).isEqualTo(poreskaStopa2);
        poreskaStopa2.setId(2L);
        assertThat(poreskaStopa1).isNotEqualTo(poreskaStopa2);
        poreskaStopa1.setId(null);
        assertThat(poreskaStopa1).isNotEqualTo(poreskaStopa2);
    }
}
