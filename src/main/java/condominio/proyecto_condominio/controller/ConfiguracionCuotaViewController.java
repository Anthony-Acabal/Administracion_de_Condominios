package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.logic.ConfiguracionCuotaLogic;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

public class ConfiguracionCuotaViewController
        implements Initializable {

    private ConfiguracionCuotaLogic cuotaLogic = new ConfiguracionCuotaLogic();

    @FXML
    private TextField txtCuotaMensual;

    @FXML
    private TextField txtNuevaCuota;

    @FXML
    private TextField txtRecaudacionMensual;

    @FXML
    private DatePicker dpFechaModificacion;

    @FXML
    private TextField lblHora;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnRegresar;

    @FXML
    private Button btnCancelar;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        cargarDatos();

        txtNuevaCuota
                .textProperty()
                .addListener((obs, oldV, newV) -> {

                    if (!newV.matches("\\d*")) {

                        txtNuevaCuota.setText(oldV);
                    }
                });

        dpFechaModificacion.setValue(LocalDate.now());

        lblHora.setText(
                LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern("HH:mm")
                        )
        );

        txtCuotaMensual.setEditable(false);
        txtRecaudacionMensual.setEditable(false);
        lblHora.setEditable(false);

        dpFechaModificacion.setEditable(false);
        dpFechaModificacion.setMouseTransparent(true);

        btnGuardar.requestFocus();
    }

    private void cargarDatos() {

        double cuotaActual = cuotaLogic.obtenerCuotaActual();

        txtCuotaMensual.setText("Q" + cuotaActual);

        double recaudacion = cuotaLogic.calcularRecaudacion();

        txtRecaudacionMensual.setText(
                "Q"
                + String.format(
                        "%,.2f",
                        recaudacion
                )
        );
    }

    @FXML
    private void guardarConfiguracion() {

        btnGuardar.setDisable(true);

        double cuotaActual = Double.parseDouble(
                txtCuotaMensual
                        .getText()
                        .replace("Q", "")
        );

        String validacion = cuotaLogic.validarCuota(
                txtNuevaCuota.getText(),
                cuotaActual
        );

        if (validacion != null) {

            mostrarAdvertencia(
                    "Validación",
                    "Error",
                    validacion
            );

            btnGuardar.setDisable(false);

            return;
        }

        boolean guardado = cuotaLogic.guardarCuota(
                Double.parseDouble(txtNuevaCuota.getText()),
                1
        );

        if (guardado) {

            mostrarExito();

            cargarDatos();

            txtNuevaCuota.clear();

        } else {

            mostrarAdvertencia(
                    "Error",
                    "No se pudo guardar",
                    "Ocurrió un error al guardar."
            );
        }

        btnGuardar.setDisable(false);
    }

    @FXML
    private void regresarMenu() {

        Stage stage = (Stage) btnRegresar
                .getScene()
                .getWindow();

        stage.close();
    }

    private void mostrarAdvertencia(
            String titulo,
            String encabezado,
            String mensaje
    ) {

        Alert alerta = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle(titulo);

        alerta.setHeaderText(encabezado);

        alerta.setContentText(mensaje);

        alerta.show();

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        e -> alerta.close()
                )
        );

        timeline.setCycleCount(1);

        timeline.play();
    }

    private void mostrarExito() {

        Alert alerta = new Alert(Alert.AlertType.INFORMATION);

        alerta.setTitle("Éxito");

        alerta.setHeaderText("Cuota actualizada");

        alerta.setContentText(
                "La cuota fue actualizada correctamente."
        );

        alerta.show();

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        e -> alerta.close()
                )
        );

        timeline.setCycleCount(1);

        timeline.play();
    }
}