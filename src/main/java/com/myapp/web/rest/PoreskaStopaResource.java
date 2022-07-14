package com.myapp.web.rest;

import com.myapp.domain.PoreskaStopa;
import com.myapp.repository.PoreskaStopaRepository;
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
 * REST controller for managing {@link com.myapp.domain.PoreskaStopa}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PoreskaStopaResource {

    private final Logger log = LoggerFactory.getLogger(PoreskaStopaResource.class);

    private static final String ENTITY_NAME = "poreskaStopa";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoreskaStopaRepository poreskaStopaRepository;

    public PoreskaStopaResource(PoreskaStopaRepository poreskaStopaRepository) {
        this.poreskaStopaRepository = poreskaStopaRepository;
    }

    /**
     * {@code POST  /poreska-stopas} : Create a new poreskaStopa.
     *
     * @param poreskaStopa the poreskaStopa to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poreskaStopa, or with status {@code 400 (Bad Request)} if the poreskaStopa has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/poreska-stopas")
    public ResponseEntity<PoreskaStopa> createPoreskaStopa(@RequestBody PoreskaStopa poreskaStopa) throws URISyntaxException {
        log.debug("REST request to save PoreskaStopa : {}", poreskaStopa);
        if (poreskaStopa.getId() != null) {
            throw new BadRequestAlertException("A new poreskaStopa cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PoreskaStopa result = poreskaStopaRepository.save(poreskaStopa);
        return ResponseEntity
            .created(new URI("/api/poreska-stopas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poreska-stopas/:id} : Updates an existing poreskaStopa.
     *
     * @param id the id of the poreskaStopa to save.
     * @param poreskaStopa the poreskaStopa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poreskaStopa,
     * or with status {@code 400 (Bad Request)} if the poreskaStopa is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poreskaStopa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/poreska-stopas/{id}")
    public ResponseEntity<PoreskaStopa> updatePoreskaStopa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoreskaStopa poreskaStopa
    ) throws URISyntaxException {
        log.debug("REST request to update PoreskaStopa : {}, {}", id, poreskaStopa);
        if (poreskaStopa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poreskaStopa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poreskaStopaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoreskaStopa result = poreskaStopaRepository.save(poreskaStopa);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poreskaStopa.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poreska-stopas/:id} : Partial updates given fields of an existing poreskaStopa, field will ignore if it is null
     *
     * @param id the id of the poreskaStopa to save.
     * @param poreskaStopa the poreskaStopa to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poreskaStopa,
     * or with status {@code 400 (Bad Request)} if the poreskaStopa is not valid,
     * or with status {@code 404 (Not Found)} if the poreskaStopa is not found,
     * or with status {@code 500 (Internal Server Error)} if the poreskaStopa couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/poreska-stopas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoreskaStopa> partialUpdatePoreskaStopa(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoreskaStopa poreskaStopa
    ) throws URISyntaxException {
        log.debug("REST request to partial update PoreskaStopa partially : {}, {}", id, poreskaStopa);
        if (poreskaStopa.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poreskaStopa.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poreskaStopaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoreskaStopa> result = poreskaStopaRepository
            .findById(poreskaStopa.getId())
            .map(existingPoreskaStopa -> {
                if (poreskaStopa.getProcenatPdv() != null) {
                    existingPoreskaStopa.setProcenatPdv(poreskaStopa.getProcenatPdv());
                }
                if (poreskaStopa.getDatumVazenja() != null) {
                    existingPoreskaStopa.setDatumVazenja(poreskaStopa.getDatumVazenja());
                }

                return existingPoreskaStopa;
            })
            .map(poreskaStopaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poreskaStopa.getId().toString())
        );
    }

    /**
     * {@code GET  /poreska-stopas} : get all the poreskaStopas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poreskaStopas in body.
     */
    @GetMapping("/poreska-stopas")
    public List<PoreskaStopa> getAllPoreskaStopas() {
        log.debug("REST request to get all PoreskaStopas");
        return poreskaStopaRepository.findAll();
    }

    /**
     * {@code GET  /poreska-stopas/:id} : get the "id" poreskaStopa.
     *
     * @param id the id of the poreskaStopa to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poreskaStopa, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/poreska-stopas/{id}")
    public ResponseEntity<PoreskaStopa> getPoreskaStopa(@PathVariable Long id) {
        log.debug("REST request to get PoreskaStopa : {}", id);
        Optional<PoreskaStopa> poreskaStopa = poreskaStopaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poreskaStopa);
    }

    /**
     * {@code DELETE  /poreska-stopas/:id} : delete the "id" poreskaStopa.
     *
     * @param id the id of the poreskaStopa to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/poreska-stopas/{id}")
    public ResponseEntity<Void> deletePoreskaStopa(@PathVariable Long id) {
        log.debug("REST request to delete PoreskaStopa : {}", id);
        poreskaStopaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
