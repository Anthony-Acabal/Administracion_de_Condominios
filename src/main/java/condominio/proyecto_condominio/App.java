package condominio.proyecto_condominio;

/*import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 */
 /*public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("primary"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

}*/
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Inicio"), 640, 480);
        stage.setScene(scene);
        stage.show();
    }

    static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(
                App.class.getResource("/condominio/proyecto_condominio/ui/" + fxml + ".fxml")
        );
        return fxmlLoader.load();
    }

    public static void main(String[] args) {

        launch();
//        try {
//            // 🔹 Conexión a BD
//            Connection conn = DriverManager.getConnection(
//                "jdbc:sqlserver://localhost;databaseName=Condominio;encrypt=false",
//                "sa",
//                "Labymed2026"
//            );
//
//            // 🔹 Ruta del .jrxml
//            String archivo = "src/main/java/org/condominio/condominio_3/Pruebas.jrxml";
//
//            // 🔹 Compilar reporte
//            JasperReport reporte = JasperCompileManager.compileReport(archivo);
//
//            // 🔹 Parámetros (si no tienes, dejas vacío)
//            HashMap<String, Object> parametros = new HashMap<>();
//
//            // 🔹 Llenar reporte con datos
//            JasperPrint print = JasperFillManager.fillReport(reporte, parametros, conn);
//
//            // 🔹 Mostrar
//            JasperViewer.viewReport(print, false);
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
}
