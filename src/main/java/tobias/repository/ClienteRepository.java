package tobias.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import tobias.domain.Cliente;

/**
 * Spring Data JPA repository for the Cliente entity.
 */
@SuppressWarnings("unused")
@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
