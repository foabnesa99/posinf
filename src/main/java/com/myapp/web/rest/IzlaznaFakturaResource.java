package com.myapp.web.rest;

import com.myapp.domain.IzlaznaFaktura;
import com.myapp.repository.IzlaznaFakturaRepository;
import com.myapp.service.PdfGenerator;
import com.myapp.web.rest.errors.BadRequestAlertException;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

import javax.servlet.http.HttpServletResponse;

/**
 * REST controller for managing {@link com.myapp.domain.IzlaznaFaktura}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class IzlaznaFakturaResource {

    private final Logger log = LoggerFactory.getLogger(IzlaznaFakturaResource.class);

    private static final String ENTITY_NAME = "izlaznaFaktura";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IzlaznaFakturaRepository izlaznaFakturaRepository;

    public IzlaznaFakturaResource(IzlaznaFakturaRepository izlaznaFakturaRepository) {
        this.izlaznaFakturaRepository = izlaznaFakturaRepository;
    }

    /**
     * {@code POST  /izlazna-fakturas} : Create a new izlaznaFaktura.
     *
     * @param izlaznaFaktura the izlaznaFaktura to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new izlaznaFaktura, or with status {@code 400 (Bad Request)} if the izlaznaFaktura has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/izlazna-fakturas")
    public ResponseEntity<IzlaznaFaktura> createIzlaznaFaktura(@RequestBody IzlaznaFaktura izlaznaFaktura) throws URISyntaxException {
        log.debug("REST request to save IzlaznaFaktura : {}", izlaznaFaktura);
        if (izlaznaFaktura.getId() != null) {
            throw new BadRequestAlertException("A new izlaznaFaktura cannot already have an ID", ENTITY_NAME, "idexists");
        }
        IzlaznaFaktura result = izlaznaFakturaRepository.save(izlaznaFaktura);
        return ResponseEntity
            .created(new URI("/api/izlazna-fakturas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /izlazna-fakturas/:id} : Updates an existing izlaznaFaktura.
     *
     * @param id the id of the izlaznaFaktura to save.
     * @param izlaznaFaktura the izlaznaFaktura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated izlaznaFaktura,
     * or with status {@code 400 (Bad Request)} if the izlaznaFaktura is not valid,
     * or with status {@code 500 (Internal Server Error)} if the izlaznaFaktura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/izlazna-fakturas/{id}")
    public ResponseEntity<IzlaznaFaktura> updateIzlaznaFaktura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IzlaznaFaktura izlaznaFaktura
    ) throws URISyntaxException {
        log.debug("REST request to update IzlaznaFaktura : {}, {}", id, izlaznaFaktura);
        if (izlaznaFaktura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, izlaznaFaktura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!izlaznaFakturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        IzlaznaFaktura result = izlaznaFakturaRepository.save(izlaznaFaktura);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, izlaznaFaktura.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /izlazna-fakturas/:id} : Partial updates given fields of an existing izlaznaFaktura, field will ignore if it is null
     *
     * @param id the id of the izlaznaFaktura to save.
     * @param izlaznaFaktura the izlaznaFaktura to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated izlaznaFaktura,
     * or with status {@code 400 (Bad Request)} if the izlaznaFaktura is not valid,
     * or with status {@code 404 (Not Found)} if the izlaznaFaktura is not found,
     * or with status {@code 500 (Internal Server Error)} if the izlaznaFaktura couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/izlazna-fakturas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<IzlaznaFaktura> partialUpdateIzlaznaFaktura(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody IzlaznaFaktura izlaznaFaktura
    ) throws URISyntaxException {
        log.debug("REST request to partial update IzlaznaFaktura partially : {}, {}", id, izlaznaFaktura);
        if (izlaznaFaktura.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, izlaznaFaktura.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!izlaznaFakturaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<IzlaznaFaktura> result = izlaznaFakturaRepository
            .findById(izlaznaFaktura.getId())
            .map(existingIzlaznaFaktura -> {
                if (izlaznaFaktura.getBrojFakture() != null) {
                    existingIzlaznaFaktura.setBrojFakture(izlaznaFaktura.getBrojFakture());
                }
                if (izlaznaFaktura.getDatumIzdavanja() != null) {
                    existingIzlaznaFaktura.setDatumIzdavanja(izlaznaFaktura.getDatumIzdavanja());
                }
                if (izlaznaFaktura.getDatumValute() != null) {
                    existingIzlaznaFaktura.setDatumValute(izlaznaFaktura.getDatumValute());
                }
                if (izlaznaFaktura.getUkupnaOsnovica() != null) {
                    existingIzlaznaFaktura.setUkupnaOsnovica(izlaznaFaktura.getUkupnaOsnovica());
                }
                if (izlaznaFaktura.getUkupanPdv() != null) {
                    existingIzlaznaFaktura.setUkupanPdv(izlaznaFaktura.getUkupanPdv());
                }
                if (izlaznaFaktura.getUkupanIznos() != null) {
                    existingIzlaznaFaktura.setUkupanIznos(izlaznaFaktura.getUkupanIznos());
                }
                if (izlaznaFaktura.getStatus() != null) {
                    existingIzlaznaFaktura.setStatus(izlaznaFaktura.getStatus());
                }

                return existingIzlaznaFaktura;
            })
            .map(izlaznaFakturaRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, izlaznaFaktura.getId().toString())
        );
    }

    /**
     * {@code GET  /izlazna-fakturas} : get all the izlaznaFakturas.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of izlaznaFakturas in body.
     */
    @GetMapping("/izlazna-fakturas")
    public List<IzlaznaFaktura> getAllIzlaznaFakturas() {
        log.debug("REST request to get all IzlaznaFakturas");
        return izlaznaFakturaRepository.findAll();
    }
    @PreAuthorize("hasAnyRole()")
    @GetMapping("/izlazna-fakturas/{id}/export")
    public void exportToPdf(@PathVariable Long id, HttpServletResponse response) throws IOException {
        log.info("\n \n Pogodjena putanja za export");
        response.setContentType("application/pdf");

        IzlaznaFaktura faktura = izlaznaFakturaRepository.getById(id);

        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=faktura_" + faktura.getBrojFakture() + ".pdf";
        response.setHeader(headerKey, headerValue);

        PdfGenerator exporter = new PdfGenerator(faktura);
        exporter.export(response);
    }

    /**
     * {@code GET  /izlazna-fakturas/:id} : get the "id" izlaznaFaktura.
     *
     * @param id the id of the izlaznaFaktura to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the izlaznaFaktura, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/izlazna-fakturas/{id}")
    public ResponseEntity<IzlaznaFaktura> getIzlaznaFaktura(@PathVariable Long id) {
        log.debug("REST request to get IzlaznaFaktura : {}", id);
        Optional<IzlaznaFaktura> izlaznaFaktura = izlaznaFakturaRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(izlaznaFaktura);
    }

    /**
     * {@code DELETE  /izlazna-fakturas/:id} : delete the "id" izlaznaFaktura.
     *
     * @param id the id of the izlaznaFaktura to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/izlazna-fakturas/{id}")
    public ResponseEntity<Void> deleteIzlaznaFaktura(@PathVariable Long id) {
        log.debug("REST request to delete IzlaznaFaktura : {}", id);
        izlaznaFakturaRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
