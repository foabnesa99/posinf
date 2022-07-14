package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PoreskaKategorija;
import com.myapp.repository.PoreskaKategorijaRepository;
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
 * Integration tests for the {@link PoreskaKategorijaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoreskaKategorijaResourceIT {

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/poreska-kategorijas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoreskaKategorijaRepository poreskaKategorijaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoreskaKategorijaMockMvc;

    private PoreskaKategorija poreskaKategorija;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoreskaKategorija createEntity(EntityManager em) {
        PoreskaKategorija poreskaKategorija = new PoreskaKategorija().naziv(DEFAULT_NAZIV);
        return poreskaKategorija;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoreskaKategorija createUpdatedEntity(EntityManager em) {
        PoreskaKategorija poreskaKategorija = new PoreskaKategorija().naziv(UPDATED_NAZIV);
        return poreskaKategorija;
    }

    @BeforeEach
    public void initTest() {
        poreskaKategorija = createEntity(em);
    }

    @Test
    @Transactional
    void createPoreskaKategorija() throws Exception {
        int databaseSizeBeforeCreate = poreskaKategorijaRepository.findAll().size();
        // Create the PoreskaKategorija
        restPoreskaKategorijaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isCreated());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeCreate + 1);
        PoreskaKategorija testPoreskaKategorija = poreskaKategorijaList.get(poreskaKategorijaList.size() - 1);
        assertThat(testPoreskaKategorija.getNaziv()).isEqualTo(DEFAULT_NAZIV);
    }

    @Test
    @Transactional
    void createPoreskaKategorijaWithExistingId() throws Exception {
        // Create the PoreskaKategorija with an existing ID
        poreskaKategorija.setId(1L);

        int databaseSizeBeforeCreate = poreskaKategorijaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoreskaKategorijaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPoreskaKategorijas() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        // Get all the poreskaKategorijaList
        restPoreskaKategorijaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poreskaKategorija.getId().intValue())))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)));
    }

    @Test
    @Transactional
    void getPoreskaKategorija() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        // Get the poreskaKategorija
        restPoreskaKategorijaMockMvc
            .perform(get(ENTITY_API_URL_ID, poreskaKategorija.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poreskaKategorija.getId().intValue()))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV));
    }

    @Test
    @Transactional
    void getNonExistingPoreskaKategorija() throws Exception {
        // Get the poreskaKategorija
        restPoreskaKategorijaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoreskaKategorija() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();

        // Update the poreskaKategorija
        PoreskaKategorija updatedPoreskaKategorija = poreskaKategorijaRepository.findById(poreskaKategorija.getId()).get();
        // Disconnect from session so that the updates on updatedPoreskaKategorija are not directly saved in db
        em.detach(updatedPoreskaKategorija);
        updatedPoreskaKategorija.naziv(UPDATED_NAZIV);

        restPoreskaKategorijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoreskaKategorija.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoreskaKategorija))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaKategorija testPoreskaKategorija = poreskaKategorijaList.get(poreskaKategorijaList.size() - 1);
        assertThat(testPoreskaKategorija.getNaziv()).isEqualTo(UPDATED_NAZIV);
    }

    @Test
    @Transactional
    void putNonExistingPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poreskaKategorija.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoreskaKategorijaWithPatch() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();

        // Update the poreskaKategorija using partial update
        PoreskaKategorija partialUpdatedPoreskaKategorija = new PoreskaKategorija();
        partialUpdatedPoreskaKategorija.setId(poreskaKategorija.getId());

        partialUpdatedPoreskaKategorija.naziv(UPDATED_NAZIV);

        restPoreskaKategorijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoreskaKategorija.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoreskaKategorija))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaKategorija testPoreskaKategorija = poreskaKategorijaList.get(poreskaKategorijaList.size() - 1);
        assertThat(testPoreskaKategorija.getNaziv()).isEqualTo(UPDATED_NAZIV);
    }

    @Test
    @Transactional
    void fullUpdatePoreskaKategorijaWithPatch() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();

        // Update the poreskaKategorija using partial update
        PoreskaKategorija partialUpdatedPoreskaKategorija = new PoreskaKategorija();
        partialUpdatedPoreskaKategorija.setId(poreskaKategorija.getId());

        partialUpdatedPoreskaKategorija.naziv(UPDATED_NAZIV);

        restPoreskaKategorijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoreskaKategorija.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoreskaKategorija))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaKategorija testPoreskaKategorija = poreskaKategorijaList.get(poreskaKategorijaList.size() - 1);
        assertThat(testPoreskaKategorija.getNaziv()).isEqualTo(UPDATED_NAZIV);
    }

    @Test
    @Transactional
    void patchNonExistingPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poreskaKategorija.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoreskaKategorija() throws Exception {
        int databaseSizeBeforeUpdate = poreskaKategorijaRepository.findAll().size();
        poreskaKategorija.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaKategorijaMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poreskaKategorija))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoreskaKategorija in the database
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoreskaKategorija() throws Exception {
        // Initialize the database
        poreskaKategorijaRepository.saveAndFlush(poreskaKategorija);

        int databaseSizeBeforeDelete = poreskaKategorijaRepository.findAll().size();

        // Delete the poreskaKategorija
        restPoreskaKategorijaMockMvc
            .perform(delete(ENTITY_API_URL_ID, poreskaKategorija.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PoreskaKategorija> poreskaKategorijaList = poreskaKategorijaRepository.findAll();
        assertThat(poreskaKategorijaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
