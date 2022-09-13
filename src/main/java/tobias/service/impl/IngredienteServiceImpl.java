package tobias.service.impl;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tobias.domain.Ingrediente;
import tobias.repository.IngredienteRepository;
import tobias.service.IngredienteService;

/**
 * Service Implementation for managing {@link Ingrediente}.
 */
@Service
@Transactional
public class IngredienteServiceImpl implements IngredienteService {

    private final Logger log = LoggerFactory.getLogger(IngredienteServiceImpl.class);

    private final IngredienteRepository ingredienteRepository;

    public IngredienteServiceImpl(IngredienteRepository ingredienteRepository) {
        this.ingredienteRepository = ingredienteRepository;
    }

    @Override
    public Ingrediente save(Ingrediente ingrediente) {
        log.debug("Request to save Ingrediente : {}", ingrediente);
        return ingredienteRepository.save(ingrediente);
    }

    @Override
    public Ingrediente update(Ingrediente ingrediente) {
        log.debug("Request to update Ingrediente : {}", ingrediente);
        return ingredienteRepository.save(ingrediente);
    }

    @Override
    public Optional<Ingrediente> partialUpdate(Ingrediente ingrediente) {
        log.debug("Request to partially update Ingrediente : {}", ingrediente);

        return ingredienteRepository
            .findById(ingrediente.getId())
            .map(existingIngrediente -> {
                if (ingrediente.getCantidad() != null) {
                    existingIngrediente.setCantidad(ingrediente.getCantidad());
                }
                if (ingrediente.getUnidad() != null) {
                    existingIngrediente.setUnidad(ingrediente.getUnidad());
                }
                if (ingrediente.getNombre() != null) {
                    existingIngrediente.setNombre(ingrediente.getNombre());
                }

                return existingIngrediente;
            })
            .map(ingredienteRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Ingrediente> findAll(Pageable pageable) {
        log.debug("Request to get all Ingredientes");
        return ingredienteRepository.findAll(pageable);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Ingrediente> findOne(Long id) {
        log.debug("Request to get Ingrediente : {}", id);
        return ingredienteRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Ingrediente : {}", id);
        ingredienteRepository.deleteById(id);
    }
}
