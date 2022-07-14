package com.myapp.web.rest;

import com.myapp.domain.Kupac;
import com.myapp.repository.KupacRepository;
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
 * REST controller for managing {@link com.myapp.domain.Kupac}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class KupacResource {

    private final Logger log = LoggerFactory.getLogger(KupacResource.class);

    private static final String ENTITY_NAME = "kupac";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final KupacRepository kupacRepository;

    public KupacResource(KupacRepository kupacRepository) {
        this.kupacRepository = kupacRepository;
    }

    /**
     * {@code POST  /kupacs} : Create a new kupac.
     *
     * @param kupac the kupac to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new kupac, or with status {@code 400 (Bad Request)} if the kupac has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/kupacs")
    public ResponseEntity<Kupac> createKupac(@RequestBody Kupac kupac) throws URISyntaxException {
        log.debug("REST request to save Kupac : {}", kupac);
        if (kupac.getId() != null) {
            throw new BadRequestAlertException("A new kupac cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Kupac result = kupacRepository.save(kupac);
        return ResponseEntity
            .created(new URI("/api/kupacs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /kupacs/:id} : Updates an existing kupac.
     *
     * @param id the id of the kupac to save.
     * @param kupac the kupac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kupac,
     * or with status {@code 400 (Bad Request)} if the kupac is not valid,
     * or with status {@code 500 (Internal Server Error)} if the kupac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/kupacs/{id}")
    public ResponseEntity<Kupac> updateKupac(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kupac kupac)
        throws URISyntaxException {
        log.debug("REST request to update Kupac : {}, {}", id, kupac);
        if (kupac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kupac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kupacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Kupac result = kupacRepository.save(kupac);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kupac.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /kupacs/:id} : Partial updates given fields of an existing kupac, field will ignore if it is null
     *
     * @param id the id of the kupac to save.
     * @param kupac the kupac to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated kupac,
     * or with status {@code 400 (Bad Request)} if the kupac is not valid,
     * or with status {@code 404 (Not Found)} if the kupac is not found,
     * or with status {@code 500 (Internal Server Error)} if the kupac couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/kupacs/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Kupac> partialUpdateKupac(@PathVariable(value = "id", required = false) final Long id, @RequestBody Kupac kupac)
        throws URISyntaxException {
        log.debug("REST request to partial update Kupac partially : {}, {}", id, kupac);
        if (kupac.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, kupac.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!kupacRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Kupac> result = kupacRepository
            .findById(kupac.getId())
            .map(existingKupac -> {
                if (kupac.getPib() != null) {
                    existingKupac.setPib(kupac.getPib());
                }
                if (kupac.getMib() != null) {
                    existingKupac.setMib(kupac.getMib());
                }
                if (kupac.getNaziv() != null) {
                    existingKupac.setNaziv(kupac.getNaziv());
                }
                if (kupac.getAdresa() != null) {
                    existingKupac.setAdresa(kupac.getAdresa());
                }
                if (kupac.getTelefon() != null) {
                    existingKupac.setTelefon(kupac.getTelefon());
                }

                return existingKupac;
            })
            .map(kupacRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, kupac.getId().toString())
        );
    }

    /**
     * {@code GET  /kupacs} : get all the kupacs.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of kupacs in body.
     */
    @GetMapping("/kupacs")
    public List<Kupac> getAllKupacs() {
        log.debug("REST request to get all Kupacs");
        return kupacRepository.findAll();
    }

    /**
     * {@code GET  /kupacs/:id} : get the "id" kupac.
     *
     * @param id the id of the kupac to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the kupac, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/kupacs/{id}")
    public ResponseEntity<Kupac> getKupac(@PathVariable Long id) {
        log.debug("REST request to get Kupac : {}", id);
        Optional<Kupac> kupac = kupacRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(kupac);
    }

    /**
     * {@code DELETE  /kupacs/:id} : delete the "id" kupac.
     *
     * @param id the id of the kupac to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/kupacs/{id}")
    public ResponseEntity<Void> deleteKupac(@PathVariable Long id) {
        log.debug("REST request to delete Kupac : {}", id);
        kupacRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
