package pe.gob.mef.recaudacion.domain.model;

import lombok.Builder;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
@Builder
public class ConciliacionBancaria {
    private String numeroSolicitud;
    private String fileName;
    private String mimeType;
    private String contenidoBase64;
    private int totalLineas;
    private EstadoProcesamiento estado;
    private OffsetDateTime fechaRecepcion;
}
