package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class JedinicaMereTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(JedinicaMere.class);
        JedinicaMere jedinicaMere1 = new JedinicaMere();
        jedinicaMere1.setId(1L);
        JedinicaMere jedinicaMere2 = new JedinicaMere();
        jedinicaMere2.setId(jedinicaMere1.getId());
        assertThat(jedinicaMere1).isEqualTo(jedinicaMere2);
        jedinicaMere2.setId(2L);
        assertThat(jedinicaMere1).isNotEqualTo(jedinicaMere2);
        jedinicaMere1.setId(null);
        assertThat(jedinicaMere1).isNotEqualTo(jedinicaMere2);
    }
}
