package pe.gob.mef.recaudacion.application.port.in;

import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

import java.time.LocalDate;
import java.util.List;

public interface ConsultarReporteUseCase {
    List<ReporteRecaudacion> consultar(
            String numeroReporteRecaudacion,
            String numeroSolicitud,
            LocalDate fechaInicio,
            LocalDate fechaFin);
}
