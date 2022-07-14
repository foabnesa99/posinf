package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CenaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Cena.class);
        Cena cena1 = new Cena();
        cena1.setId(1L);
        Cena cena2 = new Cena();
        cena2.setId(cena1.getId());
        assertThat(cena1).isEqualTo(cena2);
        cena2.setId(2L);
        assertThat(cena1).isNotEqualTo(cena2);
        cena1.setId(null);
        assertThat(cena1).isNotEqualTo(cena2);
    }
}
