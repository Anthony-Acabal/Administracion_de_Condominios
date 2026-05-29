package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Usuario;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LoginDAO {

    public Usuario validarLogin(String correo, String contrasena) {

        String sql = """
                     SELECT id_usuario,
                            usuario,
                            contrasena,
                            id_rol
                     FROM usuario
                     WHERE correo = ?
                     AND contrasena = ?
                     """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)
        ) {

            stmt.setString(1, correo);
            stmt.setString(2, contrasena);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {

                Usuario user = new Usuario();

                user.setIdUsuario(
                        rs.getInt("id_usuario")
                );

                user.setUsuario(
                        rs.getString("usuario")
                );

                user.setPassword(
                        rs.getString("contrasena")
                );

                user.setRol(
                        String.valueOf(
                                rs.getInt("id_rol")
                        )
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