package tobias.repository;

import java.time.LocalDate;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tobias.domain.Venta;
import tobias.service.dto.VentasTotalesDTO;

/**
 * Spring Data JPA repository for the Venta entity.
 */
@SuppressWarnings("unused")
@Repository
public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query(
        value = "SELECT new tobias.service.dto.VentasTotalesDTO(SUM(CASE WHEN v.tipoVenta = 'VENTA_MINORISTA' THEN v.total END) as totalVentaMinorista,SUM(CASE WHEN v.tipoVenta = 'VENTA_MAYORISTA' THEN v.total END) as totalVentaMayorista) FROM Venta v WHERE v.estadoVenta != 'CANCELADO'"
    )
    public VentasTotalesDTO getVentasTotales();

    @Query(value = "SELECT v FROM Venta v where (v.fechaVenta = :date AND v.estadoVenta != 'CANCELADO') AND " + "v.activated = :activated")
    List<Venta> getVentasByDate(@Param("date") LocalDate date, @Param("activated") boolean activated);

    Page<Venta> findByActivated(boolean activated, Pageable pageable);
}
