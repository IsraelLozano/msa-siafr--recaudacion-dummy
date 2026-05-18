package pe.gob.mef.recaudacion.application.usecase;

import org.springframework.stereotype.Service;
import pe.gob.mef.recaudacion.application.port.in.RegistrarNotaDebitoUseCase;
import pe.gob.mef.recaudacion.application.port.out.NotaDebitoRepository;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;
import pe.gob.mef.recaudacion.domain.model.NotaDebito;

import java.time.OffsetDateTime;
import java.util.UUID;

@Service
public class RegistrarNotaDebitoUseCaseImpl implements RegistrarNotaDebitoUseCase {

    private final NotaDebitoRepository repository;

    public RegistrarNotaDebitoUseCaseImpl(NotaDebitoRepository repository) {
        this.repository = repository;
    }

    @Override
    public NotaDebito registrar(NotaDebito notaDebito) {
        String uid = UUID.randomUUID().toString().replace("-", "");
        NotaDebito conDatos = NotaDebito.builder()
            .numeroNotaDebito("ND-" + uid.substring(0, 8).toUpperCase())
            .numeroReporteRecaudacion(notaDebito.getNumeroReporteRecaudacion())
            .fechaHoraEmision(notaDebito.getFechaHoraEmision())
            .rucEntidadRecaudadora(notaDebito.getRucEntidadRecaudadora())
            .codigoEntidadFinanciera(notaDebito.getCodigoEntidadFinanciera())
            .montoTotal(notaDebito.getMontoTotal())
            .moneda(notaDebito.getMoneda())
            .cuentaBancariaCut(notaDebito.getCuentaBancariaCut())
            .cargoPropietario(notaDebito.getCargoPropietario())
            .cargoCuentaBancaria(notaDebito.getCargoCuentaBancaria())
            .abonoPropietario(notaDebito.getAbonoPropietario())
            .abonoCuentaBancaria(notaDebito.getAbonoCuentaBancaria())
            .conceptoCodigo(notaDebito.getConceptoCodigo())
            .conceptoImporte(notaDebito.getConceptoImporte())
            .estado(EstadoProcesamiento.REGISTRADO)
            .numeroSolicitud("nd-" + uid.substring(0, 10))
            .fechaRegistro(OffsetDateTime.now())
            .build();
        return repository.guardar(conDatos);
    }
}
