package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Cena;
import com.myapp.repository.CenaRepository;
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
 * Integration tests for the {@link CenaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CenaResourceIT {

    private static final LocalDate DEFAULT_DATUM_VAZENJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_VAZENJA = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_IZNOS_CENE = 1D;
    private static final Double UPDATED_IZNOS_CENE = 2D;

    private static final String ENTITY_API_URL = "/api/cenas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CenaRepository cenaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCenaMockMvc;

    private Cena cena;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cena createEntity(EntityManager em) {
        Cena cena = new Cena().datumVazenja(DEFAULT_DATUM_VAZENJA).iznosCene(DEFAULT_IZNOS_CENE);
        return cena;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Cena createUpdatedEntity(EntityManager em) {
        Cena cena = new Cena().datumVazenja(UPDATED_DATUM_VAZENJA).iznosCene(UPDATED_IZNOS_CENE);
        return cena;
    }

    @BeforeEach
    public void initTest() {
        cena = createEntity(em);
    }

    @Test
    @Transactional
    void createCena() throws Exception {
        int databaseSizeBeforeCreate = cenaRepository.findAll().size();
        // Create the Cena
        restCenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cena)))
            .andExpect(status().isCreated());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeCreate + 1);
        Cena testCena = cenaList.get(cenaList.size() - 1);
        assertThat(testCena.getDatumVazenja()).isEqualTo(DEFAULT_DATUM_VAZENJA);
        assertThat(testCena.getIznosCene()).isEqualTo(DEFAULT_IZNOS_CENE);
    }

    @Test
    @Transactional
    void createCenaWithExistingId() throws Exception {
        // Create the Cena with an existing ID
        cena.setId(1L);

        int databaseSizeBeforeCreate = cenaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCenaMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cena)))
            .andExpect(status().isBadRequest());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllCenas() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        // Get all the cenaList
        restCenaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(cena.getId().intValue())))
            .andExpect(jsonPath("$.[*].datumVazenja").value(hasItem(DEFAULT_DATUM_VAZENJA.toString())))
            .andExpect(jsonPath("$.[*].iznosCene").value(hasItem(DEFAULT_IZNOS_CENE.doubleValue())));
    }

    @Test
    @Transactional
    void getCena() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        // Get the cena
        restCenaMockMvc
            .perform(get(ENTITY_API_URL_ID, cena.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(cena.getId().intValue()))
            .andExpect(jsonPath("$.datumVazenja").value(DEFAULT_DATUM_VAZENJA.toString()))
            .andExpect(jsonPath("$.iznosCene").value(DEFAULT_IZNOS_CENE.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingCena() throws Exception {
        // Get the cena
        restCenaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCena() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();

        // Update the cena
        Cena updatedCena = cenaRepository.findById(cena.getId()).get();
        // Disconnect from session so that the updates on updatedCena are not directly saved in db
        em.detach(updatedCena);
        updatedCena.datumVazenja(UPDATED_DATUM_VAZENJA).iznosCene(UPDATED_IZNOS_CENE);

        restCenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCena.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCena))
            )
            .andExpect(status().isOk());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
        Cena testCena = cenaList.get(cenaList.size() - 1);
        assertThat(testCena.getDatumVazenja()).isEqualTo(UPDATED_DATUM_VAZENJA);
        assertThat(testCena.getIznosCene()).isEqualTo(UPDATED_IZNOS_CENE);
    }

    @Test
    @Transactional
    void putNonExistingCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, cena.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cena))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(cena))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(cena)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCenaWithPatch() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();

        // Update the cena using partial update
        Cena partialUpdatedCena = new Cena();
        partialUpdatedCena.setId(cena.getId());

        restCenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCena.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCena))
            )
            .andExpect(status().isOk());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
        Cena testCena = cenaList.get(cenaList.size() - 1);
        assertThat(testCena.getDatumVazenja()).isEqualTo(DEFAULT_DATUM_VAZENJA);
        assertThat(testCena.getIznosCene()).isEqualTo(DEFAULT_IZNOS_CENE);
    }

    @Test
    @Transactional
    void fullUpdateCenaWithPatch() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();

        // Update the cena using partial update
        Cena partialUpdatedCena = new Cena();
        partialUpdatedCena.setId(cena.getId());

        partialUpdatedCena.datumVazenja(UPDATED_DATUM_VAZENJA).iznosCene(UPDATED_IZNOS_CENE);

        restCenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCena.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCena))
            )
            .andExpect(status().isOk());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
        Cena testCena = cenaList.get(cenaList.size() - 1);
        assertThat(testCena.getDatumVazenja()).isEqualTo(UPDATED_DATUM_VAZENJA);
        assertThat(testCena.getIznosCene()).isEqualTo(UPDATED_IZNOS_CENE);
    }

    @Test
    @Transactional
    void patchNonExistingCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, cena.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cena))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(cena))
            )
            .andExpect(status().isBadRequest());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCena() throws Exception {
        int databaseSizeBeforeUpdate = cenaRepository.findAll().size();
        cena.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCenaMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(cena)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Cena in the database
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCena() throws Exception {
        // Initialize the database
        cenaRepository.saveAndFlush(cena);

        int databaseSizeBeforeDelete = cenaRepository.findAll().size();

        // Delete the cena
        restCenaMockMvc
            .perform(delete(ENTITY_API_URL_ID, cena.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Cena> cenaList = cenaRepository.findAll();
        assertThat(cenaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
