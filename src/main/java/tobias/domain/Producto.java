package tobias.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Producto.
 */
@Entity
@Table(name = "producto")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Producto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;

    @NotNull
    @Column(name = "precio", nullable = false)
    private Float precio;

    @Column(name = "imagen_url")
    private String imagenUrl;

    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "producto_id")
    private Set<Ingrediente> ingredientes = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here
    @Column(name = "activated", nullable = false)
    private boolean activated;

    public Long getId() {
        return this.id;
    }

    public Producto id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return this.nombre;
    }

    public Producto nombre(String nombre) {
        this.setNombre(nombre);
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Float getPrecio() {
        return this.precio;
    }

    public Producto precio(Float precio) {
        this.setPrecio(precio);
        return this;
    }

    public void setPrecio(Float precio) {
        this.precio = precio;
    }

    public String getImagenUrl() {
        return this.imagenUrl;
    }

    public Producto imagenUrl(String imagenUrl) {
        this.setImagenUrl(imagenUrl);
        return this;
    }

    public void setImagenUrl(String imagenUrl) {
        this.imagenUrl = imagenUrl;
    }

    public Set<Ingrediente> getIngredientes() {
        return this.ingredientes;
    }

    public void setIngredientes(Set<Ingrediente> ingredientes) {
        if (this.ingredientes != null) {
            // this.ingredientes.forEach(i -> i.setProductoId(null));
        }
        if (ingredientes != null) {
            // ingredientes.forEach(i -> i.setProductoId(this));
        }
        this.ingredientes = ingredientes;
    }

    public Producto ingredientes(Set<Ingrediente> ingredientes) {
        this.setIngredientes(ingredientes);
        return this;
    }

    public Producto addIngrediente(Ingrediente ingrediente) {
        this.ingredientes.add(ingrediente);
        return this;
    }

    public Producto removeIngrediente(Ingrediente ingrediente) {
        this.ingredientes.remove(ingrediente);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here
    public boolean isActivated() {
        return activated;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Producto)) {
            return false;
        }
        return id != null && id.equals(((Producto) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Producto{" +
            "id=" + id +
            ", nombre='" + nombre + '\'' +
            ", precio=" + precio +
            ", imagenUrl='" + imagenUrl + '\'' +
            ", activaded=" + activated +
            '}';
    }
}
