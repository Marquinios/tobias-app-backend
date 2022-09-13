package tobias.domain;

import java.io.Serializable;
import java.time.LocalDate;
import javax.persistence.*;
import javax.validation.constraints.*;

/**
 * A Pago.
 */
@Entity
@Table(name = "pago")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Pago implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "fecha_pago", nullable = false)
    private LocalDate fechaPago;

    @Column(name = "tipo_pago")
    private String tipoPago;

    @Column(name = "numero_transaccion")
    private Integer numeroTransaccion;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Pago id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getFechaPago() {
        return this.fechaPago;
    }

    public Pago fechaPago(LocalDate fechaPago) {
        this.setFechaPago(fechaPago);
        return this;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getTipoPago() {
        return this.tipoPago;
    }

    public Pago tipoPago(String tipoPago) {
        this.setTipoPago(tipoPago);
        return this;
    }

    public void setTipoPago(String tipoPago) {
        this.tipoPago = tipoPago;
    }

    public Integer getNumeroTransaccion() {
        return this.numeroTransaccion;
    }

    public Pago numeroTransaccion(Integer numeroTransaccion) {
        this.setNumeroTransaccion(numeroTransaccion);
        return this;
    }

    public void setNumeroTransaccion(Integer numeroTransaccion) {
        this.numeroTransaccion = numeroTransaccion;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Pago)) {
            return false;
        }
        return id != null && id.equals(((Pago) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Pago{" +
            "id=" + getId() +
            ", fechaPago='" + getFechaPago() + "'" +
            ", tipoPago='" + getTipoPago() + "'" +
            ", numeroTransaccion=" + getNumeroTransaccion() +
            "}";
    }
}
