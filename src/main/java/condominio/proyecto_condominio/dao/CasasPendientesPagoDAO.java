package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.CasasPendientesPago;
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
        String sql = "WITH Meses AS\n"
                + "(\n"
                + "    SELECT 1 mes\n"
                + "    UNION ALL SELECT 2\n"
                + "    UNION ALL SELECT 3\n"
                + "    UNION ALL SELECT 4\n"
                + "    UNION ALL SELECT 5\n"
                + "    UNION ALL SELECT 6\n"
                + "    UNION ALL SELECT 7\n"
                + "    UNION ALL SELECT 8\n"
                + "    UNION ALL SELECT 9\n"
                + "    UNION ALL SELECT 10\n"
                + "    UNION ALL SELECT 11\n"
                + "    UNION ALL SELECT 12\n"
                + ")\n"
                + "\n"
                + "SELECT\n"
                + "    p.id_casa,\n"
                + "    p.primer_nombre + ' ' + ISNULL(p.segundo_nombre + ' ', '') + p.primer_apellido AS nombre_propietario,\n"
                + "    p.telefono,\n"
                + "    m.mes\n"
                + "FROM propietario p\n"
                + "CROSS JOIN Meses m\n"
                + "\n"
                + "WHERE\n"
                + "(\n"
                + "    ? IS NULL\n"
                + "    OR m.mes = ?\n"
                + ")\n"
                + "\n"
                + "AND p.id_estado = 2\n"
                + "\n"
                + "-- 🔥 REGLA CLAVE: RESPETAR FECHA DE CREACIÓN\n"
                + "AND (\n"
                + "    YEAR(p.fecha_creacion) < ?\n"
                + "    OR (\n"
                + "        YEAR(p.fecha_creacion) = ?\n"
                + "        AND m.mes >= MONTH(p.fecha_creacion)\n"
                + "    )\n"
                + ")\n"
                + "\n"
                + "AND NOT EXISTS\n"
                + "(\n"
                + "    SELECT 1\n"
                + "    FROM pago_cuota pc\n"
                + "    WHERE pc.id_propietario = p.id_propietario\n"
                + "      AND MONTH(pc.pago) = m.mes\n"
                + "      AND YEAR(pc.pago) = ?\n"
                + ")\n"
                + "\n"
                + "ORDER BY m.mes ASC, p.id_casa ASC;";

        String fechaFormato = String.format("%02d/%d", mes, anio);

        try (Connection con = Conexion.getInstancia().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            if (mes == 0) {
                ps.setNull(1, java.sql.Types.INTEGER);
                ps.setNull(2, java.sql.Types.INTEGER);
            } else {
                ps.setInt(1, mes);
                ps.setInt(2, mes);
            }

// 🔥 NUEVO: año para fecha_creacion
            ps.setInt(3, anio);
            ps.setInt(4, anio);

// pago_cuota
            ps.setInt(5, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(new CasasPendientesPago(
                            rs.getInt("id_casa"),
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

    public List<CasasPendientesPago> obtenerCasasPendientesAnual(int anio, int mesInicio, int mesFin) {

        List<CasasPendientesPago> lista = new ArrayList<>();

        String sql
                = "WITH Meses AS (\n"
                + "    SELECT 1 mes UNION ALL SELECT 2 UNION ALL SELECT 3 UNION ALL SELECT 4\n"
                + "    UNION ALL SELECT 5 UNION ALL SELECT 6 UNION ALL SELECT 7 UNION ALL SELECT 8\n"
                + "    UNION ALL SELECT 9 UNION ALL SELECT 10 UNION ALL SELECT 11 UNION ALL SELECT 12\n"
                + ")\n"
                + "SELECT p.id_casa,\n"
                + "       p.primer_nombre + ' ' + ISNULL(p.segundo_nombre + ' ', '') + p.primer_apellido AS nombre_propietario,\n"
                + "       p.telefono,\n"
                + "       m.mes\n"
                + "FROM propietario p\n"
                + "CROSS JOIN Meses m\n"
                + "WHERE p.id_estado = 2\n"
                + "AND m.mes BETWEEN ? AND ?\n"
                + "\n"
                + "AND (\n"
                + "    YEAR(p.fecha_creacion) < ?\n"
                + "    OR (\n"
                + "        YEAR(p.fecha_creacion) = ?\n"
                + "        AND m.mes >= MONTH(p.fecha_creacion)\n"
                + "    )\n"
                + ")\n"
                + "\n"
                + "AND NOT EXISTS (\n"
                + "    SELECT 1 FROM pago_cuota pc\n"
                + "    WHERE pc.id_propietario = p.id_propietario\n"
                + "      AND MONTH(pc.pago) = m.mes\n"
                + "      AND YEAR(pc.pago) = ?\n"
                + ")\n"
                + "ORDER BY m.mes ASC, p.id_casa ASC;";

        try (Connection con = Conexion.getInstancia().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, mesInicio);
            ps.setInt(2, mesFin);
            ps.setInt(3, anio);
            ps.setInt(4, anio);
            ps.setInt(5, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    int mes = rs.getInt("mes");

                    lista.add(new CasasPendientesPago(
                            rs.getInt("id_casa"),
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

        String sqlMin = "SELECT TOP 1 YEAR(pago) as anio, MONTH(pago) as mes FROM pago_cuota WHERE pago IS NOT NULL ORDER BY pago ASC";
        String sqlMax = "SELECT TOP 1 YEAR(pago) as anio, MONTH(pago) as mes FROM pago_cuota WHERE pago IS NOT NULL ORDER BY pago DESC";

        try (Connection con = Conexion.getInstancia().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sqlMin); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int anio = rs.getInt("anio");
                    int mes = rs.getInt("mes");
                    if (anio > 0) {
                        limites[0] = anio;
                    }
                    if (mes > 0) {
                        limites[1] = mes;
                    }
                }
            }
            try (PreparedStatement ps = con.prepareStatement(sqlMax); ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    int anio = rs.getInt("anio");
                    int mes = rs.getInt("mes");
                    if (anio > 0) {
                        limites[2] = anio;
                    }
                    if (mes > 0) {
                        limites[3] = mes;
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return limites;
    }
}
