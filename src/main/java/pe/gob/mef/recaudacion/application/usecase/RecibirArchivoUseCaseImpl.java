package pe.gob.mef.recaudacion.application.usecase;

import org.springframework.stereotype.Service;
import pe.gob.mef.recaudacion.application.port.in.RecibirArchivoUseCase;
import pe.gob.mef.recaudacion.application.port.out.ArchivoAcreditacionRepository;
import pe.gob.mef.recaudacion.domain.exception.ArchivoNombreInvalidoException;
import pe.gob.mef.recaudacion.domain.exception.ArchivoYaRecibidoException;
import pe.gob.mef.recaudacion.domain.model.ArchivoAcreditacion;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

@Service
public class RecibirArchivoUseCaseImpl implements RecibirArchivoUseCase {

    private static final Pattern NOMBRE_VALIDO =
        Pattern.compile("ACR_MEF_SUNAT_\\d{8}\\.TXT");

    private final ArchivoAcreditacionRepository repository;
    private final AtomicInteger secuencia = new AtomicInteger(0);

    public RecibirArchivoUseCaseImpl(ArchivoAcreditacionRepository repository) {
        this.repository = repository;
    }

    @Override
    public ArchivoAcreditacion recibir(String nombreArchivo) {
        if (nombreArchivo == null || !NOMBRE_VALIDO.matcher(nombreArchivo).matches()) {
            throw new ArchivoNombreInvalidoException(nombreArchivo == null ? "null" : nombreArchivo);
        }
        if (repository.existePorNombre(nombreArchivo)) {
            throw new ArchivoYaRecibidoException(nombreArchivo);
        }
        String fecha = LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE);
        String idCorrelacion = "ACRED-" + fecha + "-" + String.format("%06d", secuencia.incrementAndGet());
        ArchivoAcreditacion archivo = new ArchivoAcreditacion(
            idCorrelacion,
            nombreArchivo,
            OffsetDateTime.now(),
            "ACEPTADO"
        );
        return repository.guardar(archivo);
    }
}
