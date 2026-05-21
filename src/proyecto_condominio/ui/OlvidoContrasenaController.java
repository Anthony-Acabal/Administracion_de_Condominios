package proyecto_condominio.ui;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto_condominio.model.ConexionSQL;

public class OlvidoContrasenaController {

    @FXML private TextField txtUsuario;
    @FXML private TextField txtCorreo;
    @FXML private Label lblMensaje;

    @FXML
    private void accionGenerarClave(ActionEvent event) {
        String usuario = txtUsuario.getText().trim();
        String correo = txtCorreo.getText().trim();

        if (usuario.isEmpty() || correo.isEmpty()) {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Por favor, complete todos los campos.");
            return;
        }

        // Llamamos a tu clase de conexión que ya tiene el método listo
        ConexionSQL querys = new ConexionSQL();
        String claveTemporal = querys.generarClaveTemporal(usuario, correo);

        if (claveTemporal != null) {
            lblMensaje.setStyle("-fx-text-fill: #10b981;"); // Color verde éxito
            lblMensaje.setText("¡Clave generada! Su contraseña temporal es:\n" + claveTemporal);
            System.out.println("Clave temporal generada con éxito para: " + usuario);
        } else {
            lblMensaje.setStyle("-fx-text-fill: red;");
            lblMensaje.setText("Los datos no coinciden con ningún usuario.");
        }
    }

    @FXML
    private void accionRegresarLogin(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/proyecto_condominio/ui/Login.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Login - Condominio");
            stage.show();
        } catch (IOException e) {
            System.out.println("Error al regresar al Login: " + e.getMessage());
        }
    }
}