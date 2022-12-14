package tobias.service.impl;

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
import tobias.domain.Producto;
import tobias.repository.ProductoRepository;
import tobias.service.ProductoService;

/**
 * Service Implementation for managing {@link Producto}.
 */
@Service
@Transactional
public class ProductoServiceImpl implements ProductoService {

    private final Logger log = LoggerFactory.getLogger(ProductoServiceImpl.class);

    private final ProductoRepository productoRepository;

    public ProductoServiceImpl(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Override
    public Producto save(Producto producto) {
        log.debug("Request to save Producto : {}", producto);
        producto.setActivated(true);
        return productoRepository.save(producto);
    }

    @Override
    public Producto update(Producto producto) {
        log.debug("Request to update Producto : {}", producto);
        Producto existingProducto = productoRepository.findById(producto.getId()).get();

        if (producto.getNombre() != null) {
            existingProducto.setNombre(producto.getNombre());
        }
        if (producto.getPrecio() != null) {
            existingProducto.setPrecio(producto.getPrecio());
        }
        if (producto.getImagenUrl() != null) {
            existingProducto.setImagenUrl(producto.getImagenUrl());
        }
        if (producto.getIngredientes() != null && !producto.getIngredientes().isEmpty()) {
            existingProducto.setIngredientes(producto.getIngredientes());
        }

        return productoRepository.save(existingProducto);
    }

    @Override
    public Optional<Producto> partialUpdate(Producto producto) {
        log.debug("Request to partially update Producto : {}", producto);

        return productoRepository
            .findById(producto.getId())
            .map(existingProducto -> {
                if (producto.getNombre() != null) {
                    existingProducto.setNombre(producto.getNombre());
                }
                if (producto.getPrecio() != null) {
                    existingProducto.setPrecio(producto.getPrecio());
                }
                if (producto.getImagenUrl() != null) {
                    existingProducto.setImagenUrl(producto.getImagenUrl());
                }

                return existingProducto;
            })
            .map(productoRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<Producto> findAll(Pageable pageable) {
        log.debug("Request to get all Productos");
        Pageable page = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by("id").descending());
        return productoRepository.findByActivated(true, page);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Producto> findOne(Long id) {
        log.debug("Request to get Producto : {}", id);
        return productoRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Producto : {}", id);
        Producto existingProducto = productoRepository.findById(id).get();
        existingProducto.setActivated(false);
        productoRepository.save(existingProducto);
    }

    @Override
    public List<Producto> findAllList() {
        log.debug("Request to get all Productos without pagination");
        return productoRepository.findByActivated(true);
    }
}
