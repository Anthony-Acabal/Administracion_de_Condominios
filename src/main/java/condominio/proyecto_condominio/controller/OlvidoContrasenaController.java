package condominio.proyecto_condominio.controller;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import condominio.proyecto_condominio.logic.RecuperacionLogic;

public class OlvidoContrasenaController {

    private final RecuperacionLogic logic
            = new RecuperacionLogic();

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtCorreo;

    @FXML
    private Label lblMensaje;

    @FXML
    private void accionGenerarClave(ActionEvent event) {

        String usuario = txtUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (usuario.isEmpty() || correo.isEmpty()) {

            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        // Simulación temporal mientras integran la lógica real
        String claveTemporal
                = logic.generarClaveTemporal(
                        usuario,
                        correo
                );

        System.out.println("CLAVE GENERADA = " + claveTemporal);

        if (claveTemporal != null) {

            lblMensaje.setStyle("-fx-text-fill: #10b981;");

            lblMensaje.setText(
                    "¡Clave generada!\n"
                    + "Su contraseña temporal es:\n"
                    + claveTemporal
            );

            System.out.println(
                    "Clave temporal generada con éxito para: "
                    + usuario
            );

        } else {

            lblMensaje.setStyle("-fx-text-fill: red;");

            lblMensaje.setText(
                    "Los datos no coinciden con ningún usuario."
            );
        }
    }

    @FXML
    private void accionRegresarLogin(ActionEvent event) {

        try {

            Parent root = FXMLLoader.load(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/Login.fxml"
                    )
            );

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Login - Condominio");
            stage.show();

        } catch (IOException e) {

            System.out.println(
                    "Error al regresar al Login: "
                    + e.getMessage()
            );
        }
    }
}
