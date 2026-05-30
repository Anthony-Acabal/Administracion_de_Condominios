package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class RecuperacionDAO {

    public String generarClaveTemporal(
            String usuario,
            String correo
    ) {
        String nuevaClave
                = "TEMP"
                + ((int) (Math.random() * 9000) + 1000);

        String sql = """
                     UPDATE usuario
                     SET contrasena = ?
                     WHERE usuario = ?
                     AND correo = ?
                     """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, nuevaClave);
            stmt.setString(2, usuario);
            stmt.setString(3, correo);

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                return nuevaClave;
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al generar clave temporal"
            );

            e.printStackTrace();
        }

        return null;
    }

    public Usuario obtenerUsuario(String usuario, String correo) {

        String sql = """
        SELECT id_usuario, usuario, correo, contrasena, id_rol
        FROM usuario
        WHERE usuario = ?
        AND correo = ?
    """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, correo);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                return new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("usuario"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        false,
                        String.valueOf(rs.getInt("id_rol")),
                        0,
                        false
                );
            }

        } catch (Exception e) {
            System.out.println("Error al obtener usuario");
            e.printStackTrace();
        }

        return null;
    }
}
