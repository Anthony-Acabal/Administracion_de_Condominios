package condominio.proyecto_condominio.logic;

import java.io.IOException;
import java.util.Optional;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Stage;

public class InicioLogic {

    
    public boolean confirmarCierreSesion() {
        Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Cierre");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estás seguro de cerrar sesión?");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);

        Optional<ButtonType> resultado = alerta.showAndWait();
        return resultado.isPresent() && resultado.get() == botonSi;
    }

    
    public void cambiarPantalla(Stage stage, String fxmlArchivo, String titulo) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlArchivo));
        Parent root = loader.load();
        stage.setScene(new Scene(root));
        stage.setTitle(titulo);
        stage.show();
    }
}