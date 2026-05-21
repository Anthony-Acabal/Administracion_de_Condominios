package proyecto_condominio.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto_condominio.model.ConexionSQL;
import proyecto_condominio.model.Usuario;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class LoginController extends Application {

    // Componentes que enlazarás en Scene Builder mediante sus fx:id
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    @FXML private Button btnIngresar;
    @FXML private Label lblMensajeError;

    @Override
    public void start(Stage stage) throws Exception {
        // Carga la ventana inicial del Login
        Parent root = FXMLLoader.load(getClass().getResource("/proyecto_condominio/ui/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sistema de Administración de Condominios - Iniciar Sesión");
        stage.show();
    }

    // Método que se ejecuta al presionar el botón "Ingresar"
    @FXML
    private void accionIngresar() {
        String correo = txtCorreo.getText().trim();
        String clave = txtContrasena.getText().trim();

        if (correo.isEmpty() || clave.isEmpty()) {
            lblMensajeError.setText("Por favor, complete los campos.");
            return;
        }

        ConexionSQL querys = new ConexionSQL();
        Usuario user = querys.validarLogin(correo, clave);

        if (user != null) {
            // Si la cuenta está bloqueada en la BD, se detiene aquí y avisa
            if (user.isBloqueado()) { 
                lblMensajeError.setText("Cuenta bloqueada. Use '¿Olvidó su contraseña?'.");
                return;
            }
            
            // Si todo está bien, evalúa si es primer ingreso o va al Home (ITM-25.1)
            procesarDireccionamiento(user);
        } else {
            lblMensajeError.setText("Correo o contraseña incorrectos.");
        }
    }
    
    @FXML
    private void accionOlvidoContrasena(ActionEvent event) {
        try {
            // 1. Cargamos la nueva vista de recuperación que creaste
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto_condominio/ui/OlvidoContrasena.fxml"));
            Parent root = loader.load();
            
            // 2. Obtenemos la ventana actual usando el evento del Hyperlink
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            
            // 3. Cambiamos la escena por la de olvido de contraseña
            stage.setScene(new Scene(root));
            stage.setTitle("Recuperar Contraseña - Sistema de Condominios");
            stage.show();
            
        } catch (IOException e) {
            System.out.println("Error crítico al abrir la ventana de olvido contraseña: " + e.getMessage());
            lblMensajeError.setText("No se pudo cargar la vista de recuperación.");
        }
    }
    
    private void procesarDireccionamiento(Usuario u) {
        try {
            String fxmlDestino;
            String titulo;

            // Intercepción obligatoria del flujo según el requerimiento ITM-25.1
            if (u.isPrimerIngreso()) {
                fxmlDestino = "/proyecto_condominio/ui/CambioContrasena.fxml";
                titulo = "Cambio Obligatorio de Contraseña";
            } else {
                fxmlDestino = "/proyecto_condominio/ui/MenuPrincipal.fxml";
                titulo = "Panel Principal - " + u.getRol();
            }

            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlDestino));
            Parent root = loader.load();
            
            // Cambia la escena usando el Stage actual del botón
            Stage stage = (Stage) btnIngresar.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();

        } catch (Exception e) {
            System.out.println("Error en redirección: " + e.getMessage());
            lblMensajeError.setText("Login correcto. (Falta crear el FXML de destino final).");
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
