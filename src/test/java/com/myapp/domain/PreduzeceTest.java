package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PreduzeceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Preduzece.class);
        Preduzece preduzece1 = new Preduzece();
        preduzece1.setId(1L);
        Preduzece preduzece2 = new Preduzece();
        preduzece2.setId(preduzece1.getId());
        assertThat(preduzece1).isEqualTo(preduzece2);
        preduzece2.setId(2L);
        assertThat(preduzece1).isNotEqualTo(preduzece2);
        preduzece1.setId(null);
        assertThat(preduzece1).isNotEqualTo(preduzece2);
    }
}
