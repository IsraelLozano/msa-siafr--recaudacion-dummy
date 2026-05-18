package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Respuesta a la recepción del archivo de acreditación.")
public record ArchivoAcreditacionResponse(
    @Schema(description = "Identificador de correlación generado para el archivo recibido.",
            example = "ACRED-20250805-000001")
    String idCorrelacion,

    @Schema(description = "Estado del procesamiento del archivo.", example = "RECIBIDO")
    String estado,

    @Schema(description = "Mensaje descriptivo de la operación.",
            example = "Archivo recibido correctamente y enviado a procesamiento.")
    String mensaje,

    @Schema(description = "Fecha y hora de recepción del archivo.", example = "2025-08-05T14:30:00-05:00")
    OffsetDateTime fechaRecepcion,

    @Schema(description = "Nombre del archivo recibido.", example = "ACR_MEF_SUNAT_20250805.TXT")
    String nombreArchivo
) {}
