package pe.gob.mef.recaudacion.application.usecase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pe.gob.mef.recaudacion.application.port.out.ReporteRecaudacionRepository;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;

import java.time.OffsetDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ConsultarReporteUseCaseImplTest {

    @Mock
    ReporteRecaudacionRepository repository;

    @InjectMocks
    ConsultarReporteUseCaseImpl useCase;

    @Test
    void consultar_debeDelegarAlRepositorio() {
        ReporteRecaudacion rr = ReporteRecaudacion.builder()
            .numeroReporteRecaudacion("02345678")
            .estadoProcesamiento(EstadoProcesamiento.PROCESADO)
            .numeroSolicitud("9987654321")
            .fechaRegistro(OffsetDateTime.now())
            .build();

        when(repository.buscar("02345678", null, null, null)).thenReturn(List.of(rr));

        List<ReporteRecaudacion> result = useCase.consultar("02345678", null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getNumeroReporteRecaudacion()).isEqualTo("02345678");
    }
}
