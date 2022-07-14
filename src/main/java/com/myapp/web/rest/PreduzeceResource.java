package com.myapp.web.rest;

import com.myapp.domain.Preduzece;
import com.myapp.repository.PreduzeceRepository;
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
 * REST controller for managing {@link com.myapp.domain.Preduzece}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class PreduzeceResource {

    private final Logger log = LoggerFactory.getLogger(PreduzeceResource.class);

    private static final String ENTITY_NAME = "preduzece";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PreduzeceRepository preduzeceRepository;

    public PreduzeceResource(PreduzeceRepository preduzeceRepository) {
        this.preduzeceRepository = preduzeceRepository;
    }

    /**
     * {@code POST  /preduzeces} : Create a new preduzece.
     *
     * @param preduzece the preduzece to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new preduzece, or with status {@code 400 (Bad Request)} if the preduzece has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/preduzeces")
    public ResponseEntity<Preduzece> createPreduzece(@RequestBody Preduzece preduzece) throws URISyntaxException {
        log.debug("REST request to save Preduzece : {}", preduzece);
        if (preduzece.getId() != null) {
            throw new BadRequestAlertException("A new preduzece cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Preduzece result = preduzeceRepository.save(preduzece);
        return ResponseEntity
            .created(new URI("/api/preduzeces/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /preduzeces/:id} : Updates an existing preduzece.
     *
     * @param id the id of the preduzece to save.
     * @param preduzece the preduzece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preduzece,
     * or with status {@code 400 (Bad Request)} if the preduzece is not valid,
     * or with status {@code 500 (Internal Server Error)} if the preduzece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/preduzeces/{id}")
    public ResponseEntity<Preduzece> updatePreduzece(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Preduzece preduzece
    ) throws URISyntaxException {
        log.debug("REST request to update Preduzece : {}, {}", id, preduzece);
        if (preduzece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preduzece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preduzeceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Preduzece result = preduzeceRepository.save(preduzece);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preduzece.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /preduzeces/:id} : Partial updates given fields of an existing preduzece, field will ignore if it is null
     *
     * @param id the id of the preduzece to save.
     * @param preduzece the preduzece to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated preduzece,
     * or with status {@code 400 (Bad Request)} if the preduzece is not valid,
     * or with status {@code 404 (Not Found)} if the preduzece is not found,
     * or with status {@code 500 (Internal Server Error)} if the preduzece couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/preduzeces/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Preduzece> partialUpdatePreduzece(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Preduzece preduzece
    ) throws URISyntaxException {
        log.debug("REST request to partial update Preduzece partially : {}, {}", id, preduzece);
        if (preduzece.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, preduzece.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!preduzeceRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Preduzece> result = preduzeceRepository
            .findById(preduzece.getId())
            .map(existingPreduzece -> {
                if (preduzece.getPib() != null) {
                    existingPreduzece.setPib(preduzece.getPib());
                }
                if (preduzece.getNaziv() != null) {
                    existingPreduzece.setNaziv(preduzece.getNaziv());
                }
                if (preduzece.getAdresa() != null) {
                    existingPreduzece.setAdresa(preduzece.getAdresa());
                }

                return existingPreduzece;
            })
            .map(preduzeceRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, preduzece.getId().toString())
        );
    }

    /**
     * {@code GET  /preduzeces} : get all the preduzeces.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of preduzeces in body.
     */
    @GetMapping("/preduzeces")
    public List<Preduzece> getAllPreduzeces() {
        log.debug("REST request to get all Preduzeces");
        return preduzeceRepository.findAll();
    }

    /**
     * {@code GET  /preduzeces/:id} : get the "id" preduzece.
     *
     * @param id the id of the preduzece to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the preduzece, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/preduzeces/{id}")
    public ResponseEntity<Preduzece> getPreduzece(@PathVariable Long id) {
        log.debug("REST request to get Preduzece : {}", id);
        Optional<Preduzece> preduzece = preduzeceRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(preduzece);
    }

    /**
     * {@code DELETE  /preduzeces/:id} : delete the "id" preduzece.
     *
     * @param id the id of the preduzece to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/preduzeces/{id}")
    public ResponseEntity<Void> deletePreduzece(@PathVariable Long id) {
        log.debug("REST request to delete Preduzece : {}", id);
        preduzeceRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
