package condominio.proyecto_condominio.controller;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import condominio.proyecto_condominio.model.Usuario;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import condominio.proyecto_condominio.logic.LoginLogic;
import condominio.proyecto_condominio.model.Sesion;

public class LoginController {

    @FXML
    private HBox boxCorreo;

    @FXML
    private HBox boxContrasena;

    @FXML
    private TextField txtCorreo;

    @FXML
    private PasswordField txtContrasena;

    @FXML
    private TextField txtContrasenaVisible;

    @FXML
    private Button btnMostrarContrasena;

    @FXML
    private ImageView imgOjito;

    @FXML
    private Button btnIngresar;

    @FXML
    private Label lblMensajeError;

    private int intentosFallidos = 0;

    private boolean esContrasenaVisible = false;

    private final LoginLogic logic = new LoginLogic();

    @FXML
    private void accionIngresar() {

        if (intentosFallidos >= 3) {
            lblMensajeError.setText("Acceso denegado. Superó el límite de 3 intentos.");
            btnIngresar.setDisable(true);
            return;
        }

        boxCorreo.getStyleClass().remove("campo-error");
        boxContrasena.getStyleClass().remove("campo-error");
        lblMensajeError.setText("");

        String usuario = txtCorreo.getText().trim();

        String clave = esContrasenaVisible
                ? txtContrasenaVisible.getText().trim()
                : txtContrasena.getText().trim();

        if (usuario.isEmpty() || clave.isEmpty()) {

            lblMensajeError.setText("Por favor, complete los campos.");

            if (usuario.isEmpty()) {
                boxCorreo.getStyleClass().add("campo-error");
            }

            if (clave.isEmpty()) {
                boxContrasena.getStyleClass().add("campo-error");
            }

            return;
        }

        // Temporal mientras integran la lógica real
        Usuario user = logic.validarLogin(usuario, clave);

        if (user != null) {

            intentosFallidos = 0;

            Sesion.setUsuarioActual(user);

            procesarDireccionamiento(user);

        } else {

            intentosFallidos++;

            boxCorreo.getStyleClass().add("campo-error");
            boxContrasena.getStyleClass().add("campo-error");

            if (intentosFallidos >= 3) {

                lblMensajeError.setText(
                        "Acceso denegado. Superó el límite de 3 intentos."
                );

                btnIngresar.setDisable(true);

            } else {

                int intentosRestantes = 3 - intentosFallidos;

                lblMensajeError.setText(
                        "Credenciales incorrectas. Le quedan "
                        + intentosRestantes
                        + " intentos."
                );
            }
        }
    }

    @FXML
    private void accionMostrarContrasena() {

        if (!esContrasenaVisible) {

            txtContrasenaVisible.setText(
                    txtContrasena.getText()
            );

            txtContrasenaVisible.setVisible(true);
            txtContrasena.setVisible(false);

            esContrasenaVisible = true;

        } else {

            txtContrasena.setText(
                    txtContrasenaVisible.getText()
            );

            txtContrasena.setVisible(true);
            txtContrasenaVisible.setVisible(false);

            esContrasenaVisible = false;
        }
    }

    @FXML
    private void accionOlvidoContrasena(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/OlvidoContrasena.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));
            stage.setTitle("Recuperar Contraseña - Sistema de Condominios");
            stage.show();

        } catch (IOException e) {

            System.out.println(
                    "Error crítico al abrir la ventana de olvido contraseña: "
                    + e.getMessage()
            );

            lblMensajeError.setText(
                    "No se pudo cargar la vista de recuperación."
            );
        }
    }

    private void procesarDireccionamiento(Usuario user) {

        try {

            String nombreFxml = "Inicio.fxml";

            String rutaFxml
                    = "/condominio/proyecto_condominio/ui/"
                    + nombreFxml;

            String tituloVentana
                    = "Sistema de Administración de Condominios - Inicio";

            java.net.URL urlRecurso
                    = getClass().getResource(rutaFxml);

            if (urlRecurso == null) {

                System.out.println(
                        "\n[AVISO]: No se encontró "
                        + nombreFxml
                );

                lblMensajeError.setStyle(
                        "-fx-text-fill: #37767C;"
                );

                lblMensajeError.setText(
                        "¡Login exitoso! (Inicio.fxml aún no está integrado)"
                );

                return;
            }

            FXMLLoader loader
                    = new FXMLLoader(urlRecurso);

            Parent root = loader.load();

            Stage stage = (Stage) txtCorreo.getScene().getWindow();

            Scene scene = stage.getScene();
            scene.setRoot(root);

            stage.setTitle(tituloVentana);

            stage.show();

            javafx.application.Platform.runLater(() -> {
                stage.setMaximized(true);
                stage.setResizable(false);
            });

            stage.show();

        } catch (IOException e) {

            e.printStackTrace();

            lblMensajeError.setStyle(
                    "-fx-text-fill: #f20b0b;"
            );

            lblMensajeError.setText(
                    "Error crítico al cargar el menú principal."
            );
        }
    }
}
