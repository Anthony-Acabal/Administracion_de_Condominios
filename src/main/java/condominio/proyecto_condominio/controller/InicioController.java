package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.logic.InicioLogic;

import condominio.proyecto_condominio.model.Fecha;

import java.io.IOException;

import java.net.URL;

import java.util.ResourceBundle;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;

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

    InicioLogic logic = new InicioLogic();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        mostrarBienvenida("Usuario");

        cargarFechaActual();
    }

    public void setUsuarioLogueado(String administrador) {
        mostrarBienvenida(administrador);
    }

    private void mostrarBienvenida(String administrador) {

        lblBienvenida.setText(
                "Bienvenido, " + administrador
        );
    }

    private void cargarFechaActual() {

        lblFecha.setText(
                Fecha.obtenerFechaActualFormateada()
        );
    }

    @FXML
    private void irPropietarios(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/RegistroPropietario.fxml",
                "Registro de Propietarios");
    }

    @FXML
    private void irPagos(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/RegistroPagoView.fxml",
                "Registro de Pagos");
    }

    @FXML
    private void irCuota(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/ConfiguracionCuotaView.fxml",
                "Configuración de Cuota");
    }

    @FXML
    private void irGastos(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Gastos.fxml",
                "Gastos");
    }

    @FXML
    private void irVisitantes(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Visitantes.fxml",
                "Visitantes");
    }

    @FXML
    private void irReportes(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Reportes.fxml",
                "Reportes");
    }

    @FXML
    private void irEstadosCuenta(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Estado_CuentaporCasa.fxml",
                "Estados de Cuenta");
    }

    @FXML
    private void irReporteGeneral(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Reporte_General.fxml",
                "Reporte General");
    }

    @FXML
    private void irCasasMorosas(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Casas_Morosas.fxml",
                "Casas Morosas");
    }

    @FXML
    private void irInicio(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Inicio.fxml",
                "Panel de Inicio");
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Login.fxml",
                "Login");
    }

    @FXML
    private void irConfiguracion(ActionEvent event) {
        cambiarPantalla(event,
                "/condominio/proyecto_condominio/ui/Configuracion.fxml",
                "Configuración");
    }

    private void cambiarPantalla(
            ActionEvent event,
            String fxmlArchivo,
            String titulo
    ) {

        try {

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            logic.cambiarPantalla(
                    stage,
                    fxmlArchivo,
                    titulo
            );

        } catch (IOException e) {

            System.out.println(
                    "Error al cambiar pantalla: "
                    + e.getMessage()
            );
        }
    }

}