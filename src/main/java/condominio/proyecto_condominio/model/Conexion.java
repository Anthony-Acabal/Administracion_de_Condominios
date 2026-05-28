package condominio.proyecto_condominio.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {

    public static Connection getConexion() {
       
        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=Condominio;encrypt=true;trustServerCertificate=true;";
        
        try {
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            
            
            return DriverManager.getConnection(connectionUrl, "sa", "123456");
            
        } catch (ClassNotFoundException e) {
            System.out.println("Error: No se encontró el conector JDBC de SQL Server. " + e.getMessage());
            return null;
        } catch (SQLException e) {
            System.out.println("Error de conexión: " + e.getMessage());
            return null;
        }
    }
}