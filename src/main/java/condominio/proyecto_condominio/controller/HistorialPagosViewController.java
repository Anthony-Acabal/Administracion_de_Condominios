package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import condominio.proyecto_condominio.dao.Conexion;
import condominio.proyecto_condominio.dao.HistorialPagoDAO;
import condominio.proyecto_condominio.logic.HistorialPagoLogic;
import condominio.proyecto_condominio.model.HistorialPago;

import javafx.stage.Stage;

public class HistorialPagosViewController implements Initializable {

    @FXML private Button btnRegresar;

    @FXML private TableView<HistorialPago> tblPagos;

    @FXML private TableColumn<HistorialPago, Integer> colCasa;
    @FXML private TableColumn<HistorialPago, String> colPropietario;
    @FXML private TableColumn<HistorialPago, String> colFecha;
    @FXML private TableColumn<HistorialPago, Double> colMonto;
    @FXML private TableColumn<HistorialPago, String> colComprobante;

    @FXML private ComboBox<String> cbCasa;
    @FXML private ComboBox<String> cbMes;
    @FXML private ComboBox<String> cbAnio;

    private ObservableList<HistorialPago> listaPagos = FXCollections.observableArrayList();

    private final HistorialPagoDAO dao = new HistorialPagoDAO();
    private final HistorialPagoLogic logic = new HistorialPagoLogic();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        tblPagos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);

        colCasa.setCellValueFactory(new PropertyValueFactory<>("casa"));
        colPropietario.setCellValueFactory(new PropertyValueFactory<>("propietario"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colMonto.setCellValueFactory(new PropertyValueFactory<>("monto"));
        colComprobante.setCellValueFactory(new PropertyValueFactory<>("comprobante"));

        cargarCombos();

        buscarPagos();
    }

    @FXML
    private void cerrarVentana() {
        Stage stage = (Stage) btnRegresar.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void buscarPagos() {

        try {

            String casa = cbCasa.getValue();
            String mes = cbMes.getValue();
            String anio = cbAnio.getValue();

            // 🔥 VALIDACIÓN (LOGIC)
            if (!logic.validarMesAnio(mes, anio)) {
                Alert a = new Alert(Alert.AlertType.WARNING);
                a.setContentText("Debe seleccionar un año.");
                a.showAndWait();
                return;
            }

            var conn = Conexion.getInstancia().getConnection();

            listaPagos.clear();

            listaPagos.addAll(
                    dao.filtrarPagos(conn, casa, mes, anio)
            );

            tblPagos.setItems(listaPagos);

            tblPagos.getSortOrder().add(colFecha);
            colFecha.setSortType(TableColumn.SortType.DESCENDING);
            tblPagos.sort();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void cargarCombos() {

        cbCasa.getItems().add("Todas las casas");
        cbAnio.getItems().add("Todos los años");
        cbMes.getItems().add("Todos los meses");

        cbCasa.getSelectionModel().selectFirst();
        cbAnio.getSelectionModel().selectFirst();
        cbMes.getSelectionModel().selectFirst();
    }
}