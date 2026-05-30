package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.ReporteGeneral;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReporteGeneralDAO {

    public List<ReporteGeneral> obtenerReporteGeneral(int mes, int anio) {
        List<ReporteGeneral> lista = new ArrayList<>();
        String sql
                = "SELECT\n"
                + "    p.id_casa,\n"
                + "    p.primer_nombre + ' ' + p.primer_apellido AS nombre_propietario,\n"
                + "\n"
                + "    CASE\n"
                + "        WHEN EXISTS (\n"
                + "            SELECT 1\n"
                + "            FROM pago_cuota pc\n"
                + "            WHERE pc.id_propietario = p.id_propietario\n"
                + "              AND YEAR(pc.pago) = ?\n"
                + "              AND MONTH(pc.pago) = ?\n"
                + "        ) THEN 'Pagado'\n"
                + "        ELSE 'Pendiente'\n"
                + "    END AS estado_actual,\n"
                + "\n"
                + "    ISNULL((\n"
                + "        SELECT SUM(c.cuota)\n"
                + "        FROM pago_cuota pc\n"
                + "        INNER JOIN cuota c ON pc.id_cuota = c.id_cuota\n"
                + "        WHERE pc.id_propietario = p.id_propietario\n"
                + "          AND YEAR(pc.pago) = ?\n"
                + "          AND MONTH(pc.pago) = ?\n"
                + "    ), 0) AS total_pagado\n"
                + "\n"
                + "FROM propietario p\n"
                + "WHERE p.id_estado = 2\n"
                + "  AND p.fecha_creacion <= EOMONTH(DATEFROMPARTS(?, ?, 1))\n"
                + "ORDER BY p.id_casa ASC;";

        try (Connection con = Conexion.getInstancia().getConnection()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, anio);
                ps.setInt(2, mes);

                ps.setInt(3, anio);
                ps.setInt(4, mes);

                ps.setInt(5, anio);
                ps.setInt(6, mes);

                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new ReporteGeneral(
                                rs.getInt("id_casa"),
                                rs.getString("nombre_propietario"),
                                rs.getString("estado_actual"),
                                rs.getDouble("total_pagado")
                        ));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double[] obtenerResumenMensual(int mes, int anio) {
        double[] resumen = new double[2];

        String sql = "SELECT "
                + " (SELECT ISNULL(SUM(c.cuota), 0) "
                + "  FROM pago_cuota pc "
                + "  JOIN cuota c ON pc.id_cuota = c.id_cuota "
                + "  WHERE MONTH(pc.pago) = ? "
                + "  AND YEAR(pc.pago) = ?) AS Recaudado, "
                + " (SELECT COUNT(*) FROM propietario WHERE id_estado = 2) AS TotalCasas, "
                + " (SELECT TOP 1 cuota FROM cuota ORDER BY id_cuota DESC) AS UltimaCuota";

        try (Connection con = Conexion.getInstancia().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, mes);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resumen[0] = rs.getDouble("Recaudado");
                    int totalCasas = rs.getInt("TotalCasas");
                    double ultimaCuota = rs.getDouble("UltimaCuota");
                    resumen[1] = totalCasas * ultimaCuota;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resumen;
    }

    public double[] obtenerResumenAnual(int mesHasta, int anio) {
        double[] resumen = new double[2];

        String sql = "SELECT "
                + " (SELECT ISNULL(SUM(c.cuota), 0) "
                + "  FROM pago_cuota pc "
                + "  JOIN cuota c ON pc.id_cuota = c.id_cuota "
                + "  WHERE YEAR(pc.pago) = ? "
                + "  AND MONTH(pc.pago) <= ?) AS RecaudadoAnual, "
                + " (SELECT COUNT(*) FROM propietario WHERE id_estado = 2) AS TotalCasas, "
                + " (SELECT TOP 1 cuota FROM cuota ORDER BY id_cuota DESC) AS UltimaCuota";

        try (Connection con = Conexion.getInstancia().getConnection(); PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, anio);
            ps.setInt(2, mesHasta);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    resumen[0] = rs.getDouble("RecaudadoAnual");
                    int totalCasas = rs.getInt("TotalCasas");
                    double ultimaCuota = rs.getDouble("UltimaCuota");
                    resumen[1] = totalCasas * ultimaCuota * 12;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resumen;
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
