package pe.gob.mef.recaudacion.domain.model;

import java.math.BigDecimal;
import java.util.List;

public record Beneficiario(
    String codigo,
    String cuentaRegistro,
    String descripcion,
    BigDecimal montoTotalBeneficiario,
    List<Ingreso> ingresos
) {}
