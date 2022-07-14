package com.myapp.web.rest;

import com.myapp.domain.StavkaFakture;
import com.myapp.repository.StavkaFaktureRepository;
import com.myapp.service.IzlaznaFakturaService;
import com.myapp.service.StavkaFaktureService;
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
 * REST controller for managing {@link com.myapp.domain.StavkaFakture}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StavkaFaktureResource {

    private final Logger log = LoggerFactory.getLogger(StavkaFaktureResource.class);

    private final IzlaznaFakturaService izlaznaFakturaService;
    private final StavkaFaktureService stavkaFaktureService;
    private static final String ENTITY_NAME = "stavkaFakture";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StavkaFaktureRepository stavkaFaktureRepository;

    public StavkaFaktureResource(IzlaznaFakturaService izlaznaFakturaService, StavkaFaktureService stavkaFaktureService, StavkaFaktureRepository stavkaFaktureRepository) {
        this.izlaznaFakturaService = izlaznaFakturaService;
        this.stavkaFaktureService = stavkaFaktureService;
        this.stavkaFaktureRepository = stavkaFaktureRepository;
    }

    /**
     * {@code POST  /stavka-faktures} : Create a new stavkaFakture.
     *
     * @param stavkaFakture the stavkaFakture to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new stavkaFakture, or with status {@code 400 (Bad Request)} if the stavkaFakture has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/stavka-faktures")
    public ResponseEntity<StavkaFakture> createStavkaFakture(@RequestBody StavkaFakture stavkaFakture) throws URISyntaxException {
        log.debug("REST request to save StavkaFakture : {}", stavkaFakture);
        if (stavkaFakture.getId() != null) {
            throw new BadRequestAlertException("A new stavkaFakture cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StavkaFakture result = stavkaFaktureService.generateStavka(stavkaFakture);
        stavkaFaktureRepository.save(result);

        log.info("\n STAVKA FAKTURE IZ REQUESTA : " + stavkaFakture.getIzlaznaFaktura().getId() + " OBRADJENA: " + result.getIzlaznaFaktura());

        if(result.getIzlaznaFaktura() != null){
            izlaznaFakturaService.updateExistingFaktura(result, stavkaFakture.getIzlaznaFaktura().getId());
        }
        return ResponseEntity
            .created(new URI("/api/stavka-faktures/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /stavka-faktures/:id} : Updates an existing stavkaFakture.
     *
     * @param id the id of the stavkaFakture to save.
     * @param stavkaFakture the stavkaFakture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stavkaFakture,
     * or with status {@code 400 (Bad Request)} if the stavkaFakture is not valid,
     * or with status {@code 500 (Internal Server Error)} if the stavkaFakture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/stavka-faktures/{id}")
    public ResponseEntity<StavkaFakture> updateStavkaFakture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StavkaFakture stavkaFakture
    ) throws URISyntaxException {
        log.debug("REST request to update StavkaFakture : {}, {}", id, stavkaFakture);
        if (stavkaFakture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stavkaFakture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stavkaFaktureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StavkaFakture result = stavkaFaktureRepository.save(stavkaFakture);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stavkaFakture.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /stavka-faktures/:id} : Partial updates given fields of an existing stavkaFakture, field will ignore if it is null
     *
     * @param id the id of the stavkaFakture to save.
     * @param stavkaFakture the stavkaFakture to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated stavkaFakture,
     * or with status {@code 400 (Bad Request)} if the stavkaFakture is not valid,
     * or with status {@code 404 (Not Found)} if the stavkaFakture is not found,
     * or with status {@code 500 (Internal Server Error)} if the stavkaFakture couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/stavka-faktures/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StavkaFakture> partialUpdateStavkaFakture(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody StavkaFakture stavkaFakture
    ) throws URISyntaxException {
        log.debug("REST request to partial update StavkaFakture partially : {}, {}", id, stavkaFakture);
        if (stavkaFakture.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, stavkaFakture.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!stavkaFaktureRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StavkaFakture> result = stavkaFaktureRepository
            .findById(stavkaFakture.getId())
            .map(existingStavkaFakture -> {
                if (stavkaFakture.getKolicina() != null) {
                    existingStavkaFakture.setKolicina(stavkaFakture.getKolicina());
                }
                if (stavkaFakture.getJedinicnaCena() != null) {
                    existingStavkaFakture.setJedinicnaCena(stavkaFakture.getJedinicnaCena());
                }
                if (stavkaFakture.getRabat() != null) {
                    existingStavkaFakture.setRabat(stavkaFakture.getRabat());
                }
                if (stavkaFakture.getProcenatPDV() != null) {
                    existingStavkaFakture.setProcenatPDV(stavkaFakture.getProcenatPDV());
                }
                if (stavkaFakture.getIznosPDV() != null) {
                    existingStavkaFakture.setIznosPDV(stavkaFakture.getIznosPDV());
                }
                if (stavkaFakture.getUkupnaCena() != null) {
                    existingStavkaFakture.setUkupnaCena(stavkaFakture.getUkupnaCena());
                }

                return existingStavkaFakture;
            })
            .map(stavkaFaktureRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, stavkaFakture.getId().toString())
        );
    }

    /**
     * {@code GET  /stavka-faktures} : get all the stavkaFaktures.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of stavkaFaktures in body.
     */
    @GetMapping("/stavka-faktures")
    public List<StavkaFakture> getAllStavkaFaktures() {
        log.debug("REST request to get all StavkaFaktures");
        return stavkaFaktureRepository.findAll();
    }

    /**
     * {@code GET  /stavka-faktures/:id} : get the "id" stavkaFakture.
     *
     * @param id the id of the stavkaFakture to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the stavkaFakture, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/stavka-faktures/{id}")
    public ResponseEntity<StavkaFakture> getStavkaFakture(@PathVariable Long id) {
        log.debug("REST request to get StavkaFakture : {}", id);
        Optional<StavkaFakture> stavkaFakture = stavkaFaktureRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(stavkaFakture);
    }

    /**
     * {@code DELETE  /stavka-faktures/:id} : delete the "id" stavkaFakture.
     *
     * @param id the id of the stavkaFakture to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/stavka-faktures/{id}")
    public ResponseEntity<Void> deleteStavkaFakture(@PathVariable Long id) {
        log.debug("REST request to delete StavkaFakture : {}", id);
        stavkaFaktureRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
