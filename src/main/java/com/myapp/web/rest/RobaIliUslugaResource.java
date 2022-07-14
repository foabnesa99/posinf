package com.myapp.web.rest;

import com.myapp.domain.RobaIliUsluga;
import com.myapp.repository.RobaIliUslugaRepository;
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
 * REST controller for managing {@link com.myapp.domain.RobaIliUsluga}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class RobaIliUslugaResource {

    private final Logger log = LoggerFactory.getLogger(RobaIliUslugaResource.class);

    private static final String ENTITY_NAME = "robaIliUsluga";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final RobaIliUslugaRepository robaIliUslugaRepository;

    public RobaIliUslugaResource(RobaIliUslugaRepository robaIliUslugaRepository) {
        this.robaIliUslugaRepository = robaIliUslugaRepository;
    }

    /**
     * {@code POST  /roba-ili-uslugas} : Create a new robaIliUsluga.
     *
     * @param robaIliUsluga the robaIliUsluga to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new robaIliUsluga, or with status {@code 400 (Bad Request)} if the robaIliUsluga has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/roba-ili-uslugas")
    public ResponseEntity<RobaIliUsluga> createRobaIliUsluga(@RequestBody RobaIliUsluga robaIliUsluga) throws URISyntaxException {
        log.debug("REST request to save RobaIliUsluga : {}", robaIliUsluga);
        if (robaIliUsluga.getId() != null) {
            throw new BadRequestAlertException("A new robaIliUsluga cannot already have an ID", ENTITY_NAME, "idexists");
        }
        RobaIliUsluga result = robaIliUslugaRepository.save(robaIliUsluga);
        return ResponseEntity
            .created(new URI("/api/roba-ili-uslugas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /roba-ili-uslugas/:id} : Updates an existing robaIliUsluga.
     *
     * @param id the id of the robaIliUsluga to save.
     * @param robaIliUsluga the robaIliUsluga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated robaIliUsluga,
     * or with status {@code 400 (Bad Request)} if the robaIliUsluga is not valid,
     * or with status {@code 500 (Internal Server Error)} if the robaIliUsluga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/roba-ili-uslugas/{id}")
    public ResponseEntity<RobaIliUsluga> updateRobaIliUsluga(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RobaIliUsluga robaIliUsluga
    ) throws URISyntaxException {
        log.debug("REST request to update RobaIliUsluga : {}, {}", id, robaIliUsluga);
        if (robaIliUsluga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, robaIliUsluga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!robaIliUslugaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        RobaIliUsluga result = robaIliUslugaRepository.save(robaIliUsluga);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, robaIliUsluga.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /roba-ili-uslugas/:id} : Partial updates given fields of an existing robaIliUsluga, field will ignore if it is null
     *
     * @param id the id of the robaIliUsluga to save.
     * @param robaIliUsluga the robaIliUsluga to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated robaIliUsluga,
     * or with status {@code 400 (Bad Request)} if the robaIliUsluga is not valid,
     * or with status {@code 404 (Not Found)} if the robaIliUsluga is not found,
     * or with status {@code 500 (Internal Server Error)} if the robaIliUsluga couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/roba-ili-uslugas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<RobaIliUsluga> partialUpdateRobaIliUsluga(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody RobaIliUsluga robaIliUsluga
    ) throws URISyntaxException {
        log.debug("REST request to partial update RobaIliUsluga partially : {}, {}", id, robaIliUsluga);
        if (robaIliUsluga.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, robaIliUsluga.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!robaIliUslugaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<RobaIliUsluga> result = robaIliUslugaRepository
            .findById(robaIliUsluga.getId())
            .map(existingRobaIliUsluga -> {
                if (robaIliUsluga.getNaziv() != null) {
                    existingRobaIliUsluga.setNaziv(robaIliUsluga.getNaziv());
                }
                if (robaIliUsluga.getOpis() != null) {
                    existingRobaIliUsluga.setOpis(robaIliUsluga.getOpis());
                }

                return existingRobaIliUsluga;
            })
            .map(robaIliUslugaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, robaIliUsluga.getId().toString())
        );
    }

    /**
     * {@code GET  /roba-ili-uslugas} : get all the robaIliUslugas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of robaIliUslugas in body.
     */
    @GetMapping("/roba-ili-uslugas")
    public List<RobaIliUsluga> getAllRobaIliUslugas() {
        log.debug("REST request to get all RobaIliUslugas");
        return robaIliUslugaRepository.findAll();
    }

    /**
     * {@code GET  /roba-ili-uslugas/:id} : get the "id" robaIliUsluga.
     *
     * @param id the id of the robaIliUsluga to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the robaIliUsluga, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/roba-ili-uslugas/{id}")
    public ResponseEntity<RobaIliUsluga> getRobaIliUsluga(@PathVariable Long id) {
        log.debug("REST request to get RobaIliUsluga : {}", id);
        Optional<RobaIliUsluga> robaIliUsluga = robaIliUslugaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(robaIliUsluga);
    }

    /**
     * {@code DELETE  /roba-ili-uslugas/:id} : delete the "id" robaIliUsluga.
     *
     * @param id the id of the robaIliUsluga to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/roba-ili-uslugas/{id}")
    public ResponseEntity<Void> deleteRobaIliUsluga(@PathVariable Long id) {
        log.debug("REST request to delete RobaIliUsluga : {}", id);
        robaIliUslugaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
