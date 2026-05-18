package proyecto_condominio.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
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
    @FXML private TableColumn<CasasPendientesPago, String> colCPPFecha;
    @FXML private ChoiceBox<String> choiceBoxCPPFiltroMes;
    @FXML private ChoiceBox<Integer> choiceBoxCPPFiltroAno;
    @FXML private Label lblCPPCantidad;
    @FXML private Button btnCPPRegresar;
    @FXML private Button btnCPPGenerarReporte;

    private CasasPendientesPagoDAO casasDAO = new CasasPendientesPagoDAO();
    private ObservableList<CasasPendientesPago> listaCasas;
    private int[] limitesGlobales;

    private final String[] nombresMeses = {
        "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
        "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
    };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        configurarColumnas();
        limitesGlobales = casasDAO.obtenerLimitesFechas();
        inicializarFiltros();
        cargarDatos();
    }

    private void configurarColumnas() {
        colCPPNumeroCasa.setCellValueFactory(new PropertyValueFactory<>("numeroCasa"));
        colCPPNombrePropietario.setCellValueFactory(new PropertyValueFactory<>("nombrePropietario"));
        colCPPTelefono.setCellValueFactory(new PropertyValueFactory<>("telefono"));
        colCPPFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
    }

    private void inicializarFiltros() {
        LocalDate fechaActual = LocalDate.now();
        
        int anioMin = limitesGlobales[0];
        int anioMax = fechaActual.getYear();

        choiceBoxCPPFiltroAno.getItems().clear();
        for (int i = anioMin; i <= anioMax; i++) {
            choiceBoxCPPFiltroAno.getItems().add(i);
        }
        choiceBoxCPPFiltroAno.setValue(anioMax);

        actualizarMesesDisponibles(anioMax);

        choiceBoxCPPFiltroAno.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                actualizarMesesDisponibles(newVal);
                cargarDatos();
            }
        });

        choiceBoxCPPFiltroMes.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) cargarDatos();
        });
    }

    private void actualizarMesesDisponibles(int anioSeleccionado) {
        String mesPrevio = choiceBoxCPPFiltroMes.getValue();
        choiceBoxCPPFiltroMes.getItems().clear();
        choiceBoxCPPFiltroMes.getItems().add("Ver todo");

        int mesInicio = 1;
        int mesFin = 12;

        if (anioSeleccionado == limitesGlobales[0]) {
            mesInicio = limitesGlobales[1];
        }
        
        if (anioSeleccionado == LocalDate.now().getYear()) {
            mesFin = LocalDate.now().getMonthValue();
        }

        for (int i = mesInicio; i <= mesFin; i++) {
            choiceBoxCPPFiltroMes.getItems().add(nombresMeses[i - 1]);
        }

        if (mesPrevio != null && choiceBoxCPPFiltroMes.getItems().contains(mesPrevio)) {
            choiceBoxCPPFiltroMes.setValue(mesPrevio);
        } else {
            int mesActual = LocalDate.now().getMonthValue();
            if (anioSeleccionado == LocalDate.now().getYear()) {
                choiceBoxCPPFiltroMes.setValue(nombresMeses[mesActual - 1]);
            } else {
                choiceBoxCPPFiltroMes.setValue(choiceBoxCPPFiltroMes.getItems().get(choiceBoxCPPFiltroMes.getItems().size() - 1));
            }
        }
    }

    private void cargarDatos() {
        if (choiceBoxCPPFiltroMes.getValue() == null || choiceBoxCPPFiltroAno.getValue() == null) {
            return;
        }

        String seleccionMes = choiceBoxCPPFiltroMes.getValue();
        int anio = choiceBoxCPPFiltroAno.getValue();
        List<CasasPendientesPago> datos;

        if (seleccionMes.equals("Ver todo")) {
            lblCPPFecha.setText("Pendientes del año " + anio);
            
            // Calcular límites para el año seleccionado
            int mesInicio = 1;
            int mesFin = 12;

            if (anio == limitesGlobales[0]) {
                mesInicio = limitesGlobales[1];
            }
            if (anio == LocalDate.now().getYear()) {
                mesFin = LocalDate.now().getMonthValue();
            }

            datos = casasDAO.obtenerCasasPendientesAnual(anio, mesInicio, mesFin);
        } else {
            int mes = 1;
            for (int i = 0; i < nombresMeses.length; i++) {
                if (nombresMeses[i].equals(seleccionMes)) {
                    mes = i + 1;
                    break;
                }
            }
            lblCPPFecha.setText("Cuota de " + seleccionMes + " del " + anio);
            datos = casasDAO.obtenerCasasPendientes(mes, anio);
        }

        listaCasas = FXCollections.observableArrayList(datos);
        tbCasasPendientesPago.setItems(listaCasas);
        lblCPPCantidad.setText("Casas pendientes de pago: " + datos.size());
    }
}