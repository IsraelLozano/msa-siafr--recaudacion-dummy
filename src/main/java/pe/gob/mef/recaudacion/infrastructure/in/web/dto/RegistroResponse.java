package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.OffsetDateTime;

@Schema(description = "Respuesta estándar de registro exitoso.")
public record RegistroResponse(
    @Schema(description = "Número de solicitud generado.", example = "12345")
    String numeroSolicitud,

    @Schema(description = "Estado del registro.", example = "REGISTRADO")
    String estado,

    @Schema(description = "Fecha y hora en que se registró la solicitud.", example = "2026-01-17T10:15:30-05:00")
    OffsetDateTime fechaRegistro
) {}
