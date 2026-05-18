package pe.gob.mef.recaudacion.infrastructure.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.mef.recaudacion.application.port.in.RegistrarNotaDebitoUseCase;
import pe.gob.mef.recaudacion.domain.model.NotaDebito;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ErrorResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistrarNotaDebitoRequest;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistroResponse;
import pe.gob.mef.recaudacion.infrastructure.out.file.ReporteRecaudacionFileWriter;

@Tag(name = "IngresosTributarios")
@RestController
@RequestMapping("${api.base-path}${api.endpoints.nota-debito}")
public class NotaDebitoController {

    private final RegistrarNotaDebitoUseCase registrarUseCase;
    private final ReporteRecaudacionFileWriter fileWriter;

    public NotaDebitoController(RegistrarNotaDebitoUseCase registrarUseCase,
                                ReporteRecaudacionFileWriter fileWriter) {
        this.registrarUseCase = registrarUseCase;
        this.fileWriter = fileWriter;
    }

    @Operation(
        summary = "Registrar nota de débito",
        description = "Registra una nota de débito asociada a un reporte de recaudación.",
        operationId = "registrarNotaDebito")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Nota de débito registrada correctamente.",
            content = @Content(schema = @Schema(implementation = RegistroResponse.class))),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping
    public ResponseEntity<RegistroResponse> registrar(@RequestBody RegistrarNotaDebitoRequest request) {
        NotaDebito nd = registrarUseCase.registrar(NotaDebitoMapper.toDomain(request));
        fileWriter.guardarNotaDebito(nd);
        return ResponseEntity.status(HttpStatus.CREATED).body(NotaDebitoMapper.toRegistroResponse(nd));
    }
}
