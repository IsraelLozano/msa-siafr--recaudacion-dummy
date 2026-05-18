package pe.gob.mef.recaudacion.application.port.in;

import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

public interface RegistrarReporteUseCase {
    ReporteRecaudacion registrar(ReporteRecaudacion reporte);
}
