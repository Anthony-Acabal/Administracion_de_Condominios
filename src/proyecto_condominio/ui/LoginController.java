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

    public static void main(String[] args) {
    launch(args);
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
    
    private void procesarDireccionamiento(Usuario user) {
        try {
            String rutaFxml = "";
            String tituloVentana = "";

        // 1. CONTROL DE PRIMER INGRESO 
        if (user.isPrimerIngreso()) {
            rutaFxml = "/proyecto_condominio/ui/CambioContrasena.fxml";
            tituloVentana = "Actualizar Contraseña Obligatoria";
        } 
        // 2. REDIRECCIÓN POR ROLES 
        else {
            String rol = user.getRol().toUpperCase().trim(); 
            
            switch (rol) {
                case "ADMINISTRADOR":
                case "ADMIN":
                    rutaFxml = "/proyecto_condominio/ui/MenuAdmin.fxml";
                    tituloVentana = "Panel de Control - Administrador";
                    break;
                    
                case "RESIDENTE":
                    rutaFxml = "/proyecto_condominio/ui/MenuResidente.fxml";
                    tituloVentana = "Portal del Residente - Condominio";
                    break;
                    
                case "GUARDIA":
                case "SEGURIDAD":
                    rutaFxml = "/proyecto_condominio/ui/MenuGuardia.fxml";
                    tituloVentana = "Control de Accesos - Seguridad";
                    break;
                    
                default:
                    // Ventana genérica por si un usuario no tiene rol asignado o es diferente
                    rutaFxml = "/proyecto_condominio/ui/MenuPrincipal.fxml";
                    tituloVentana = "Sistema de Administración de Condominios";
                    break;
            }
        }

        // 3. CARGAR LA VISTA SELECCIONADA
        FXMLLoader loader = new FXMLLoader(getClass().getResource(rutaFxml));
        Parent root = loader.load();
        
        Stage stage = (Stage) txtCorreo.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle(tituloVentana);
        stage.show();

        } catch (IOException e) {
        System.out.println("Error crítico al redireccionar por rol: " + e.getMessage());
        lblMensajeError.setText("Error al cargar el menú correspondiente a su rol.");
        }
    }
}
