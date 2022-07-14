package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PoreskaKategorijaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PoreskaKategorija.class);
        PoreskaKategorija poreskaKategorija1 = new PoreskaKategorija();
        poreskaKategorija1.setId(1L);
        PoreskaKategorija poreskaKategorija2 = new PoreskaKategorija();
        poreskaKategorija2.setId(poreskaKategorija1.getId());
        assertThat(poreskaKategorija1).isEqualTo(poreskaKategorija2);
        poreskaKategorija2.setId(2L);
        assertThat(poreskaKategorija1).isNotEqualTo(poreskaKategorija2);
        poreskaKategorija1.setId(null);
        assertThat(poreskaKategorija1).isNotEqualTo(poreskaKategorija2);
    }
}
