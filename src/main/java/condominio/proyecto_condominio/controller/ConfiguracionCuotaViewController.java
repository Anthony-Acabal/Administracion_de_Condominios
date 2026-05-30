package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.util.Duration;

import java.time.format.DateTimeFormatter;

import javafx.stage.Stage;

import javafx.application.Platform;

import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Rectangle;

import condominio.proyecto_condominio.dao.ConfiguracionCuotaDAO;

import condominio.proyecto_condominio.model.Cuota;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

/**
 * FXML Controller class
 *
 * @author yecut
 */
public class ConfiguracionCuotaViewController
        implements Initializable {

    private Cuota cuota = new Cuota();

    private ConfiguracionCuotaDAO cuotaDAO
            = new ConfiguracionCuotaDAO();

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

    @FXML
    private ScrollPane scrollPane;

    @FXML
    private AnchorPane cardCentral;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        cargarCuotaActual();
        txtNuevaCuota
                .textProperty()
                .addListener(
                        (observable, oldValue, newValue) -> {

                            if (!newValue.matches("\\d*")) {

                                txtNuevaCuota.setText(
                                        oldValue
                                );
                            }
                        }
                );

        dpFechaModificacion.setValue(
                java.time.LocalDate.now()
        );

        lblHora.setText(
                java.time.LocalTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "HH:mm"
                                )
                        )
        );

        txtCuotaMensual.setEditable(false);

        txtCuotaMensual
                .setFocusTraversable(false);

        txtCuotaMensual.deselect();

        txtRecaudacionMensual
                .setEditable(false);

        txtRecaudacionMensual
                .setFocusTraversable(false);

        dpFechaModificacion.setEditable(false);

        dpFechaModificacion.setMouseTransparent(true);

        lblHora.setEditable(false);

        btnGuardar.requestFocus();

        scrollPane.setStyle(
                "-fx-background: transparent;"
                + "-fx-background-color: transparent;"
        );
        Platform.runLater(() -> {

            scrollPane.setStyle("-fx-background-color: transparent;");

            scrollPane.lookup(".viewport")
                    .setStyle("-fx-background-color: transparent;");
        });

        Rectangle clip = new Rectangle();
        clip.widthProperty().bind(cardCentral.widthProperty());
        clip.heightProperty().bind(cardCentral.heightProperty());

        clip.setArcWidth(48);
        clip.setArcHeight(48);

        cardCentral.setClip(clip);
    }

    @FXML
    private void regresarMenu() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/Inicio.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) btnRegresar
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @FXML
    private void guardarConfiguracion() {

        btnGuardar.setDisable(true);

        if (txtNuevaCuota.getText() == null
                || txtNuevaCuota
                        .getText()
                        .trim()
                        .isEmpty()) {

            mostrarAdvertencia(
                    "Campo Vacío",
                    "No se ingresó una cuota",
                    "Debe ingresar un monto válido para actualizar la cuota."
            );

            btnGuardar.setDisable(false);

            return;
        }

        cuota.setMontoCuota(
                Integer.parseInt(
                        txtNuevaCuota.getText()
                )
        );

        if (cuota.getMontoCuota() <= 0) {

            mostrarAdvertencia(
                    "Monto inválido",
                    "La cuota no es válida",
                    "La cuota debe ser mayor a 0."
            );

            btnGuardar.setDisable(false);

            return;
        }

        if (cuota.getMontoCuota() > 100000) {

            mostrarAdvertencia(
                    "Monto demasiado alto",
                    "La cuota no puede exceder Q100,000.",
                    "Ingrese una nueva cuota."
            );

            btnGuardar.setDisable(false);

            return;
        }

        if (cuota.getMontoCuota()
                == Double.parseDouble(
                        txtCuotaMensual
                                .getText()
                                .replace("Q", "")
                )) {

            mostrarAdvertencia(
                    "Cuota sin cambios",
                    "La cuota ingresada ya existe",
                    "Debe ingresar un monto diferente\nal actual para realizar una actualización."
            );

            btnGuardar.setDisable(false);

            return;
        }

        int idUsuario = 1;

        boolean guardado
                = cuotaDAO.guardarCuota(
                        cuota,
                        idUsuario
                );

        if (guardado) {

            mostrarExito();

            cargarCuotaActual();

            dpFechaModificacion.setValue(
                    java.time.LocalDate.now()
            );

            lblHora.setText(
                    java.time.LocalTime.now()
                            .format(
                                    DateTimeFormatter.ofPattern(
                                            "HH:mm"
                                    )
                            )
            );

            txtNuevaCuota.clear();

        } else {

            mostrarAdvertencia(
                    "Error",
                    "No se pudo guardar la cuota",
                    "Ocurrió un error al actualizar la cuota."
            );
        }

        btnGuardar.setDisable(false);

        btnGuardar.requestFocus();
    }

    private void cargarCuotaActual() {

        int ultimaCuota = cuotaDAO.obtenerUltimaCuota();

        cuota.setMontoCuota(ultimaCuota);

        txtCuotaMensual.setText(
                "Q" + cuota.getMontoCuota()
        );

        calcularRecaudacion();
    }

    private void calcularRecaudacion() {

        int totalCasas
                = cuotaDAO.obtenerTotalCasas();

        double recaudacion
                = totalCasas
                * cuota.getMontoCuota();

        txtRecaudacionMensual.setText(
                "Q"
                + String.format(
                        "%,.2f",
                        recaudacion
                )
        );
    }

    private void mostrarAdvertencia(
            String titulo,
            String encabezado,
            String mensaje
    ) {

        Alert alerta
                = new Alert(
                        Alert.AlertType.WARNING
                );

        alerta.setTitle(titulo);

        alerta.setHeaderText(encabezado);

        alerta.setContentText(
                mensaje
                + "\n\nEsta ventana se cerrará en 5 segundos."
        );

        alerta.show();

        Timeline timeline
                = new Timeline(
                        new KeyFrame(
                                Duration.seconds(5),
                                event -> alerta.close()
                        )
                );

        timeline.setCycleCount(1);

        timeline.play();
    }

    private void mostrarExito() {

        Alert alerta
                = new Alert(
                        Alert.AlertType.INFORMATION
                );

        alerta.setTitle(
                "Cuota Actualizada"
        );

        alerta.setHeaderText(
                "Actualización exitosa"
        );

        alerta.setContentText(
                "La cuota mensual fue actualizada correctamente."
        );

        alerta.show();

        Timeline timeline
                = new Timeline(
                        new KeyFrame(
                                Duration.seconds(5),
                                event -> alerta.close()
                        )
                );

        timeline.setCycleCount(1);

        timeline.play();
    }
}
