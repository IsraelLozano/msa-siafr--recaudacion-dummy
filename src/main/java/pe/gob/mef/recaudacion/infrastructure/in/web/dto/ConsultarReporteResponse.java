package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@Schema(description = "Respuesta de consulta de reportes de recaudación con paginación.")
public record ConsultarReporteResponse(
    @Schema(description = "Lista de reportes que coinciden con los filtros de búsqueda.")
    List<ListaReporteItem> content,

    @Schema(description = "Metadatos de paginación.")
    PaginationResponse pagination
) {}
