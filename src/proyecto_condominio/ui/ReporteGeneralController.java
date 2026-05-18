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
    @FXML private ChoiceBox<String> choiceBoxReporteGeneralFiltroMes;
    @FXML private ChoiceBox<Integer> choiceBoxReporteGeneralFiltroAno;

    private ReporteGeneralDAO reporteDAO = new ReporteGeneralDAO();
    private ObservableList<ReporteGeneral> listaReporte;
    
    private final String[] nombresMeses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    private int[] limitesGlobales;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        limitesGlobales = reporteDAO.obtenerLimitesFechas();
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
        
        int anioMin = Math.min(limitesGlobales[0], fechaActual.getYear());
        int anioMax = Math.max(limitesGlobales[2], fechaActual.getYear());

        choiceBoxReporteGeneralFiltroAno.getItems().clear();
        for (int i = anioMin; i <= anioMax; i++) {
            choiceBoxReporteGeneralFiltroAno.getItems().add(i);
        }
        
        if (choiceBoxReporteGeneralFiltroAno.getItems().contains(fechaActual.getYear())) {
            choiceBoxReporteGeneralFiltroAno.setValue(fechaActual.getYear());
        } else {
            choiceBoxReporteGeneralFiltroAno.setValue(anioMax);
        }

        actualizarMesesDisponibles(choiceBoxReporteGeneralFiltroAno.getValue());

        choiceBoxReporteGeneralFiltroAno.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                actualizarMesesDisponibles(newVal);
                cargarDatos();
            }
        });

        choiceBoxReporteGeneralFiltroMes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarDatos();
        });
    }

    private void actualizarMesesDisponibles(int anioSeleccionado) {
        String mesActualSeleccionado = choiceBoxReporteGeneralFiltroMes.getValue();
        choiceBoxReporteGeneralFiltroMes.getItems().clear();

        int mesInicio = 1;
        int mesFin = 12;

        if (anioSeleccionado == limitesGlobales[0]) {
            mesInicio = limitesGlobales[1];
        }
        if (anioSeleccionado == limitesGlobales[2]) {
            mesFin = limitesGlobales[3];
        }

        if (anioSeleccionado == LocalDate.now().getYear()) {
            mesFin = Math.max(mesFin, LocalDate.now().getMonthValue());
        }

        for (int i = mesInicio; i <= mesFin; i++) {
            choiceBoxReporteGeneralFiltroMes.getItems().add(nombresMeses[i - 1]);
        }

        if (mesActualSeleccionado != null && choiceBoxReporteGeneralFiltroMes.getItems().contains(mesActualSeleccionado)) {
            choiceBoxReporteGeneralFiltroMes.setValue(mesActualSeleccionado);
        } else if (!choiceBoxReporteGeneralFiltroMes.getItems().isEmpty()) {
            int mesActual = LocalDate.now().getMonthValue();
            if (anioSeleccionado == LocalDate.now().getYear() && mesActual >= mesInicio && mesActual <= mesFin) {
                choiceBoxReporteGeneralFiltroMes.setValue(nombresMeses[mesActual - 1]);
            } else {
                choiceBoxReporteGeneralFiltroMes.setValue(choiceBoxReporteGeneralFiltroMes.getItems().get(choiceBoxReporteGeneralFiltroMes.getItems().size() - 1));
            }
        }
    }

    private void cargarDatos() {
        if (choiceBoxReporteGeneralFiltroMes.getValue() == null || choiceBoxReporteGeneralFiltroAno.getValue() == null) {
            return;
        }

        String mesNombre = choiceBoxReporteGeneralFiltroMes.getValue();
        int mesSeleccionado = 1;
        for (int i = 0; i < nombresMeses.length; i++) {
            if (nombresMeses[i].equals(mesNombre)) {
                mesSeleccionado = i + 1;
                break;
            }
        }
        
        int anioSeleccionado = choiceBoxReporteGeneralFiltroAno.getValue();

        // Datos de la tabla
        List<ReporteGeneral> datos = reporteDAO.obtenerReporteGeneral(mesSeleccionado, anioSeleccionado);
        listaReporte = FXCollections.observableArrayList(datos);
        tbReporteGeneral.setItems(listaReporte);

        double[] resumenMes = reporteDAO.obtenerResumenMensual(mesSeleccionado, anioSeleccionado);
        double recaudadoMes = resumenMes[0];
        double esperadoMes = resumenMes[1];
        double pendienteMes = Math.max(0, esperadoMes - recaudadoMes);

        lblTotalRecaudadoMes.setText(String.format("Total recaudado de %s %d: Q%.2f / esperado: Q%.2f", 
                mesNombre, anioSeleccionado, recaudadoMes, esperadoMes));
        actualizarGrafica(charGraficaReporteGeneralMes, recaudadoMes, pendienteMes, "#2ecc71", "#9b59b6");

        double[] resumenAno = reporteDAO.obtenerResumenAnual(mesSeleccionado, anioSeleccionado);
        double recaudadoAno = resumenAno[0];
        double esperadoAno = resumenAno[1];
        double pendienteAno = Math.max(0, esperadoAno - recaudadoAno);

        lblTotalRecaudadoAno.setText(String.format("Total recaudado del año %d (acumulado a %s): Q%.2f / esperado anual: Q%.2f", 
                anioSeleccionado, mesNombre, recaudadoAno, esperadoAno));
        actualizarGrafica(charGraficaReporteGeneralAno, recaudadoAno, pendienteAno, "#3498db", "#e67e22");
    }

    private void actualizarGrafica(PieChart grafica, double recaudado, double pendiente, String colorRecaudado, String colorPendiente) {
        double total = recaudado + pendiente;
        if (total == 0) return;

        double porcRecaudado = (recaudado / total) * 100;
        double porcPendiente = (pendiente / total) * 100;

        ObservableList<PieChart.Data> pieChartData = FXCollections.observableArrayList();
        
        PieChart.Data sliceRecaudado = new PieChart.Data(
            String.format("Recaudado: Q%.2f (%.1f%%)", recaudado, porcRecaudado), 
            recaudado
        );
        pieChartData.add(sliceRecaudado);

        if (pendiente > 0) {
            PieChart.Data slicePendiente = new PieChart.Data(
                String.format("Pendiente: Q%.2f (%.1f%%)", pendiente, porcPendiente), 
                pendiente
            );
            pieChartData.add(slicePendiente);
        }

        grafica.setData(pieChartData);
        grafica.setLabelsVisible(true);
        grafica.setLegendVisible(true);
        grafica.setLegendSide(Side.RIGHT);
        grafica.setTitle(null);

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
