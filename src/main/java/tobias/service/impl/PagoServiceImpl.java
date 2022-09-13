package tobias.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.domain.Pago;
import tobias.repository.PagoRepository;
import tobias.service.PagoService;

/**
 * Service Implementation for managing {@link Pago}.
 */
@Service
@Transactional
public class PagoServiceImpl implements PagoService {

    private final Logger log = LoggerFactory.getLogger(PagoServiceImpl.class);

    private final PagoRepository pagoRepository;

    public PagoServiceImpl(PagoRepository pagoRepository) {
        this.pagoRepository = pagoRepository;
    }

    @Override
    public Pago save(Pago pago) {
        log.debug("Request to save Pago : {}", pago);
        return pagoRepository.save(pago);
    }

    @Override
    public Pago update(Pago pago) {
        log.debug("Request to update Pago : {}", pago);
        return pagoRepository.save(pago);
    }

    @Override
    public Optional<Pago> partialUpdate(Pago pago) {
        log.debug("Request to partially update Pago : {}", pago);

        return pagoRepository
            .findById(pago.getId())
            .map(existingPago -> {
                if (pago.getFechaPago() != null) {
                    existingPago.setFechaPago(pago.getFechaPago());
                }
                if (pago.getTipoPago() != null) {
                    existingPago.setTipoPago(pago.getTipoPago());
                }
                if (pago.getNumeroTransaccion() != null) {
                    existingPago.setNumeroTransaccion(pago.getNumeroTransaccion());
                }

                return existingPago;
            })
            .map(pagoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Pago> findAll(Pageable pageable) {
        log.debug("Request to get all Pagos");
        return pagoRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Pago> findOne(Long id) {
        log.debug("Request to get Pago : {}", id);
        return pagoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Pago : {}", id);
        pagoRepository.deleteById(id);
    }
}
