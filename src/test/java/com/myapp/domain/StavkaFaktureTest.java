package com.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StavkaFaktureTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StavkaFakture.class);
        StavkaFakture stavkaFakture1 = new StavkaFakture();
        stavkaFakture1.setId(1L);
        StavkaFakture stavkaFakture2 = new StavkaFakture();
        stavkaFakture2.setId(stavkaFakture1.getId());
        assertThat(stavkaFakture1).isEqualTo(stavkaFakture2);
        stavkaFakture2.setId(2L);
        assertThat(stavkaFakture1).isNotEqualTo(stavkaFakture2);
        stavkaFakture1.setId(null);
        assertThat(stavkaFakture1).isNotEqualTo(stavkaFakture2);
    }
}
