package tobias.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import javax.persistence.*;

/**
 * A Ingrediente.
 */
@Entity
@Table(name = "ingrediente")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Ingrediente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "unidad")
    private String unidad;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "producto_id", insertable = false, updatable = false)
    private Integer productoId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Ingrediente id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return this.cantidad;
    }

    public Ingrediente cantidad(Integer cantidad) {
        this.setCantidad(cantidad);
        return this;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return this.unidad;
    }

    public Ingrediente unidad(String unidad) {
        this.setUnidad(unidad);
        return this;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Ingrediente nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    /*public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }*/

    public Integer getProductoId() {
        return productoId;
    }

    public void setProductoId(Integer productoId) {
        this.productoId = productoId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Ingrediente)) {
            return false;
        }
        return id != null && id.equals(((Ingrediente) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Ingrediente{" +
            "id=" + getId() +
            ", cantidad=" + getCantidad() +
            ", unidad='" + getUnidad() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", productoId='" + getProductoId() + "'" +
            "}";
    }
}
