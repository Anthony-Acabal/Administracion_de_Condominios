package proyecto_condominio.controller;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */



import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class Registro_PropietarioController {

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
    
    @FXML
    void actionGuardar(ActionEvent event) {
        String primerNombre = txtPrimerNombre.getText();
        String segundoNombre = txtSegundoNombre.getText();
        String tercerNombre = txtTercerNombre.getText();
        String primerApellido = txtPrimerApellido.getText();
        String segundoApellido = txtSegundoApellido.getText();
        String telefono = txtNumeroTelefono.getText();
        String correo = txtCorreoElectronico.getText();
        Object casaSeleccionada = cmbNumeroCasa.getValue();

        if (primerNombre.isEmpty() || primerApellido.isEmpty() || telefono.isEmpty() || casaSeleccionada == null) {
            System.out.println("⚠️ Por favor, complete los campos obligatorios (Nombre, Apellido, Teléfono y Casa).");
            return;
        }

        String numeroCasa = casaSeleccionada.toString();

        System.out.println("PROPIETARIO REGISTRADO EXITOSAMENTE");
        System.out.println("Nombre: " + primerNombre + " " + primerApellido);
        System.out.println("Casa asignada: " + numeroCasa);
        System.out.println("Teléfono: " + telefono);
        
        
        actionLimpiar(event);
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/proyecto_condominio/ui/Inicio.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel de Inicio");
            stage.show();
        } catch (IOException e) {
            System.out.println("X Error al regresar al inicio: " + e.getMessage()); 
            
        }   
        
    }
    @FXML
    void initialize() {
        
        cmbNumeroCasa.getItems().addAll(
            "Casa 01",
            "Casa 02",
            "Casa 03",
            "Casa 04",
            "Casa 05",
            "Casa 06"
        );
    }
}