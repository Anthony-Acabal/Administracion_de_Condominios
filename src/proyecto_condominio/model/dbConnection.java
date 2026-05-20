/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_condominio.model;
import java.sql.*;
/**
 *
 * @author Sneaselgengar
 */
public class dbConnection {
    private static String url = "jdbc:sqlserver://localhost:1433;databaseName=Condominio;encrypt=true;trustServerCertificate=true;";
    private static String user="SqlUsuario";
    private static String pass="Hola"      ;  
    
    public static Connection conectar()
    {
        Connection con=null;
        try
        {
        con=DriverManager.getConnection(url,user,pass);
            
        }catch(SQLException e)
        {
         e.printStackTrace();
        }    
        
        return con;
    }
    
}
