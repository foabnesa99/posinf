package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Preduzece;
import com.myapp.repository.PreduzeceRepository;
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
 * Integration tests for the {@link PreduzeceResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PreduzeceResourceIT {

    private static final String DEFAULT_PIB = "AAAAAAAAAA";
    private static final String UPDATED_PIB = "BBBBBBBBBB";

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESA = "AAAAAAAAAA";
    private static final String UPDATED_ADRESA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/preduzeces";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PreduzeceRepository preduzeceRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPreduzeceMockMvc;

    private Preduzece preduzece;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preduzece createEntity(EntityManager em) {
        Preduzece preduzece = new Preduzece().pib(DEFAULT_PIB).naziv(DEFAULT_NAZIV).adresa(DEFAULT_ADRESA);
        return preduzece;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Preduzece createUpdatedEntity(EntityManager em) {
        Preduzece preduzece = new Preduzece().pib(UPDATED_PIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA);
        return preduzece;
    }

    @BeforeEach
    public void initTest() {
        preduzece = createEntity(em);
    }

    @Test
    @Transactional
    void createPreduzece() throws Exception {
        int databaseSizeBeforeCreate = preduzeceRepository.findAll().size();
        // Create the Preduzece
        restPreduzeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(preduzece)))
            .andExpect(status().isCreated());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeCreate + 1);
        Preduzece testPreduzece = preduzeceList.get(preduzeceList.size() - 1);
        assertThat(testPreduzece.getPib()).isEqualTo(DEFAULT_PIB);
        assertThat(testPreduzece.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testPreduzece.getAdresa()).isEqualTo(DEFAULT_ADRESA);
    }

    @Test
    @Transactional
    void createPreduzeceWithExistingId() throws Exception {
        // Create the Preduzece with an existing ID
        preduzece.setId(1L);

        int databaseSizeBeforeCreate = preduzeceRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPreduzeceMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(preduzece)))
            .andExpect(status().isBadRequest());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPreduzeces() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        // Get all the preduzeceList
        restPreduzeceMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(preduzece.getId().intValue())))
            .andExpect(jsonPath("$.[*].pib").value(hasItem(DEFAULT_PIB)))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)))
            .andExpect(jsonPath("$.[*].adresa").value(hasItem(DEFAULT_ADRESA)));
    }

    @Test
    @Transactional
    void getPreduzece() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        // Get the preduzece
        restPreduzeceMockMvc
            .perform(get(ENTITY_API_URL_ID, preduzece.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(preduzece.getId().intValue()))
            .andExpect(jsonPath("$.pib").value(DEFAULT_PIB))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV))
            .andExpect(jsonPath("$.adresa").value(DEFAULT_ADRESA));
    }

    @Test
    @Transactional
    void getNonExistingPreduzece() throws Exception {
        // Get the preduzece
        restPreduzeceMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPreduzece() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();

        // Update the preduzece
        Preduzece updatedPreduzece = preduzeceRepository.findById(preduzece.getId()).get();
        // Disconnect from session so that the updates on updatedPreduzece are not directly saved in db
        em.detach(updatedPreduzece);
        updatedPreduzece.pib(UPDATED_PIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA);

        restPreduzeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPreduzece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPreduzece))
            )
            .andExpect(status().isOk());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
        Preduzece testPreduzece = preduzeceList.get(preduzeceList.size() - 1);
        assertThat(testPreduzece.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testPreduzece.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testPreduzece.getAdresa()).isEqualTo(UPDATED_ADRESA);
    }

    @Test
    @Transactional
    void putNonExistingPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, preduzece.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(preduzece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(preduzece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(preduzece)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePreduzeceWithPatch() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();

        // Update the preduzece using partial update
        Preduzece partialUpdatedPreduzece = new Preduzece();
        partialUpdatedPreduzece.setId(preduzece.getId());

        partialUpdatedPreduzece.pib(UPDATED_PIB);

        restPreduzeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreduzece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPreduzece))
            )
            .andExpect(status().isOk());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
        Preduzece testPreduzece = preduzeceList.get(preduzeceList.size() - 1);
        assertThat(testPreduzece.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testPreduzece.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testPreduzece.getAdresa()).isEqualTo(DEFAULT_ADRESA);
    }

    @Test
    @Transactional
    void fullUpdatePreduzeceWithPatch() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();

        // Update the preduzece using partial update
        Preduzece partialUpdatedPreduzece = new Preduzece();
        partialUpdatedPreduzece.setId(preduzece.getId());

        partialUpdatedPreduzece.pib(UPDATED_PIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA);

        restPreduzeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPreduzece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPreduzece))
            )
            .andExpect(status().isOk());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
        Preduzece testPreduzece = preduzeceList.get(preduzeceList.size() - 1);
        assertThat(testPreduzece.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testPreduzece.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testPreduzece.getAdresa()).isEqualTo(UPDATED_ADRESA);
    }

    @Test
    @Transactional
    void patchNonExistingPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, preduzece.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(preduzece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(preduzece))
            )
            .andExpect(status().isBadRequest());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPreduzece() throws Exception {
        int databaseSizeBeforeUpdate = preduzeceRepository.findAll().size();
        preduzece.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPreduzeceMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(preduzece))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Preduzece in the database
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePreduzece() throws Exception {
        // Initialize the database
        preduzeceRepository.saveAndFlush(preduzece);

        int databaseSizeBeforeDelete = preduzeceRepository.findAll().size();

        // Delete the preduzece
        restPreduzeceMockMvc
            .perform(delete(ENTITY_API_URL_ID, preduzece.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Preduzece> preduzeceList = preduzeceRepository.findAll();
        assertThat(preduzeceList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
