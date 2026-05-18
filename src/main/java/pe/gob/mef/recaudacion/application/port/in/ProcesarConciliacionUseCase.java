package pe.gob.mef.recaudacion.application.port.in;

import pe.gob.mef.recaudacion.domain.model.ConciliacionBancaria;

public interface ProcesarConciliacionUseCase {
    ConciliacionBancaria procesar(ConciliacionBancaria conciliacion);
}
