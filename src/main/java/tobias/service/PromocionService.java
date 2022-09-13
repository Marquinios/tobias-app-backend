package tobias.service;

import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tobias.domain.Promocion;

/**
 * Service Interface for managing {@link Promocion}.
 */
public interface PromocionService {
    /**
     * Save a promocion.
     *
     * @param promocion the entity to save.
     * @return the persisted entity.
     */
    Promocion save(Promocion promocion);

    /**
     * Updates a promocion.
     *
     * @param promocion the entity to update.
     * @return the persisted entity.
     */
    Promocion update(Promocion promocion);

    /**
     * Partially updates a promocion.
     *
     * @param promocion the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Promocion> partialUpdate(Promocion promocion);

    /**
     * Get all the promocions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Promocion> findAll(Pageable pageable);

    /**
     * Get all the promocions with eager load of many-to-many relationships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<Promocion> findAllWithEagerRelationships(Pageable pageable);

    /**
     * Get the "id" promocion.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Promocion> findOne(Long id);

    /**
     * Delete the "id" promocion.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
