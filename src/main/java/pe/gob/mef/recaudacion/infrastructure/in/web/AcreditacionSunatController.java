package pe.gob.mef.recaudacion.infrastructure.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import pe.gob.mef.recaudacion.application.port.in.RecibirArchivoUseCase;
import pe.gob.mef.recaudacion.domain.model.ArchivoAcreditacion;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ArchivoAcreditacionResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ErrorResponse;

@Tag(name = "AcreditacionSUNAT")
@RestController
@RequestMapping("${sunat.acreditacion.base-path}${sunat.acreditacion.endpoints.archivo}")
public class AcreditacionSunatController {

    private final RecibirArchivoUseCase recibirArchivoUseCase;

    public AcreditacionSunatController(RecibirArchivoUseCase recibirArchivoUseCase) {
        this.recibirArchivoUseCase = recibirArchivoUseCase;
    }

    @Operation(
        summary = "Recibir archivo de acreditación (simulación SUNAT)",
        description =
            "Recibe el archivo de acreditación en formato multipart/form-data con el nombre " +
            "ACR_MEF_SUNAT_AAAAMMDD.TXT. Este endpoint simula el servicio expuesto por SUNAT " +
            "para el proceso de conciliación de ingresos tributarios.",
        operationId = "recibirArchivoAcreditacion")
    @ApiResponses({
        @ApiResponse(responseCode = "202", description = "Archivo recibido y encolado para procesamiento.",
            content = @Content(schema = @Schema(implementation = ArchivoAcreditacionResponse.class))),
        @ApiResponse(responseCode = "400", description = "Nombre de archivo inválido.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "401", description = "No autorizado.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "409", description = "El archivo ya fue recibido anteriormente.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
        @ApiResponse(responseCode = "500", description = "Error interno del servidor.",
            content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ArchivoAcreditacionResponse> recibirArchivo(
            @RequestParam("archivo") MultipartFile archivo) {
        ArchivoAcreditacion resultado = recibirArchivoUseCase.recibir(archivo.getOriginalFilename());
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(new ArchivoAcreditacionResponse(
            resultado.idCorrelacion(),
            resultado.estado(),
            "Archivo recibido correctamente y enviado a procesamiento.",
            resultado.fechaRecepcion(),
            resultado.nombreArchivo()
        ));
    }
}
