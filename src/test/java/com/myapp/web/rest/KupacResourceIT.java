package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.Kupac;
import com.myapp.repository.KupacRepository;
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
 * Integration tests for the {@link KupacResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class KupacResourceIT {

    private static final String DEFAULT_PIB = "AAAAAAAAAA";
    private static final String UPDATED_PIB = "BBBBBBBBBB";

    private static final String DEFAULT_MIB = "AAAAAAAAAA";
    private static final String UPDATED_MIB = "BBBBBBBBBB";

    private static final String DEFAULT_NAZIV = "AAAAAAAAAA";
    private static final String UPDATED_NAZIV = "BBBBBBBBBB";

    private static final String DEFAULT_ADRESA = "AAAAAAAAAA";
    private static final String UPDATED_ADRESA = "BBBBBBBBBB";

    private static final String DEFAULT_TELEFON = "AAAAAAAAAA";
    private static final String UPDATED_TELEFON = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/kupacs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private KupacRepository kupacRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restKupacMockMvc;

    private Kupac kupac;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kupac createEntity(EntityManager em) {
        Kupac kupac = new Kupac().pib(DEFAULT_PIB).mib(DEFAULT_MIB).naziv(DEFAULT_NAZIV).adresa(DEFAULT_ADRESA).telefon(DEFAULT_TELEFON);
        return kupac;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Kupac createUpdatedEntity(EntityManager em) {
        Kupac kupac = new Kupac().pib(UPDATED_PIB).mib(UPDATED_MIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA).telefon(UPDATED_TELEFON);
        return kupac;
    }

    @BeforeEach
    public void initTest() {
        kupac = createEntity(em);
    }

    @Test
    @Transactional
    void createKupac() throws Exception {
        int databaseSizeBeforeCreate = kupacRepository.findAll().size();
        // Create the Kupac
        restKupacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kupac)))
            .andExpect(status().isCreated());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeCreate + 1);
        Kupac testKupac = kupacList.get(kupacList.size() - 1);
        assertThat(testKupac.getPib()).isEqualTo(DEFAULT_PIB);
        assertThat(testKupac.getMib()).isEqualTo(DEFAULT_MIB);
        assertThat(testKupac.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testKupac.getAdresa()).isEqualTo(DEFAULT_ADRESA);
        assertThat(testKupac.getTelefon()).isEqualTo(DEFAULT_TELEFON);
    }

    @Test
    @Transactional
    void createKupacWithExistingId() throws Exception {
        // Create the Kupac with an existing ID
        kupac.setId(1L);

        int databaseSizeBeforeCreate = kupacRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restKupacMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kupac)))
            .andExpect(status().isBadRequest());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllKupacs() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        // Get all the kupacList
        restKupacMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(kupac.getId().intValue())))
            .andExpect(jsonPath("$.[*].pib").value(hasItem(DEFAULT_PIB)))
            .andExpect(jsonPath("$.[*].mib").value(hasItem(DEFAULT_MIB)))
            .andExpect(jsonPath("$.[*].naziv").value(hasItem(DEFAULT_NAZIV)))
            .andExpect(jsonPath("$.[*].adresa").value(hasItem(DEFAULT_ADRESA)))
            .andExpect(jsonPath("$.[*].telefon").value(hasItem(DEFAULT_TELEFON)));
    }

    @Test
    @Transactional
    void getKupac() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        // Get the kupac
        restKupacMockMvc
            .perform(get(ENTITY_API_URL_ID, kupac.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(kupac.getId().intValue()))
            .andExpect(jsonPath("$.pib").value(DEFAULT_PIB))
            .andExpect(jsonPath("$.mib").value(DEFAULT_MIB))
            .andExpect(jsonPath("$.naziv").value(DEFAULT_NAZIV))
            .andExpect(jsonPath("$.adresa").value(DEFAULT_ADRESA))
            .andExpect(jsonPath("$.telefon").value(DEFAULT_TELEFON));
    }

    @Test
    @Transactional
    void getNonExistingKupac() throws Exception {
        // Get the kupac
        restKupacMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewKupac() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();

        // Update the kupac
        Kupac updatedKupac = kupacRepository.findById(kupac.getId()).get();
        // Disconnect from session so that the updates on updatedKupac are not directly saved in db
        em.detach(updatedKupac);
        updatedKupac.pib(UPDATED_PIB).mib(UPDATED_MIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA).telefon(UPDATED_TELEFON);

        restKupacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedKupac.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedKupac))
            )
            .andExpect(status().isOk());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
        Kupac testKupac = kupacList.get(kupacList.size() - 1);
        assertThat(testKupac.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testKupac.getMib()).isEqualTo(UPDATED_MIB);
        assertThat(testKupac.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testKupac.getAdresa()).isEqualTo(UPDATED_ADRESA);
        assertThat(testKupac.getTelefon()).isEqualTo(UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void putNonExistingKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, kupac.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kupac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(kupac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(kupac)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateKupacWithPatch() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();

        // Update the kupac using partial update
        Kupac partialUpdatedKupac = new Kupac();
        partialUpdatedKupac.setId(kupac.getId());

        partialUpdatedKupac.pib(UPDATED_PIB).mib(UPDATED_MIB);

        restKupacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKupac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKupac))
            )
            .andExpect(status().isOk());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
        Kupac testKupac = kupacList.get(kupacList.size() - 1);
        assertThat(testKupac.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testKupac.getMib()).isEqualTo(UPDATED_MIB);
        assertThat(testKupac.getNaziv()).isEqualTo(DEFAULT_NAZIV);
        assertThat(testKupac.getAdresa()).isEqualTo(DEFAULT_ADRESA);
        assertThat(testKupac.getTelefon()).isEqualTo(DEFAULT_TELEFON);
    }

    @Test
    @Transactional
    void fullUpdateKupacWithPatch() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();

        // Update the kupac using partial update
        Kupac partialUpdatedKupac = new Kupac();
        partialUpdatedKupac.setId(kupac.getId());

        partialUpdatedKupac.pib(UPDATED_PIB).mib(UPDATED_MIB).naziv(UPDATED_NAZIV).adresa(UPDATED_ADRESA).telefon(UPDATED_TELEFON);

        restKupacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedKupac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedKupac))
            )
            .andExpect(status().isOk());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
        Kupac testKupac = kupacList.get(kupacList.size() - 1);
        assertThat(testKupac.getPib()).isEqualTo(UPDATED_PIB);
        assertThat(testKupac.getMib()).isEqualTo(UPDATED_MIB);
        assertThat(testKupac.getNaziv()).isEqualTo(UPDATED_NAZIV);
        assertThat(testKupac.getAdresa()).isEqualTo(UPDATED_ADRESA);
        assertThat(testKupac.getTelefon()).isEqualTo(UPDATED_TELEFON);
    }

    @Test
    @Transactional
    void patchNonExistingKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, kupac.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kupac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(kupac))
            )
            .andExpect(status().isBadRequest());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamKupac() throws Exception {
        int databaseSizeBeforeUpdate = kupacRepository.findAll().size();
        kupac.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restKupacMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(kupac)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Kupac in the database
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteKupac() throws Exception {
        // Initialize the database
        kupacRepository.saveAndFlush(kupac);

        int databaseSizeBeforeDelete = kupacRepository.findAll().size();

        // Delete the kupac
        restKupacMockMvc
            .perform(delete(ENTITY_API_URL_ID, kupac.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Kupac> kupacList = kupacRepository.findAll();
        assertThat(kupacList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
