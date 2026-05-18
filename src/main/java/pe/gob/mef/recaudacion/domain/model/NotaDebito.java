package pe.gob.mef.recaudacion.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Getter
@Builder
public class NotaDebito {
    private String numeroNotaDebito;
    private String numeroReporteRecaudacion;
    private OffsetDateTime fechaHoraEmision;
    private String rucEntidadRecaudadora;
    private String codigoEntidadFinanciera;
    private BigDecimal montoTotal;
    private String moneda;
    private String cuentaBancariaCut;
    private String cargoPropietario;
    private String cargoCuentaBancaria;
    private String abonoPropietario;
    private String abonoCuentaBancaria;
    private String conceptoCodigo;
    private BigDecimal conceptoImporte;
    private EstadoProcesamiento estado;
    private String numeroSolicitud;
    private OffsetDateTime fechaRegistro;
}
