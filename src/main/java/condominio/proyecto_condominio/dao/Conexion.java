package condominio.proyecto_condominio.dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class Conexion {

    private static Conexion instancia;

    private Connection connection;

    private final String URL = "jdbc:sqlserver://localhost;databaseName=Condominio;encrypt=false";
    private final String USER = "sa";
    private final String PASSWORD = "as";

    private Conexion() {

        try {

            connection = DriverManager.getConnection(URL, USER, PASSWORD);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static Conexion getInstancia() {

        if (instancia == null) {
            instancia = new Conexion();
        }

        return instancia;
    }

    public Connection getConnection() {
        return connection;
    }

}