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
import tobias.domain.ListaVenta;
import tobias.repository.ListaVentaRepository;
import tobias.service.ListaVentaService;
import tobias.web.rest.errors.BadRequestAlertException;

/**
 * REST controller for managing {@link tobias.domain.ListaVenta}.
 */
@RestController
@RequestMapping("/api")
public class ListaVentaResource {

    private final Logger log = LoggerFactory.getLogger(ListaVentaResource.class);

    private static final String ENTITY_NAME = "listaVenta";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ListaVentaService listaVentaService;

    private final ListaVentaRepository listaVentaRepository;

    public ListaVentaResource(ListaVentaService listaVentaService, ListaVentaRepository listaVentaRepository) {
        this.listaVentaService = listaVentaService;
        this.listaVentaRepository = listaVentaRepository;
    }

    /**
     * {@code POST  /lista-ventas} : Create a new listaVenta.
     *
     * @param listaVenta the listaVenta to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new listaVenta, or with status {@code 400 (Bad Request)} if the listaVenta has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/lista-ventas")
    public ResponseEntity<ListaVenta> createListaVenta(@Valid @RequestBody ListaVenta listaVenta) throws URISyntaxException {
        log.debug("REST request to save ListaVenta : {}", listaVenta);
        if (listaVenta.getId() != null) {
            throw new BadRequestAlertException("A new listaVenta cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ListaVenta result = listaVentaService.save(listaVenta);
        return ResponseEntity
            .created(new URI("/api/lista-ventas/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /lista-ventas/:id} : Updates an existing listaVenta.
     *
     * @param id the id of the listaVenta to save.
     * @param listaVenta the listaVenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listaVenta,
     * or with status {@code 400 (Bad Request)} if the listaVenta is not valid,
     * or with status {@code 500 (Internal Server Error)} if the listaVenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/lista-ventas/{id}")
    public ResponseEntity<ListaVenta> updateListaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ListaVenta listaVenta
    ) throws URISyntaxException {
        log.debug("REST request to update ListaVenta : {}, {}", id, listaVenta);
        if (listaVenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listaVenta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ListaVenta result = listaVentaService.update(listaVenta);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, listaVenta.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /lista-ventas/:id} : Partial updates given fields of an existing listaVenta, field will ignore if it is null
     *
     * @param id the id of the listaVenta to save.
     * @param listaVenta the listaVenta to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated listaVenta,
     * or with status {@code 400 (Bad Request)} if the listaVenta is not valid,
     * or with status {@code 404 (Not Found)} if the listaVenta is not found,
     * or with status {@code 500 (Internal Server Error)} if the listaVenta couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/lista-ventas/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ListaVenta> partialUpdateListaVenta(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ListaVenta listaVenta
    ) throws URISyntaxException {
        log.debug("REST request to partial update ListaVenta partially : {}, {}", id, listaVenta);
        if (listaVenta.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, listaVenta.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!listaVentaRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ListaVenta> result = listaVentaService.partialUpdate(listaVenta);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, listaVenta.getId().toString())
        );
    }

    /**
     * {@code GET  /lista-ventas} : get all the listaVentas.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of listaVentas in body.
     */
    @GetMapping("/lista-ventas")
    public ResponseEntity<List<ListaVenta>> getAllListaVentas(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ListaVentas");
        Page<ListaVenta> page = listaVentaService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /lista-ventas/:id} : get the "id" listaVenta.
     *
     * @param id the id of the listaVenta to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the listaVenta, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/lista-ventas/{id}")
    public ResponseEntity<ListaVenta> getListaVenta(@PathVariable Long id) {
        log.debug("REST request to get ListaVenta : {}", id);
        Optional<ListaVenta> listaVenta = listaVentaService.findOne(id);
        return ResponseUtil.wrapOrNotFound(listaVenta);
    }

    /**
     * {@code DELETE  /lista-ventas/:id} : delete the "id" listaVenta.
     *
     * @param id the id of the listaVenta to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/lista-ventas/{id}")
    public ResponseEntity<Void> deleteListaVenta(@PathVariable Long id) {
        log.debug("REST request to delete ListaVenta : {}", id);
        listaVentaService.delete(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
