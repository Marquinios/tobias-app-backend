package tobias.web.rest;

import java.io.FileNotFoundException;
import net.sf.jasperreports.engine.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tobias.service.ReporteService;
import tobias.service.dto.ReporteInsumosDTO;

@RestController
@RequestMapping("/api/reporte")
public class ReporteResource {

    private final Logger log = LoggerFactory.getLogger(ReporteResource.class);

    private final ReporteService reporteService;

    public ReporteResource(ReporteService reporteService) {
        this.reporteService = reporteService;
    }

    @PostMapping("calculo-insumos")
    public ResponseEntity<byte[]> generateReporteInsumos(@RequestBody ReporteInsumosDTO reporteInsumosDTO)
        throws FileNotFoundException, JRException {
        log.debug("REST request to generate Reportes Insumos: {}", reporteInsumosDTO.getFechaReporte());

        byte[] data = reporteService.generateReporteInsumos(reporteInsumosDTO);

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline;filename=Calculos-Insumos.pdf");

        return ResponseEntity.ok().headers(headers).contentType(MediaType.APPLICATION_PDF).body(data);
    }
}
