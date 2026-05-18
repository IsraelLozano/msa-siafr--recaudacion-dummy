package pe.gob.mef.recaudacion.infrastructure.in.web;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.mef.recaudacion.application.port.in.ConsultarReporteUseCase;
import pe.gob.mef.recaudacion.application.port.in.RegistrarReporteUseCase;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ConsultarReporteResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ErrorResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ListaReporteItem;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.PaginationResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistrarReporteRequest;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistroResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ReporteRecaudacionDetalleResponse;
import pe.gob.mef.recaudacion.infrastructure.out.file.ReporteRecaudacionFileWriter;

import java.time.LocalDate;
import java.util.List;

@Tag(name = "IngresosTributarios")
@RestController
@RequestMapping("${api.base-path}${api.endpoints.reporte-recaudacion}")
public class ReporteRecaudacionController {

    private final RegistrarReporteUseCase registrarUseCase;
    private final ConsultarReporteUseCase consultarUseCase;
    private final ReporteRecaudacionFileWriter fileWriter;

    public ReporteRecaudacionController(RegistrarReporteUseCase registrarUseCase,
            ConsultarReporteUseCase consultarUseCase,
            ReporteRecaudacionFileWriter fileWriter) {
        this.registrarUseCase = registrarUseCase;
        this.consultarUseCase = consultarUseCase;
        this.fileWriter = fileWriter;
    }

    @Operation(
        summary = "Registrar reporte de recaudación",
        description = "Registra un reporte de recaudación (nota de abono).",
        operationId = "registrarReporteRecaudacion")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Reporte de recaudación registrado correctamente.",
            content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistrarReporteRequest request) {
        ReporteRecaudacion rr = registrarUseCase.registrar(ReporteRecaudacionMapper.toDomain(request));
        fileWriter.guardar(rr);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ReporteRecaudacionMapper.toRegistroResponse(rr));
    }

    @Operation(
        summary = "Consultar reportes de recaudación",
        description = "Retorna la relación de reportes de recaudación según filtros de búsqueda.",
        operationId = "consultarReporteRecaudacion")
    @ApiResponses({
        @ApiResponse(responseCode = "200", description = "Consulta realizada correctamente.",
            content = @Content(schema = @Schema(implementation = ConsultarReporteResponse.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "404", description = "Recurso no encontrado.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping
    public ResponseEntity<ConsultarReporteResponse> consultar(
            @Parameter(description = "Número de reporte de recaudación.", example = "123456789")
            @RequestParam(required = false) String numeroReporteRecaudacion,
            @Parameter(description = "Número de solicitud.", example = "9987654321")
            @RequestParam(required = false) String numeroSolicitud,
            @Parameter(description = "Fecha inicial del rango de consulta.", example = "2026-02-20")
            @RequestParam(required = false) LocalDate fechaInicio,
            @Parameter(description = "Fecha final del rango de consulta.", example = "2026-02-26")
            @RequestParam(required = false) LocalDate fechaFin) {
        List<ReporteRecaudacion> resultados = consultarUseCase.consultar(
                numeroReporteRecaudacion, numeroSolicitud, fechaInicio, fechaFin);

        List<ListaReporteItem> items = resultados.stream()
                .map(ReporteRecaudacionMapper::toListaItem)
                .toList();

        PaginationResponse pagination = new PaginationResponse(
                true, true,
                items.isEmpty() ? 0 : 1,
                items.size(),
                10, 0);

        return ResponseEntity.ok(new ConsultarReporteResponse(items, pagination));
    }

    @Hidden
    @GetMapping("/todos")
    public ResponseEntity<List<ReporteRecaudacionDetalleResponse>> listarTodos() {
        List<ReporteRecaudacion> todos = consultarUseCase.consultar(null, null, null, null);
        return ResponseEntity.ok(
                todos.stream().map(ReporteRecaudacionMapper::toDetalleResponse).toList());
    }
}
