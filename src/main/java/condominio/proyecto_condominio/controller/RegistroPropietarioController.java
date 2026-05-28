package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.logic.RegistroPropietarioLogic;
import condominio.proyecto_condominio.model.Propietario;

import java.io.IOException;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

public class RegistroPropietarioController {

    @FXML
    private TextField txtPrimerNombre;

    @FXML
    private TextField txtSegundoNombre;

    @FXML
    private TextField txtTercerNombre;

    @FXML
    private TextField txtPrimerApellido;

    @FXML
    private TextField txtSegundoApellido;

    @FXML
    private ComboBox<String> cmbNumeroCasa;

    @FXML
    private TextField txtNumeroTelefono;

    @FXML
    private TextField txtCorreoElectronico;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnCancelar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnRegresar;

    RegistroPropietarioLogic logic = new RegistroPropietarioLogic();

    @FXML
    void actionGuardar(ActionEvent event) {

        String numeroCasaTexto = cmbNumeroCasa.getValue();
        String numeroCasa = numeroCasaTexto.replaceAll("[^0-9]", "");

        Propietario propietario = new Propietario();

        propietario.setPrimerNombre(txtPrimerNombre.getText().trim());
        propietario.setSegundoNombre(txtSegundoNombre.getText().trim());
        propietario.setTercerNombre(txtTercerNombre.getText().trim());

        propietario.setPrimerApellido(txtPrimerApellido.getText().trim());
        propietario.setSegundoApellido(txtSegundoApellido.getText().trim());

        propietario.setTelefono(txtNumeroTelefono.getText().trim());

        propietario.setCorreoElectronico(txtCorreoElectronico.getText().trim());

        propietario.setNumeroCasa(Integer.parseInt(numeroCasa));

        String validacion = logic.validarPropietario(propietario);

        if (validacion != null) {

            mostrarAlerta(
                    "Validación",
                    validacion,
                    AlertType.WARNING
            );

            return;
        }

        boolean guardado = logic.guardarPropietario(propietario);

        if (guardado) {

            mostrarAlerta(
                    "Registro Exitoso",
                    "Propietario guardado correctamente.",
                    AlertType.INFORMATION
            );

            actionLimpiar(event);

        } else {

            mostrarAlerta(
                    "Error",
                    "No se pudo guardar el propietario.",
                    AlertType.ERROR
            );
        }
    }

    @FXML
    void actionCancelar(ActionEvent event) {
        actionLimpiar(event);
        actionRegresar(event);
    }

    @FXML
    void actionLimpiar(ActionEvent event) {

        txtPrimerNombre.clear();
        txtSegundoNombre.clear();
        txtTercerNombre.clear();

        txtPrimerApellido.clear();
        txtSegundoApellido.clear();

        txtNumeroTelefono.clear();
        txtCorreoElectronico.clear();

        cmbNumeroCasa.getSelectionModel().clearSelection();
    }

    @FXML
    void actionRegresar(ActionEvent event) {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/condominio/proyecto_condominio/ui/Inicio.fxml")
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.setScene(new Scene(root));

            stage.setTitle("Panel de Inicio");

            stage.show();

        } catch (IOException e) {

            mostrarAlerta(
                    "Error",
                    "No se pudo regresar.",
                    AlertType.ERROR
            );
        }
    }

    private void mostrarAlerta(
            String titulo,
            String mensaje,
            AlertType tipo
    ) {

        Alert alert = new Alert(tipo);

        alert.setTitle(titulo);

        alert.setHeaderText(null);

        alert.setContentText(mensaje);

        alert.showAndWait();
    }

    @FXML
    void initialize() {

        for (int i = 1; i <= 30; i++) {
            cmbNumeroCasa.getItems().add(
                    String.format("Casa %02d", i)
            );
        }
    }
}