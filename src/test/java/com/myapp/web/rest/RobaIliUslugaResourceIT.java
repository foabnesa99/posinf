package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.RobaIliUsluga;
import com.myapp.repository.RobaIliUslugaRepository;
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
import org.springframework.util.Base64Utils;

/**
 * Integration tests for the {@link RobaIliUslugaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class RobaIliUslugaResourceIT {

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final String DEFAULT_OPIS = "AAAAAAAAAA";
    private static final String UPDATED_OPIS = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/roba-ili-uslugas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private RobaIliUslugaRepository robaIliUslugaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restRobaIliUslugaMockMvc;

    private RobaIliUsluga robaIliUsluga;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RobaIliUsluga createEntity(EntityManager em) {
        RobaIliUsluga robaIliUsluga = new RobaIliUsluga().naziv(DEFAULT_NAZIV).opis(DEFAULT_OPIS);
        return robaIliUsluga;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static RobaIliUsluga createUpdatedEntity(EntityManager em) {
        RobaIliUsluga robaIliUsluga = new RobaIliUsluga().naziv(UPDATED_NAZIV).opis(UPDATED_OPIS);
        return robaIliUsluga;
    }

    @BeforeEach
    public void initTest() {
        robaIliUsluga = createEntity(em);
    }

    @Test
    @Transactional
    void createRobaIliUsluga() throws Exception {
        int databaseSizeBeforeCreate = robaIliUslugaRepository.findAll().size();
        // Create the RobaIliUsluga
        restRobaIliUslugaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(robaIliUsluga)))
            .andExpect(status().isCreated());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeCreate + 1);
        RobaIliUsluga testRobaIliUsluga = robaIliUslugaList.get(robaIliUslugaList.size() - 1);
        assertThat(testRobaIliUsluga.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testRobaIliUsluga.getOpis()).isEqualTo(DEFAULT_OPIS);
    }

    @Test
    @Transactional
    void createRobaIliUslugaWithExistingId() throws Exception {
        // Create the RobaIliUsluga with an existing ID
        robaIliUsluga.setId(1L);

        int databaseSizeBeforeCreate = robaIliUslugaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restRobaIliUslugaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(robaIliUsluga)))
            .andExpect(status().isBadRequest());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllRobaIliUslugas() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        // Get all the robaIliUslugaList
        restRobaIliUslugaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(robaIliUsluga.getId().intValue())))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)))
            .andExpect(jsonPath("$.[*].opis").value(hasItem(DEFAULT_OPIS.toString())));
    }

    @Test
    @Transactional
    void getRobaIliUsluga() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        // Get the robaIliUsluga
        restRobaIliUslugaMockMvc
            .perform(get(ENTITY_API_URL_ID, robaIliUsluga.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(robaIliUsluga.getId().intValue()))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV))
            .andExpect(jsonPath("$.opis").value(DEFAULT_OPIS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingRobaIliUsluga() throws Exception {
        // Get the robaIliUsluga
        restRobaIliUslugaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewRobaIliUsluga() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();

        // Update the robaIliUsluga
        RobaIliUsluga updatedRobaIliUsluga = robaIliUslugaRepository.findById(robaIliUsluga.getId()).get();
        // Disconnect from session so that the updates on updatedRobaIliUsluga are not directly saved in db
        em.detach(updatedRobaIliUsluga);
        updatedRobaIliUsluga.naziv(UPDATED_NAZIV).opis(UPDATED_OPIS);

        restRobaIliUslugaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedRobaIliUsluga.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedRobaIliUsluga))
            )
            .andExpect(status().isOk());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
        RobaIliUsluga testRobaIliUsluga = robaIliUslugaList.get(robaIliUslugaList.size() - 1);
        assertThat(testRobaIliUsluga.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testRobaIliUsluga.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void putNonExistingRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, robaIliUsluga.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robaIliUsluga))
            )
            .andExpect(status().isBadRequest());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(robaIliUsluga))
            )
            .andExpect(status().isBadRequest());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(robaIliUsluga)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateRobaIliUslugaWithPatch() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();

        // Update the robaIliUsluga using partial update
        RobaIliUsluga partialUpdatedRobaIliUsluga = new RobaIliUsluga();
        partialUpdatedRobaIliUsluga.setId(robaIliUsluga.getId());

        partialUpdatedRobaIliUsluga.naziv(UPDATED_NAZIV).opis(UPDATED_OPIS);

        restRobaIliUslugaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRobaIliUsluga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRobaIliUsluga))
            )
            .andExpect(status().isOk());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
        RobaIliUsluga testRobaIliUsluga = robaIliUslugaList.get(robaIliUslugaList.size() - 1);
        assertThat(testRobaIliUsluga.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testRobaIliUsluga.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void fullUpdateRobaIliUslugaWithPatch() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();

        // Update the robaIliUsluga using partial update
        RobaIliUsluga partialUpdatedRobaIliUsluga = new RobaIliUsluga();
        partialUpdatedRobaIliUsluga.setId(robaIliUsluga.getId());

        partialUpdatedRobaIliUsluga.naziv(UPDATED_NAZIV).opis(UPDATED_OPIS);

        restRobaIliUslugaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedRobaIliUsluga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedRobaIliUsluga))
            )
            .andExpect(status().isOk());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
        RobaIliUsluga testRobaIliUsluga = robaIliUslugaList.get(robaIliUslugaList.size() - 1);
        assertThat(testRobaIliUsluga.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testRobaIliUsluga.getOpis()).isEqualTo(UPDATED_OPIS);
    }

    @Test
    @Transactional
    void patchNonExistingRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, robaIliUsluga.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(robaIliUsluga))
            )
            .andExpect(status().isBadRequest());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(robaIliUsluga))
            )
            .andExpect(status().isBadRequest());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamRobaIliUsluga() throws Exception {
        int databaseSizeBeforeUpdate = robaIliUslugaRepository.findAll().size();
        robaIliUsluga.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restRobaIliUslugaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(robaIliUsluga))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the RobaIliUsluga in the database
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteRobaIliUsluga() throws Exception {
        // Initialize the database
        robaIliUslugaRepository.saveAndFlush(robaIliUsluga);

        int databaseSizeBeforeDelete = robaIliUslugaRepository.findAll().size();

        // Delete the robaIliUsluga
        restRobaIliUslugaMockMvc
            .perform(delete(ENTITY_API_URL_ID, robaIliUsluga.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<RobaIliUsluga> robaIliUslugaList = robaIliUslugaRepository.findAll();
        assertThat(robaIliUslugaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
