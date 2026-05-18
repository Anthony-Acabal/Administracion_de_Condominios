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
import javafx.scene.control.ChoiceBox;
import java.time.LocalDate;
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
    @FXML private Label lblTotalRecaudadoAno;
    @FXML private Button btnImprimirReporte;
    @FXML private PieChart charGraficaReporteGeneralMes;
    @FXML private PieChart charGraficaReporteGeneralAno;
    @FXML private ChoiceBox<Integer> choiceBoxReporteGeneralFiltroMes;
    @FXML private ChoiceBox<Integer> choiceBoxReporteGeneralFiltroAno;

    private ReporteGeneralDAO reporteDAO = new ReporteGeneralDAO();
    private ObservableList<ReporteGeneral> listaReporte;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        inicializarFiltros();
        cargarDatos();
    }

    private void configurarColumnas() {
        colNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        colNombrePropietario.setCellValueFactory(new PropertyValueFactory<>("nombrePropietario"));
        colEstadoActual.setCellValueFactory(new PropertyValueFactory<>("estadoActual"));
        colTotalPagado.setCellValueFactory(new PropertyValueFactory<>("totalPagado"));
    }

    private void inicializarFiltros() {
        LocalDate fechaActual = LocalDate.now();
        
        // Meses 1-12
        for (int i = 1; i <= 12; i++) {
            choiceBoxReporteGeneralFiltroMes.getItems().add(i);
        }
        choiceBoxReporteGeneralFiltroMes.setValue(fechaActual.getMonthValue());

        // Años 2000-2040
        for (int i = 2000; i <= 2040; i++) {
            choiceBoxReporteGeneralFiltroAno.getItems().add(i);
        }
        choiceBoxReporteGeneralFiltroAno.setValue(fechaActual.getYear());

        // Listeners para actualizar al cambiar selección
        choiceBoxReporteGeneralFiltroMes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarDatos();
        });
        choiceBoxReporteGeneralFiltroAno.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarDatos();
        });
    }

    private void cargarDatos() {
        if (choiceBoxReporteGeneralFiltroMes.getValue() == null || choiceBoxReporteGeneralFiltroAno.getValue() == null) {
            return;
        }

        int mesSeleccionado = choiceBoxReporteGeneralFiltroMes.getValue();
        int anioSeleccionado = choiceBoxReporteGeneralFiltroAno.getValue();

        // Datos de la tabla
        List<ReporteGeneral> datos = reporteDAO.obtenerReporteGeneral(mesSeleccionado, anioSeleccionado);
        listaReporte = FXCollections.observableArrayList(datos);
        tbReporteGeneral.setItems(listaReporte);

        // Resumen Mensual
        double[] resumenMes = reporteDAO.obtenerResumenMensual(mesSeleccionado, anioSeleccionado);
        double recaudadoMes = resumenMes[0];
        double esperadoMes = resumenMes[1];
        double pendienteMes = Math.max(0, esperadoMes - recaudadoMes);

        lblTotalRecaudadoMes.setText(String.format("Total recaudado del mes %d/%d: $%.2f / esperado: $%.2f", 
                mesSeleccionado, anioSeleccionado, recaudadoMes, esperadoMes));
        actualizarGrafica(charGraficaReporteGeneralMes, recaudadoMes, pendienteMes, "#2ecc71", "#9b59b6");

        // Resumen Anual (hasta el mes seleccionado)
        double[] resumenAno = reporteDAO.obtenerResumenAnual(mesSeleccionado, anioSeleccionado);
        double recaudadoAno = resumenAno[0];
        double esperadoAno = resumenAno[1];
        double pendienteAno = Math.max(0, esperadoAno - recaudadoAno);

        lblTotalRecaudadoAno.setText(String.format("Total recaudado del año %d (hasta mes %d): $%.2f / esperado: $%.2f", 
                anioSeleccionado, mesSeleccionado, recaudadoAno, esperadoAno));
        actualizarGrafica(charGraficaReporteGeneralAno, recaudadoAno, pendienteAno, "#3498db", "#e67e22");
    }

    private void actualizarGrafica(PieChart grafica, double recaudado, double pendiente, String colorRecaudado, String colorPendiente) {
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

        grafica.setData(pieChartData);
        grafica.setLabelsVisible(true);
        grafica.setLegendVisible(true);
        grafica.setLegendSide(Side.RIGHT);
        grafica.setTitle(null); // Quitar encabezado

        Platform.runLater(() -> {
            if (sliceRecaudado.getNode() != null) {
                sliceRecaudado.getNode().setStyle("-fx-pie-color: " + colorRecaudado + ";");
            }
            if (pendiente > 0 && pieChartData.size() > 1 && pieChartData.get(1).getNode() != null) {
                pieChartData.get(1).getNode().setStyle("-fx-pie-color: " + colorPendiente + ";");
            }
            
            int i = 0;
            for (javafx.scene.Node node : grafica.lookupAll(".chart-legend-item-symbol")) {
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
