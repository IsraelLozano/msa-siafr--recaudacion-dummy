package pe.gob.mef.recaudacion.application.usecase;

import org.springframework.stereotype.Service;
import pe.gob.mef.recaudacion.application.port.in.RegistrarReporteUseCase;
import pe.gob.mef.recaudacion.application.port.out.ReporteRecaudacionRepository;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RegistrarReporteUseCaseImpl implements RegistrarReporteUseCase {

    private final ReporteRecaudacionRepository repository;

    public RegistrarReporteUseCaseImpl(ReporteRecaudacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ReporteRecaudacion registrar(ReporteRecaudacion reporte) {
        ReporteRecaudacion conSolicitud = ReporteRecaudacion.builder()
            .numeroReporteRecaudacion(reporte.getNumeroReporteRecaudacion())
            .fechaHoraEmision(reporte.getFechaHoraEmision())
            .codigoEntidadFinanciera(reporte.getCodigoEntidadFinanciera())
            .montoTotal(reporte.getMontoTotal())
            .moneda(reporte.getMoneda())
            .cuentaCut(reporte.getCuentaCut())
            .entidadRecaudadora(reporte.getEntidadRecaudadora())
            .beneficiarios(reporte.getBeneficiarios())
            .estadoProcesamiento(EstadoProcesamiento.REGISTRADO)
            .numeroSolicitud(UUID.randomUUID().toString().replace("-", "").substring(0, 10))
            .fechaRegistro(OffsetDateTime.now())
            .build();
        return repository.guardar(conSolicitud);
    }
}
