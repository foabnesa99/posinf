package com.myapp.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.myapp.IntegrationTest;
import com.myapp.domain.IzlaznaFaktura;
import com.myapp.domain.enumeration.StatusFakture;
import com.myapp.repository.IzlaznaFakturaRepository;
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
 * Integration tests for the {@link IzlaznaFakturaResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class IzlaznaFakturaResourceIT {

    private static final String DEFAULT_BROJ_FAKTURE = "AAAAAAAAAA";
    private static final String UPDATED_BROJ_FAKTURE = "BBBBBBBBBB";

    private static final LocalDate DEFAULT_DATUM_IZDAVANJA = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_IZDAVANJA = LocalDate.now(ZoneId.systemDefault());

    private static final LocalDate DEFAULT_DATUM_VALUTE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_DATUM_VALUTE = LocalDate.now(ZoneId.systemDefault());

    private static final Double DEFAULT_UKUPNA_OSNOVICA = 1D;
    private static final Double UPDATED_UKUPNA_OSNOVICA = 2D;

    private static final Double DEFAULT_UKUPAN_RABAT = 1.0;
    private static final Double UPDATED_UKUPAN_RABAT = 2.0;

    private static final Double DEFAULT_UKUPAN_IZNOS = 1D;
    private static final Double UPDATED_UKUPAN_IZNOS = 2D;

    private static final StatusFakture DEFAULT_STATUS = StatusFakture.FORMIRANJE;
    private static final StatusFakture UPDATED_STATUS = StatusFakture.POSLATA;

    private static final String ENTITY_API_URL = "/api/izlazna-fakturas";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private IzlaznaFakturaRepository izlaznaFakturaRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restIzlaznaFakturaMockMvc;

    private IzlaznaFaktura izlaznaFaktura;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IzlaznaFaktura createEntity(EntityManager em) {
        IzlaznaFaktura izlaznaFaktura = new IzlaznaFaktura()
            .brojFakture(DEFAULT_BROJ_FAKTURE)
            .datumIzdavanja(DEFAULT_DATUM_IZDAVANJA)
            .datumValute(DEFAULT_DATUM_VALUTE)
            .ukupnaOsnovica(DEFAULT_UKUPNA_OSNOVICA)
            .ukupanRabat(DEFAULT_UKUPAN_RABAT)
            .ukupanIznos(DEFAULT_UKUPAN_IZNOS)
            .status(DEFAULT_STATUS);
        return izlaznaFaktura;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static IzlaznaFaktura createUpdatedEntity(EntityManager em) {
        IzlaznaFaktura izlaznaFaktura = new IzlaznaFaktura()
            .brojFakture(UPDATED_BROJ_FAKTURE)
            .datumIzdavanja(UPDATED_DATUM_IZDAVANJA)
            .datumValute(UPDATED_DATUM_VALUTE)
            .ukupnaOsnovica(UPDATED_UKUPNA_OSNOVICA)
            .ukupanRabat(UPDATED_UKUPAN_RABAT)
            .ukupanIznos(UPDATED_UKUPAN_IZNOS)
            .status(UPDATED_STATUS);
        return izlaznaFaktura;
    }

    @BeforeEach
    public void initTest() {
        izlaznaFaktura = createEntity(em);
    }

    @Test
    @Transactional
    void createIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeCreate = izlaznaFakturaRepository.findAll().size();
        // Create the IzlaznaFaktura
        restIzlaznaFakturaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isCreated());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeCreate + 1);
        IzlaznaFaktura testIzlaznaFaktura = izlaznaFakturaList.get(izlaznaFakturaList.size() - 1);
        assertThat(testIzlaznaFaktura.getBrojFakture()).isEqualTo(DEFAULT_BROJ_FAKTURE);
        assertThat(testIzlaznaFaktura.getDatumIzdavanja()).isEqualTo(DEFAULT_DATUM_IZDAVANJA);
        assertThat(testIzlaznaFaktura.getDatumValute()).isEqualTo(DEFAULT_DATUM_VALUTE);
        assertThat(testIzlaznaFaktura.getUkupnaOsnovica()).isEqualTo(DEFAULT_UKUPNA_OSNOVICA);
        assertThat(testIzlaznaFaktura.getUkupanPdv()).isEqualTo(DEFAULT_UKUPAN_RABAT);
        assertThat(testIzlaznaFaktura.getUkupanIznos()).isEqualTo(DEFAULT_UKUPAN_IZNOS);
        assertThat(testIzlaznaFaktura.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void createIzlaznaFakturaWithExistingId() throws Exception {
        // Create the IzlaznaFaktura with an existing ID
        izlaznaFaktura.setId(1L);

        int databaseSizeBeforeCreate = izlaznaFakturaRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restIzlaznaFakturaMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isBadRequest());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllIzlaznaFakturas() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        // Get all the izlaznaFakturaList
        restIzlaznaFakturaMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(izlaznaFaktura.getId().intValue())))
            .andExpect(jsonPath("$.[*].brojFakture").value(hasItem(DEFAULT_BROJ_FAKTURE)))
            .andExpect(jsonPath("$.[*].datumIzdavanja").value(hasItem(DEFAULT_DATUM_IZDAVANJA.toString())))
            .andExpect(jsonPath("$.[*].datumValute").value(hasItem(DEFAULT_DATUM_VALUTE.toString())))
            .andExpect(jsonPath("$.[*].ukupnaOsnovica").value(hasItem(DEFAULT_UKUPNA_OSNOVICA.doubleValue())))
            .andExpect(jsonPath("$.[*].ukupanRabat").value(hasItem(DEFAULT_UKUPAN_RABAT)))
            .andExpect(jsonPath("$.[*].ukupanIznos").value(hasItem(DEFAULT_UKUPAN_IZNOS.doubleValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())));
    }

    @Test
    @Transactional
    void getIzlaznaFaktura() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        // Get the izlaznaFaktura
        restIzlaznaFakturaMockMvc
            .perform(get(ENTITY_API_URL_ID, izlaznaFaktura.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(izlaznaFaktura.getId().intValue()))
            .andExpect(jsonPath("$.brojFakture").value(DEFAULT_BROJ_FAKTURE))
            .andExpect(jsonPath("$.datumIzdavanja").value(DEFAULT_DATUM_IZDAVANJA.toString()))
            .andExpect(jsonPath("$.datumValute").value(DEFAULT_DATUM_VALUTE.toString()))
            .andExpect(jsonPath("$.ukupnaOsnovica").value(DEFAULT_UKUPNA_OSNOVICA.doubleValue()))
            .andExpect(jsonPath("$.ukupanRabat").value(DEFAULT_UKUPAN_RABAT))
            .andExpect(jsonPath("$.ukupanIznos").value(DEFAULT_UKUPAN_IZNOS.doubleValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()));
    }

    @Test
    @Transactional
    void getNonExistingIzlaznaFaktura() throws Exception {
        // Get the izlaznaFaktura
        restIzlaznaFakturaMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewIzlaznaFaktura() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();

        // Update the izlaznaFaktura
        IzlaznaFaktura updatedIzlaznaFaktura = izlaznaFakturaRepository.findById(izlaznaFaktura.getId()).get();
        // Disconnect from session so that the updates on updatedIzlaznaFaktura are not directly saved in db
        em.detach(updatedIzlaznaFaktura);
        updatedIzlaznaFaktura
            .brojFakture(UPDATED_BROJ_FAKTURE)
            .datumIzdavanja(UPDATED_DATUM_IZDAVANJA)
            .datumValute(UPDATED_DATUM_VALUTE)
            .ukupnaOsnovica(UPDATED_UKUPNA_OSNOVICA)
            .ukupanRabat(UPDATED_UKUPAN_RABAT)
            .ukupanIznos(UPDATED_UKUPAN_IZNOS)
            .status(UPDATED_STATUS);

        restIzlaznaFakturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedIzlaznaFaktura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedIzlaznaFaktura))
            )
            .andExpect(status().isOk());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
        IzlaznaFaktura testIzlaznaFaktura = izlaznaFakturaList.get(izlaznaFakturaList.size() - 1);
        assertThat(testIzlaznaFaktura.getBrojFakture()).isEqualTo(UPDATED_BROJ_FAKTURE);
        assertThat(testIzlaznaFaktura.getDatumIzdavanja()).isEqualTo(UPDATED_DATUM_IZDAVANJA);
        assertThat(testIzlaznaFaktura.getDatumValute()).isEqualTo(UPDATED_DATUM_VALUTE);
        assertThat(testIzlaznaFaktura.getUkupnaOsnovica()).isEqualTo(UPDATED_UKUPNA_OSNOVICA);
        assertThat(testIzlaznaFaktura.getUkupanPdv()).isEqualTo(UPDATED_UKUPAN_RABAT);
        assertThat(testIzlaznaFaktura.getUkupanIznos()).isEqualTo(UPDATED_UKUPAN_IZNOS);
        assertThat(testIzlaznaFaktura.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void putNonExistingIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, izlaznaFaktura.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isBadRequest());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isBadRequest());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateIzlaznaFakturaWithPatch() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();

        // Update the izlaznaFaktura using partial update
        IzlaznaFaktura partialUpdatedIzlaznaFaktura = new IzlaznaFaktura();
        partialUpdatedIzlaznaFaktura.setId(izlaznaFaktura.getId());

        partialUpdatedIzlaznaFaktura.ukupanRabat(UPDATED_UKUPAN_RABAT).ukupanIznos(UPDATED_UKUPAN_IZNOS);

        restIzlaznaFakturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIzlaznaFaktura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIzlaznaFaktura))
            )
            .andExpect(status().isOk());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
        IzlaznaFaktura testIzlaznaFaktura = izlaznaFakturaList.get(izlaznaFakturaList.size() - 1);
        assertThat(testIzlaznaFaktura.getBrojFakture()).isEqualTo(DEFAULT_BROJ_FAKTURE);
        assertThat(testIzlaznaFaktura.getDatumIzdavanja()).isEqualTo(DEFAULT_DATUM_IZDAVANJA);
        assertThat(testIzlaznaFaktura.getDatumValute()).isEqualTo(DEFAULT_DATUM_VALUTE);
        assertThat(testIzlaznaFaktura.getUkupnaOsnovica()).isEqualTo(DEFAULT_UKUPNA_OSNOVICA);
        assertThat(testIzlaznaFaktura.getUkupanPdv()).isEqualTo(UPDATED_UKUPAN_RABAT);
        assertThat(testIzlaznaFaktura.getUkupanIznos()).isEqualTo(UPDATED_UKUPAN_IZNOS);
        assertThat(testIzlaznaFaktura.getStatus()).isEqualTo(DEFAULT_STATUS);
    }

    @Test
    @Transactional
    void fullUpdateIzlaznaFakturaWithPatch() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();

        // Update the izlaznaFaktura using partial update
        IzlaznaFaktura partialUpdatedIzlaznaFaktura = new IzlaznaFaktura();
        partialUpdatedIzlaznaFaktura.setId(izlaznaFaktura.getId());

        partialUpdatedIzlaznaFaktura
            .brojFakture(UPDATED_BROJ_FAKTURE)
            .datumIzdavanja(UPDATED_DATUM_IZDAVANJA)
            .datumValute(UPDATED_DATUM_VALUTE)
            .ukupnaOsnovica(UPDATED_UKUPNA_OSNOVICA)
            .ukupanRabat(UPDATED_UKUPAN_RABAT)
            .ukupanIznos(UPDATED_UKUPAN_IZNOS)
            .status(UPDATED_STATUS);

        restIzlaznaFakturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedIzlaznaFaktura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedIzlaznaFaktura))
            )
            .andExpect(status().isOk());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
        IzlaznaFaktura testIzlaznaFaktura = izlaznaFakturaList.get(izlaznaFakturaList.size() - 1);
        assertThat(testIzlaznaFaktura.getBrojFakture()).isEqualTo(UPDATED_BROJ_FAKTURE);
        assertThat(testIzlaznaFaktura.getDatumIzdavanja()).isEqualTo(UPDATED_DATUM_IZDAVANJA);
        assertThat(testIzlaznaFaktura.getDatumValute()).isEqualTo(UPDATED_DATUM_VALUTE);
        assertThat(testIzlaznaFaktura.getUkupnaOsnovica()).isEqualTo(UPDATED_UKUPNA_OSNOVICA);
        assertThat(testIzlaznaFaktura.getUkupanPdv()).isEqualTo(UPDATED_UKUPAN_RABAT);
        assertThat(testIzlaznaFaktura.getUkupanIznos()).isEqualTo(UPDATED_UKUPAN_IZNOS);
        assertThat(testIzlaznaFaktura.getStatus()).isEqualTo(UPDATED_STATUS);
    }

    @Test
    @Transactional
    void patchNonExistingIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, izlaznaFaktura.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isBadRequest());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isBadRequest());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamIzlaznaFaktura() throws Exception {
        int databaseSizeBeforeUpdate = izlaznaFakturaRepository.findAll().size();
        izlaznaFaktura.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restIzlaznaFakturaMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(izlaznaFaktura))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the IzlaznaFaktura in the database
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteIzlaznaFaktura() throws Exception {
        // Initialize the database
        izlaznaFakturaRepository.saveAndFlush(izlaznaFaktura);

        int databaseSizeBeforeDelete = izlaznaFakturaRepository.findAll().size();

        // Delete the izlaznaFaktura
        restIzlaznaFakturaMockMvc
            .perform(delete(ENTITY_API_URL_ID, izlaznaFaktura.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<IzlaznaFaktura> izlaznaFakturaList = izlaznaFakturaRepository.findAll();
        assertThat(izlaznaFakturaList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
