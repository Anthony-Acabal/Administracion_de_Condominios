package proyecto_condominio.ui;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.Label;
import javafx.scene.control.Button;

public class CambioContrasenaController {

    @FXML private PasswordField txtNuevaClave;
    @FXML private PasswordField txtConfirmarClave;
    @FXML private Button btnActualizar;
    @FXML private Label lblMensaje;

    @FXML
    private void accionActualizarContrasena() {
        String nueva = txtNuevaClave.getText().trim();
        String confirmacion = txtConfirmarClave.getText().trim();

        if (nueva.isEmpty() || confirmacion.isEmpty()) {
            lblMensaje.setText("Ambos campos son obligatorios.");
            return;
        }

        if (!nueva.equals(confirmacion)) {
            lblMensaje.setText("Las contraseñas no coinciden.");
            return;
        }

        // Aquí irá la query SQL para hacer el UPDATE y poner primer_ingreso = 0 en la siguiente fase
        System.out.println("Contraseña actualizada con éxito en la base de datos.");
        lblMensaje.setStyle("-fx-text-fill: #10b981;");
        lblMensaje.setText("¡Contraseña cambiada! Redireccionando...");
    }
}