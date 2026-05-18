package pe.gob.mef.recaudacion.domain.exception;

public class ArchivoYaRecibidoException extends RuntimeException {
    public ArchivoYaRecibidoException(String nombreArchivo) {
        super("El archivo " + nombreArchivo + " ya fue recibido anteriormente.");
    }
}
