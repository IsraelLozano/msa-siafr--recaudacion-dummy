package pe.gob.mef.recaudacion.infrastructure.in.web;

import tools.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pe.gob.mef.recaudacion.application.port.in.ConsultarReporteUseCase;
import pe.gob.mef.recaudacion.application.port.in.RegistrarReporteUseCase;
import pe.gob.mef.recaudacion.domain.model.EstadoProcesamiento;
import pe.gob.mef.recaudacion.domain.model.ReporteRecaudacion;
import pe.gob.mef.recaudacion.infrastructure.in.web.dto.RegistrarReporteRequest;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class ReporteRecaudacionControllerTest {

    @Autowired
    WebApplicationContext context;

    @Autowired
    ObjectMapper objectMapper;

    @MockitoBean
    RegistrarReporteUseCase registrarUseCase;

    @MockitoBean
    ConsultarReporteUseCase consultarUseCase;

    private MockMvc mockMvc() {
        return MockMvcBuilders.webAppContextSetup(context)
            .apply(springSecurity())
            .build();
    }

    @Test
    void post_debeRetornar201_conToken() throws Exception {
        RegistrarReporteRequest request = new RegistrarReporteRequest(
            "02345678",
            OffsetDateTime.parse("2025-08-05T14:30:00-05:00"),
            "002",
            new BigDecimal("143264.52"),
            "PEN",
            "000392930212",
            "20345678901",
            List.of()
        );

        ReporteRecaudacion rrGuardado = ReporteRecaudacion.builder()
            .numeroSolicitud("sol-001")
            .estadoProcesamiento(EstadoProcesamiento.REGISTRADO)
            .fechaRegistro(OffsetDateTime.now())
            .build();

        when(registrarUseCase.registrar(any())).thenReturn(rrGuardado);

        mockMvc().perform(post("/api/v1/reporte-recaudacion")
                .with(jwt())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.estado").value("REGISTRADO"));
    }

    @Test
    void post_debeRetornar401_sinToken() throws Exception {
        mockMvc().perform(post("/api/v1/reporte-recaudacion")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().isUnauthorized());
    }

    @Test
    void get_debeRetornar200_conListaPaginada() throws Exception {
        ReporteRecaudacion rr = ReporteRecaudacion.builder()
            .numeroReporteRecaudacion("02345678")
            .estadoProcesamiento(EstadoProcesamiento.PROCESADO)
            .numeroSolicitud("9987654321")
            .build();

        when(consultarUseCase.consultar(any(), any(), any(), any()))
            .thenReturn(List.of(rr));

        mockMvc().perform(get("/api/v1/reporte-recaudacion")
                .with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.content").isArray())
            .andExpect(jsonPath("$.pagination.totalElements").value(1));
    }

    @Test
    void get_conFiltroNumeroRR_debePasarParametroAlUseCase() throws Exception {
        when(consultarUseCase.consultar("02345678", null, null, null))
            .thenReturn(List.of());

        mockMvc().perform(get("/api/v1/reporte-recaudacion")
                .param("numeroReporteRecaudacion", "02345678")
                .with(jwt()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.pagination.totalElements").value(0));
    }
}
