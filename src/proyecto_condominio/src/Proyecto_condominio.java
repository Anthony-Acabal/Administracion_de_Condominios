package proyecto_condominio.src;

import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Proyecto_condominio extends Application {
    
    public static void main(String[] args) {
        // 🔌 prueba de conexión
        try {
            Connection conn = Config.getConexion();

            if (conn != null) {
                System.out.println("✅ Conectado a SQL Server");
                conn.close(); // importante
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

        // 🚀 inicia JavaFX
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // 🔄 MODIFICADO: Apunta directo a tu archivo Inicio.fxml dentro del paquete ui
        Parent root = FXMLLoader.load(getClass().getResource("/proyecto_condominio/ui/Inicio.fxml"));

        Scene scene = new Scene(root);

        // Cambiamos el título de la ventana para que combine con tu pantalla
        stage.setTitle("Panel de Inicio - Condominio");
        stage.setScene(scene);
        stage.show();
    }
}