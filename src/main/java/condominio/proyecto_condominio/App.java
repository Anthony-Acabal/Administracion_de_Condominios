package condominio.proyecto_condominio;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.view.JasperViewer;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import static javafx.application.Application.launch;

public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("Login"));

        stage.setScene(scene);
        stage.setMaximized(true);
        stage.setResizable(false);

        stage.maximizedProperty().addListener((obs, oldVal, newVal) -> {
            if (!newVal) {
                stage.setMaximized(true);
            }
        });

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

    }
}
