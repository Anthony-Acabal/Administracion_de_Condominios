package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Cuota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class ConfiguracionCuotaDAO {

    private static final int ID_ESTADO_ACTIVO = 2;
    private static final int ID_ESTADO_INACTIVO = 3;

    public boolean guardarCuota(Cuota cuota, int idUsuario) {

        String sqlDesactivar = """
            UPDATE cuota
            SET id_estado = ?
            WHERE id_estado = ?
        """;

        String sqlInsertar = """
            INSERT INTO cuota
            (
                cuota,
                id_estado,
                id_usuario_creacion,
                fecha_creacion
            )
            VALUES (?, ?, ?, GETDATE())
        """;

        try (Connection conn = Conexion.getInstancia().getConnection()) {

            conn.setAutoCommit(false);

            try (
                PreparedStatement psUpdate = conn.prepareStatement(sqlDesactivar);
                PreparedStatement psInsert = conn.prepareStatement(sqlInsertar)
            ) {

                // DESACTIVAR ANTERIOR
                psUpdate.setInt(1, ID_ESTADO_INACTIVO);
                psUpdate.setInt(2, ID_ESTADO_ACTIVO);
                psUpdate.executeUpdate();

                // INSERT NUEVA CUOTA
                psInsert.setDouble(1, cuota.getMontoCuota());
                psInsert.setInt(2, ID_ESTADO_ACTIVO);
                psInsert.setInt(3, idUsuario);

                boolean guardado = psInsert.executeUpdate() > 0;

                conn.commit();

                return guardado;

            } catch (Exception e) {
                conn.rollback();
                throw e;
            }

        } catch (Exception e) {
            System.out.println("Error al guardar cuota:");
            System.out.println(e.getMessage());
            return false;
        }
    }

    public int obtenerUltimaCuota() {

        String sql = """
            SELECT TOP 1 cuota
            FROM cuota
            ORDER BY id_cuota DESC
        """;

        try (
            Connection conn = Conexion.getInstancia().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {
                return rs.getInt("cuota");
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
            Connection conn = Conexion.getInstancia().getConnection();
            PreparedStatement ps = conn.prepareStatement(sql);
            ResultSet rs = ps.executeQuery()
        ) {

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