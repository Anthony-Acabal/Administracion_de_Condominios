package proyecto_condominio.model;

import proyecto_condominio.src.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReporteGeneralDAO {
    public List<ReporteGeneral> obtenerReporteGeneral() {
        List<ReporteGeneral> lista = new ArrayList<>();
        String sql = "SELECT " +
                     "p.numero_casa, " +
                     "p.primer_nombre + ' ' + p.primer_apellido AS nombre_propietario, " +
                     "(SELECT CASE WHEN COUNT(*) > 0 THEN 'Pagado' ELSE 'Pendiente' END " +
                     " FROM pago_cuota pc " +
                     " WHERE pc.id_propietario = p.id_propietario " +
                     " AND MONTH(pc.fecha_pago) = MONTH(GETDATE()) " +
                     " AND YEAR(pc.fecha_pago) = YEAR(GETDATE())) AS estado_actual, " +
                     "COALESCE((SELECT SUM(c.cuota) " +
                     " FROM pago_cuota pc " +
                     " JOIN cuota c ON pc.id_cuota = c.id_cuota " +
                     " WHERE pc.id_propietario = p.id_propietario " +
                     " AND YEAR(pc.fecha_pago) = YEAR(GETDATE())), 0) AS total_pagado " +
                     "FROM propietario p";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                lista.add(new ReporteGeneral(
                        rs.getInt("numero_casa"),
                        rs.getString("nombre_propietario"),
                        rs.getString("estado_actual"),
                        rs.getDouble("total_pagado")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
