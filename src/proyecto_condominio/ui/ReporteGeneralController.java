package proyecto_condominio.ui;

import javafx.application.Platform;
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
import javafx.geometry.Side;
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
    @FXML private Label lblTotalRecaudadoMes;
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

        lblTotalRecaudadoMes.setText(String.format("Total recaudado del mes: $%.2f / esperado: $%.2f", recaudado, esperado));

        actualizarGrafica(recaudado, pendiente);
    }

    private void actualizarGrafica(double recaudado, double pendiente) {
        double total = recaudado + pendiente;
        if (total == 0) return;

        double porcRecaudado = (recaudado / total) * 100;
        double porcPendiente = (pendiente / total) * 100;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        PieChart.Data sliceRecaudado = new PieChart.Data(
            String.format("Recaudado: $%.2f (%.1f%%)", recaudado, porcRecaudado), 
            recaudado
        );
        pieChartData.add(sliceRecaudado);

        if (pendiente > 0) {
            PieChart.Data slicePendiente = new PieChart.Data(
                String.format("Pendiente: $%.2f (%.1f%%)", pendiente, porcPendiente), 
                pendiente
            );
            pieChartData.add(slicePendiente);
        }

        charGraficaReporteGeneral.setData(pieChartData);
        charGraficaReporteGeneral.setLabelsVisible(true);
        charGraficaReporteGeneral.setLegendVisible(true);
        charGraficaReporteGeneral.setLegendSide(Side.RIGHT);
        charGraficaReporteGeneral.setTitle(null);

        Platform.runLater(() -> {
            // Colores consistentes: Recaudado (Verde), Pendiente (Púrpura)
            String colorRecaudado = "#2ecc71";
            String colorPendiente = "#9b59b6";

            if (sliceRecaudado.getNode() != null) {
                sliceRecaudado.getNode().setStyle("-fx-pie-color: " + colorRecaudado + ";");
            }
            if (pendiente > 0 && pieChartData.size() > 1 && pieChartData.get(1).getNode() != null) {
                pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + colorPendiente + ";");
            }
            
            // Sincronizar los símbolos de la leyenda
            int i = 0;
            for (javafx.scene.Node node : charGraficaReporteGeneral.lookupAll(".chart-legend-item-symbol")) {
                if (i == 0) {
                    node.setStyle("-fx-background-color: " + colorRecaudado + ";");
                } else if (i == 1) {
                    node.setStyle("-fx-background-color: " + colorPendiente + ";");
                }
                i++;
            }
        });
    }
}
