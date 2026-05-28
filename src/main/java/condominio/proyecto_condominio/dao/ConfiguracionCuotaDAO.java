<<<<<<< HEAD
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package condominio.proyecto_condominio.dao;

/**
 *
 * @author Antony
 */

=======
package condominio.proyecto_condominio.dao;

>>>>>>> origin/main
import condominio.proyecto_condominio.model.Cuota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

<<<<<<< HEAD

public class ConfiguracionCuotaDAO {
    
=======
public class ConfiguracionCuotaDAO {

    private static final int ID_ESTADO_ACTIVO = 18;

>>>>>>> origin/main
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

<<<<<<< HEAD
        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ps.setDouble(1, cuota.getMontoCuota());
            ps.setInt(2, 2);
=======
        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, cuota.getMontoCuota());

            ps.setInt(2, ID_ESTADO_ACTIVO);

>>>>>>> origin/main
            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

<<<<<<< HEAD
            System.out.println(e.getMessage());
=======
            System.out.println("Error al guardar cuota:");
            System.out.println(e.getMessage());

>>>>>>> origin/main
            return false;
        }
    }

    public double obtenerUltimaCuota() {

        String sql = """
            SELECT TOP 1 cuota
            FROM cuota
            ORDER BY id_cuota DESC
        """;

<<<<<<< HEAD
        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
=======
        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
>>>>>>> origin/main

            if (rs.next()) {

                return rs.getDouble("cuota");
            }

        } catch (Exception e) {

<<<<<<< HEAD
=======
            System.out.println("Error al obtener última cuota:");
>>>>>>> origin/main
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public int obtenerTotalCasas() {

        String sql = """
            SELECT COUNT(*) total_casas
            FROM propietario
        """;

<<<<<<< HEAD
        try {

            Connection conn = Conexion.getInstancia().getConnection();

            PreparedStatement ps = conn.prepareStatement(sql);

            ResultSet rs = ps.executeQuery();
=======
        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
>>>>>>> origin/main

            if (rs.next()) {

                return rs.getInt("total_casas");
            }

        } catch (Exception e) {

<<<<<<< HEAD
=======
            System.out.println("Error al obtener total de casas:");
>>>>>>> origin/main
            System.out.println(e.getMessage());
        }

        return 0;
    }
<<<<<<< HEAD
    
=======
>>>>>>> origin/main
}
