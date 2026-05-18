package pe.gob.mef.recaudacion.infrastructure.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import pe.gob.mef.recaudacion.domain.exception.ArchivoNombreInvalidoException;
import pe.gob.mef.recaudacion.domain.exception.ArchivoYaRecibidoException;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ErrorResponse;

import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ArchivoNombreInvalidoException.class)
    public ResponseEntity<ErrorResponse> handleArchivoNombreInvalido(ArchivoNombreInvalidoException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("ERR-400", ex.getMessage(), List.of()));
    }

    @ExceptionHandler(ArchivoYaRecibidoException.class)
    public ResponseEntity<ErrorResponse> handleArchivoYaRecibido(ArchivoYaRecibidoException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(new ErrorResponse("ERR-409", ex.getMessage(), List.of()));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
            .body(new ErrorResponse("ERR-400", ex.getMessage(), List.of()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneral(Exception ex) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ErrorResponse(
                "ERR-500",
                "Ocurrió un error interno al procesar la solicitud.",
                List.of()
            ));
    }
}
