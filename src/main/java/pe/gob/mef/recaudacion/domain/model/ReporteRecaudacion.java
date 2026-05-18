package pe.gob.mef.recaudacion.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Getter
@Builder
public class ReporteRecaudacion {
    private String numeroReporteRecaudacion;
    private OffsetDateTime fechaHoraEmision;
    private String codigoEntidadFinanciera;
    private BigDecimal montoTotal;
    private String moneda;
    private String cuentaCut;
    private String entidadRecaudadora;
    private EstadoProcesamiento estadoProcesamiento;
    private String numeroSolicitud;
    private OffsetDateTime fechaRegistro;
    private List<Beneficiario> beneficiarios;
}
