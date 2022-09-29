package tobias.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A ListaVenta.
 */
@Entity
@Table(name = "lista_venta")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ListaVenta implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "subtotal")
    private Float subtotal;

    @Column(name = "precio")
    private Float precio;

    @Column(name = "descuento")
    private Float descuento;

    @Column(name = "venta_id", insertable = false, updatable = false)
    private Integer ventaId;

    @OneToOne
    @JoinColumn(unique = true)
    private Producto producto;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ListaVenta id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public ListaVenta cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Float getSubtotal() {
        return this.subtotal;
    }

    public ListaVenta subtotal(Float subtotal) {
        this.setSubtotal(subtotal);
        return this;
    }

    public void setSubtotal(Float subtotal) {
        this.subtotal = subtotal;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public ListaVenta precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public Float getDescuento() {
        return this.descuento;
    }

    public ListaVenta descuento(Float descuento) {
        this.setDescuento(descuento);
        return this;
    }

    public void setDescuento(Float descuento) {
        this.descuento = descuento;
    }

    public Integer getVentaId() {
        return ventaId;
    }

    public void setVentaId(Integer ventaId) {
        this.ventaId = ventaId;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ListaVenta)) {
            return false;
        }
        return id != null && id.equals(((ListaVenta) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ListaVenta{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", subtotal=" + getSubtotal() +
            ", precio=" + getPrecio() +
            ", descuento=" + getDescuento() +
            "}";
    }
}
