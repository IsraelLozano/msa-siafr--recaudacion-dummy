package pe.gob.mef.recaudacion.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.mef.recaudacion.application.port.out.ReporteRecaudacionRepository;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegistrarReporteUseCaseImplTest {

    @Mock
    ReporteRecaudacionRepository repository;

    @InjectMocks
    RegistrarReporteUseCaseImpl useCase;

    @Test
    void registrar_debeAsignarEstadoRegistradoYNumeroSolicitud() {
        ReporteRecaudacion input = ReporteRecaudacion.builder()
            .numeroReporteRecaudacion("02345678")
            .codigoEntidadFinanciera("002")
            .montoTotal(new BigDecimal("143264.52"))
            .moneda("PEN")
            .cuentaCut("000392930212")
            .entidadRecaudadora("20345678901")
            .beneficiarios(List.of())
            .build();

        when(repository.guardar(any())).thenAnswer(inv -> inv.getArgument(0));

        ReporteRecaudacion result = useCase.registrar(input);

        assertThat(result.getEstadoProcesamiento()).isEqualTo(EstadoProcesamiento.REGISTRADO);
        assertThat(result.getNumeroSolicitud()).isNotBlank();
        assertThat(result.getFechaRegistro()).isNotNull();
    }
}
