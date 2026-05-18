package proyecto_condominio.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import proyecto_condominio.model.CasasPendientesPago;
import proyecto_condominio.model.CasasPendientesPagoDAO;

public class CasasPendientesPagoController implements Initializable {

    @FXML private Label lblCPPFecha;
    @FXML private TableView<CasasPendientesPago> tbCasasPendientesPago;
    @FXML private TableColumn<CasasPendientesPago, Integer> colCPPNumeroCasa;
    @FXML private TableColumn<CasasPendientesPago, String> colCPPNombrePropietario;
    @FXML private TableColumn<CasasPendientesPago, String> colCPPTelefono;
    @FXML private Label lblCPPCantidad;
    @FXML private Button btnCPPRegresar;
    @FXML private Button btnCPPGenerarReporte;

    private CasasPendientesPagoDAO casasDAO = new CasasPendientesPagoDAO();
    private ObservableList<CasasPendientesPago> listaCasas;

    private final String[] nombresMeses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        colCPPNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        colCPPNombrePropietario.setCellValueFactory(new PropertyValueFactory<>("nombrePropietario"));
        colCPPTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
    }

    private void cargarDatos() {
        LocalDate fechaActual = LocalDate.now();
        int mes = fechaActual.getMonthValue();
        int anio = fechaActual.getYear();

        // Actualizar etiqueta de fecha
        String nombreMes = nombresMeses[mes - 1];
        lblCPPFecha.setText("Cuota de " + nombreMes + " del " + anio);

        // Obtener datos del DAO
        List<CasasPendientesPago> datos = casasDAO.obtenerCasasPendientes(mes, anio);
        listaCasas = FXCollections.observableArrayList(datos);
        tbCasasPendientesPago.setItems(listaCasas);

        // Actualizar etiqueta de cantidad
        lblCPPCantidad.setText("Casas pendientes de pago: " + datos.size());
    }
}
