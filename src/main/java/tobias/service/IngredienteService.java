package tobias.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tobias.domain.Ingrediente;

/**
 * Service Interface for managing {@link Ingrediente}.
 */
public interface IngredienteService {
    /**
     * Save a ingrediente.
     *
     * @param ingrediente the entity to save.
     * @return the persisted entity.
     */
    Ingrediente save(Ingrediente ingrediente);

    /**
     * Updates a ingrediente.
     *
     * @param ingrediente the entity to update.
     * @return the persisted entity.
     */
    Ingrediente update(Ingrediente ingrediente);

    /**
     * Partially updates a ingrediente.
     *
     * @param ingrediente the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Ingrediente> partialUpdate(Ingrediente ingrediente);

    /**
     * Get all the ingredientes.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Ingrediente> findAll(Pageable pageable);

    /**
     * Get the "id" ingrediente.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Ingrediente> findOne(Long id);

    /**
     * Delete the "id" ingrediente.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
