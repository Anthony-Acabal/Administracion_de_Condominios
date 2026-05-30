package condominio.proyecto_condominio.service;

import condominio.proyecto_condominio.dao.Conexion;
import net.sf.jasperreports.engine.*;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReporteService {

    public String generarRecibo(int idPropietario, int idPagoCuota) throws Exception {

        // 🔥 Cargar recurso correctamente (NO InputStream directo)
        URL url = getClass().getResource(
                "/condominio/proyecto_condominio/reports/Recibo.jrxml"
        );

        System.out.println("JRXML URL: " + url);

        if (url == null) {
            throw new RuntimeException("No se encontró el JRXML en classpath");
        }

        // 🔥 Compilar desde URL (más estable que InputStream en JavaFX modular)
        JasperReport jasperReport = JasperCompileManager.compileReport(
                url.openStream()
        );

        System.out.println("Enviando correo a: " + idPagoCuota);

        // 🔥 Parámetros del reporte
        Map<String, Object> parametros = new HashMap<>();
        parametros.put("P_ID_PROPIETARIO", idPropietario);
        parametros.put("P_ID_PAGO_CUOTA", idPagoCuota);

        // 🔥 Conexión BD
        Connection conn = Conexion.getInstancia().getConnection();

        // 🔥 Llenar reporte
        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parametros,
                conn
        );

        // 🔥 PDF de salida dinámico
        String rutaPDF = "recibo_" + idPropietario + ".pdf";

        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaPDF);

        return rutaPDF;
    }

    public String generarReportePagosPendientes(int mes, int anio) throws Exception {

        URL url = getClass().getResource(
                "/condominio/proyecto_condominio/reports/CasasPendientePago.jrxml"
        );

        if (url == null) {
            throw new RuntimeException("No se encontró CasasPendientePago.jrxml");
        }

        JasperReport jasperReport
                = JasperCompileManager.compileReport(url.openStream());

        Map<String, Object> parametros = new HashMap<>();

        parametros.put("P_MES", mes == 0 ? null : mes);
        parametros.put("P_ANIO", anio);

        Connection conn = Conexion.getInstancia().getConnection();

        JasperPrint jasperPrint = JasperFillManager.fillReport(
                jasperReport,
                parametros,
                conn
        );

        String rutaPDF = "pagos_pendientes_" + anio + "_" + mes + ".pdf";

        JasperExportManager.exportReportToPdfFile(jasperPrint, rutaPDF);

        return rutaPDF;
    }
}
