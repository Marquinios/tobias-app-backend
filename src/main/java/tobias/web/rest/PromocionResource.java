package tobias.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;
import tobias.domain.Promocion;
import tobias.repository.PromocionRepository;
import tobias.service.PromocionService;
import tobias.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tobias.domain.Promocion}.
 */
@RestController
@RequestMapping("/api")
public class PromocionResource {

    private final Logger log = LoggerFactory.getLogger(PromocionResource.class);

    private static final String ENTITY_NAME = "promocion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final PromocionService promocionService;

    private final PromocionRepository promocionRepository;

    public PromocionResource(PromocionService promocionService, PromocionRepository promocionRepository) {
        this.promocionService = promocionService;
        this.promocionRepository = promocionRepository;
    }

    /**
     * {@code POST  /promocions} : Create a new promocion.
     *
     * @param promocion the promocion to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new promocion, or with status {@code 400 (Bad Request)} if the promocion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/promocions")
    public ResponseEntity<Promocion> createPromocion(@Valid @RequestBody Promocion promocion) throws URISyntaxException {
        log.debug("REST request to save Promocion : {}", promocion);
        if (promocion.getId() != null) {
            throw new BadRequestAlertException("A new promocion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Promocion result = promocionService.save(promocion);
        return ResponseEntity
            .created(new URI("/api/promocions/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /promocions/:id} : Updates an existing promocion.
     *
     * @param id the id of the promocion to save.
     * @param promocion the promocion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocion,
     * or with status {@code 400 (Bad Request)} if the promocion is not valid,
     * or with status {@code 500 (Internal Server Error)} if the promocion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/promocions/{id}")
    public ResponseEntity<Promocion> updatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Promocion promocion
    ) throws URISyntaxException {
        log.debug("REST request to update Promocion : {}, {}", id, promocion);
        if (promocion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Promocion result = promocionService.update(promocion);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, promocion.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /promocions/:id} : Partial updates given fields of an existing promocion, field will ignore if it is null
     *
     * @param id the id of the promocion to save.
     * @param promocion the promocion to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated promocion,
     * or with status {@code 400 (Bad Request)} if the promocion is not valid,
     * or with status {@code 404 (Not Found)} if the promocion is not found,
     * or with status {@code 500 (Internal Server Error)} if the promocion couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/promocions/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Promocion> partialUpdatePromocion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Promocion promocion
    ) throws URISyntaxException {
        log.debug("REST request to partial update Promocion partially : {}, {}", id, promocion);
        if (promocion.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, promocion.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!promocionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Promocion> result = promocionService.partialUpdate(promocion);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, promocion.getId().toString())
        );
    }

    /**
     * {@code GET  /promocions} : get all the promocions.
     *
     * @param pageable the pagination information.
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of promocions in body.
     */
    @GetMapping("/promocions")
    public ResponseEntity<List<Promocion>> getAllPromocions(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable,
        @RequestParam(required = false, defaultValue = "false") boolean eagerload
    ) {
        log.debug("REST request to get a page of Promocions");
        Page<Promocion> page;
        if (eagerload) {
            page = promocionService.findAllWithEagerRelationships(pageable);
        } else {
            page = promocionService.findAll(pageable);
        }
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /promocions/:id} : get the "id" promocion.
     *
     * @param id the id of the promocion to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the promocion, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/promocions/{id}")
    public ResponseEntity<Promocion> getPromocion(@PathVariable Long id) {
        log.debug("REST request to get Promocion : {}", id);
        Optional<Promocion> promocion = promocionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(promocion);
    }

    /**
     * {@code DELETE  /promocions/:id} : delete the "id" promocion.
     *
     * @param id the id of the promocion to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/promocions/{id}")
    public ResponseEntity<Void> deletePromocion(@PathVariable Long id) {
        log.debug("REST request to delete Promocion : {}", id);
        promocionService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
