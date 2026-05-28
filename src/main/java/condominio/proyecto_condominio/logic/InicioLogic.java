package condominio.proyecto_condominio.logic;

import java.io.IOException;

import javafx.fxml.FXMLLoader;

import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class InicioLogic {

    public void cambiarPantalla(
            Stage stage,
            String fxmlArchivo,
            String titulo
    ) throws IOException {

        FXMLLoader loader = new FXMLLoader(
                condominio.proyecto_condominio.App.class.getResource(fxmlArchivo)
        );

        Parent root = loader.load();

        stage.setScene(new Scene(root));

        stage.setTitle(titulo);

        stage.show();
    }

}
