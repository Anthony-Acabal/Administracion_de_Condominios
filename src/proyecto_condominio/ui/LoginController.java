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
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import proyecto_condominio.model.ConexionSQL;
import proyecto_condominio.model.Usuario;
import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.scene.Node;

public class LoginController extends Application {

    // Componentes visuales mapeados desde el FXML
    @FXML private HBox boxCorreo;
    @FXML private HBox boxContrasena;
    @FXML private TextField txtCorreo;
    @FXML private PasswordField txtContrasena;
    
    // Nuevos componentes para la funcionalidad de revelar contraseña
    @FXML private TextField txtContrasenaVisible;
    @FXML private Button btnMostrarContrasena;
    @FXML private ImageView imgOjito;
    
    @FXML private Button btnIngresar;
    @FXML private Label lblMensajeError;
    
    // Variables de control interno
    private int intentosFallidos = 0; 
    private boolean esContrasenaVisible = false;
    
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/proyecto_condominio/ui/Login.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Sistema de Administración de Condominios - Iniciar Sesión");
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
    
    @FXML
    private void accionIngresar() {
        // 1. Control de bloqueo preventivo inmediato
        if (intentosFallidos >= 3) {
            lblMensajeError.setText("Acceso denegado. Superó el límite de 3 intentos.");
            btnIngresar.setDisable(true);
            return;
        }

        // Limpiar estilos de error y mensajes previos antes de validar
        boxCorreo.getStyleClass().remove("campo-error");
        boxContrasena.getStyleClass().remove("campo-error");
        lblMensajeError.setText("");

        String correo = txtCorreo.getText().trim();
        
        // Obtener la clave dinámicamente según qué campo esté activo
        String clave = esContrasenaVisible ? txtContrasenaVisible.getText().trim() : txtContrasena.getText().trim();

        // Validar campos vacíos
        if (correo.isEmpty() || clave.isEmpty()) {
            lblMensajeError.setText("Por favor, complete los campos.");
            if (correo.isEmpty()) boxCorreo.getStyleClass().add("campo-error");
            if (clave.isEmpty()) boxContrasena.getStyleClass().add("campo-error");
            return;
        }

        ConexionSQL querys = new ConexionSQL();
        Usuario user = querys.validarLogin(correo, clave);

        if (user != null) {
            if (user.isBloqueado()) { 
                lblMensajeError.setText("Cuenta bloqueada. Use '¿Olvidó su contraseña?'.");
                boxCorreo.getStyleClass().add("campo-error");
                return;
            }
            
            // Si el login es correcto, reiniciamos el contador por seguridad
            intentosFallidos = 0;
            
            // Si la consulta fue exitosa, procesa el cambio de vista directo
            procesarDireccionamiento(user);
        } else {
            // Incrementar contador si las credenciales fallan
            intentosFallidos++;
            
            boxCorreo.getStyleClass().add("campo-error");
            boxContrasena.getStyleClass().add("campo-error");

            // Control exacto de los 3 intentos solicitados
            if (intentosFallidos >= 3) {
                lblMensajeError.setText("Acceso denegado. Superó el límite de 3 intentos.");
                btnIngresar.setDisable(true);
            } else {
                int intentosRestantes = 3 - intentosFallidos;
                lblMensajeError.setText("Credenciales incorrectas. Le quedan " + intentosRestantes + " intentos.");
            }
        }
    }
    
    @FXML
    private void accionMostrarContrasena() {
        if (!esContrasenaVisible) {
            // Copiar el texto oculto al campo plano y alternar visibilidad
            txtContrasenaVisible.setText(txtContrasena.getText());
            txtContrasenaVisible.setVisible(true);
            txtContrasena.setVisible(false);
            esContrasenaVisible = true;
        } else {
            // Copiar el texto plano de vuelta al campo oculto y alternar visibilidad
            txtContrasena.setText(txtContrasenaVisible.getText());
            txtContrasena.setVisible(true);
            txtContrasenaVisible.setVisible(false);
            esContrasenaVisible = false;
        }
    }
    
    @FXML
    private void accionOlvidoContrasena(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto_condominio/ui/OlvidoContrasena.fxml"));
            Parent root = loader.load();
            
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
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

            // Redirección directa por roles
            String rol = user.getRol().trim(); 
            
            switch (rol) {
                case "1": // Rol Administrador
                    rutaFxml = "/proyecto_condominio/ui/MenuAdmin.fxml";
                    tituloVentana = "Panel de Control - Administrador";
                    break;
                    
                default: // Ruta por defecto
                    rutaFxml = "/proyecto_condominio/ui/MenuAdmin.fxml";
                    tituloVentana = "Sistema de Administración de Condominios";
                    break;
            }

            // SEGURIDAD: Validar que la URL del recurso exista antes de llamar a FXMLLoader
            java.net.URL urlRecurso = getClass().getResource(rutaFxml);
            if (urlRecurso == null) {
                System.err.println("\n[ERROR CRÍTICO]: No se encontró el archivo FXML en la ruta: " + rutaFxml);
                System.err.println("[SUGERENCIA]: Revisa si el archivo dentro de 'proyecto_condominio.ui' se escribe exactamente así, respetando mayúsculas y minúsculas.");
                lblMensajeError.setText("Error del sistema: Archivo de vista no encontrado.");
                return; // Detiene la ejecución limpia evitando que la app estalle en rojo
            }

            // Cargar la vista del menú de manera segura
            FXMLLoader loader = new FXMLLoader(urlRecurso);
            Parent root = loader.load();
            
            Stage stage = (Stage) txtCorreo.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(tituloVentana);
            stage.show();

        } catch (IOException e) {
            System.out.println("Error crítico al redireccionar por rol: " + e.getMessage());
            e.printStackTrace(); 
            lblMensajeError.setText("Error al cargar el menú correspondiente a su rol. Verifique MenuAdmin.fxml");
        }
    }
}