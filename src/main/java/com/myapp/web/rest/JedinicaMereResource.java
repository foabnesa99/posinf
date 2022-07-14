package com.myapp.web.rest;

import com.myapp.domain.JedinicaMere;
import com.myapp.repository.JedinicaMereRepository;
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
 * REST controller for managing {@link com.myapp.domain.JedinicaMere}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class JedinicaMereResource {

    private final Logger log = LoggerFactory.getLogger(JedinicaMereResource.class);

    private static final String ENTITY_NAME = "jedinicaMere";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final JedinicaMereRepository jedinicaMereRepository;

    public JedinicaMereResource(JedinicaMereRepository jedinicaMereRepository) {
        this.jedinicaMereRepository = jedinicaMereRepository;
    }

    /**
     * {@code POST  /jedinica-meres} : Create a new jedinicaMere.
     *
     * @param jedinicaMere the jedinicaMere to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new jedinicaMere, or with status {@code 400 (Bad Request)} if the jedinicaMere has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/jedinica-meres")
    public ResponseEntity<JedinicaMere> createJedinicaMere(@RequestBody JedinicaMere jedinicaMere) throws URISyntaxException {
        log.debug("REST request to save JedinicaMere : {}", jedinicaMere);
        if (jedinicaMere.getId() != null) {
            throw new BadRequestAlertException("A new jedinicaMere cannot already have an ID", ENTITY_NAME, "idexists");
        }
        JedinicaMere result = jedinicaMereRepository.save(jedinicaMere);
        return ResponseEntity
            .created(new URI("/api/jedinica-meres/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /jedinica-meres/:id} : Updates an existing jedinicaMere.
     *
     * @param id the id of the jedinicaMere to save.
     * @param jedinicaMere the jedinicaMere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jedinicaMere,
     * or with status {@code 400 (Bad Request)} if the jedinicaMere is not valid,
     * or with status {@code 500 (Internal Server Error)} if the jedinicaMere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/jedinica-meres/{id}")
    public ResponseEntity<JedinicaMere> updateJedinicaMere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JedinicaMere jedinicaMere
    ) throws URISyntaxException {
        log.debug("REST request to update JedinicaMere : {}, {}", id, jedinicaMere);
        if (jedinicaMere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jedinicaMere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jedinicaMereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        JedinicaMere result = jedinicaMereRepository.save(jedinicaMere);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jedinicaMere.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /jedinica-meres/:id} : Partial updates given fields of an existing jedinicaMere, field will ignore if it is null
     *
     * @param id the id of the jedinicaMere to save.
     * @param jedinicaMere the jedinicaMere to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated jedinicaMere,
     * or with status {@code 400 (Bad Request)} if the jedinicaMere is not valid,
     * or with status {@code 404 (Not Found)} if the jedinicaMere is not found,
     * or with status {@code 500 (Internal Server Error)} if the jedinicaMere couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/jedinica-meres/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<JedinicaMere> partialUpdateJedinicaMere(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody JedinicaMere jedinicaMere
    ) throws URISyntaxException {
        log.debug("REST request to partial update JedinicaMere partially : {}, {}", id, jedinicaMere);
        if (jedinicaMere.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, jedinicaMere.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!jedinicaMereRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<JedinicaMere> result = jedinicaMereRepository
            .findById(jedinicaMere.getId())
            .map(existingJedinicaMere -> {
                if (jedinicaMere.getNaziv() != null) {
                    existingJedinicaMere.setNaziv(jedinicaMere.getNaziv());
                }
                if (jedinicaMere.getSkraceniNaziv() != null) {
                    existingJedinicaMere.setSkraceniNaziv(jedinicaMere.getSkraceniNaziv());
                }

                return existingJedinicaMere;
            })
            .map(jedinicaMereRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, jedinicaMere.getId().toString())
        );
    }

    /**
     * {@code GET  /jedinica-meres} : get all the jedinicaMeres.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of jedinicaMeres in body.
     */
    @GetMapping("/jedinica-meres")
    public List<JedinicaMere> getAllJedinicaMeres() {
        log.debug("REST request to get all JedinicaMeres");
        return jedinicaMereRepository.findAll();
    }

    /**
     * {@code GET  /jedinica-meres/:id} : get the "id" jedinicaMere.
     *
     * @param id the id of the jedinicaMere to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the jedinicaMere, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/jedinica-meres/{id}")
    public ResponseEntity<JedinicaMere> getJedinicaMere(@PathVariable Long id) {
        log.debug("REST request to get JedinicaMere : {}", id);
        Optional<JedinicaMere> jedinicaMere = jedinicaMereRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(jedinicaMere);
    }

    /**
     * {@code DELETE  /jedinica-meres/:id} : delete the "id" jedinicaMere.
     *
     * @param id the id of the jedinicaMere to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/jedinica-meres/{id}")
    public ResponseEntity<Void> deleteJedinicaMere(@PathVariable Long id) {
        log.debug("REST request to delete JedinicaMere : {}", id);
        jedinicaMereRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
