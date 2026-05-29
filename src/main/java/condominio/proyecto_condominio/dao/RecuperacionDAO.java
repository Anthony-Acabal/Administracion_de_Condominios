package condominio.proyecto_condominio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;

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
                Connection conn = Conexion.getConnection(); PreparedStatement stmt
                = conn.prepareStatement(sql)) {

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
}
