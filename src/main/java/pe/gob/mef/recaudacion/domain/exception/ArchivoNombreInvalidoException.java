package pe.gob.mef.recaudacion.domain.exception;

public class ArchivoNombreInvalidoException extends RuntimeException {
    public ArchivoNombreInvalidoException(String nombreArchivo) {
        super("El nombre del archivo no cumple el formato requerido: " +
              "ACR_MEF_<Negocio>_<aaaammdd>.txt. Recibido: " + nombreArchivo);
    }
}
