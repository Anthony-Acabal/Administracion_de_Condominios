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
import javafx.scene.chart.PieChart;
import proyecto_condominio.model.ReporteGeneral;
import proyecto_condominio.model.ReporteGeneralDAO;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ReporteGeneralController implements Initializable {

    @FXML private TableView<ReporteGeneral> tbReporteGeneral;
    @FXML private TableColumn<ReporteGeneral, Integer> colNumeroCasa;
    @FXML private TableColumn<ReporteGeneral, String> colNombrePropietario;
    @FXML private TableColumn<ReporteGeneral, String> colEstadoActual;
    @FXML private TableColumn<ReporteGeneral, Double> colTotalPagado;
    @FXML private Label lblTotalRecaudado;
    @FXML private Button btnImprimirReporte;
    @FXML private PieChart charGraficaReporteGeneral;

    private ReporteGeneralDAO reporteDAO = new ReporteGeneralDAO();
    private ObservableList<ReporteGeneral> listaReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        cargarDatos();
    }

    private void configurarColumnas() {
        colNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        colNombrePropietario.setCellValueFactory(new PropertyValueFactory<>("nombrePropietario"));
        colEstadoActual.setCellValueFactory(new PropertyValueFactory<>("estadoActual"));
        colTotalPagado.setCellValueFactory(new PropertyValueFactory<>("totalPagado"));
    }

    private void cargarDatos() {
        List<ReporteGeneral> datos = reporteDAO.obtenerReporteGeneral();
        listaReporte = FXCollections.observableArrayList(datos);
        tbReporteGeneral.setItems(listaReporte);

        double[] resumen = reporteDAO.obtenerResumenMensual();
        double recaudado = resumen[0];
        double esperado = resumen[1];
        double pendiente = Math.max(0, esperado - recaudado);

        lblTotalRecaudado.setText(String.format("Total recaudado del mes: $%.2f / esperado: $%.2f", recaudado, esperado));

        actualizarGrafica(recaudado, pendiente);
    }

    private void actualizarGrafica(double recaudado, double pendiente) {
        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList(
                new PieChart.Data("Recaudado ($" + String.format("%.2f", recaudado) + ")", recaudado),
                new PieChart.Data("Pendiente ($" + String.format("%.2f", pendiente) + ")", pendiente)
        );
        charGraficaReporteGeneral.setData(pieChartData);
    }
}

