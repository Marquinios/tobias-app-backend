package tobias.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.Ingrediente;

/**
 * Spring Data JPA repository for the Ingrediente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface IngredienteRepository extends JpaRepository<Ingrediente, Long> {}
