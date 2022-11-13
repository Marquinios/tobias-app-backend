package tobias.service.impl;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.transaction.Transactional;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanArrayDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import tobias.domain.Ingrediente;
import tobias.service.ReporteService;
import tobias.service.dto.InsumosDTO;
import tobias.service.dto.ReporteInsumosDTO;

@Service
@Transactional
public class ReporteServiceImpl implements ReporteService {

    private final Logger log = LoggerFactory.getLogger(ReporteServiceImpl.class);

    /*public static List<InsumosDTO> generateIngList(){
        return Arrays.asList(
            new InsumosDTO(Float.valueOf(3), "kg", "pan"),
            new InsumosDTO(Float.valueOf(3), "kg", "papas"),
            new InsumosDTO(Float.valueOf(3), "gr", "carne"),
            new InsumosDTO(Float.valueOf(3), "onza", "sal")
        );
    }*/

    @Override
    public byte[] generateReporteInsumos(ReporteInsumosDTO reporteInsumosDTO) throws FileNotFoundException, JRException {
        log.debug("Request to generate Reporte Insumos: {}", reporteInsumosDTO.getFechaReporte());

        JRBeanArrayDataSource ds = new JRBeanArrayDataSource(reporteInsumosDTO.getInsumos().toArray());
        JasperReport compileReport = JasperCompileManager.compileReport(
            new FileInputStream("src/main/resources/templates/reporte/ReporteCalculosInsumos.jrxml")
        );
        InputStream logoEmpresa = new FileInputStream("src/main/resources/img/logo_tobias.png");

        HashMap<String, Object> map = new HashMap<>();
        map.put("ds", ds);
        map.put("logoEmpresa", logoEmpresa);
        map.put("fechaReporte", reporteInsumosDTO.getFechaReporte());
        map.put("totalVentas", reporteInsumosDTO.getTotalVentas());
        map.put("totalPedidos", reporteInsumosDTO.getTotalPedidos());

        JasperPrint report = JasperFillManager.fillReport(compileReport, map, new JREmptyDataSource());

        byte[] data = JasperExportManager.exportReportToPdf(report);

        return data;
    }
}
