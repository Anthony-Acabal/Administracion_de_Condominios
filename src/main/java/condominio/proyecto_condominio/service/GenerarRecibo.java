package condominio.proyecto_condominio.service;

import condominio.proyecto_condominio.dao.Conexion;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.net.URL;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class GenerarRecibo {

    private JasperReport cargarReporte() throws Exception {

        URL url = getClass().getResource(
                "/condominio/proyecto_condominio/reports/Recibo.jrxml"
        );

        if (url == null) {
            throw new RuntimeException("No se encontró el reporte Recibo.jrxml");
        }

        return JasperCompileManager.compileReport(url.openStream());
    }

    private JasperPrint llenarReporte(int idPropietario, int idPagoCuota) throws Exception {

        JasperReport report = cargarReporte();

        Map<String, Object> params = new HashMap<>();
        params.put("P_ID_PROPIETARIO", idPropietario);
        params.put("P_ID_PAGO_CUOTA", idPagoCuota);

        Connection conn = Conexion.getInstancia().getConnection();

        return JasperFillManager.fillReport(report, params, conn);
    }

    // 🖨️ SOLO MOSTRAR (lo que quieres ahora)
    public void mostrarRecibo(int idPropietario, int idPagoCuota) throws Exception {

        JasperPrint print = llenarReporte(idPropietario, idPagoCuota);

        JasperViewer viewer = new JasperViewer(print, false);
        viewer.setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        viewer.setZoomRatio(0.5f);
        viewer.setVisible(true);
    }

    // 💾 opcional: exportar PDF
    public String exportarPDF(int idPropietario, int idPagoCuota) throws Exception {

        JasperPrint print = llenarReporte(idPropietario, idPagoCuota);

        String ruta = "recibo_" + idPropietario + "_" + idPagoCuota + ".pdf";

        JasperExportManager.exportReportToPdfFile(print, ruta);

        return ruta;
    }
}
