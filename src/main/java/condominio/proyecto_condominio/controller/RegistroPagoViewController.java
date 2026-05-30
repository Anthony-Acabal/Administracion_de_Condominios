package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.time.LocalDate;
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

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

import condominio.proyecto_condominio.logic.RegistroPagoCuotaLogic;
import condominio.proyecto_condominio.model.Propietario;

import condominio.proyecto_condominio.service.CorreoService;
import condominio.proyecto_condominio.service.ReporteService;

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

    @FXML
    private Button btnHistorial;

    private RegistroPagoCuotaLogic registroLogic = new RegistroPagoCuotaLogic();

    private CorreoService correoService = new CorreoService();

    private String correoPropietario;
    private int idPropietario;

    private final String[] meses = {
        "Enero", "Febrero", "Marzo", "Abril",
        "Mayo", "Junio", "Julio", "Agosto",
        "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        int anioActual = LocalDate.now().getYear();

        cbAnio.setValue(String.valueOf(anioActual));

        cargarCasas();
    }

    // ---------------- HISTORIAL ----------------
    @FXML
    private void abrirHistorial(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/HistorialPagosView.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) btnHistorial.getScene().getWindow();

            stage.setScene(new Scene(root));
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // ---------------- REGISTRAR PAGO ----------------
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

        if (cbMes.getValue() == null || cbAnio.getValue() == null) {

            mostrarAdvertencia(
                    "Validación de Registro",
                    "Mes o año no seleccionado",
                    "Por favor, seleccione un mes y un año antes de registrar el pago."
            );
            return;
        }

        int numeroCasa = Integer.parseInt(cbCasa.getValue());
        int mesSeleccionado = obtenerNumeroMes(cbMes.getValue());
        int anioSeleccionado = Integer.parseInt(cbAnio.getValue());

        String validacion = registroLogic.validarPago(
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

        int idPagoCuota = registroLogic.registrarPago(
                numeroCasa,
                mesSeleccionado,
                anioSeleccionado
        );

        if (idPagoCuota > 0) {

            try {

                // 🔥 Generar PDF
                ReporteService reporteService = new ReporteService();
                String rutaPDF = reporteService.generarRecibo(idPropietario, idPagoCuota);

                // 🔥 Enviar correo al propietario
                if (correoPropietario != null && !correoPropietario.isEmpty()) {

                    correoService.enviarCorreo(
                            rutaPDF,
                            correoPropietario
                    );

                } else {
                    mostrarAdvertencia(
                            "Correo",
                            "No se envió correo",
                            "El propietario no tiene correo registrado."
                    );
                }

            } catch (Exception e) {

                e.printStackTrace();

                mostrarAdvertencia(
                        "Correo",
                        "Error al enviar comprobante",
                        e.getMessage()
                );
            }

            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);

            alerta.setTitle("Registro Exitoso");
            alerta.setHeaderText("¡Registro Exitoso!");
            alerta.setContentText("¿Desea imprimir el recibo?");

            ButtonType btnSi = new ButtonType("Sí");
            ButtonType btnNo = new ButtonType("No");

            alerta.getButtonTypes().setAll(btnSi, btnNo);

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

    // ---------------- CARGAR CASAS ----------------
    private void cargarCasas() {

        cbCasa.getItems().addAll(
                registroLogic.obtenerCasas()
        );
    }

    // ---------------- CASA SELECCIONADA ----------------
    @FXML
    private void seleccionarCasa() {

        if (cbCasa.getValue() == null) {
            return;
        }

        int numeroCasa = Integer.parseInt(cbCasa.getValue());

        Propietario propietario = registroLogic.obtenerPropietario(numeroCasa);

        if (propietario == null) {
            return;
        }

        String nombre = propietario.getPrimerNombre();

        if (propietario.getSegundoNombre() != null) {
            nombre += " " + propietario.getSegundoNombre();
        }

        if (propietario.getTercerNombre() != null) {
            nombre += " " + propietario.getTercerNombre();
        }

        String apellido = propietario.getPrimerApellido();

        if (propietario.getSegundoApellido() != null) {
            apellido += " " + propietario.getSegundoApellido();
        }

        // ✅ correo del propietario
        correoPropietario = propietario.getCorreoElectronico();
        idPropietario = propietario.getIdPropietario();

        txtNombre.setText(nombre);
        txtApellido.setText(apellido);

        String telefono = propietario.getTelefono();

        if (telefono != null && telefono.length() == 8) {
            telefono = telefono.substring(0, 4) + "-" + telefono.substring(4);
        }

        txtTelefono.setText(telefono);

        double cuotaActual = registroLogic.obtenerCuotaActual();

        txtCuota.setText("Q" + String.format("%,.2f", cuotaActual));

        cargarMesesPendientes();
    }

    // ---------------- MESES PENDIENTES ----------------
    private void cargarMesesPendientes() {

        cbMes.getItems().clear();

        if (cbCasa.getValue() == null) {
            return;
        }

        int numeroCasa = Integer.parseInt(cbCasa.getValue());
        int anio = Integer.parseInt(cbAnio.getValue());

        cbMes.getItems().addAll(
                registroLogic.obtenerMesesPendientes(numeroCasa, anio)
        );

        if (cbMes.getItems().isEmpty()) {

            Alert alerta = new Alert(Alert.AlertType.INFORMATION);

            alerta.setTitle("Sin cuotas pendientes");
            alerta.setHeaderText("No quedan cuotas pendientes");
            alerta.setContentText("No quedan cuotas pendientes por pagar.");

            Timeline timeline = new Timeline(
                    new KeyFrame(Duration.seconds(5), e -> alerta.close())
            );

            timeline.setCycleCount(1);
            timeline.play();

            alerta.showAndWait();
        }
    }

    // ---------------- UTILIDADES ----------------
    @FXML
    private void cerrarVentana(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource())
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

        Alert alerta = new Alert(Alert.AlertType.WARNING);

        alerta.setTitle(titulo);
        alerta.setHeaderText(encabezado);
        alerta.setContentText(
                mensaje
                + "\n\nLa ventana se cerrará automáticamente en 5 segundos."
        );

        alerta.getDialogPane().setPrefWidth(450);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(5), e -> alerta.close())
        );

        timeline.setCycleCount(1);
        timeline.play();

        alerta.showAndWait();
    }
}
