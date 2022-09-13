package tobias.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.ListaVenta;

/**
 * Spring Data JPA repository for the ListaVenta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ListaVentaRepository extends JpaRepository<ListaVenta, Long> {}
