package pe.gob.mef.recaudacion.infrastructure.in.web;

import pe.gob.mef.recaudacion.domain.model.Beneficiario;
import pe.gob.mef.recaudacion.domain.model.Ingreso;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ListaReporteItem;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistrarReporteRequest;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistroResponse;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.ReporteRecaudacionDetalleResponse;

import java.util.List;

final class ReporteRecaudacionMapper {

        private ReporteRecaudacionMapper() {
        }

        static ReporteRecaudacion toDomain(RegistrarReporteRequest req) {
                List<Beneficiario> beneficiarios = req.beneficiarios() == null ? List.of()
                                : req.beneficiarios().stream()
                                                .map(b -> new Beneficiario(
                                                                b.codigo(),
                                                                b.cuentaRegistro(),
                                                                b.descripcion(),
                                                                b.montoTotalBeneficiario(),
                                                                b.ingresos() == null ? List.of()
                                                                                : b.ingresos().stream()
                                                                                                .map(i -> new Ingreso(i
                                                                                                                .codigo(),
                                                                                                                i.monto()))
                                                                                                .toList()))
                                                .toList();

                return ReporteRecaudacion.builder()
                                .numeroReporteRecaudacion(req.numeroReporteRecaudacion())
                                .fechaHoraEmision(req.fechaHoraEmision())
                                .codigoEntidadFinanciera(req.codigoEntidadFinanciera())
                                .montoTotal(req.montoTotal())
                                .moneda(req.moneda())
                                .cuentaCut(req.cuentaCut())
                                .entidadRecaudadora(req.entidadRecaudadora())
                                .beneficiarios(beneficiarios)
                                .build();
        }

        static RegistroResponse toRegistroResponse(ReporteRecaudacion rr) {
                return new RegistroResponse(
                                rr.getNumeroSolicitud(),
                                rr.getEstadoProcesamiento().name(),
                                rr.getFechaRegistro());
        }

        static ListaReporteItem toListaItem(ReporteRecaudacion rr) {
                return new ListaReporteItem(
                                rr.getNumeroReporteRecaudacion(),
                                rr.getNumeroSolicitud(),
                                rr.getEstadoProcesamiento().name());
        }

        static ReporteRecaudacionDetalleResponse toDetalleResponse(ReporteRecaudacion rr) {
                List<ReporteRecaudacionDetalleResponse.BeneficiarioDetalle> bens = rr.getBeneficiarios() == null
                                ? List.of()
                                : rr.getBeneficiarios().stream()
                                                .map(b -> new ReporteRecaudacionDetalleResponse.BeneficiarioDetalle(
                                                                b.codigo(),
                                                                b.cuentaRegistro(),
                                                                b.descripcion(),
                                                                b.montoTotalBeneficiario(),
                                                                b.ingresos() == null ? List.of()
                                                                                : b.ingresos().stream()
                                                                                                .map(i -> new ReporteRecaudacionDetalleResponse.IngresoDetalle(
                                                                                                                i.codigo(),
                                                                                                                i.monto()))
                                                                                                .toList()))
                                                .toList();
                return new ReporteRecaudacionDetalleResponse(
                                rr.getNumeroReporteRecaudacion(),
                                rr.getFechaHoraEmision(),
                                rr.getCodigoEntidadFinanciera(),
                                rr.getMontoTotal(),
                                rr.getMoneda(),
                                rr.getCuentaCut(),
                                rr.getEntidadRecaudadora(),
                                rr.getEstadoProcesamiento() != null ? rr.getEstadoProcesamiento().name() : null,
                                rr.getNumeroSolicitud(),
                                rr.getFechaRegistro(),
                                bens);
        }
}
