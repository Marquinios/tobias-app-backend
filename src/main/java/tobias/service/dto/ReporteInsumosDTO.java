package tobias.service.dto;

import java.util.List;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class ReporteInsumosDTO {

    @NotBlank
    private String fechaReporte;

    @NotNull
    private Float totalVentas;

    @NotNull
    private Float totalPedidos;

    @NotEmpty
    private List<InsumosDTO> insumos;

    public String getFechaReporte() {
        return fechaReporte;
    }

    public void setFechaReporte(String fechaReporte) {
        this.fechaReporte = fechaReporte;
    }

    public Float getTotalVentas() {
        return totalVentas;
    }

    public void setTotalVentas(Float totalVentas) {
        this.totalVentas = totalVentas;
    }

    public Float getTotalPedidos() {
        return totalPedidos;
    }

    public void setTotalPedidos(Float totalPedidos) {
        this.totalPedidos = totalPedidos;
    }

    public List<InsumosDTO> getInsumos() {
        return insumos;
    }

    public void setInsumos(List<InsumosDTO> insumos) {
        this.insumos = insumos;
    }
}
