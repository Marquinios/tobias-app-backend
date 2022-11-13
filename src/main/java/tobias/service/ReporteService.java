package tobias.service;

import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.JRException;
import tobias.service.dto.ReporteInsumosDTO;

public interface ReporteService {
    byte[] generateReporteInsumos(ReporteInsumosDTO reporteInsumosDTO) throws FileNotFoundException, JRException;
}
