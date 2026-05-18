package pe.gob.mef.recaudacion.application.usecase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import pe.gob.mef.recaudacion.application.port.in.ProcesarConciliacionUseCase;
import pe.gob.mef.recaudacion.application.port.out.ConciliacionRepository;
import pe.gob.mef.recaudacion.domain.exception.ArchivoNombreInvalidoException;
import pe.gob.mef.recaudacion.domain.model.ConciliacionBancaria;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;

import java.nio.charset.StandardCharsets;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.util.Base64;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class ProcesarConciliacionUseCaseImpl implements ProcesarConciliacionUseCase {

    private static final Logger log = LoggerFactory.getLogger(ProcesarConciliacionUseCaseImpl.class);

    /**
     * Patrón de nombre de archivo definido en el RF3:
     * ACR_MEF_<Negocio>_<aaaammdd>.txt
     * Ejemplo: ACR_MEF_SUNAT_20260615.txt
     */
    private static final Pattern FILE_NAME_PATTERN =
            Pattern.compile("ACR_MEF_[A-Z0-9]+_\\d{8}\\.txt", Pattern.CASE_INSENSITIVE);

    /** Ventana horaria permitida según RF3: 00:00 – 00:30 */
    private static final LocalTime HORA_INICIO = LocalTime.of(0, 0);
    private static final LocalTime HORA_FIN    = LocalTime.of(0, 30);

    private final ConciliacionRepository repository;

    public ProcesarConciliacionUseCaseImpl(ConciliacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ConciliacionBancaria procesar(ConciliacionBancaria conciliacion) {
        validarNombreArchivo(conciliacion.getFileName());
        validarMimeType(conciliacion.getMimeType());
        advertirVentanaHoraria();

        int totalLineas = contarLineas(conciliacion.getContenidoBase64());

        ConciliacionBancaria procesada = ConciliacionBancaria.builder()
                .numeroSolicitud(UUID.randomUUID().toString().replace("-", "").substring(0, 10))
                .fileName(conciliacion.getFileName())
                .mimeType(conciliacion.getMimeType())
                .contenidoBase64(conciliacion.getContenidoBase64())
                .totalLineas(totalLineas)
                .estado(EstadoProcesamiento.REGISTRADO)
                .fechaRecepcion(OffsetDateTime.now())
                .build();

        return repository.guardar(procesada);
    }

    // -------------------------------------------------------------------------
    // Validaciones
    // -------------------------------------------------------------------------

    private void validarNombreArchivo(String fileName) {
        if (fileName == null || !FILE_NAME_PATTERN.matcher(fileName).matches()) {
            throw new ArchivoNombreInvalidoException(fileName != null ? fileName : "<null>");
        }
    }

    private void validarMimeType(String mimeType) {
        if (!"text/plain".equalsIgnoreCase(mimeType)) {
            throw new IllegalArgumentException(
                    "El mimeType debe ser 'text/plain'. Recibido: " + mimeType);
        }
    }

    /**
     * El RF3 establece que el envío ocurre entre las 00:00 y 00:30 h.
     * En el arquetipo dummy se registra una advertencia pero no se rechaza la llamada,
     * para no bloquear pruebas de conectividad en cualquier horario.
     */
    private void advertirVentanaHoraria() {
        LocalTime ahora = LocalTime.now();
        if (ahora.isBefore(HORA_INICIO) || ahora.isAfter(HORA_FIN)) {
            log.warn("[conciliacion] Llamada recibida fuera de la ventana horaria permitida (00:00-00:30). " +
                     "Hora actual: {}. En producción esta solicitud sería rechazada.", ahora);
        }
    }

    // -------------------------------------------------------------------------
    // Utilidades
    // -------------------------------------------------------------------------

    private int contarLineas(String base64Content) {
        try {
            byte[] decoded = Base64.getDecoder().decode(base64Content);
            String text = new String(decoded, StandardCharsets.UTF_8);
            return (int) text.lines().filter(l -> !l.isBlank()).count();
        } catch (Exception e) {
            log.warn("[conciliacion] No se pudo decodificar base64 para contar líneas: {}", e.getMessage());
            return 0;
        }
    }
}
