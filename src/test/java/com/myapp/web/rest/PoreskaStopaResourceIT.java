package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PoreskaStopa;
import com.myapp.repository.PoreskaStopaRepository;
import java.time.LocalDate;
import java.time.ZoneId;
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
 * Integration tests for the {@link PoreskaStopaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoreskaStopaResourceIT {

    private static final Double DEFAULT_PROCENAT_PDV = 1D;
    private static final Double UPDATED_PROCENAT_PDV = 2D;

    private static final LocalDate DEFAULT_DATUM_VAZENJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_VAZENJA = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/poreska-stopas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoreskaStopaRepository poreskaStopaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoreskaStopaMockMvc;

    private PoreskaStopa poreskaStopa;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoreskaStopa createEntity(EntityManager em) {
        PoreskaStopa poreskaStopa = new PoreskaStopa().procenatPdv(DEFAULT_PROCENAT_PDV).datumVazenja(DEFAULT_DATUM_VAZENJA);
        return poreskaStopa;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoreskaStopa createUpdatedEntity(EntityManager em) {
        PoreskaStopa poreskaStopa = new PoreskaStopa().procenatPdv(UPDATED_PROCENAT_PDV).datumVazenja(UPDATED_DATUM_VAZENJA);
        return poreskaStopa;
    }

    @BeforeEach
    public void initTest() {
        poreskaStopa = createEntity(em);
    }

    @Test
    @Transactional
    void createPoreskaStopa() throws Exception {
        int databaseSizeBeforeCreate = poreskaStopaRepository.findAll().size();
        // Create the PoreskaStopa
        restPoreskaStopaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaStopa)))
            .andExpect(status().isCreated());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeCreate + 1);
        PoreskaStopa testPoreskaStopa = poreskaStopaList.get(poreskaStopaList.size() - 1);
        assertThat(testPoreskaStopa.getProcenatPdv()).isEqualTo(DEFAULT_PROCENAT_PDV);
        assertThat(testPoreskaStopa.getDatumVazenja()).isEqualTo(DEFAULT_DATUM_VAZENJA);
    }

    @Test
    @Transactional
    void createPoreskaStopaWithExistingId() throws Exception {
        // Create the PoreskaStopa with an existing ID
        poreskaStopa.setId(1L);

        int databaseSizeBeforeCreate = poreskaStopaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoreskaStopaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaStopa)))
            .andExpect(status().isBadRequest());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPoreskaStopas() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        // Get all the poreskaStopaList
        restPoreskaStopaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poreskaStopa.getId().intValue())))
            .andExpect(jsonPath("$.[*].procenatPdv").value(hasItem(DEFAULT_PROCENAT_PDV.doubleValue())))
            .andExpect(jsonPath("$.[*].datumVazenja").value(hasItem(DEFAULT_DATUM_VAZENJA.toString())));
    }

    @Test
    @Transactional
    void getPoreskaStopa() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        // Get the poreskaStopa
        restPoreskaStopaMockMvc
            .perform(get(ENTITY_API_URL_ID, poreskaStopa.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poreskaStopa.getId().intValue()))
            .andExpect(jsonPath("$.procenatPdv").value(DEFAULT_PROCENAT_PDV.doubleValue()))
            .andExpect(jsonPath("$.datumVazenja").value(DEFAULT_DATUM_VAZENJA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingPoreskaStopa() throws Exception {
        // Get the poreskaStopa
        restPoreskaStopaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoreskaStopa() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();

        // Update the poreskaStopa
        PoreskaStopa updatedPoreskaStopa = poreskaStopaRepository.findById(poreskaStopa.getId()).get();
        // Disconnect from session so that the updates on updatedPoreskaStopa are not directly saved in db
        em.detach(updatedPoreskaStopa);
        updatedPoreskaStopa.procenatPdv(UPDATED_PROCENAT_PDV).datumVazenja(UPDATED_DATUM_VAZENJA);

        restPoreskaStopaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoreskaStopa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoreskaStopa))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaStopa testPoreskaStopa = poreskaStopaList.get(poreskaStopaList.size() - 1);
        assertThat(testPoreskaStopa.getProcenatPdv()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testPoreskaStopa.getDatumVazenja()).isEqualTo(UPDATED_DATUM_VAZENJA);
    }

    @Test
    @Transactional
    void putNonExistingPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poreskaStopa.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poreskaStopa))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poreskaStopa))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poreskaStopa)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoreskaStopaWithPatch() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();

        // Update the poreskaStopa using partial update
        PoreskaStopa partialUpdatedPoreskaStopa = new PoreskaStopa();
        partialUpdatedPoreskaStopa.setId(poreskaStopa.getId());

        partialUpdatedPoreskaStopa.procenatPdv(UPDATED_PROCENAT_PDV);

        restPoreskaStopaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoreskaStopa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoreskaStopa))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaStopa testPoreskaStopa = poreskaStopaList.get(poreskaStopaList.size() - 1);
        assertThat(testPoreskaStopa.getProcenatPdv()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testPoreskaStopa.getDatumVazenja()).isEqualTo(DEFAULT_DATUM_VAZENJA);
    }

    @Test
    @Transactional
    void fullUpdatePoreskaStopaWithPatch() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();

        // Update the poreskaStopa using partial update
        PoreskaStopa partialUpdatedPoreskaStopa = new PoreskaStopa();
        partialUpdatedPoreskaStopa.setId(poreskaStopa.getId());

        partialUpdatedPoreskaStopa.procenatPdv(UPDATED_PROCENAT_PDV).datumVazenja(UPDATED_DATUM_VAZENJA);

        restPoreskaStopaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoreskaStopa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoreskaStopa))
            )
            .andExpect(status().isOk());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
        PoreskaStopa testPoreskaStopa = poreskaStopaList.get(poreskaStopaList.size() - 1);
        assertThat(testPoreskaStopa.getProcenatPdv()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testPoreskaStopa.getDatumVazenja()).isEqualTo(UPDATED_DATUM_VAZENJA);
    }

    @Test
    @Transactional
    void patchNonExistingPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poreskaStopa.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poreskaStopa))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poreskaStopa))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoreskaStopa() throws Exception {
        int databaseSizeBeforeUpdate = poreskaStopaRepository.findAll().size();
        poreskaStopa.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoreskaStopaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poreskaStopa))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoreskaStopa in the database
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoreskaStopa() throws Exception {
        // Initialize the database
        poreskaStopaRepository.saveAndFlush(poreskaStopa);

        int databaseSizeBeforeDelete = poreskaStopaRepository.findAll().size();

        // Delete the poreskaStopa
        restPoreskaStopaMockMvc
            .perform(delete(ENTITY_API_URL_ID, poreskaStopa.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PoreskaStopa> poreskaStopaList = poreskaStopaRepository.findAll();
        assertThat(poreskaStopaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
