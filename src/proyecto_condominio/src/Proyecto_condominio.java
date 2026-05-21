package proyecto_condominio.src;

import java.sql.Connection;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Proyecto_condominio extends Application {
    
    public static void main(String[] args) {
        
        try {
            Connection conn = Config.getConexion();

            if (conn != null) {
                System.out.println("✅ Conectado a SQL Server");
                conn.close(); 
            }

        } catch (Exception e) {
            System.out.println("❌ Error: " + e.getMessage());
        }

       
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("/proyecto_condominio/ui/Inicio.fxml"));

        Scene scene = new Scene(root);

      
        stage.setTitle("Panel de Inicio - Condominio");
        stage.setScene(scene);
        stage.show();
    }
}