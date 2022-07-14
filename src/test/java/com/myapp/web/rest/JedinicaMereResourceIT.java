package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.JedinicaMere;
import com.myapp.repository.JedinicaMereRepository;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link JedinicaMereResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class JedinicaMereResourceIT {

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final String DEFAULT_SKRACENI_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_SKRACENI_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/jedinica-meres";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private JedinicaMereRepository jedinicaMereRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restJedinicaMereMockMvc;

    private JedinicaMere jedinicaMere;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JedinicaMere createEntity(EntityManager em) {
        JedinicaMere jedinicaMere = new JedinicaMere().naziv(DEFAULT_NAZIV).skraceniNaziv(DEFAULT_SKRACENI_NAZIV);
        return jedinicaMere;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static JedinicaMere createUpdatedEntity(EntityManager em) {
        JedinicaMere jedinicaMere = new JedinicaMere().naziv(UPDATED_NAZIV).skraceniNaziv(UPDATED_SKRACENI_NAZIV);
        return jedinicaMere;
    }

    @BeforeEach
    public void initTest() {
        jedinicaMere = createEntity(em);
    }

    @Test
    @Transactional
    void createJedinicaMere() throws Exception {
        int databaseSizeBeforeCreate = jedinicaMereRepository.findAll().size();
        // Create the JedinicaMere
        restJedinicaMereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jedinicaMere)))
            .andExpect(status().isCreated());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeCreate + 1);
        JedinicaMere testJedinicaMere = jedinicaMereList.get(jedinicaMereList.size() - 1);
        assertThat(testJedinicaMere.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testJedinicaMere.getSkraceniNaziv()).isEqualTo(DEFAULT_SKRACENI_NAZIV);
    }

    @Test
    @Transactional
    void createJedinicaMereWithExistingId() throws Exception {
        // Create the JedinicaMere with an existing ID
        jedinicaMere.setId(1L);

        int databaseSizeBeforeCreate = jedinicaMereRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restJedinicaMereMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jedinicaMere)))
            .andExpect(status().isBadRequest());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllJedinicaMeres() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        // Get all the jedinicaMereList
        restJedinicaMereMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(jedinicaMere.getId().intValue())))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)))
            .andExpect(jsonPath("$.[*].skraceniNaziv").value(hasItem(DEFAULT_SKRACENI_NAZIV)));
    }

    @Test
    @Transactional
    void getJedinicaMere() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        // Get the jedinicaMere
        restJedinicaMereMockMvc
            .perform(get(ENTITY_API_URL_ID, jedinicaMere.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(jedinicaMere.getId().intValue()))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV))
            .andExpect(jsonPath("$.skraceniNaziv").value(DEFAULT_SKRACENI_NAZIV));
    }

    @Test
    @Transactional
    void getNonExistingJedinicaMere() throws Exception {
        // Get the jedinicaMere
        restJedinicaMereMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewJedinicaMere() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();

        // Update the jedinicaMere
        JedinicaMere updatedJedinicaMere = jedinicaMereRepository.findById(jedinicaMere.getId()).get();
        // Disconnect from session so that the updates on updatedJedinicaMere are not directly saved in db
        em.detach(updatedJedinicaMere);
        updatedJedinicaMere.naziv(UPDATED_NAZIV).skraceniNaziv(UPDATED_SKRACENI_NAZIV);

        restJedinicaMereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedJedinicaMere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedJedinicaMere))
            )
            .andExpect(status().isOk());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
        JedinicaMere testJedinicaMere = jedinicaMereList.get(jedinicaMereList.size() - 1);
        assertThat(testJedinicaMere.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testJedinicaMere.getSkraceniNaziv()).isEqualTo(UPDATED_SKRACENI_NAZIV);
    }

    @Test
    @Transactional
    void putNonExistingJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, jedinicaMere.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jedinicaMere))
            )
            .andExpect(status().isBadRequest());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(jedinicaMere))
            )
            .andExpect(status().isBadRequest());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(jedinicaMere)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateJedinicaMereWithPatch() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();

        // Update the jedinicaMere using partial update
        JedinicaMere partialUpdatedJedinicaMere = new JedinicaMere();
        partialUpdatedJedinicaMere.setId(jedinicaMere.getId());

        partialUpdatedJedinicaMere.skraceniNaziv(UPDATED_SKRACENI_NAZIV);

        restJedinicaMereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJedinicaMere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJedinicaMere))
            )
            .andExpect(status().isOk());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
        JedinicaMere testJedinicaMere = jedinicaMereList.get(jedinicaMereList.size() - 1);
        assertThat(testJedinicaMere.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testJedinicaMere.getSkraceniNaziv()).isEqualTo(UPDATED_SKRACENI_NAZIV);
    }

    @Test
    @Transactional
    void fullUpdateJedinicaMereWithPatch() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();

        // Update the jedinicaMere using partial update
        JedinicaMere partialUpdatedJedinicaMere = new JedinicaMere();
        partialUpdatedJedinicaMere.setId(jedinicaMere.getId());

        partialUpdatedJedinicaMere.naziv(UPDATED_NAZIV).skraceniNaziv(UPDATED_SKRACENI_NAZIV);

        restJedinicaMereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedJedinicaMere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedJedinicaMere))
            )
            .andExpect(status().isOk());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
        JedinicaMere testJedinicaMere = jedinicaMereList.get(jedinicaMereList.size() - 1);
        assertThat(testJedinicaMere.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testJedinicaMere.getSkraceniNaziv()).isEqualTo(UPDATED_SKRACENI_NAZIV);
    }

    @Test
    @Transactional
    void patchNonExistingJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, jedinicaMere.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jedinicaMere))
            )
            .andExpect(status().isBadRequest());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(jedinicaMere))
            )
            .andExpect(status().isBadRequest());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamJedinicaMere() throws Exception {
        int databaseSizeBeforeUpdate = jedinicaMereRepository.findAll().size();
        jedinicaMere.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restJedinicaMereMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(jedinicaMere))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the JedinicaMere in the database
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteJedinicaMere() throws Exception {
        // Initialize the database
        jedinicaMereRepository.saveAndFlush(jedinicaMere);

        int databaseSizeBeforeDelete = jedinicaMereRepository.findAll().size();

        // Delete the jedinicaMere
        restJedinicaMereMockMvc
            .perform(delete(ENTITY_API_URL_ID, jedinicaMere.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<JedinicaMere> jedinicaMereList = jedinicaMereRepository.findAll();
        assertThat(jedinicaMereList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
