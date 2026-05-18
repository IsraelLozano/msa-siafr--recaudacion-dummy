package pe.gob.mef.recaudacion.infrastructure.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Filtro de auditoría: registra cada request a /api/** con IP origen,
 * método HTTP, URI, status de respuesta y tiempo de procesamiento.
 * Los logs se escriben en el logger "AUDIT" (archivo audit-access.log).
 */
@Component
@Order(1)
public class AuditLogFilter extends OncePerRequestFilter {

    private static final Logger AUDIT = LoggerFactory.getLogger("AUDIT");

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String uri = request.getRequestURI();
        // Solo auditar endpoints de negocio; excluir swagger, actuator, oauth2
        return !uri.startsWith("/api/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long inicio = System.currentTimeMillis();

        try {
            filterChain.doFilter(request, response);
        } finally {
            long duracion = System.currentTimeMillis() - inicio;
            String ip = resolverIpOrigen(request);
            String metodo = request.getMethod();
            String uri = request.getRequestURI();
            String query = request.getQueryString() != null ? "?" + request.getQueryString() : "";
            int status = response.getStatus();

            AUDIT.info("ip={} method={} uri={}{} status={} duration={}ms",
                    ip, metodo, uri, query, status, duracion);
        }
    }

    /**
     * Resuelve la IP real del cliente considerando proxies y API Gateways (WSO2).
     * Orden de precedencia: X-Forwarded-For → X-Real-IP → RemoteAddr
     */
    private String resolverIpOrigen(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isBlank()) {
            // X-Forwarded-For puede contener cadena: "ip-cliente, proxy1, proxy2"
            return xForwardedFor.split(",")[0].trim();
        }
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isBlank()) {
            return xRealIp.trim();
        }
        return request.getRemoteAddr();
    }
}
