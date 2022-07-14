package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.PoslovnaGodina;
import com.myapp.repository.PoslovnaGodinaRepository;
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
 * Integration tests for the {@link PoslovnaGodinaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PoslovnaGodinaResourceIT {

    private static final Integer DEFAULT_GODINA = 1;
    private static final Integer UPDATED_GODINA = 2;

    private static final Boolean DEFAULT_ZAKLJUCENA = false;
    private static final Boolean UPDATED_ZAKLJUCENA = true;

    private static final String ENTITY_API_URL = "/api/poslovna-godinas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PoslovnaGodinaRepository poslovnaGodinaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPoslovnaGodinaMockMvc;

    private PoslovnaGodina poslovnaGodina;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoslovnaGodina createEntity(EntityManager em) {
        PoslovnaGodina poslovnaGodina = new PoslovnaGodina().godina(DEFAULT_GODINA).zakljucena(DEFAULT_ZAKLJUCENA);
        return poslovnaGodina;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PoslovnaGodina createUpdatedEntity(EntityManager em) {
        PoslovnaGodina poslovnaGodina = new PoslovnaGodina().godina(UPDATED_GODINA).zakljucena(UPDATED_ZAKLJUCENA);
        return poslovnaGodina;
    }

    @BeforeEach
    public void initTest() {
        poslovnaGodina = createEntity(em);
    }

    @Test
    @Transactional
    void createPoslovnaGodina() throws Exception {
        int databaseSizeBeforeCreate = poslovnaGodinaRepository.findAll().size();
        // Create the PoslovnaGodina
        restPoslovnaGodinaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isCreated());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeCreate + 1);
        PoslovnaGodina testPoslovnaGodina = poslovnaGodinaList.get(poslovnaGodinaList.size() - 1);
        assertThat(testPoslovnaGodina.getGodina()).isEqualTo(DEFAULT_GODINA);
        assertThat(testPoslovnaGodina.getZakljucena()).isEqualTo(DEFAULT_ZAKLJUCENA);
    }

    @Test
    @Transactional
    void createPoslovnaGodinaWithExistingId() throws Exception {
        // Create the PoslovnaGodina with an existing ID
        poslovnaGodina.setId(1L);

        int databaseSizeBeforeCreate = poslovnaGodinaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPoslovnaGodinaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPoslovnaGodinas() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        // Get all the poslovnaGodinaList
        restPoslovnaGodinaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(poslovnaGodina.getId().intValue())))
            .andExpect(jsonPath("$.[*].godina").value(hasItem(DEFAULT_GODINA)))
            .andExpect(jsonPath("$.[*].zakljucena").value(hasItem(DEFAULT_ZAKLJUCENA.booleanValue())));
    }

    @Test
    @Transactional
    void getPoslovnaGodina() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        // Get the poslovnaGodina
        restPoslovnaGodinaMockMvc
            .perform(get(ENTITY_API_URL_ID, poslovnaGodina.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(poslovnaGodina.getId().intValue()))
            .andExpect(jsonPath("$.godina").value(DEFAULT_GODINA))
            .andExpect(jsonPath("$.zakljucena").value(DEFAULT_ZAKLJUCENA.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingPoslovnaGodina() throws Exception {
        // Get the poslovnaGodina
        restPoslovnaGodinaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewPoslovnaGodina() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();

        // Update the poslovnaGodina
        PoslovnaGodina updatedPoslovnaGodina = poslovnaGodinaRepository.findById(poslovnaGodina.getId()).get();
        // Disconnect from session so that the updates on updatedPoslovnaGodina are not directly saved in db
        em.detach(updatedPoslovnaGodina);
        updatedPoslovnaGodina.godina(UPDATED_GODINA).zakljucena(UPDATED_ZAKLJUCENA);

        restPoslovnaGodinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedPoslovnaGodina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedPoslovnaGodina))
            )
            .andExpect(status().isOk());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
        PoslovnaGodina testPoslovnaGodina = poslovnaGodinaList.get(poslovnaGodinaList.size() - 1);
        assertThat(testPoslovnaGodina.getGodina()).isEqualTo(UPDATED_GODINA);
        assertThat(testPoslovnaGodina.getZakljucena()).isEqualTo(UPDATED_ZAKLJUCENA);
    }

    @Test
    @Transactional
    void putNonExistingPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, poslovnaGodina.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(poslovnaGodina)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePoslovnaGodinaWithPatch() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();

        // Update the poslovnaGodina using partial update
        PoslovnaGodina partialUpdatedPoslovnaGodina = new PoslovnaGodina();
        partialUpdatedPoslovnaGodina.setId(poslovnaGodina.getId());

        partialUpdatedPoslovnaGodina.godina(UPDATED_GODINA).zakljucena(UPDATED_ZAKLJUCENA);

        restPoslovnaGodinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoslovnaGodina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoslovnaGodina))
            )
            .andExpect(status().isOk());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
        PoslovnaGodina testPoslovnaGodina = poslovnaGodinaList.get(poslovnaGodinaList.size() - 1);
        assertThat(testPoslovnaGodina.getGodina()).isEqualTo(UPDATED_GODINA);
        assertThat(testPoslovnaGodina.getZakljucena()).isEqualTo(UPDATED_ZAKLJUCENA);
    }

    @Test
    @Transactional
    void fullUpdatePoslovnaGodinaWithPatch() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();

        // Update the poslovnaGodina using partial update
        PoslovnaGodina partialUpdatedPoslovnaGodina = new PoslovnaGodina();
        partialUpdatedPoslovnaGodina.setId(poslovnaGodina.getId());

        partialUpdatedPoslovnaGodina.godina(UPDATED_GODINA).zakljucena(UPDATED_ZAKLJUCENA);

        restPoslovnaGodinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPoslovnaGodina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPoslovnaGodina))
            )
            .andExpect(status().isOk());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
        PoslovnaGodina testPoslovnaGodina = poslovnaGodinaList.get(poslovnaGodinaList.size() - 1);
        assertThat(testPoslovnaGodina.getGodina()).isEqualTo(UPDATED_GODINA);
        assertThat(testPoslovnaGodina.getZakljucena()).isEqualTo(UPDATED_ZAKLJUCENA);
    }

    @Test
    @Transactional
    void patchNonExistingPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, poslovnaGodina.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isBadRequest());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPoslovnaGodina() throws Exception {
        int databaseSizeBeforeUpdate = poslovnaGodinaRepository.findAll().size();
        poslovnaGodina.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPoslovnaGodinaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(poslovnaGodina))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PoslovnaGodina in the database
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePoslovnaGodina() throws Exception {
        // Initialize the database
        poslovnaGodinaRepository.saveAndFlush(poslovnaGodina);

        int databaseSizeBeforeDelete = poslovnaGodinaRepository.findAll().size();

        // Delete the poslovnaGodina
        restPoslovnaGodinaMockMvc
            .perform(delete(ENTITY_API_URL_ID, poslovnaGodina.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PoslovnaGodina> poslovnaGodinaList = poslovnaGodinaRepository.findAll();
        assertThat(poslovnaGodinaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
