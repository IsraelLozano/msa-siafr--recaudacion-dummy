package pe.gob.mef.recaudacion.infrastructure.in.web;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.gob.mef.recaudacion.application.port.in.ProcesarConciliacionUseCase;
import pe.gob.mef.recaudacion.domain.model.ConciliacionBancaria;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ConciliacionUploadRequest;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ConciliacionUploadResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ErrorResponse;
import pe.gob.mef.recaudacion.infrastructure.out.file.ConciliacionFileWriter;

/**
 * Endpoint mock que simula el servicio expuesto por SUNAT para recibir
 * el archivo plano de conciliación bancaria enviado por el SIAF-RP (MEF).
 *
 * <p>
 * Referencia RF3 – Interface Externa Nro. 01:
 * {@code POST /api/conciliacion-sunat/reporte-diario/upload}
 *
 * <p>
 * Flujo de trazabilidad:
 * <ol>
 * <li>Valida nombre de archivo y mimeType.</li>
 * <li>Decodifica el contenido base64 y cuenta las líneas del archivo
 * plano.</li>
 * <li>Persiste la trama completa en memoria y en archivos de trazabilidad
 * ({@code .jsonl} de metadatos + {@code .txt} original decodificado).</li>
 * <li>Retorna HTTP 201 con número de solicitud y resumen de la recepción.</li>
 * </ol>
 */
@Tag(name = "ConciliacionBancaria")
@RestController
@RequestMapping("${api.conciliacion-path}")
public class ConciliacionController {

    private final ProcesarConciliacionUseCase procesarUseCase;
    private final ConciliacionFileWriter fileWriter;

    public ConciliacionController(ProcesarConciliacionUseCase procesarUseCase,
            ConciliacionFileWriter fileWriter) {
        this.procesarUseCase = procesarUseCase;
        this.fileWriter = fileWriter;
    }

    @Operation(summary = "Enviar archivo de conciliación bancaria (RF3)", description = """
            Recibe el archivo plano de conciliación bancaria enviado por el SIAF-RP al servicio SUNAT.
            Referencia: RF3 – Interface Externa Nro. 01 – "Enviar información para Conciliación Bancaria de Tesoro".

            El archivo viaja codificado en Base64 dentro del body JSON. El servicio:
            - Valida el nombre del archivo (patrón: ACR_MEF_<Negocio>_<aaaammdd>.txt).
            - Valida que el mimeType sea text/plain.
            - Decodifica el contenido Base64 y cuenta las líneas registradas.
            - Persiste los metadatos en conciliaciones-recibidas.jsonl y el archivo .txt decodificado.
            - Retorna el número de solicitud y el total de líneas como confirmación.

            Ventana horaria acordada con SUNAT: 00:00 – 00:30 h.
            Fuera de ventana: la llamada se acepta pero se registra una advertencia en los logs.""", operationId = "enviarArchivoConciliacion")
    @PostMapping
    public ResponseEntity<ConciliacionUploadResponse> upload(@RequestBody ConciliacionUploadRequest request) {

        ConciliacionBancaria conciliacion = ConciliacionBancaria.builder()
                .fileName(request.fileName())
                .mimeType(request.mimeType())
                .contenidoBase64(request.fileContent())
                .build();

        ConciliacionBancaria procesada = procesarUseCase.procesar(conciliacion);
        fileWriter.guardar(procesada);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ConciliacionUploadResponse(
                        procesada.getNumeroSolicitud(),
                        procesada.getEstado().name(),
                        procesada.getFechaRecepcion(),
                        procesada.getFileName(),
                        procesada.getTotalLineas()));
    }
}
