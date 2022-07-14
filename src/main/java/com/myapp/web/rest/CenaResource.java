package com.myapp.web.rest;

import com.myapp.domain.Cena;
import com.myapp.repository.CenaRepository;
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
 * REST controller for managing {@link com.myapp.domain.Cena}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CenaResource {

    private final Logger log = LoggerFactory.getLogger(CenaResource.class);

    private static final String ENTITY_NAME = "cena";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CenaRepository cenaRepository;

    public CenaResource(CenaRepository cenaRepository) {
        this.cenaRepository = cenaRepository;
    }

    /**
     * {@code POST  /cenas} : Create a new cena.
     *
     * @param cena the cena to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new cena, or with status {@code 400 (Bad Request)} if the cena has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/cenas")
    public ResponseEntity<Cena> createCena(@RequestBody Cena cena) throws URISyntaxException {
        log.debug("REST request to save Cena : {}", cena);
        if (cena.getId() != null) {
            throw new BadRequestAlertException("A new cena cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Cena result = cenaRepository.save(cena);
        return ResponseEntity
            .created(new URI("/api/cenas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /cenas/:id} : Updates an existing cena.
     *
     * @param id the id of the cena to save.
     * @param cena the cena to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cena,
     * or with status {@code 400 (Bad Request)} if the cena is not valid,
     * or with status {@code 500 (Internal Server Error)} if the cena couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/cenas/{id}")
    public ResponseEntity<Cena> updateCena(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cena cena)
        throws URISyntaxException {
        log.debug("REST request to update Cena : {}, {}", id, cena);
        if (cena.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cena.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cenaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Cena result = cenaRepository.save(cena);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cena.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /cenas/:id} : Partial updates given fields of an existing cena, field will ignore if it is null
     *
     * @param id the id of the cena to save.
     * @param cena the cena to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated cena,
     * or with status {@code 400 (Bad Request)} if the cena is not valid,
     * or with status {@code 404 (Not Found)} if the cena is not found,
     * or with status {@code 500 (Internal Server Error)} if the cena couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/cenas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Cena> partialUpdateCena(@PathVariable(value = "id", required = false) final Long id, @RequestBody Cena cena)
        throws URISyntaxException {
        log.debug("REST request to partial update Cena partially : {}, {}", id, cena);
        if (cena.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, cena.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!cenaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Cena> result = cenaRepository
            .findById(cena.getId())
            .map(existingCena -> {
                if (cena.getDatumVazenja() != null) {
                    existingCena.setDatumVazenja(cena.getDatumVazenja());
                }
                if (cena.getIznosCene() != null) {
                    existingCena.setIznosCene(cena.getIznosCene());
                }

                return existingCena;
            })
            .map(cenaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, cena.getId().toString())
        );
    }

    /**
     * {@code GET  /cenas} : get all the cenas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of cenas in body.
     */
    @GetMapping("/cenas")
    public List<Cena> getAllCenas() {
        log.debug("REST request to get all Cenas");
        return cenaRepository.findAll();
    }

    /**
     * {@code GET  /cenas/:id} : get the "id" cena.
     *
     * @param id the id of the cena to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the cena, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/cenas/{id}")
    public ResponseEntity<Cena> getCena(@PathVariable Long id) {
        log.debug("REST request to get Cena : {}", id);
        Optional<Cena> cena = cenaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(cena);
    }

    /**
     * {@code DELETE  /cenas/:id} : delete the "id" cena.
     *
     * @param id the id of the cena to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/cenas/{id}")
    public ResponseEntity<Void> deleteCena(@PathVariable Long id) {
        log.debug("REST request to delete Cena : {}", id);
        cenaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
