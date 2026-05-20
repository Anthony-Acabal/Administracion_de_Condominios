package proyecto_condominio.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionSQL {

    // Lógica para validar credenciales 
    public Usuario validarLogin(String correo, String contrasena) {
        Connection conexion = dbConnection.conectar(); 
        if (conexion == null) return null;

        String query = "SELECT nombre, correo, contrasena, primer_ingreso FROM Usuarios WHERE correo = ? AND contrasena = ?";
        try {
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setString(1, correo);
            pst.setString(2, contrasena);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Usuario(
                    rs.getString("nombre"),
                    rs.getString("correo"),
                    rs.getString("contrasena"),
                    rs.getBoolean("primer_ingreso")
                );
            }
        } catch (SQLException e) {
            System.out.println("Error en Login SQL: " + e.getMessage());
        } finally {
            try { if (conexion != null) conexion.close(); } catch (SQLException e) {}
        }
        return null;
    }

    // Lógica para generar credenciales temporales 
    public String generarClaveTemporal(String usuario, String correo) {
        Connection conexion = dbConnection.conectar();
        if (conexion == null) return null;

        // Genera una clave aleatoria de 6 caracteres
        String claveTemporal = "CONDO" + (int)(Math.random() * 9000 + 1000);
        
        String query = "UPDATE Usuarios SET contrasena = ?, primer_ingreso = 1 WHERE nombre = ? AND correo = ?";

        try {
            PreparedStatement pst = conexion.prepareStatement(query);
            pst.setString(1, claveTemporal);
            pst.setString(2, usuario);
            pst.setString(3, correo);

            int filasAfectadas = pst.executeUpdate();
            if (filasAfectadas > 0) {
                return claveTemporal; // Retorna la clave para que la interfaz la muestre/envíe
            }
        } catch (SQLException e) {
            System.out.println("Error al resetear clave: " + e.getMessage());
        } finally {
            try { if (conexion != null) conexion.close(); } catch (SQLException e) {}
        }
        return null;
    }
}