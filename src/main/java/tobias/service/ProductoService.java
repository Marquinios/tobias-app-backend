package tobias.service;

import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tobias.domain.Producto;

/**
 * Service Interface for managing {@link Producto}.
 */
public interface ProductoService {
    /**
     * Save a producto.
     *
     * @param producto the entity to save.
     * @return the persisted entity.
     */
    Producto save(Producto producto);

    /**
     * Updates a producto.
     *
     * @param producto the entity to update.
     * @return the persisted entity.
     */
    Producto update(Producto producto);

    /**
     * Partially updates a producto.
     *
     * @param producto the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Producto> partialUpdate(Producto producto);

    /**
     * Get all the productos.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Producto> findAll(Pageable pageable);

    /**
     * Get the "id" producto.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Producto> findOne(Long id);

    /**
     * Delete the "id" producto.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);

    /**
     * Get all the productos without pagination.
     *
     * @return the list of entities.
     */
    List<Producto> findAllList();
}
