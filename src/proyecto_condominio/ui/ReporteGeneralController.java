package proyecto_condominio.ui;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.ResourceBundle;
/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Marcos Antonio
 */
public class ReporteGeneralController implements Initializable {
    @FXML private TableView<?> tbReporteGeneral;
    @FXML private TableColumn<?, ?> colNumeroCasa;
    @FXML private TableColumn<?, ?> colNombrePropietario;
    @FXML private TableColumn<?, ?> colEstadoActual;
    @FXML private TableColumn<?, ?> colTotalPagado;
    @FXML private Label lblTotalRecaudado;
    @FXML private Button btnImprimirReporte;
    @FXML private Button btnVerGrafica;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Ventana de Reporte General cargada correctamente.");
    }
}
