package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Metadatos de paginación de la respuesta.")
public record PaginationResponse(
    @Schema(description = "Indica si es la primera página.", example = "true")
    boolean first,

    @Schema(description = "Indica si es la última página.", example = "false")
    boolean last,

    @Schema(description = "Total de páginas disponibles.", example = "1")
    int totalPages,

    @Schema(description = "Total de elementos encontrados.", example = "1")
    int totalElements,

    @Schema(description = "Tamaño de la página.", example = "10")
    int pageSize,

    @Schema(description = "Número de página actual (base 0).", example = "0")
    int page
) {}
