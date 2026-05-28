
package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import javafx.event.ActionEvent;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.util.Duration;

import condominio.proyecto_condominio.logic.RegistroPagoCuotaLogic;

import condominio.proyecto_condominio.model.Propietario;
import condominio.proyecto_condominio.model.PagoCuota;

public class RegistroPagoViewController implements Initializable {

    @FXML
    private ComboBox<String> cbCasa;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCuota;

    @FXML
    private ComboBox<String> cbMes;

    @FXML
    private ComboBox<String> cbAnio;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnRegresar;

        private RegistroPagoCuotaLogic registroLogic
                = new RegistroPagoCuotaLogic();

    private final String[] meses = {
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        int anioActual = LocalDate.now().getYear();

        cbAnio.setValue(
                String.valueOf(anioActual)
        );

        cargarCasas();
    }

    @FXML
    private void registrarPago(ActionEvent event) {

        if (cbCasa.getValue() == null) {

            mostrarAdvertencia(
                    "Validación de Registro",
                    "No se ha seleccionado una casa",
                    "Por favor, seleccione una casa antes de registrar el pago."
            );

            return;
        }

        if (cbMes.getValue() == null
                || cbAnio.getValue() == null) {

            mostrarAdvertencia(
                    "Validación de Registro",
                    "Mes o año no seleccionado",
                    "Por favor, seleccione un mes y un año antes de registrar el pago."
            );

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        int mesSeleccionado
                = obtenerNumeroMes(
                        cbMes.getValue()
                );

        int anioSeleccionado
                = Integer.parseInt(
                        cbAnio.getValue()
                );
        String validacion
        = registroLogic.validarPago(
                numeroCasa,
                mesSeleccionado,
                anioSeleccionado
        );

if (validacion != null) {

    mostrarAdvertencia(
            "Validación",
            "No se pudo registrar el pago",
            validacion
    );

    return;
}

boolean registrado
        = registroLogic.registrarPago(
                numeroCasa,
                mesSeleccionado,
                anioSeleccionado
        );

        if (registrado) {

            Alert alerta = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alerta.setTitle(
                    "Registro Exitoso"
            );

            alerta.setHeaderText(
                    "¡Registro Exitoso!"
            );

            alerta.setContentText(
                    "¿Desea imprimir el recibo?"
            );

            ButtonType btnSi
                    = new ButtonType("Sí");

            ButtonType btnNo
                    = new ButtonType("No");

            alerta.getButtonTypes().setAll(
                    btnSi,
                    btnNo
            );

            alerta.showAndWait();

            cargarMesesPendientes();

        } else {

            mostrarAdvertencia(
                    "Error",
                    "No se pudo registrar el pago",
                    "Ocurrió un error al guardar el pago."
            );
        }
    }

    private void cargarCasas() {

        cbCasa.getItems().addAll(
                registroLogic.obtenerCasas()
        );
    }

    private void cargarMesesPendientes() {

        cbMes.getItems().clear();

        if (cbCasa.getValue() == null) {

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        int anio
                = Integer.parseInt(
                        cbAnio.getValue()
                );

        cbMes.getItems().addAll(
        registroLogic.obtenerMesesPendientes(
                numeroCasa,
                anio
        )
        );

        if (cbMes.getItems().isEmpty()) {

            Alert alerta = new Alert(
                    Alert.AlertType.INFORMATION
            );

            alerta.setTitle(
                    "Sin cuotas pendientes"
            );

            alerta.setHeaderText(
                    "No quedan cuotas pendientes"
            );

            alerta.setContentText(
                    "No quedan cuotas pendientes por pagar."
            );

            alerta.getDialogPane().setPrefWidth(400);

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(5),
                            event -> alerta.close()
                    )
            );

            timeline.setCycleCount(1);

            timeline.play();

            alerta.showAndWait();
        }
    }

    @FXML
    private void seleccionarCasa() {

        if (cbCasa.getValue() == null) {

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        Propietario propietario
                = registroLogic.obtenerPropietario(
                        numeroCasa
                );

        if (propietario == null) {

            return;
        }

        String nombre
                = propietario.getPrimerNombre();

        if (propietario.getSegundoNombre() != null) {

            nombre += " "
                    + propietario.getSegundoNombre();
        }

        if (propietario.getTercerNombre() != null) {

            nombre += " "
                    + propietario.getTercerNombre();
        }

        String apellido
                = propietario.getPrimerApellido();

        if (propietario.getSegundoApellido() != null) {

            apellido += " "
                    + propietario.getSegundoApellido();
        }

        txtNombre.setText(nombre);

        txtApellido.setText(apellido);

        String telefono
                = propietario.getTelefono();

        if (telefono != null
                && telefono.length() == 8) {

            telefono = telefono.substring(0, 4)
                    + "-"
                    + telefono.substring(4);
        }

        txtTelefono.setText(telefono);

        double cuotaActual
        = registroLogic.obtenerCuotaActual();

txtCuota.setText(
        "Q"
        + String.format(
                "%,.2f",
                cuotaActual
        )
);

        cargarMesesPendientes();
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.close();
    }

    private int obtenerNumeroMes(String mes) {

        for (int i = 0; i < meses.length; i++) {

            if (meses[i].equals(mes)) {

                return i + 1;
            }
        }

        return 0;
    }

    private void mostrarAdvertencia(
            String titulo,
            String encabezado,
            String mensaje
    ) {

        Alert alerta = new Alert(
                Alert.AlertType.WARNING
        );

        alerta.setTitle(titulo);

        alerta.setHeaderText(encabezado);

        alerta.setContentText(
                mensaje
                + "\n\n"
                + "La ventana se cerrará automáticamente en 5 segundos."
                + "\n"
                + "También puede presionar OK para continuar."
        );

        alerta.getDialogPane().setPrefWidth(450);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        event -> alerta.close()
                )
        );

        timeline.setCycleCount(1);

        timeline.play();

        alerta.showAndWait();
    }
}