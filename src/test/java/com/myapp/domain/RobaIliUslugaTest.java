package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RobaIliUslugaTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(RobaIliUsluga.class);
        RobaIliUsluga robaIliUsluga1 = new RobaIliUsluga();
        robaIliUsluga1.setId(1L);
        RobaIliUsluga robaIliUsluga2 = new RobaIliUsluga();
        robaIliUsluga2.setId(robaIliUsluga1.getId());
        assertThat(robaIliUsluga1).isEqualTo(robaIliUsluga2);
        robaIliUsluga2.setId(2L);
        assertThat(robaIliUsluga1).isNotEqualTo(robaIliUsluga2);
        robaIliUsluga1.setId(null);
        assertThat(robaIliUsluga1).isNotEqualTo(robaIliUsluga2);
    }
}
