package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

@Schema(description = "Payload para registrar un reporte de recaudación.")
public record RegistrarReporteRequest(
    @Schema(description = "Número único del reporte de recaudación.", example = "02345678")
    String numeroReporteRecaudacion,

    @Schema(description = "Fecha y hora de emisión del reporte.", example = "2025-08-05T14:30:00-05:00")
    OffsetDateTime fechaHoraEmision,

    @Schema(description = "Código de la entidad financiera recaudadora.", example = "002")
    String codigoEntidadFinanciera,

    @Schema(description = "Monto total del reporte.", example = "100000.00")
    BigDecimal montoTotal,

    @Schema(description = "Código de moneda ISO 4217.", example = "PEN")
    String moneda,

    @Schema(description = "Cuenta CUT vinculada al reporte.", example = "000392930212")
    String cuentaCut,

    @Schema(description = "RUC de la entidad recaudadora.", example = "20345678901")
    String entidadRecaudadora,

    @Schema(description = "Lista de beneficiarios del reporte. Mínimo un elemento.")
    List<BeneficiarioRequest> beneficiarios
) {
    @Schema(description = "Información del beneficiario del reporte.")
    public record BeneficiarioRequest(
        @Schema(description = "Código del beneficiario.", example = "1234")
        String codigo,

        @Schema(description = "Cuenta de registro del beneficiario.", example = "12345678")
        String cuentaRegistro,

        @Schema(description = "Descripción del beneficiario.", example = "Beneficiario principal")
        String descripcion,

        @Schema(description = "Monto total asignado al beneficiario.", example = "100000.00")
        BigDecimal montoTotalBeneficiario,

        @Schema(description = "Ingresos asociados al beneficiario.")
        List<IngresoRequest> ingresos
    ) {}

    @Schema(description = "Ingreso asociado al beneficiario.")
    public record IngresoRequest(
        @Schema(description = "Código del ingreso.", example = "11.22.33.44")
        String codigo,

        @Schema(description = "Monto del ingreso.", example = "20000.00")
        BigDecimal monto
    ) {}
}
