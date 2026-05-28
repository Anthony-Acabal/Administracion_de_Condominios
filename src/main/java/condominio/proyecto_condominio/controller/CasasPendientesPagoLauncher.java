package proyecto_condominio.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marcos Antonio
 */
public class CasasPendientesPagoLauncher extends Application {

    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("CasasPendientesPagoView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Vista Previa: Casas Pendientes de Pago");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
