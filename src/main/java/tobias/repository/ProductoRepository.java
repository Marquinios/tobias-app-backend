package tobias.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.Producto;

/**
 * Spring Data JPA repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {}
