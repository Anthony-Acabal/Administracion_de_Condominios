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

        try (Connection con = Config.getConexion()) {
            try (PreparedStatement psTest = con.prepareStatement("SELECT COUNT(*) FROM propietario");
                 ResultSet rsTest = psTest.executeQuery()) {
                if (rsTest.next()) {
                    System.out.println("Total propietarios en DB: " + rsTest.getInt(1));
                }
            }

            try (PreparedStatement ps = con.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                int count = 0;
                while (rs.next()) {
                    lista.add(new ReporteGeneral(
                            rs.getInt("numero_casa"),
                            rs.getString("nombre_propietario"),
                            rs.getString("estado_actual"),
                            rs.getDouble("total_pagado")
                    ));
                    count++;
                }
                System.out.println("Registros recuperados por la consulta JOIN: " + count);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }

    public double[] obtenerResumenMensual() {
        double[] resumen = new double[2]; 
        
        String sql = "SELECT " +
                     " (SELECT ISNULL(SUM(c.cuota), 0) " +
                     "  FROM pago_cuota pc " +
                     "  JOIN cuota c ON pc.id_cuota = c.id_cuota " +
                     "  WHERE MONTH(pc.fecha_pago) = MONTH(GETDATE()) " +
                     "  AND YEAR(pc.fecha_pago) = YEAR(GETDATE())) AS Recaudado, " +
                     " (SELECT COUNT(*) FROM propietario) AS TotalCasas, " +
                     " (SELECT TOP 1 cuota FROM cuota ORDER BY id_cuota DESC) AS UltimaCuota";

        try (Connection con = Config.getConexion();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                resumen[0] = rs.getDouble("Recaudado");
                int totalCasas = rs.getInt("TotalCasas");
                double ultimaCuota = rs.getDouble("UltimaCuota");
                resumen[1] = totalCasas * ultimaCuota;
                
                System.out.println("Total Casas: " + totalCasas);
                System.out.println("Ultima Cuota: " + ultimaCuota);
                System.out.println("Calculo Esperado (" + totalCasas + " * " + ultimaCuota + "): " + resumen[1]);
                System.out.println("Recaudado Real: " + resumen[0]);
            }
        } catch (SQLException e) {
            System.err.println("Error en obtenerResumenMensual: " + e.getMessage());
            e.printStackTrace();
        }
        return resumen;
    }
}
