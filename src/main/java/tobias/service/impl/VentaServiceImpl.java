package tobias.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.domain.Venta;
import tobias.repository.VentaRepository;
import tobias.service.VentaService;
import tobias.service.dto.VentasTotalesDTO;

/**
 * Service Implementation for managing {@link Venta}.
 */
@Service
@Transactional
public class VentaServiceImpl implements VentaService {

    private final Logger log = LoggerFactory.getLogger(VentaServiceImpl.class);

    private final VentaRepository ventaRepository;

    public VentaServiceImpl(VentaRepository ventaRepository) {
        this.ventaRepository = ventaRepository;
    }

    @Override
    public Venta save(Venta venta) {
        log.debug("Request to save Venta : {}", venta);
        venta.setActivated(true);
        return ventaRepository.save(venta);
    }

    @Override
    public Venta update(Venta venta) {
        log.debug("Request to update Venta : {}", venta);
        Venta existingVenta = ventaRepository.findById(venta.getId()).get();

        if (venta.getFechaVenta() != null) {
            existingVenta.setFechaVenta(venta.getFechaVenta());
        }
        if (venta.getTotal() != null) {
            existingVenta.setTotal(venta.getTotal());
        }
        if (venta.getTipoVenta() != null) {
            existingVenta.setTipoVenta(venta.getTipoVenta());
        }
        if (venta.getEstadoVenta() != null) {
            existingVenta.setEstadoVenta(venta.getEstadoVenta());
        }
        if (venta.getPago() != null) {
            existingVenta.setPago(venta.getPago());
        }
        if (venta.getCliente() != null) {
            existingVenta.setCliente(venta.getCliente());
        }
        if (venta.getListaVentas() != null && !venta.getListaVentas().isEmpty()) {
            existingVenta.setListaVentas(venta.getListaVentas());
        }

        return ventaRepository.save(existingVenta);
    }

    @Override
    public Optional<Venta> partialUpdate(Venta venta) {
        log.debug("Request to partially update Venta : {}", venta);

        return ventaRepository
            .findById(venta.getId())
            .map(existingVenta -> {
                if (venta.getFechaVenta() != null) {
                    existingVenta.setFechaVenta(venta.getFechaVenta());
                }
                if (venta.getTotal() != null) {
                    existingVenta.setTotal(venta.getTotal());
                }
                if (venta.getTipoVenta() != null) {
                    existingVenta.setTipoVenta(venta.getTipoVenta());
                }
                if (venta.getEstadoVenta() != null) {
                    existingVenta.setEstadoVenta(venta.getEstadoVenta());
                }

                return existingVenta;
            })
            .map(ventaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Venta> findAll(Pageable pageable) {
        log.debug("Request to get all Ventas");
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        return ventaRepository.findByActivated(true, page);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Venta> findOne(Long id) {
        log.debug("Request to get Venta : {}", id);
        return ventaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Venta : {}", id);
        Venta existingVenta = ventaRepository.findById(id).get();
        existingVenta.setActivated(false);
        ventaRepository.save(existingVenta);
    }

    @Override
    public VentasTotalesDTO getVentasTotales() {
        log.debug("Request to get retail and wholesale sales totals : {}");
        return ventaRepository.getVentasTotales();
    }

    @Override
    public List<Venta> getAllVentasByDate(LocalDate date) {
        log.debug("Request to get Ventas by date : {}", date);
        return ventaRepository.getVentasByDate(date, true);
    }
}
