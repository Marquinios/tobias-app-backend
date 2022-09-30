package tobias.service.dto;

public class VentasTotalesDTO {

    private double totalVentaMinorista;
    private double totalVentaMayorista;

    public VentasTotalesDTO(double totalVentaMinorista, double totalVentaMayorista) {
        this.totalVentaMinorista = totalVentaMinorista;
        this.totalVentaMayorista = totalVentaMayorista;
    }

    public double getTotalVentaMinorista() {
        return totalVentaMinorista;
    }

    public void setTotalVentaMinorista(double totalVentaMinorista) {
        this.totalVentaMinorista = totalVentaMinorista;
    }

    public double getTotalVentaMayorista() {
        return totalVentaMayorista;
    }

    public void setTotalVentaMayorista(double totalVentaMayorista) {
        this.totalVentaMayorista = totalVentaMayorista;
    }
}
