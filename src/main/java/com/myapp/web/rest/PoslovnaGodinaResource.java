package com.myapp.web.rest;

import com.myapp.domain.PoslovnaGodina;
import com.myapp.repository.PoslovnaGodinaRepository;
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
 * REST controller for managing {@link com.myapp.domain.PoslovnaGodina}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PoslovnaGodinaResource {

    private final Logger log = LoggerFactory.getLogger(PoslovnaGodinaResource.class);

    private static final String ENTITY_NAME = "poslovnaGodina";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PoslovnaGodinaRepository poslovnaGodinaRepository;

    public PoslovnaGodinaResource(PoslovnaGodinaRepository poslovnaGodinaRepository) {
        this.poslovnaGodinaRepository = poslovnaGodinaRepository;
    }

    /**
     * {@code POST  /poslovna-godinas} : Create a new poslovnaGodina.
     *
     * @param poslovnaGodina the poslovnaGodina to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new poslovnaGodina, or with status {@code 400 (Bad Request)} if the poslovnaGodina has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/poslovna-godinas")
    public ResponseEntity<PoslovnaGodina> createPoslovnaGodina(@RequestBody PoslovnaGodina poslovnaGodina) throws URISyntaxException {
        log.debug("REST request to save PoslovnaGodina : {}", poslovnaGodina);
        if (poslovnaGodina.getId() != null) {
            throw new BadRequestAlertException("A new poslovnaGodina cannot already have an ID", ENTITY_NAME, "idexists");
        }
        PoslovnaGodina result = poslovnaGodinaRepository.save(poslovnaGodina);
        return ResponseEntity
            .created(new URI("/api/poslovna-godinas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /poslovna-godinas/:id} : Updates an existing poslovnaGodina.
     *
     * @param id the id of the poslovnaGodina to save.
     * @param poslovnaGodina the poslovnaGodina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poslovnaGodina,
     * or with status {@code 400 (Bad Request)} if the poslovnaGodina is not valid,
     * or with status {@code 500 (Internal Server Error)} if the poslovnaGodina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/poslovna-godinas/{id}")
    public ResponseEntity<PoslovnaGodina> updatePoslovnaGodina(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoslovnaGodina poslovnaGodina
    ) throws URISyntaxException {
        log.debug("REST request to update PoslovnaGodina : {}, {}", id, poslovnaGodina);
        if (poslovnaGodina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poslovnaGodina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poslovnaGodinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        PoslovnaGodina result = poslovnaGodinaRepository.save(poslovnaGodina);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poslovnaGodina.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /poslovna-godinas/:id} : Partial updates given fields of an existing poslovnaGodina, field will ignore if it is null
     *
     * @param id the id of the poslovnaGodina to save.
     * @param poslovnaGodina the poslovnaGodina to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated poslovnaGodina,
     * or with status {@code 400 (Bad Request)} if the poslovnaGodina is not valid,
     * or with status {@code 404 (Not Found)} if the poslovnaGodina is not found,
     * or with status {@code 500 (Internal Server Error)} if the poslovnaGodina couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/poslovna-godinas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<PoslovnaGodina> partialUpdatePoslovnaGodina(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody PoslovnaGodina poslovnaGodina
    ) throws URISyntaxException {
        log.debug("REST request to partial update PoslovnaGodina partially : {}, {}", id, poslovnaGodina);
        if (poslovnaGodina.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, poslovnaGodina.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!poslovnaGodinaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<PoslovnaGodina> result = poslovnaGodinaRepository
            .findById(poslovnaGodina.getId())
            .map(existingPoslovnaGodina -> {
                if (poslovnaGodina.getGodina() != null) {
                    existingPoslovnaGodina.setGodina(poslovnaGodina.getGodina());
                }
                if (poslovnaGodina.getZakljucena() != null) {
                    existingPoslovnaGodina.setZakljucena(poslovnaGodina.getZakljucena());
                }

                return existingPoslovnaGodina;
            })
            .map(poslovnaGodinaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, poslovnaGodina.getId().toString())
        );
    }

    /**
     * {@code GET  /poslovna-godinas} : get all the poslovnaGodinas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of poslovnaGodinas in body.
     */
    @GetMapping("/poslovna-godinas")
    public List<PoslovnaGodina> getAllPoslovnaGodinas() {
        log.debug("REST request to get all PoslovnaGodinas");
        return poslovnaGodinaRepository.findAll();
    }

    /**
     * {@code GET  /poslovna-godinas/:id} : get the "id" poslovnaGodina.
     *
     * @param id the id of the poslovnaGodina to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the poslovnaGodina, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/poslovna-godinas/{id}")
    public ResponseEntity<PoslovnaGodina> getPoslovnaGodina(@PathVariable Long id) {
        log.debug("REST request to get PoslovnaGodina : {}", id);
        Optional<PoslovnaGodina> poslovnaGodina = poslovnaGodinaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(poslovnaGodina);
    }

    /**
     * {@code DELETE  /poslovna-godinas/:id} : delete the "id" poslovnaGodina.
     *
     * @param id the id of the poslovnaGodina to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/poslovna-godinas/{id}")
    public ResponseEntity<Void> deletePoslovnaGodina(@PathVariable Long id) {
        log.debug("REST request to delete PoslovnaGodina : {}", id);
        poslovnaGodinaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
