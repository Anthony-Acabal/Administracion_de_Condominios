package proyecto_condominio.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */



import proyecto_condominio.model.fecha;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class InicioController implements Initializable {
    
    @FXML
    private Label lblBienvenida;
    
    @FXML
    private Label lblFecha;
    
    @FXML
    private BorderPane vistasContenedor;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarBienvenida("Usuario"); 
        cargarFechaActual();
    }    

    public void setUsuarioLogueado(String administrador) {
        mostrarBienvenida(administrador);
    }

    private void mostrarBienvenida(String administrador) {
        lblBienvenida.setText("Bienvenido, " + administrador);
    }

    private void cargarFechaActual() {
        lblFecha.setText(fecha.obtenerFechaActualFormateada());
    }

    @FXML
    private void irPropietarios(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Registro_Propietario.fxml", "Registro de Propietarios");
    }

    @FXML
    private void irPagos(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Registro_Pagos.fxml", "Registro de Pagos");
    }

    @FXML
    private void irCuota(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Configuracion_Cuota.fxml", "Configuración de Cuota");
    }

    @FXML
    private void irGastos(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Gastos.fxml", "Gastos");
    }

    @FXML
    private void irVisitantes(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Visitantes.fxml", "Visitantes");
    }

    @FXML
    private void irReportes(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Reportes.fxml", "Reportes");
    }

    @FXML
    private void irEstadosCuenta(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Estado_CuentaporCasa.fxml", "Estados de Cuenta");
    }

    @FXML
    private void irReporteGeneral(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Reporte_General.fxml", "Reporte General");
    }

    @FXML
    private void irCasasMorosas(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Casas_Morosas.fxml", "Casas Morosas");
    }

    @FXML
    private void irInicio(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Inicio.fxml", "Panel de Inicio");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Login.fxml", "Login");
    }
    
    @FXML
    private void irConfiguracion(ActionEvent event) {
        cambiarPantallaCompleta(event, "/proyecto_condominio/ui/Configuracion.fxml", "Configuración");
    }
    
    private void cambiarPantallaCompleta(ActionEvent event, String fxmlArchivo, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlArchivo));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
            stage.show();
        } catch (IOException e) {
            System.out.println("X Error al cambiar de pantalla: " + e.getMessage());
        }
    }
}