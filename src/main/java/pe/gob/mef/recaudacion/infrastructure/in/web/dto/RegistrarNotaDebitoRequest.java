package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Schema(description = "Payload para registrar una nota de débito.")
public record RegistrarNotaDebitoRequest(
    @Schema(description = "Número del reporte de recaudación relacionado.", example = "02345678")
    String numeroReporteRecaudacion,

    @Schema(description = "Fecha y hora de emisión de la nota de débito.", example = "2025-08-05T14:30:00-05:00")
    OffsetDateTime fechaHoraEmision,

    @Schema(description = "RUC de la entidad recaudadora.", example = "20345678901")
    String rucEntidadRecaudadora,

    @Schema(description = "Código de la entidad financiera.", example = "BCP")
    String codigoEntidadFinanciera,

    @Schema(description = "Monto total de la nota de débito.", example = "35.50")
    BigDecimal montoTotal,

    @Schema(description = "Código de moneda ISO 4217.", example = "PEN")
    String moneda,

    @Schema(description = "Cuenta bancaria CUT asociada.", example = "000392930212")
    String cuentaBancariaCut,

    @Schema(description = "Cuenta de cargo de la operación.")
    CargoRequest cargo,

    @Schema(description = "Cuenta de abono de la operación.")
    AbonoRequest abono,

    @Schema(description = "Concepto contable o presupuestal asociado.")
    ConceptoRequest concepto
) {
    @Schema(description = "Cuenta de cargo de la operación.")
    public record CargoRequest(
        @Schema(description = "Nombre o razón social del titular de la cuenta de cargo.", example = "SUNAT")
        String propietario,

        @Schema(description = "Número de cuenta bancaria de cargo.", example = "000764560546")
        String cuentaBancaria
    ) {}

    @Schema(description = "Cuenta de abono de la operación.")
    public record AbonoRequest(
        @Schema(description = "Nombre o razón social del titular de la cuenta de abono.", example = "BCP")
        String propietario,

        @Schema(description = "Número de cuenta bancaria de abono.", example = "000764560546")
        String cuentaBancaria
    ) {}

    @Schema(description = "Concepto contable o presupuestal asociado.")
    public record ConceptoRequest(
        @Schema(description = "Código del concepto.", example = "2.3.2.6.2.1")
        String codigo,

        @Schema(description = "Importe asociado al concepto.", example = "35.50")
        BigDecimal importe
    ) {}
}
