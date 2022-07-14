package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class KupacTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Kupac.class);
        Kupac kupac1 = new Kupac();
        kupac1.setId(1L);
        Kupac kupac2 = new Kupac();
        kupac2.setId(kupac1.getId());
        assertThat(kupac1).isEqualTo(kupac2);
        kupac2.setId(2L);
        assertThat(kupac1).isNotEqualTo(kupac2);
        kupac1.setId(null);
        assertThat(kupac1).isNotEqualTo(kupac2);
    }
}
