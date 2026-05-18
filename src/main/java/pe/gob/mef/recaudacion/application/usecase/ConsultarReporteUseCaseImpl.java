package pe.gob.mef.recaudacion.application.usecase;

import org.springframework.stereotype.Service;
import pe.gob.mef.recaudacion.application.port.in.ConsultarReporteUseCase;
import pe.gob.mef.recaudacion.application.port.out.ReporteRecaudacionRepository;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConsultarReporteUseCaseImpl implements ConsultarReporteUseCase {

    private final ReporteRecaudacionRepository repository;

    public ConsultarReporteUseCaseImpl(ReporteRecaudacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<ReporteRecaudacion> consultar(String numeroRR, String numeroSolicitud,
                                               LocalDate fechaInicio, LocalDate fechaFin) {
        return repository.buscar(numeroRR, numeroSolicitud, fechaInicio, fechaFin);
    }
}
