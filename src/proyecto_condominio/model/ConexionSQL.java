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

        // CONSULTA REAL: Solo los 3 campos existentes en tu BD
        String queryBuscar = "SELECT id_usuario, contrasena, id_rol FROM usuario WHERE correo = ?";

        try {
            PreparedStatement pst = conexion.prepareStatement(queryBuscar);
            pst.setString(1, correo);
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                int id = rs.getInt("id_usuario");
                String claveReal = rs.getString("contrasena");
                int idRol = rs.getInt("id_rol"); // Leemos el rol como entero puro (vale 1)

                // Comparación directa de contraseñas
                if (claveReal.equals(contrasena)) {
                    System.out.println("Login exitoso en BD. id_rol: " + idRol);
                    
                    // Datos locales en memoria (ya no se leen de la BD)
                    String nombrePorDefecto = correo.split("@")[0]; 
                    boolean primerIngresoLocal = false; 
                    int intentosLocales = 0;
                    boolean bloqueadoLocal = false;

                    // NOTA: Si tu constructor de Usuario esperaba un String en el rol, 
                    // pasamos String.valueOf(idRol) para que reciba el "1" sin romper el objeto.
                    Usuario usuarioLogueado = new Usuario(
                        id, 
                        nombrePorDefecto, 
                        correo, 
                        claveReal, 
                        primerIngresoLocal, 
                        String.valueOf(idRol), // Pasa el 1 como texto para mantener la firma
                        intentosLocales, 
                        bloqueadoLocal
                    );
                    
                    Sesion.setUsuarioActual(usuarioLogueado);
                    return usuarioLogueado;
                    
                } else {
                    System.out.println("Contraseña incorrecta para: " + correo);
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
        String query = "UPDATE usuario SET contrasena = ? WHERE correo = ?"; 

        try { 
            PreparedStatement pst = conexion.prepareStatement(query); 
            pst.setString(1, claveTemporal); 
            pst.setString(2, correo); 

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