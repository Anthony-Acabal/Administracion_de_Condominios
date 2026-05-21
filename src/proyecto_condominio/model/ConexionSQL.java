package proyecto_condominio.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConexionSQL {

    private final dbConnection dbCon = new dbConnection();

    public Usuario validarLogin(String correo, String contrasena) {
        Connection conexion = dbCon.conectar(); 
        if (conexion == null) return null; 

        String queryBuscar = "SELECT nombre, contrasena, primer_ingreso, rol, intentos_fallidos, bloqueado FROM Usuarios WHERE correo = ?";
        
        try {
            PreparedStatement pst = conexion.prepareStatement(queryBuscar);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                boolean estaBloqueado = rs.getBoolean("bloqueado");
                int intentosActuales = rs.getInt("intentos_fallidos");
                String claveReal = rs.getString("contrasena");
                String nombre = rs.getString("nombre");
                boolean primerIngreso = rs.getBoolean("primer_ingreso");
                String rol = rs.getString("rol");

                if (estaBloqueado) {
                    System.out.println("Intento de acceso a cuenta bloqueada: " + correo);
                    return new Usuario(nombre, correo, claveReal, primerIngreso, rol, intentosActuales, true); 
                }

                if (claveReal.equals(contrasena)) {
                    String queryReset = "UPDATE Usuarios SET intentos_fallidos = 0 WHERE correo = ?";
                    PreparedStatement pstReset = conexion.prepareStatement(queryReset);
                    pstReset.setString(1, correo);
                    pstReset.executeUpdate();
                    
                    System.out.println("Login exitoso para: " + correo + ". Contador limpio.");
                    return new Usuario(nombre, correo, claveReal, primerIngreso, rol, 0, false);
                    
                } else {
                    intentosActuales++;
                    System.out.println("Contraseña incorrecta para: " + correo + ". Intento #" + intentosActuales);

                    String queryUpdateIntentos;
                    if (intentosActuales >= 3) {
                        queryUpdateIntentos = "UPDATE Usuarios SET intentos_fallidos = ?, bloqueado = 1 WHERE correo = ?";
                        System.out.println("¡Cuenta bloqueada automáticamente!: " + correo);
                    } else {
                        queryUpdateIntentos = "UPDATE Usuarios SET intentos_fallidos = ? WHERE correo = ?";
                    }
                    
                    PreparedStatement pstUpdate = conexion.prepareStatement(queryUpdateIntentos);
                    pstUpdate.setInt(1, intentosActuales);
                    pstUpdate.setString(2, correo);
                    pstUpdate.executeUpdate();
                    
                    return null; 
                }
            } else {
                System.out.println("El correo ingresado no existe: " + correo);
            }
            
        } catch (SQLException e) {
            System.out.println("Error crítico en consulta de login: " + e.getMessage());
        } finally {
            try { 
                if (conexion != null) conexion.close(); 
            } catch (SQLException e) {
                System.out.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
        return null; 
    }

    public String generarClaveTemporal(String usuario, String correo) { 
        Connection conexion = dbCon.conectar(); 
        if (conexion == null) return null; 

        String claveTemporal = "CONDO" + (int)(Math.random() * 9000 + 1000); 
        String query = "UPDATE Usuarios SET contrasena = ?, primer_ingreso = 1, intentos_fallidos = 0, bloqueado = 0 WHERE nombre = ? AND correo = ?"; 

        try { 
            PreparedStatement pst = conexion.prepareStatement(query); 
            pst.setString(1, claveTemporal); 
            pst.setString(2, usuario); 
            pst.setString(3, correo); 

            int filasAfectadas = pst.executeUpdate(); 
            if (filasAfectadas > 0) { 
                return claveTemporal; 
            } 
        } catch (SQLException e) { 
            System.out.println("Error al resetear clave: " + e.getMessage()); 
        } finally { 
            try { if (conexion != null) conexion.close(); } catch (SQLException e) {} 
        } 
        return null; 
    }
}