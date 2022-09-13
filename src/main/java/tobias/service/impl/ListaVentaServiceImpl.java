package tobias.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.domain.ListaVenta;
import tobias.repository.ListaVentaRepository;
import tobias.service.ListaVentaService;

/**
 * Service Implementation for managing {@link ListaVenta}.
 */
@Service
@Transactional
public class ListaVentaServiceImpl implements ListaVentaService {

    private final Logger log = LoggerFactory.getLogger(ListaVentaServiceImpl.class);

    private final ListaVentaRepository listaVentaRepository;

    public ListaVentaServiceImpl(ListaVentaRepository listaVentaRepository) {
        this.listaVentaRepository = listaVentaRepository;
    }

    @Override
    public ListaVenta save(ListaVenta listaVenta) {
        log.debug("Request to save ListaVenta : {}", listaVenta);
        return listaVentaRepository.save(listaVenta);
    }

    @Override
    public ListaVenta update(ListaVenta listaVenta) {
        log.debug("Request to update ListaVenta : {}", listaVenta);
        return listaVentaRepository.save(listaVenta);
    }

    @Override
    public Optional<ListaVenta> partialUpdate(ListaVenta listaVenta) {
        log.debug("Request to partially update ListaVenta : {}", listaVenta);

        return listaVentaRepository
            .findById(listaVenta.getId())
            .map(existingListaVenta -> {
                if (listaVenta.getCantidad() != null) {
                    existingListaVenta.setCantidad(listaVenta.getCantidad());
                }
                if (listaVenta.getSubtotal() != null) {
                    existingListaVenta.setSubtotal(listaVenta.getSubtotal());
                }
                if (listaVenta.getPrecio() != null) {
                    existingListaVenta.setPrecio(listaVenta.getPrecio());
                }
                if (listaVenta.getDescuento() != null) {
                    existingListaVenta.setDescuento(listaVenta.getDescuento());
                }

                return existingListaVenta;
            })
            .map(listaVentaRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ListaVenta> findAll(Pageable pageable) {
        log.debug("Request to get all ListaVentas");
        return listaVentaRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ListaVenta> findOne(Long id) {
        log.debug("Request to get ListaVenta : {}", id);
        return listaVentaRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete ListaVenta : {}", id);
        listaVentaRepository.deleteById(id);
    }
}
