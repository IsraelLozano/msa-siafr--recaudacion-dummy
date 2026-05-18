package pe.gob.mef.recaudacion;

import tools.jackson.databind.JsonNode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MsaServiceRecaudacionIntegrationTest {

    @LocalServerPort
    int port;

    RestClient restClient;

    @BeforeEach
    void setUp() {
        restClient = RestClient.builder()
            .baseUrl("http://localhost:" + port)
            .build();
    }

    private String obtenerToken() {
        String credentials = Base64.getEncoder().encodeToString("sunat-client:sunat-secret-key".getBytes());

        ResponseEntity<JsonNode> response = restClient.post()
            .uri("/oauth2/token")
            .header("Authorization", "Basic " + credentials)
            .contentType(MediaType.APPLICATION_FORM_URLENCODED)
            .body("grant_type=client_credentials&scope=recaudacion.read")
            .retrieve()
            .toEntity(JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        return response.getBody().get("access_token").asText();
    }

    @Test
    void flujoCompleto_obtenerToken_yConsultarReportes() {
        String token = obtenerToken();

        ResponseEntity<JsonNode> response = restClient.get()
            .uri("/api/v1/reporte-recaudacion")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode content = response.getBody().get("content");
        assertThat(content.size()).isGreaterThanOrEqualTo(3);
    }

    @Test
    void flujoCompleto_obtenerToken_yRegistrarReporte() {
        String token = obtenerToken();

        String requestBody = """
            {
              "numeroReporteRecaudacion": "99999999",
              "fechaHoraEmision": "2025-08-05T14:30:00-05:00",
              "codigoEntidadFinanciera": "002",
              "montoTotal": 143264.52,
              "moneda": "PEN",
              "cuentaCut": "000392930212",
              "entidadRecaudadora": "20345678901",
              "beneficiarios": []
            }
            """;

        ResponseEntity<JsonNode> postResponse = restClient.post()
            .uri("/api/v1/reporte-recaudacion")
            .header("Authorization", "Bearer " + token)
            .contentType(MediaType.APPLICATION_JSON)
            .body(requestBody)
            .retrieve()
            .toEntity(JsonNode.class);

        assertThat(postResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(postResponse.getBody().get("numeroSolicitud")).isNotNull();
    }

    @Test
    void get_sinToken_debeRetornar401() {
        ResponseEntity<String> response = restClient.get()
            .uri("/api/v1/reporte-recaudacion")
            .retrieve()
            .onStatus(status -> status.is4xxClientError(), (req, res) -> {})
            .toEntity(String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
    }

    @Test
    void get_conFiltroNumeroRR_debeRetornarUno() {
        String token = obtenerToken();

        ResponseEntity<JsonNode> response = restClient.get()
            .uri("/api/v1/reporte-recaudacion?numeroReporteRecaudacion=02345678")
            .header("Authorization", "Bearer " + token)
            .retrieve()
            .toEntity(JsonNode.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        JsonNode pagination = response.getBody().get("pagination");
        assertThat(pagination.get("totalElements").asInt()).isEqualTo(1);
    }
}

