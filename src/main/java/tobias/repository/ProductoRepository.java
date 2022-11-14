package tobias.repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.Producto;

/**
 * Spring Data JPA repository for the Producto entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ProductoRepository extends JpaRepository<Producto, Long> {
    Page<Producto> findByActivated(boolean activated, Pageable pageable);

    List<Producto> findByActivated(boolean activated);
}
