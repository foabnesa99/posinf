package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class IzlaznaFakturaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(IzlaznaFaktura.class);
        IzlaznaFaktura izlaznaFaktura1 = new IzlaznaFaktura();
        izlaznaFaktura1.setId(1L);
        IzlaznaFaktura izlaznaFaktura2 = new IzlaznaFaktura();
        izlaznaFaktura2.setId(izlaznaFaktura1.getId());
        assertThat(izlaznaFaktura1).isEqualTo(izlaznaFaktura2);
        izlaznaFaktura2.setId(2L);
        assertThat(izlaznaFaktura1).isNotEqualTo(izlaznaFaktura2);
        izlaznaFaktura1.setId(null);
        assertThat(izlaznaFaktura1).isNotEqualTo(izlaznaFaktura2);
    }
}
