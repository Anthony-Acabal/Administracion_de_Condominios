package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Cuota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfiguracionCuotaDAO {

    private static final int ID_ESTADO_ACTIVO = 18;

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

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setDouble(1, cuota.getMontoCuota());

            ps.setInt(2, ID_ESTADO_ACTIVO);

            ps.setInt(3, idUsuario);

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println("Error al guardar cuota:");
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

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                return rs.getDouble("cuota");
            }

        } catch (Exception e) {

            System.out.println("Error al obtener última cuota:");
            System.out.println(e.getMessage());
        }

        return 0;
    }

    public int obtenerTotalCasas() {

        String sql = """
            SELECT COUNT(*) total_casas
            FROM propietario
        """;

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {

                return rs.getInt("total_casas");
            }

        } catch (Exception e) {

            System.out.println("Error al obtener total de casas:");
            System.out.println(e.getMessage());
        }

        return 0;
    }
}
