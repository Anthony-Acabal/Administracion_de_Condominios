/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package condominio.proyecto_condominio.dao;

/**
 *
 * @author Antony
 */

import condominio.proyecto_condominio.model.Cuota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class ConfiguracionCuotaDAO {
    
    public boolean guardarCuota(Cuota cuota, int idUsuario) {

        String sql = """
                     
            INSERT INTO cuota
            (
                cuota,
                id_estado,
                id_usuario_creacion,
                fecha_creacion
            )
            VALUES (?, ?, ?, GETDATE())
        """;

        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, cuota.getMontoCuota());
            ps.setInt(2, 2);
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(e.getMessage());
            return false;
        }
    }

    public double obtenerUltimaCuota() {

        String sql = """
            SELECT TOP 1 cuota
            FROM cuota
            ORDER BY id_cuota DESC
        """;

        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getDouble("cuota");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }

    public int obtenerTotalCasas() {

        String sql = """
            SELECT COUNT(*) total_casas
            FROM propietario
        """;

        try {

            Connection conn = Conexion.getInstancia().getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                return rs.getInt("total_casas");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return 0;
    }
    
}
