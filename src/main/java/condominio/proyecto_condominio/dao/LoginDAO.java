package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public Usuario validarLogin(String usuario, String contrasena) {

        String sql = """
    SELECT id_usuario,
           usuario,
           correo,
           contrasena,
           id_rol
    FROM usuario
    WHERE usuario = ?
    AND contrasena = ?
""";

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario user = new Usuario(
                        rs.getInt("id_usuario"),
                        rs.getString("usuario"),
                        rs.getString("correo"),
                        rs.getString("contrasena"),
                        false, // primerIngreso (no viene en query)
                        String.valueOf(rs.getInt("id_rol")),
                        0, // intentosFallidos
                        false // bloqueado
                );

                return user;
            }

        } catch (Exception e) {
            System.out.println("Error al validar login");
            e.printStackTrace();
        }

        return null;
    }
}
