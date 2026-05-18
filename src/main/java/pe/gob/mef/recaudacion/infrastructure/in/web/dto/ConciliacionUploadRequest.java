package pe.gob.mef.recaudacion.infrastructure.in.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request para el endpoint POST /api/conciliacion-sunat/reporte-diario/upload.
 *
 * <p>El SIAF-RP envía el archivo plano de conciliación bancaria codificado en base64.
 * El nombre del archivo debe seguir el patrón: {@code ACR_MEF_<Negocio>_<aaaammdd>.txt}
 * (Ej: {@code ACR_MEF_SUNAT_20260511.txt}).
 *
 * <p>Estructura del archivo (delimitado por punto y coma):
 * <pre>
 *   codigoTransaccion(3) ; nroNotaAbono(8) ; nroCuentaCorriente(10) ;
 *   fechaPago(aaaammdd) ; agencia(4) ; signo(1) ; importe(15)
 * </pre>
 */
@Schema(description = "Payload para enviar el archivo plano de conciliación bancaria al servicio SUNAT (RF3).")
public record ConciliacionUploadRequest(

        @Schema(
            description = "Nombre del archivo plano. Debe seguir el patrón ACR_MEF_<Negocio>_<aaaammdd>.txt.",
            example = "ACR_MEF_SUNAT_20260511.txt",
            pattern = "ACR_MEF_[A-Z0-9]+_\\d{8}\\.txt"
        )
        String fileName,

        @Schema(
            description = """
                Contenido del archivo plano codificado en Base64 (RFC 4648).
                El archivo está delimitado por punto y coma (;). Estructura por línea:
                  codTransaccion(3) ; nroNotaAbono(8) ; nroCuentaCorriente(10) ;
                  fechaPago(aaaammdd) ; agencia(4) ; signo(1) ; importe(15 dígitos).
                Códigos de transacción:
                  000 = Saldo de cuenta al cierre del día (un único registro por cuenta).
                  061 = Nota de abono informada (agencia: 1613 - SEC. INFORMES RECAUDACIÓN).
                  602 = Suma total de abonos del día (agencia: 1752 - SEC. EGRESOS DEL TESORO).""",
            example = "MDAwOzAwMDAwMDAwO1hYWFhYWFgxNjc7MDAwMDAwMDA7MDAwMDsrMDAwMDAwMDAwMDAwMDAwOw=="
        )
        String fileContent,

        @Schema(
            description = "Tipo MIME del archivo. Debe ser text/plain.",
            example = "text/plain",
            allowableValues = "text/plain"
        )
        String mimeType) {
}
