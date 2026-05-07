/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_condominio.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
/**
 * FXML Controller class
 *
 * @author yecut
 */
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
    private Button btnLimpiar;

    @FXML
    private Button btnImprimir;

    @FXML
    private Label lblEstado;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
