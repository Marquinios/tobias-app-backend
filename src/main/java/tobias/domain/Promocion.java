package tobias.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Promocion.
 */
@Entity
@Table(name = "promocion")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Promocion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_inicio", nullable = false)
    private LocalDate fechaInicio;

    @NotNull
    @Column(name = "fecha_fin", nullable = false)
    private LocalDate fechaFin;

    @NotNull
    @Column(name = "porcentaje_descuento", nullable = false)
    private Float porcentajeDescuento;

    @ManyToMany
    @JoinTable(
        name = "rel_promocion__producto",
        joinColumns = @JoinColumn(name = "promocion_id"),
        inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    @JsonIgnoreProperties(value = { "ingredientes" }, allowSetters = true)
    private Set<Producto> productos = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Promocion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaInicio() {
        return this.fechaInicio;
    }

    public Promocion fechaInicio(LocalDate fechaInicio) {
        this.setFechaInicio(fechaInicio);
        return this;
    }

    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public LocalDate getFechaFin() {
        return this.fechaFin;
    }

    public Promocion fechaFin(LocalDate fechaFin) {
        this.setFechaFin(fechaFin);
        return this;
    }

    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Float getPorcentajeDescuento() {
        return this.porcentajeDescuento;
    }

    public Promocion porcentajeDescuento(Float porcentajeDescuento) {
        this.setPorcentajeDescuento(porcentajeDescuento);
        return this;
    }

    public void setPorcentajeDescuento(Float porcentajeDescuento) {
        this.porcentajeDescuento = porcentajeDescuento;
    }

    public Set<Producto> getProductos() {
        return this.productos;
    }

    public void setProductos(Set<Producto> productos) {
        this.productos = productos;
    }

    public Promocion productos(Set<Producto> productos) {
        this.setProductos(productos);
        return this;
    }

    public Promocion addProducto(Producto producto) {
        this.productos.add(producto);
        return this;
    }

    public Promocion removeProducto(Producto producto) {
        this.productos.remove(producto);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Promocion)) {
            return false;
        }
        return id != null && id.equals(((Promocion) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Promocion{" +
            "id=" + getId() +
            ", fechaInicio='" + getFechaInicio() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            ", porcentajeDescuento=" + getPorcentajeDescuento() +
            "}";
    }
}
