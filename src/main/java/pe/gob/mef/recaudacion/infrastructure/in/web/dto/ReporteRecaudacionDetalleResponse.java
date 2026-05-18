package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

public record ReporteRecaudacionDetalleResponse(
    String numeroReporteRecaudacion,
    OffsetDateTime fechaHoraEmision,
    String codigoEntidadFinanciera,
    BigDecimal montoTotal,
    String moneda,
    String cuentaCut,
    String entidadRecaudadora,
    String estadoProcesamiento,
    String numeroSolicitud,
    OffsetDateTime fechaRegistro,
    List<BeneficiarioDetalle> beneficiarios
) {
    public record BeneficiarioDetalle(
        String codigo,
        String cuentaRegistro,
        String descripcion,
        BigDecimal montoTotalBeneficiario,
        List<IngresoDetalle> ingresos
    ) {}

    public record IngresoDetalle(String codigo, BigDecimal monto) {}
}
