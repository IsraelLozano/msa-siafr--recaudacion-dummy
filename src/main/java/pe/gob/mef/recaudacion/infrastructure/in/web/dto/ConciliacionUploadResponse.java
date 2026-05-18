package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

/**
 * Respuesta del endpoint POST /api/conciliacion-sunat/reporte-diario/upload.
 *
 * <p>Confirma la recepción del archivo e informa los metadatos de trazabilidad
 * generados por el arquetipo: número de solicitud, estado, timestamp y total de
 * líneas detectadas en el archivo plano.
 */
@Schema(description = "Confirmación de recepción del archivo de conciliación bancaria.")
public record ConciliacionUploadResponse(

        @Schema(
            description = "Número único de solicitud generado por el servicio.",
            example = "a1b2c3d4e5"
        )
        String numeroSolicitud,

        @Schema(
            description = "Estado del procesamiento.",
            example = "REGISTRADO",
            allowableValues = "REGISTRADO"
        )
        String estado,

        @Schema(
            description = "Fecha y hora de recepción del archivo (ISO-8601 con offset).",
            example = "2026-05-11T00:15:00-05:00"
        )
        OffsetDateTime fechaRecepcion,

        @Schema(
            description = "Nombre del archivo plano recibido.",
            example = "ACR_MEF_SUNAT_20260511.txt"
        )
        String fileName,

        @Schema(
            description = "Total de líneas no vacías detectadas en el archivo plano decodificado.",
            example = "12"
        )
        int totalLineas) {
}
