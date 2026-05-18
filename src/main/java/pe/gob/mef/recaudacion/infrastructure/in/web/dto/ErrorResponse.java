package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta estándar de error.")
public record ErrorResponse(
    @Schema(description = "Código de error.", example = "ERR-400")
    String code,

    @Schema(description = "Mensaje descriptivo del error.", example = "La solicitud contiene datos inválidos.")
    String message,

    @Schema(description = "Lista de detalles adicionales sobre el error.",
            example = "[\"El campo numeroReporteRecaudacion es obligatorio.\"]")
    List<String> details
) {}
