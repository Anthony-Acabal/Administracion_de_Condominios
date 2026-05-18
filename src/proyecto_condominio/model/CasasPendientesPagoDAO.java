package proyecto_condominio.model;

import proyecto_condominio.src.Config;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
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

        String fechaFormato = String.format("%02d/%d", mes, anio);

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CasasPendientesPago(
                            rs.getInt("numero_casa"),
                            rs.getString("nombre_propietario"),
                            rs.getString("telefono"),
                            fechaFormato
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public List<CasasPendientesPago> obtenerCasasPendientesAnual(int anio) {
        List<CasasPendientesPago> lista = new ArrayList<>();

        String sql = "WITH Meses AS (" +
                     "    SELECT 1 AS mes UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4 " +
                     "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8 " +
                     "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12" +
                     ") " +
                     "SELECT p.numero_casa, " +
                     "       p.primer_nombre + ' ' + ISNULL(p.segundo_nombre + ' ', '') + p.primer_apellido AS nombre_propietario, " +
                     "       p.telefono, " +
                     "       m.mes " +
                     "FROM propietario p " +
                     "CROSS JOIN Meses m " +
                     "WHERE NOT EXISTS (" +
                     "    SELECT 1 FROM pago_cuota pc " +
                     "    WHERE pc.id_propietario = p.id_propietario " +
                     "    AND MONTH(pc.fecha_pago) = m.mes " +
                     "    AND YEAR(pc.fecha_pago) = ?" +
                     ") " +
                     "ORDER BY m.mes ASC, p.numero_casa ASC";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int mes = rs.getInt("mes");
                    lista.add(new CasasPendientesPago(
                            rs.getInt("numero_casa"),
                            rs.getString("nombre_propietario"),
                            rs.getString("telefono"),
                            String.format("%02d/%d", mes, anio)
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public int[] obtenerLimitesFechas() {
        int[] limites = new int[]{
            LocalDate.now().getYear(), LocalDate.now().getMonthValue(),
            LocalDate.now().getYear(), LocalDate.now().getMonthValue() 
        };

        String sqlMin = "SELECT TOP 1 YEAR(fecha_pago) as anio, MONTH(fecha_pago) as mes FROM pago_cuota ORDER BY fecha_pago ASC";
        String sqlMax = "SELECT TOP 1 YEAR(fecha_pago) as anio, MONTH(fecha_pago) as mes FROM pago_cuota ORDER BY fecha_pago DESC";

        try (Connection con = Config.getConexion()) {
            try (PreparedStatement ps = con.prepareStatement(sqlMin);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    limites[0] = rs.getInt("anio");
                    limites[1] = rs.getInt("mes");
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlMax);
                 ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    limites[2] = rs.getInt("anio");
                    limites[3] = rs.getInt("mes");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return limites;
    }
}
