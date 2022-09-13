package tobias.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.domain.Promocion;
import tobias.repository.PromocionRepository;
import tobias.service.PromocionService;

/**
 * Service Implementation for managing {@link Promocion}.
 */
@Service
@Transactional
public class PromocionServiceImpl implements PromocionService {

    private final Logger log = LoggerFactory.getLogger(PromocionServiceImpl.class);

    private final PromocionRepository promocionRepository;

    public PromocionServiceImpl(PromocionRepository promocionRepository) {
        this.promocionRepository = promocionRepository;
    }

    @Override
    public Promocion save(Promocion promocion) {
        log.debug("Request to save Promocion : {}", promocion);
        return promocionRepository.save(promocion);
    }

    @Override
    public Promocion update(Promocion promocion) {
        log.debug("Request to update Promocion : {}", promocion);
        return promocionRepository.save(promocion);
    }

    @Override
    public Optional<Promocion> partialUpdate(Promocion promocion) {
        log.debug("Request to partially update Promocion : {}", promocion);

        return promocionRepository
            .findById(promocion.getId())
            .map(existingPromocion -> {
                if (promocion.getFechaInicio() != null) {
                    existingPromocion.setFechaInicio(promocion.getFechaInicio());
                }
                if (promocion.getFechaFin() != null) {
                    existingPromocion.setFechaFin(promocion.getFechaFin());
                }
                if (promocion.getPorcentajeDescuento() != null) {
                    existingPromocion.setPorcentajeDescuento(promocion.getPorcentajeDescuento());
                }

                return existingPromocion;
            })
            .map(promocionRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Promocion> findAll(Pageable pageable) {
        log.debug("Request to get all Promocions");
        return promocionRepository.findAll(pageable);
    }

    public Page<Promocion> findAllWithEagerRelationships(Pageable pageable) {
        return promocionRepository.findAllWithEagerRelationships(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Promocion> findOne(Long id) {
        log.debug("Request to get Promocion : {}", id);
        return promocionRepository.findOneWithEagerRelationships(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Promocion : {}", id);
        promocionRepository.deleteById(id);
    }
}
