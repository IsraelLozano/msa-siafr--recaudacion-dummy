package pe.gob.mef.recaudacion.application.port.in;

import pe.gob.mef.recaudacion.domain.model.ArchivoAcreditacion;

public interface RecibirArchivoUseCase {
    ArchivoAcreditacion recibir(String nombreArchivo);
}
