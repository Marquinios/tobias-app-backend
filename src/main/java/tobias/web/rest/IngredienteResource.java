package tobias.web.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
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
import tobias.domain.Ingrediente;
import tobias.repository.IngredienteRepository;
import tobias.service.IngredienteService;
import tobias.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tobias.domain.Ingrediente}.
 */
@RestController
@RequestMapping("/api")
public class IngredienteResource {

    private final Logger log = LoggerFactory.getLogger(IngredienteResource.class);

    private static final String ENTITY_NAME = "ingrediente";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final IngredienteService ingredienteService;

    private final IngredienteRepository ingredienteRepository;

    public IngredienteResource(IngredienteService ingredienteService, IngredienteRepository ingredienteRepository) {
        this.ingredienteService = ingredienteService;
        this.ingredienteRepository = ingredienteRepository;
    }

    /**
     * {@code POST  /ingredientes} : Create a new ingrediente.
     *
     * @param ingrediente the ingrediente to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new ingrediente, or with status {@code 400 (Bad Request)} if the ingrediente has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/ingredientes")
    public ResponseEntity<Ingrediente> createIngrediente(@RequestBody Ingrediente ingrediente) throws URISyntaxException {
        log.debug("REST request to save Ingrediente : {}", ingrediente);
        if (ingrediente.getId() != null) {
            throw new BadRequestAlertException("A new ingrediente cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Ingrediente result = ingredienteService.save(ingrediente);
        return ResponseEntity
            .created(new URI("/api/ingredientes/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /ingredientes/:id} : Updates an existing ingrediente.
     *
     * @param id the id of the ingrediente to save.
     * @param ingrediente the ingrediente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingrediente,
     * or with status {@code 400 (Bad Request)} if the ingrediente is not valid,
     * or with status {@code 500 (Internal Server Error)} if the ingrediente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/ingredientes/{id}")
    public ResponseEntity<Ingrediente> updateIngrediente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ingrediente ingrediente
    ) throws URISyntaxException {
        log.debug("REST request to update Ingrediente : {}, {}", id, ingrediente);
        if (ingrediente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingrediente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Ingrediente result = ingredienteService.update(ingrediente);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingrediente.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /ingredientes/:id} : Partial updates given fields of an existing ingrediente, field will ignore if it is null
     *
     * @param id the id of the ingrediente to save.
     * @param ingrediente the ingrediente to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated ingrediente,
     * or with status {@code 400 (Bad Request)} if the ingrediente is not valid,
     * or with status {@code 404 (Not Found)} if the ingrediente is not found,
     * or with status {@code 500 (Internal Server Error)} if the ingrediente couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/ingredientes/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Ingrediente> partialUpdateIngrediente(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody Ingrediente ingrediente
    ) throws URISyntaxException {
        log.debug("REST request to partial update Ingrediente partially : {}, {}", id, ingrediente);
        if (ingrediente.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, ingrediente.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!ingredienteRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Ingrediente> result = ingredienteService.partialUpdate(ingrediente);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, ingrediente.getId().toString())
        );
    }

    /**
     * {@code GET  /ingredientes} : get all the ingredientes.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of ingredientes in body.
     */
    @GetMapping("/ingredientes")
    public ResponseEntity<List<Ingrediente>> getAllIngredientes(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Ingredientes");
        Page<Ingrediente> page = ingredienteService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /ingredientes/:id} : get the "id" ingrediente.
     *
     * @param id the id of the ingrediente to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the ingrediente, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/ingredientes/{id}")
    public ResponseEntity<Ingrediente> getIngrediente(@PathVariable Long id) {
        log.debug("REST request to get Ingrediente : {}", id);
        Optional<Ingrediente> ingrediente = ingredienteService.findOne(id);
        return ResponseUtil.wrapOrNotFound(ingrediente);
    }

    /**
     * {@code DELETE  /ingredientes/:id} : delete the "id" ingrediente.
     *
     * @param id the id of the ingrediente to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/ingredientes/{id}")
    public ResponseEntity<Void> deleteIngrediente(@PathVariable Long id) {
        log.debug("REST request to delete Ingrediente : {}", id);
        ingredienteService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
