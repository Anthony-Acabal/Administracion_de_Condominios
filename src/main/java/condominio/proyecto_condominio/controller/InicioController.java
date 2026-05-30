package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.model.Fecha;
import condominio.proyecto_condominio.logic.InicioLogic;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.fxml.Initializable;

public class InicioController implements Initializable {

    // --- NUEVO: Instancia estática para permitir el acceso desde otros controladores ---
    private static InicioController instancia;

    @FXML
    private Label lblBienvenida;
    @FXML
    private Label lblFecha;
    @FXML
    private BorderPane vistasContenedor;

    @FXML
    private Button irPropietarios;
    @FXML
    private Button irPagos;
    @FXML
    private Button irConfiguracion;
    @FXML
    private Button irEstadosCuenta;
    @FXML
    private Button irReporteGeneral;
    @FXML
    private Button irCasasMorosas;
    @FXML
    private Button cerrarSesion;

    private final InicioLogic logic = new InicioLogic();

    // --- NUEVO: Método estático público para recuperar este controlador ---
    public static InicioController getController() {
        return instancia;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Guardamos la referencia de esta vista activa en la variable estática
        instancia = this;

        mostrarBienvenida("ADMINISTRADOR");
        try {
            cargarFechaActual();
        } catch (Exception e) {
            System.out.println("⚠️ Error fecha: " + e.getMessage());
        }
    }

    public void setUsuarioLogueado(String administrador) {
        mostrarBienvenida(administrador);
    }

    private void mostrarBienvenida(String administrador) {
        if (lblBienvenida != null) {
            String textoBienvenida = logic.generarMensajeBienvenida(administrador);
            lblBienvenida.setText(textoBienvenida);
        }
    }

    private void cargarFechaActual() {
        if (lblFecha != null) {
            lblFecha.setText(Fecha.obtenerFechaActualFormateada());
        }
    }

    private void limpiarBotonesActivos() {
        if (irPropietarios != null) {
            irPropietarios.getStyleClass().remove("boton-activo");
        }
        if (irPagos != null) {
            irPagos.getStyleClass().remove("boton-activo");
        }
        if (irConfiguracion != null) {
            irConfiguracion.getStyleClass().remove("boton-activo");
        }
        if (irEstadosCuenta != null) {
            irEstadosCuenta.getStyleClass().remove("boton-activo");
        }
        if (irReporteGeneral != null) {
            irReporteGeneral.getStyleClass().remove("boton-activo");
        }
        if (irCasasMorosas != null) {
            irCasasMorosas.getStyleClass().remove("boton-activo");
        }
    }

    @FXML
    private void irPropietarios(ActionEvent event) {
        System.out.println("➔ [CLICK] Registro Propietario");
        limpiarBotonesActivos();
        irPropietarios.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/RegistroPropietario.fxml");
    }

    @FXML
    private void irPagos(ActionEvent event) {
        System.out.println("➔ [CLICK] Registro Pagos");
        limpiarBotonesActivos();
        irPagos.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/RegistroPagoView.fxml");
    }

    @FXML
    private void irConfiguracion(ActionEvent event) {
        System.out.println("➔ [CLICK] Configuración Cuota");
        limpiarBotonesActivos();
        irConfiguracion.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/ConfiguracionCuotaView.fxml");
    }

    @FXML
    private void irEstadosCuenta(ActionEvent event) {
        System.out.println("➔ [CLICK] Estados de Cuenta");
        limpiarBotonesActivos();
        irEstadosCuenta.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/HistorialPagosView.fxml");
    }

    @FXML
    private void irReporteGeneral(ActionEvent event) {
        System.out.println("➔ [CLICK] Reporte General");
        limpiarBotonesActivos();
        irReporteGeneral.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/ReporteGeneralView.fxml");
    }

    @FXML
    private void irCasasMorosas(ActionEvent event) {
        System.out.println("➔ [CLICK] Casas Morosas");
        limpiarBotonesActivos();
        irCasasMorosas.getStyleClass().add("boton-activo");
        cargarVistaEnCentro("/condominio/proyecto_condominio/ui/CasasPendientesPagoView.fxml");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println("➔ [CLICK] Botón Cerrar Sesión presionado");

        Alert alerta = new Alert(AlertType.CONFIRMATION);
        alerta.setTitle("Confirmación de Cierre");
        alerta.setHeaderText(null);
        alerta.setContentText("¿Estás seguro de cerrar sesión?");

        ButtonType botonSi = new ButtonType("Sí");
        ButtonType botonNo = new ButtonType("No");
        alerta.getButtonTypes().setAll(botonSi, botonNo);

        Optional<ButtonType> resultado = alerta.showAndWait();

        if (resultado.isPresent() && resultado.get() == botonSi) {
            System.out.println("➔ [CONFIRMADO] Cerrando sesión...");
            try {
                Stage stage = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource(
                                "/condominio/proyecto_condominio/ui/Login.fxml"
                        )
                );

                Parent root = loader.load();

                stage.getScene().setRoot(root);
                stage.setTitle("Login");
                stage.show();

            } catch (IOException e) {
                System.out.println("X Error al salir: " + e.getMessage());
            }
        } else {
            System.out.println("➔ [CANCELADO] El usuario canceló el cierre de sesión.");
        }
    }

    // --- MODIFICADO: De 'private' a 'public' para que otros controladores la usen al navegar ---
    public void cargarVistaEnCentro(String fxmlArchivo) {
        try {
            System.out.println("Cargando en el centro: " + fxmlArchivo);
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlArchivo));
            Parent root = loader.load();

            vistasContenedor.setCenter(root);
            System.out.println("✅ OK Cargado.");
        } catch (IOException e) {
            System.out.println("X Error FXML: " + e.getMessage());
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, "Error al cargar archivo FXML", e);
        } catch (Exception ex) {
            System.out.println("X Error: " + ex.getMessage());
            Logger.getLogger(InicioController.class.getName()).log(Level.SEVERE, "Error inesperado", ex);
        }
    }
}
