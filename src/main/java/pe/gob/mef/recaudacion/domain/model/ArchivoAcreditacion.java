package pe.gob.mef.recaudacion.domain.model;

import java.time.OffsetDateTime;

public record ArchivoAcreditacion(
    String idCorrelacion,
    String nombreArchivo,
    OffsetDateTime fechaRecepcion,
    String estado
) {}
