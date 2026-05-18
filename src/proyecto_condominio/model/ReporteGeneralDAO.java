package proyecto_condominio.model;

import proyecto_condominio.src.Config;
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
            String sql = "SELECT " +
             "  p.numero_casa, " +
             "  p.primer_nombre + ' ' + p.primer_apellido AS nombre_propietario, " +
             "  CASE " +
             "    WHEN COUNT(CASE WHEN MONTH(pc.fecha_pago) = ? AND YEAR(pc.fecha_pago) = ? THEN 1 END) > 0 THEN 'Pagado' " +
             "    ELSE 'Pendiente' " +
             "  END AS estado_actual, " +
             "  COALESCE(SUM(CASE WHEN YEAR(pc.fecha_pago) = ? THEN c.cuota ELSE 0 END), 0) AS total_pagado " +
             "FROM propietario p " +
             "LEFT JOIN pago_cuota pc ON p.id_propietario = pc.id_propietario " +
             "LEFT JOIN cuota c ON pc.id_cuota = c.id_cuota " +
             "GROUP BY p.numero_casa, p.primer_nombre, p.primer_apellido " +
             "ORDER BY p.numero_casa ASC;";

        try (Connection con = Config.getConexion()) {
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setInt(1, mes);
                ps.setInt(2, anio);
                ps.setInt(3, anio);
                
                try (ResultSet rs = ps.executeQuery()) {
                    while (rs.next()) {
                        lista.add(new ReporteGeneral(
                                rs.getInt("numero_casa"),
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
        
        String sql = "SELECT " +
                     " (SELECT ISNULL(SUM(c.cuota), 0) " +
                     "  FROM pago_cuota pc " +
                     "  JOIN cuota c ON pc.id_cuota = c.id_cuota " +
                     "  WHERE MONTH(pc.fecha_pago) = ? " +
                     "  AND YEAR(pc.fecha_pago) = ?) AS Recaudado, " +
                     " (SELECT COUNT(*) FROM propietario) AS TotalCasas, " +
                     " (SELECT TOP 1 cuota FROM cuota ORDER BY id_cuota DESC) AS UltimaCuota";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
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
        
        String sql = "SELECT " +
                     " (SELECT ISNULL(SUM(c.cuota), 0) " +
                     "  FROM pago_cuota pc " +
                     "  JOIN cuota c ON pc.id_cuota = c.id_cuota " +
                     "  WHERE YEAR(pc.fecha_pago) = ? " +
                     "  AND MONTH(pc.fecha_pago) <= ?) AS RecaudadoAnual, " +
                     " (SELECT COUNT(*) FROM propietario) AS TotalCasas, " +
                     " (SELECT TOP 1 cuota FROM cuota ORDER BY id_cuota DESC) AS UltimaCuota";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql)) {
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
            LocalDate.now().getYear(), LocalDate.now().getMonthValue(), // min anio, min mes
            LocalDate.now().getYear(), LocalDate.now().getMonthValue()  // max anio, max mes
        };
        String sql = "SELECT " +
                     "MIN(YEAR(fecha_pago)) as min_anio, " +
                     "MIN(CASE WHEN YEAR(fecha_pago) = (SELECT MIN(YEAR(fecha_pago)) FROM pago_cuota) THEN MONTH(fecha_pago) ELSE 12 END) as min_mes, " +
                     "MAX(YEAR(fecha_pago)) as max_anio, " +
                     "MAX(CASE WHEN YEAR(fecha_pago) = (SELECT MAX(YEAR(fecha_pago)) FROM pago_cuota) THEN MONTH(fecha_pago) ELSE 1 END) as max_mes " +
                     "FROM pago_cuota";

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
