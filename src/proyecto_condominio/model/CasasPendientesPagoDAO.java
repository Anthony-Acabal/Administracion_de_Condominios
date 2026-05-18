package proyecto_condominio.model;

import proyecto_condominio.src.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CasasPendientesPagoDAO {

    public List<CasasPendientesPago> obtenerCasasPendientes(int mes, int anio) {
        List<CasasPendientesPago> lista = new ArrayList<>();
        String sql = "SELECT p.numero_casa, " +
                     "       p.primer_nombre + ' ' + ISNULL(p.segundo_nombre + ' ', '') + p.primer_apellido AS nombre_propietario, " +
                     "       p.telefono " +
                     "FROM propietario p " +
                     "WHERE NOT EXISTS (" +
                     "    SELECT 1 FROM pago_cuota pc " +
                     "    WHERE pc.id_propietario = p.id_propietario " +
                     "    AND MONTH(pc.fecha_pago) = ? " +
                     "    AND YEAR(pc.fecha_pago) = ?" +
                     ") " +
                     "ORDER BY p.numero_casa ASC";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CasasPendientesPago(
                            rs.getInt("numero_casa"),
                            rs.getString("nombre_propietario"),
                            rs.getString("telefono")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
