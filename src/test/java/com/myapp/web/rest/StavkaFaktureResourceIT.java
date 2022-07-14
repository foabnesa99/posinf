package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.StavkaFakture;
import com.myapp.repository.StavkaFaktureRepository;
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
 * Integration tests for the {@link StavkaFaktureResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StavkaFaktureResourceIT {

    private static final Double DEFAULT_KOLICINA = 1D;
    private static final Double UPDATED_KOLICINA = 2D;

    private static final Double DEFAULT_JEDINICNA_CENA = 1D;
    private static final Double UPDATED_JEDINICNA_CENA = 2D;

    private static final Integer DEFAULT_RABAT = 1;
    private static final Integer UPDATED_RABAT = 2;

    private static final Integer DEFAULT_PROCENAT_PDV = 1;
    private static final Integer UPDATED_PROCENAT_PDV = 2;

    private static final Double DEFAULT_IZNOS_PDV = 1D;
    private static final Double UPDATED_IZNOS_PDV = 2D;

    private static final Double DEFAULT_UKUPNA_CENA = 1D;
    private static final Double UPDATED_UKUPNA_CENA = 2D;

    private static final String ENTITY_API_URL = "/api/stavka-faktures";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StavkaFaktureRepository stavkaFaktureRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStavkaFaktureMockMvc;

    private StavkaFakture stavkaFakture;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StavkaFakture createEntity(EntityManager em) {
        StavkaFakture stavkaFakture = new StavkaFakture()
            .kolicina(DEFAULT_KOLICINA)
            .jedinicnaCena(DEFAULT_JEDINICNA_CENA)
            .rabat(DEFAULT_RABAT)
            .procenatPDV(DEFAULT_PROCENAT_PDV)
            .iznosPDV(DEFAULT_IZNOS_PDV)
            .ukupnaCena(DEFAULT_UKUPNA_CENA);
        return stavkaFakture;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StavkaFakture createUpdatedEntity(EntityManager em) {
        StavkaFakture stavkaFakture = new StavkaFakture()
            .kolicina(UPDATED_KOLICINA)
            .jedinicnaCena(UPDATED_JEDINICNA_CENA)
            .rabat(UPDATED_RABAT)
            .procenatPDV(UPDATED_PROCENAT_PDV)
            .iznosPDV(UPDATED_IZNOS_PDV)
            .ukupnaCena(UPDATED_UKUPNA_CENA);
        return stavkaFakture;
    }

    @BeforeEach
    public void initTest() {
        stavkaFakture = createEntity(em);
    }

    @Test
    @Transactional
    void createStavkaFakture() throws Exception {
        int databaseSizeBeforeCreate = stavkaFaktureRepository.findAll().size();
        // Create the StavkaFakture
        restStavkaFaktureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stavkaFakture)))
            .andExpect(status().isCreated());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeCreate + 1);
        StavkaFakture testStavkaFakture = stavkaFaktureList.get(stavkaFaktureList.size() - 1);
        assertThat(testStavkaFakture.getKolicina()).isEqualTo(DEFAULT_KOLICINA);
        assertThat(testStavkaFakture.getJedinicnaCena()).isEqualTo(DEFAULT_JEDINICNA_CENA);
        assertThat(testStavkaFakture.getRabat()).isEqualTo(DEFAULT_RABAT);
        assertThat(testStavkaFakture.getProcenatPDV()).isEqualTo(DEFAULT_PROCENAT_PDV);
        assertThat(testStavkaFakture.getIznosPDV()).isEqualTo(DEFAULT_IZNOS_PDV);
        assertThat(testStavkaFakture.getUkupnaCena()).isEqualTo(DEFAULT_UKUPNA_CENA);
    }

    @Test
    @Transactional
    void createStavkaFaktureWithExistingId() throws Exception {
        // Create the StavkaFakture with an existing ID
        stavkaFakture.setId(1L);

        int databaseSizeBeforeCreate = stavkaFaktureRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStavkaFaktureMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stavkaFakture)))
            .andExpect(status().isBadRequest());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllStavkaFaktures() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        // Get all the stavkaFaktureList
        restStavkaFaktureMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(stavkaFakture.getId().intValue())))
            .andExpect(jsonPath("$.[*].kolicina").value(hasItem(DEFAULT_KOLICINA.doubleValue())))
            .andExpect(jsonPath("$.[*].jedinicnaCena").value(hasItem(DEFAULT_JEDINICNA_CENA.doubleValue())))
            .andExpect(jsonPath("$.[*].rabat").value(hasItem(DEFAULT_RABAT)))
            .andExpect(jsonPath("$.[*].procenatPDV").value(hasItem(DEFAULT_PROCENAT_PDV)))
            .andExpect(jsonPath("$.[*].iznosPDV").value(hasItem(DEFAULT_IZNOS_PDV.doubleValue())))
            .andExpect(jsonPath("$.[*].ukupnaCena").value(hasItem(DEFAULT_UKUPNA_CENA.doubleValue())));
    }

    @Test
    @Transactional
    void getStavkaFakture() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        // Get the stavkaFakture
        restStavkaFaktureMockMvc
            .perform(get(ENTITY_API_URL_ID, stavkaFakture.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(stavkaFakture.getId().intValue()))
            .andExpect(jsonPath("$.kolicina").value(DEFAULT_KOLICINA.doubleValue()))
            .andExpect(jsonPath("$.jedinicnaCena").value(DEFAULT_JEDINICNA_CENA.doubleValue()))
            .andExpect(jsonPath("$.rabat").value(DEFAULT_RABAT))
            .andExpect(jsonPath("$.procenatPDV").value(DEFAULT_PROCENAT_PDV))
            .andExpect(jsonPath("$.iznosPDV").value(DEFAULT_IZNOS_PDV.doubleValue()))
            .andExpect(jsonPath("$.ukupnaCena").value(DEFAULT_UKUPNA_CENA.doubleValue()));
    }

    @Test
    @Transactional
    void getNonExistingStavkaFakture() throws Exception {
        // Get the stavkaFakture
        restStavkaFaktureMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStavkaFakture() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();

        // Update the stavkaFakture
        StavkaFakture updatedStavkaFakture = stavkaFaktureRepository.findById(stavkaFakture.getId()).get();
        // Disconnect from session so that the updates on updatedStavkaFakture are not directly saved in db
        em.detach(updatedStavkaFakture);
        updatedStavkaFakture
            .kolicina(UPDATED_KOLICINA)
            .jedinicnaCena(UPDATED_JEDINICNA_CENA)
            .rabat(UPDATED_RABAT)
            .procenatPDV(UPDATED_PROCENAT_PDV)
            .iznosPDV(UPDATED_IZNOS_PDV)
            .ukupnaCena(UPDATED_UKUPNA_CENA);

        restStavkaFaktureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStavkaFakture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStavkaFakture))
            )
            .andExpect(status().isOk());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
        StavkaFakture testStavkaFakture = stavkaFaktureList.get(stavkaFaktureList.size() - 1);
        assertThat(testStavkaFakture.getKolicina()).isEqualTo(UPDATED_KOLICINA);
        assertThat(testStavkaFakture.getJedinicnaCena()).isEqualTo(UPDATED_JEDINICNA_CENA);
        assertThat(testStavkaFakture.getRabat()).isEqualTo(UPDATED_RABAT);
        assertThat(testStavkaFakture.getProcenatPDV()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testStavkaFakture.getIznosPDV()).isEqualTo(UPDATED_IZNOS_PDV);
        assertThat(testStavkaFakture.getUkupnaCena()).isEqualTo(UPDATED_UKUPNA_CENA);
    }

    @Test
    @Transactional
    void putNonExistingStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, stavkaFakture.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stavkaFakture))
            )
            .andExpect(status().isBadRequest());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(stavkaFakture))
            )
            .andExpect(status().isBadRequest());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(stavkaFakture)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStavkaFaktureWithPatch() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();

        // Update the stavkaFakture using partial update
        StavkaFakture partialUpdatedStavkaFakture = new StavkaFakture();
        partialUpdatedStavkaFakture.setId(stavkaFakture.getId());

        partialUpdatedStavkaFakture.kolicina(UPDATED_KOLICINA).procenatPDV(UPDATED_PROCENAT_PDV).iznosPDV(UPDATED_IZNOS_PDV);

        restStavkaFaktureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStavkaFakture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStavkaFakture))
            )
            .andExpect(status().isOk());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
        StavkaFakture testStavkaFakture = stavkaFaktureList.get(stavkaFaktureList.size() - 1);
        assertThat(testStavkaFakture.getKolicina()).isEqualTo(UPDATED_KOLICINA);
        assertThat(testStavkaFakture.getJedinicnaCena()).isEqualTo(DEFAULT_JEDINICNA_CENA);
        assertThat(testStavkaFakture.getRabat()).isEqualTo(DEFAULT_RABAT);
        assertThat(testStavkaFakture.getProcenatPDV()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testStavkaFakture.getIznosPDV()).isEqualTo(UPDATED_IZNOS_PDV);
        assertThat(testStavkaFakture.getUkupnaCena()).isEqualTo(DEFAULT_UKUPNA_CENA);
    }

    @Test
    @Transactional
    void fullUpdateStavkaFaktureWithPatch() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();

        // Update the stavkaFakture using partial update
        StavkaFakture partialUpdatedStavkaFakture = new StavkaFakture();
        partialUpdatedStavkaFakture.setId(stavkaFakture.getId());

        partialUpdatedStavkaFakture
            .kolicina(UPDATED_KOLICINA)
            .jedinicnaCena(UPDATED_JEDINICNA_CENA)
            .rabat(UPDATED_RABAT)
            .procenatPDV(UPDATED_PROCENAT_PDV)
            .iznosPDV(UPDATED_IZNOS_PDV)
            .ukupnaCena(UPDATED_UKUPNA_CENA);

        restStavkaFaktureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStavkaFakture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStavkaFakture))
            )
            .andExpect(status().isOk());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
        StavkaFakture testStavkaFakture = stavkaFaktureList.get(stavkaFaktureList.size() - 1);
        assertThat(testStavkaFakture.getKolicina()).isEqualTo(UPDATED_KOLICINA);
        assertThat(testStavkaFakture.getJedinicnaCena()).isEqualTo(UPDATED_JEDINICNA_CENA);
        assertThat(testStavkaFakture.getRabat()).isEqualTo(UPDATED_RABAT);
        assertThat(testStavkaFakture.getProcenatPDV()).isEqualTo(UPDATED_PROCENAT_PDV);
        assertThat(testStavkaFakture.getIznosPDV()).isEqualTo(UPDATED_IZNOS_PDV);
        assertThat(testStavkaFakture.getUkupnaCena()).isEqualTo(UPDATED_UKUPNA_CENA);
    }

    @Test
    @Transactional
    void patchNonExistingStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, stavkaFakture.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stavkaFakture))
            )
            .andExpect(status().isBadRequest());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(stavkaFakture))
            )
            .andExpect(status().isBadRequest());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStavkaFakture() throws Exception {
        int databaseSizeBeforeUpdate = stavkaFaktureRepository.findAll().size();
        stavkaFakture.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStavkaFaktureMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(stavkaFakture))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StavkaFakture in the database
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStavkaFakture() throws Exception {
        // Initialize the database
        stavkaFaktureRepository.saveAndFlush(stavkaFakture);

        int databaseSizeBeforeDelete = stavkaFaktureRepository.findAll().size();

        // Delete the stavkaFakture
        restStavkaFaktureMockMvc
            .perform(delete(ENTITY_API_URL_ID, stavkaFakture.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StavkaFakture> stavkaFaktureList = stavkaFaktureRepository.findAll();
        assertThat(stavkaFaktureList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
