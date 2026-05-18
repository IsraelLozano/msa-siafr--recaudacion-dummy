package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Elemento de la lista de reportes de recaudación.")
public record ListaReporteItem(
    @Schema(description = "Número del reporte de recaudación.", example = "02345678")
    String numeroReporteRecaudacion,

    @Schema(description = "Número de solicitud generado al registrar.", example = "9987654321")
    String numeroSolicitud,

    @Schema(description = "Estado de procesamiento del reporte.", example = "REGISTRADO")
    String estadoProcesamiento
) {}
