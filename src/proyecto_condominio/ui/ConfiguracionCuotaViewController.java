/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_condominio.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author yecut
 */
public class ConfiguracionCuotaViewController implements Initializable {
    @FXML
private TextField txtCuotaMensual;

@FXML
private TextField txtNuevaCuota;

@FXML
private TextField txtRecaudacionMensual;

@FXML
private TextField txtUsuario;

@FXML
private DatePicker dpFechaModificacion;

@FXML
private Label lblHora;

@FXML
private Button btnGuardar;

@FXML
private Button btnLimpiar;

@FXML
private Button btnCancelar;

@FXML
private Label lblEstado;

@FXML
private Button btnCerrar;


    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
}
