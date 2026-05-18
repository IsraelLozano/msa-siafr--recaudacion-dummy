package pe.gob.mef.recaudacion.application.port.in;

import pe.gob.mef.recaudacion.domain.model.NotaDebito;

public interface RegistrarNotaDebitoUseCase {
    NotaDebito registrar(NotaDebito notaDebito);
}
