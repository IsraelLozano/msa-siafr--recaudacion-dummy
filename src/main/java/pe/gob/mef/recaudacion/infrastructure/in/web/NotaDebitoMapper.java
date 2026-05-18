package pe.gob.mef.recaudacion.infrastructure.in.web;

import pe.gob.mef.recaudacion.domain.model.NotaDebito;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistrarNotaDebitoRequest;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistroResponse;

final class NotaDebitoMapper {

    private NotaDebitoMapper() {}

    static NotaDebito toDomain(RegistrarNotaDebitoRequest req) {
        return NotaDebito.builder()
            .numeroReporteRecaudacion(req.numeroReporteRecaudacion())
            .fechaHoraEmision(req.fechaHoraEmision())
            .rucEntidadRecaudadora(req.rucEntidadRecaudadora())
            .codigoEntidadFinanciera(req.codigoEntidadFinanciera())
            .montoTotal(req.montoTotal())
            .moneda(req.moneda())
            .cuentaBancariaCut(req.cuentaBancariaCut())
            .cargoPropietario(req.cargo() != null ? req.cargo().propietario() : null)
            .cargoCuentaBancaria(req.cargo() != null ? req.cargo().cuentaBancaria() : null)
            .abonoPropietario(req.abono() != null ? req.abono().propietario() : null)
            .abonoCuentaBancaria(req.abono() != null ? req.abono().cuentaBancaria() : null)
            .conceptoCodigo(req.concepto() != null ? req.concepto().codigo() : null)
            .conceptoImporte(req.concepto() != null ? req.concepto().importe() : null)
            .build();
    }

    static RegistroResponse toRegistroResponse(NotaDebito nd) {
        return new RegistroResponse(
            nd.getNumeroSolicitud(),
            nd.getEstado().name(),
            nd.getFechaRegistro()
        );
    }
}
