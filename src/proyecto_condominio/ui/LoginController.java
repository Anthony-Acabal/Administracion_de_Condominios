package proyecto_condominio.ui;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import proyecto_condominio.model.ConexionSQL;
import proyecto_condominio.model.Usuario;

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
            // 1. FILTRO DE SEGURIDAD: Validar si la cuenta está bloqueada en la base de datos
            if (user.isBloqueado()) {
                lblMensajeError.setText("Esta cuenta está bloqueada por exceso de intentos.");
                System.out.println("Acceso denegado: El usuario " + correo + " se encuentra bloqueado.");
            } 
            // 2. Si no está bloqueado, procede con el direccionamiento normal (ITM-25.1)
            else {
                procesarDireccionamiento(user);
            }
        } else {
            // 3. Si validarLogin devolvió null, significa que la contraseña falló
            lblMensajeError.setText("Credenciales incorrectas. Intento fallido registrado.");
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
