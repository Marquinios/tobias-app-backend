package tobias.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tobias.domain.ListaVenta;

/**
 * Service Interface for managing {@link ListaVenta}.
 */
public interface ListaVentaService {
    /**
     * Save a listaVenta.
     *
     * @param listaVenta the entity to save.
     * @return the persisted entity.
     */
    ListaVenta save(ListaVenta listaVenta);

    /**
     * Updates a listaVenta.
     *
     * @param listaVenta the entity to update.
     * @return the persisted entity.
     */
    ListaVenta update(ListaVenta listaVenta);

    /**
     * Partially updates a listaVenta.
     *
     * @param listaVenta the entity to update partially.
     * @return the persisted entity.
     */
    Optional<ListaVenta> partialUpdate(ListaVenta listaVenta);

    /**
     * Get all the listaVentas.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ListaVenta> findAll(Pageable pageable);

    /**
     * Get the "id" listaVenta.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ListaVenta> findOne(Long id);

    /**
     * Delete the "id" listaVenta.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
