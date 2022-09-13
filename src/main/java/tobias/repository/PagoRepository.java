package tobias.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.Pago;

/**
 * Spring Data JPA repository for the Pago entity.
 */
@SuppressWarnings("unused")
@Repository
public interface PagoRepository extends JpaRepository<Pago, Long> {}
