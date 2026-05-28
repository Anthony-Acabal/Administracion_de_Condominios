package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.logic.RegistroPagoCuotaLogic;
import condominio.proyecto_condominio.model.Propietario;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class RegistroPagoViewController
        implements Initializable {

    private final RegistroPagoCuotaLogic logic
            = new RegistroPagoCuotaLogic();

    @FXML
    private ComboBox<String> cbCasa;

    @FXML
    private ComboBox<String> cbMes;

    @FXML
    private ComboBox<String> cbAnio;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCuota;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnRegresar;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        int anioActual = LocalDate.now().getYear();

        cbAnio.setValue(
                String.valueOf(anioActual)
        );

        cargarCasas();
    }

    private void cargarCasas() {

        cbCasa.getItems().addAll(
                logic.obtenerCasas()
        );
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
                = logic.obtenerPropietario(
                        numeroCasa
                );

        if (propietario != null) {

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

            txtTelefono.setText(
                    propietario.getTelefono()
            );

            txtCuota.setText("Q1500");

            cargarMesesPendientes();
        }
    }

    private void cargarMesesPendientes() {

        cbMes.getItems().clear();

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        int anio
                = Integer.parseInt(
                        cbAnio.getValue()
                );

        cbMes.getItems().addAll(
                logic.obtenerMesesPendientes(
                        numeroCasa,
                        anio
                )
        );
    }

    @FXML
    private void registrarPago(
            ActionEvent event
    ) {

        if (cbCasa.getValue() == null) {

            mostrarAdvertencia(
                    "Validación",
                    "Casa no seleccionada",
                    "Debe seleccionar una casa."
            );

            return;
        }

        if (cbMes.getValue() == null) {

            mostrarAdvertencia(
                    "Validación",
                    "Mes no seleccionado",
                    "Debe seleccionar un mes."
            );

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        int mes = logic.obtenerNumeroMes(
                cbMes.getValue()
        );

        int anio
                = Integer.parseInt(
                        cbAnio.getValue()
                );

        String validacion
                = logic.validarPago(
                        numeroCasa,
                        mes,
                        anio
                );

        if (validacion != null) {

            mostrarAdvertencia(
                    "Pago inválido",
                    "Error",
                    validacion
            );

            return;
        }

        boolean registrado
                = logic.registrarPago(
                        numeroCasa,
                        mes,
                        anio
                );

        if (registrado) {

            Alert alerta = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alerta.setTitle(
                    "Registro exitoso"
            );

            alerta.setHeaderText(
                    "Pago registrado"
            );

            alerta.setContentText(
                    "¿Desea imprimir el recibo?"
            );

            alerta.getButtonTypes().setAll(
                    new ButtonType("Sí"),
                    new ButtonType("No")
            );

            alerta.showAndWait();

            cargarMesesPendientes();

        } else {

            mostrarAdvertencia(
                    "Error",
                    "No se pudo registrar",
                    "Ocurrió un error al guardar."
            );
        }
    }

    @FXML
    private void cerrarVentana(
            ActionEvent event
    ) {

        Stage stage = (Stage)
                ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.close();
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

        alerta.setContentText(mensaje);

        alerta.show();

        Timeline timeline
                = new Timeline(
                        new KeyFrame(
                                Duration.seconds(5),
                                e -> alerta.close()
                        )
                );

        timeline.setCycleCount(1);

        timeline.play();
    }
}