package condominio.proyecto_condominio.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import condominio.proyecto_condominio.model.HistorialPago;

public class HistorialPagoDAO {

    public List<HistorialPago> filtrarPagos(Connection conn,
            String casa,
            String mes,
            String anio) {

        List<HistorialPago> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.numero_casa,
                p.primer_nombre + ' ' + p.segundo_nombre + ' ' +
                p.primer_apellido + ' ' + p.segundo_apellido AS propietario,
                pc.fecha_pago,
                c.cuota,
                pc.imprime_comprobante
            FROM pago_cuota pc
            INNER JOIN propietario p ON pc.id_propietario = p.id_propietario
            INNER JOIN cuota c ON pc.id_cuota = c.id_cuota
            WHERE 1=1
        """;

        if (casa != null && !casa.equals("Todas las casas")) {
            sql += " AND p.numero_casa = ?";
        }

        if (mes != null && !mes.equals("Todos los meses")) {
            sql += " AND MONTH(pc.fecha_pago) = ?";
        }

        if (anio != null && !anio.equals("Todos los años")) {
            sql += " AND YEAR(pc.fecha_pago) = ?";
        }

        try (PreparedStatement ps = conn.prepareStatement(sql)) {

            int i = 1;

            if (casa != null && !casa.equals("Todas las casas")) {
                ps.setInt(i++, Integer.parseInt(casa));
            }

            if (mes != null && !mes.equals("Todos los meses")) {
                ps.setInt(i++, Integer.parseInt(mes));
            }

            if (anio != null && !anio.equals("Todos los años")) {
                ps.setInt(i++, Integer.parseInt(anio));
            }

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                lista.add(new HistorialPago(
                        rs.getInt("numero_casa"),
                        rs.getString("propietario"),
                        rs.getString("fecha_pago"),
                        rs.getDouble("cuota"),
                        rs.getString("imprime_comprobante")
                ));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }
}
