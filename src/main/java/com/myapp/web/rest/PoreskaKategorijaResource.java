package com.myapp.web.rest;

import com.myapp.domain.PoreskaKategorija;
import com.myapp.repository.PoreskaKategorijaRepository;
import com.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.myapp.domain.PoreskaKategorija}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PoreskaKategorijaResource {

    private final Logger log = LoggerFactory.getLogger(PoreskaKategorijaResource.class);

    private static final String ENTITY_NAME = "poreskaKategorija";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoreskaKategorijaRepository poreskaKategorijaRepository;

    public PoreskaKategorijaResource(PoreskaKategorijaRepository poreskaKategorijaRepository) {
        this.poreskaKategorijaRepository = poreskaKategorijaRepository;
    }

    /**
     * {@code POST  /poreska-kategorijas} : Create a new poreskaKategorija.
     *
     * @param poreskaKategorija the poreskaKategorija to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poreskaKategorija, or with status {@code 400 (Bad Request)} if the poreskaKategorija has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/poreska-kategorijas")
    public ResponseEntity<PoreskaKategorija> createPoreskaKategorija(@RequestBody PoreskaKategorija poreskaKategorija)
        throws URISyntaxException {
        log.debug("REST request to save PoreskaKategorija : {}", poreskaKategorija);
        if (poreskaKategorija.getId() != null) {
            throw new BadRequestAlertException("A new poreskaKategorija cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PoreskaKategorija result = poreskaKategorijaRepository.save(poreskaKategorija);
        return ResponseEntity
            .created(new URI("/api/poreska-kategorijas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poreska-kategorijas/:id} : Updates an existing poreskaKategorija.
     *
     * @param id the id of the poreskaKategorija to save.
     * @param poreskaKategorija the poreskaKategorija to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poreskaKategorija,
     * or with status {@code 400 (Bad Request)} if the poreskaKategorija is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poreskaKategorija couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/poreska-kategorijas/{id}")
    public ResponseEntity<PoreskaKategorija> updatePoreskaKategorija(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoreskaKategorija poreskaKategorija
    ) throws URISyntaxException {
        log.debug("REST request to update PoreskaKategorija : {}, {}", id, poreskaKategorija);
        if (poreskaKategorija.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poreskaKategorija.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poreskaKategorijaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoreskaKategorija result = poreskaKategorijaRepository.save(poreskaKategorija);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poreskaKategorija.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poreska-kategorijas/:id} : Partial updates given fields of an existing poreskaKategorija, field will ignore if it is null
     *
     * @param id the id of the poreskaKategorija to save.
     * @param poreskaKategorija the poreskaKategorija to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poreskaKategorija,
     * or with status {@code 400 (Bad Request)} if the poreskaKategorija is not valid,
     * or with status {@code 404 (Not Found)} if the poreskaKategorija is not found,
     * or with status {@code 500 (Internal Server Error)} if the poreskaKategorija couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/poreska-kategorijas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoreskaKategorija> partialUpdatePoreskaKategorija(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoreskaKategorija poreskaKategorija
    ) throws URISyntaxException {
        log.debug("REST request to partial update PoreskaKategorija partially : {}, {}", id, poreskaKategorija);
        if (poreskaKategorija.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poreskaKategorija.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poreskaKategorijaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoreskaKategorija> result = poreskaKategorijaRepository
            .findById(poreskaKategorija.getId())
            .map(existingPoreskaKategorija -> {
                if (poreskaKategorija.getNaziv() != null) {
                    existingPoreskaKategorija.setNaziv(poreskaKategorija.getNaziv());
                }

                return existingPoreskaKategorija;
            })
            .map(poreskaKategorijaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poreskaKategorija.getId().toString())
        );
    }

    /**
     * {@code GET  /poreska-kategorijas} : get all the poreskaKategorijas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poreskaKategorijas in body.
     */
    @GetMapping("/poreska-kategorijas")
    public List<PoreskaKategorija> getAllPoreskaKategorijas() {
        log.debug("REST request to get all PoreskaKategorijas");
        return poreskaKategorijaRepository.findAll();
    }

    /**
     * {@code GET  /poreska-kategorijas/:id} : get the "id" poreskaKategorija.
     *
     * @param id the id of the poreskaKategorija to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poreskaKategorija, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/poreska-kategorijas/{id}")
    public ResponseEntity<PoreskaKategorija> getPoreskaKategorija(@PathVariable Long id) {
        log.debug("REST request to get PoreskaKategorija : {}", id);
        Optional<PoreskaKategorija> poreskaKategorija = poreskaKategorijaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poreskaKategorija);
    }

    /**
     * {@code DELETE  /poreska-kategorijas/:id} : delete the "id" poreskaKategorija.
     *
     * @param id the id of the poreskaKategorija to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/poreska-kategorijas/{id}")
    public ResponseEntity<Void> deletePoreskaKategorija(@PathVariable Long id) {
        log.debug("REST request to delete PoreskaKategorija : {}", id);
        poreskaKategorijaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
