package pe.gob.mef.recaudacion.infrastructure.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .info(new Info()
                .title("Contrato de Integración MEF - SUNAT - Ingresos Tributarios")
                .description(
                    "Especificación OpenAPI para la interoperabilidad entre MEF y SUNAT " +
                    "correspondiente al módulo de ingresos tributarios. El contrato define " +
                    "operaciones para el registro y consulta de reportes de recaudación, " +
                    "el registro de notas de débito, y la recepción del archivo de acreditación.")
                .version("1.3.0")
                .contact(new Contact()
                    .name("Equipo de Integración MEF")
                    .email("angelnoriegamedori@mef.gob.pe")))
            .servers(List.of(
                new Server()
                    .url("https://api.mef.gob.pe/integraciones/sunat/v1")
                    .description("Endpoint de integración MEF - SUNAT"),
                new Server()
                    .url("http://localhost:443")
                    .description("Mock local (desarrollo)")))
            .tags(List.of(
                new Tag()
                    .name("IngresosTributarios")
                    .description("Operaciones del módulo de ingresos tributarios: " +
                        "registro y consulta de reportes de recaudación (notas de abono) " +
                        "y registro de notas de débito. " +
                        "Dirección: SUNAT llama a MEF."),
                new Tag()
                    .name("AcreditacionSUNAT")
                    .description(
                        "Simulación del endpoint SUNAT para recepción del archivo de acreditación " +
                        "(ACR_MEF_SUNAT_AAAAMMDD.TXT) en formato multipart/form-data. " +
                        "En producción este endpoint es expuesto por SUNAT; " +
                        "en este mock MEF lo simula para pruebas de conectividad. " +
                        "Dirección: SIAF-RP (MEF) llama a SUNAT."),
                new Tag()
                    .name("ConciliacionBancaria")
                    .description(
                        "Simulación del endpoint SUNAT para recepción del archivo plano de " +
                        "conciliación bancaria (RF3 – Interface Externa Nro. 01: " +
                        "\"Enviar información para Conciliación Bancaria de Tesoro\"). " +
                        "El archivo viaja codificado en Base64 dentro del body JSON. " +
                        "En producción este endpoint es expuesto por SUNAT; " +
                        "en este mock MEF lo simula para pruebas de conectividad. " +
                        "Dirección: SIAF-RP (MEF) llama a SUNAT. " +
                        "Ventana horaria acordada: 00:00 – 00:30 h.")))
            .addSecurityItem(new SecurityRequirement().addList("BearerAuth"))
            .components(new Components()
                .addSecuritySchemes("BearerAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT")
                    .description(
                        "OAuth2 Client Credentials (Spring Authorization Server embebido en el mock). " +
                        "Token endpoint: POST /oauth2/token con grant_type=client_credentials."))
                .addSecuritySchemes("ApiKeyAuth", new SecurityScheme()
                    .type(SecurityScheme.Type.APIKEY)
                    .in(SecurityScheme.In.HEADER)
                    .name("X-API-Key")
                    .description(
                        "API Key utilizada por MEF al consumir los endpoints reales de SUNAT en producción. " +
                        "No aplica en este mock.")));
    }
}
