package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import condominio.proyecto_condominio.logic.HistorialPagoLogic;
import condominio.proyecto_condominio.model.HistorialPago;
import condominio.proyecto_condominio.service.GenerarRecibo;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;

public class HistorialPagosViewController
        implements Initializable {

    @FXML
    private Button btnRegresar;

    @FXML
    private TableView<HistorialPago> tblPagos;

    @FXML
    private TableColumn<HistorialPago, Integer> colCasa;

    @FXML
    private TableColumn<HistorialPago, String> colPropietario;

    @FXML
    private TableColumn<HistorialPago, String> colFecha;

    @FXML
    private TableColumn<HistorialPago, Double> colMonto;

    @FXML
    private TableColumn<HistorialPago, String> colComprobante;

    @FXML
    private ComboBox<String> cbCasa;

    @FXML
    private ComboBox<String> cbMes;

    @FXML
    private ComboBox<String> cbAnio;

    @FXML
    private Button btnBuscar;

    @FXML
    private void imprimirRecibo() {

        HistorialPago pago = tblPagos.getSelectionModel().getSelectedItem();

        if (pago == null) {
            System.out.println("Selecciona un pago");
            return;
        }

        try {

            GenerarRecibo service = new GenerarRecibo();

            service.mostrarRecibo(
                    pago.getIdPropietario(),
                    pago.getIdPagoCuota()
            );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<HistorialPago> listaPagos
            = FXCollections.observableArrayList();

    private HistorialPagoLogic historialLogic
            = new HistorialPagoLogic();

    @FXML
    private void regresarRegistroPago() {

        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/RegistroPagoView.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) btnRegresar
                    .getScene()
                    .getWindow();

            stage.setScene(
                    new Scene(root)
            );

            stage.show();

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        tblPagos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );

        colCasa.setCellValueFactory(
                new PropertyValueFactory<>("casa")
        );

        colCasa.setReorderable(false);

        colPropietario.setCellValueFactory(
                new PropertyValueFactory<>("propietario")
        );

        colPropietario.setReorderable(false);

        colFecha.setCellValueFactory(
                new PropertyValueFactory<>("fecha")
        );

        colFecha.setReorderable(false);

        colMonto.setCellValueFactory(
                new PropertyValueFactory<>("monto")
        );

        colMonto.setReorderable(false);

        colComprobante.setCellValueFactory(
                new PropertyValueFactory<>("comprobante")
        );

        colComprobante.setSortable(false);

        colComprobante.setReorderable(false);

        colFecha.setCellFactory(
                columna -> new TableCell<
                HistorialPago, String>() {

            @Override
            protected void updateItem(
                    String fecha,
                    boolean empty
            ) {

                super.updateItem(
                        fecha,
                        empty
                );

                if (empty
                        || fecha == null) {

                    setText(null);

                } else {

                    setText(fecha);

                    setStyle(
                            "-fx-alignment: CENTER-RIGHT;"
                    );
                }
            }
        }
        );

        colMonto.setCellFactory(
                columna -> new TableCell<
                HistorialPago, Double>() {

            @Override
            protected void updateItem(
                    Double monto,
                    boolean empty
            ) {

                super.updateItem(
                        monto,
                        empty
                );

                if (empty
                        || monto == null) {

                    setText(null);

                } else {

                    setText(
                            "Q"
                            + String.format(
                                    "%,.2f",
                                    monto
                            )
                    );

                    setStyle(
                            "-fx-alignment: CENTER-RIGHT;"
                    );
                }
            }
        }
        );

        colComprobante.setCellFactory(columna -> new TableCell<HistorialPago, String>() {

            private final Button btnImprimir = new Button("Imprimir");

            {
                btnImprimir.setOnAction(event -> {

                    HistorialPago pago = getTableView()
                            .getItems()
                            .get(getIndex());

                    if (pago == null) {
                        return;
                    }

                    try {

                        GenerarRecibo service = new GenerarRecibo();

                        service.mostrarRecibo(
                                pago.getIdPropietario(),
                                pago.getIdPagoCuota()
                        );

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {

                super.updateItem(item, empty);

                if (empty || item == null) {
                    setGraphic(null);
                    return;
                }

                setGraphic(btnImprimir);
                setStyle("-fx-alignment: CENTER;");
            }
        });

        cargarCombos();

        buscarPagos();

        Label lblPlaceholder
                = new Label(
                        "No hay registros de pagos"
                );

        lblPlaceholder.setStyle(
                "-fx-text-fill: #666666;"
                + "-fx-font-size: 15px;"
                + "-fx-font-weight: bold;"
        );

        tblPagos.setPlaceholder(
                lblPlaceholder
        );

        cbAnio.setOnAction(
                event -> cargarMeses()
        );
    }

    private void cargarCombos() {

        cbCasa.getItems().add(
                "Todas las casas"
        );

        cbCasa.getItems().addAll(
                historialLogic.obtenerCasas()
        );

        cbCasa.getSelectionModel()
                .selectFirst();

        cbAnio.getItems().add(
                "Todos los años"
        );

        int anioActual
                = LocalDate.now()
                        .getYear();

        for (int i = 2026;
                i <= anioActual;
                i++) {

            cbAnio.getItems().add(
                    String.valueOf(i)
            );
        }

        cbMes.setValue(
                "Todos los meses"
        );

        cbAnio.setValue(
                "Todos los años"
        );

        cargarMeses();
    }

    @FXML
    private void buscarPagos() {

        listaPagos.clear();

        String casa = cbCasa.getValue();

        String mes = cbMes.getValue();

        String anio = cbAnio.getValue();

        if (!historialLogic.validarMesAnio(
                mes,
                anio
        )) {

            Alert alerta = new Alert(
                    Alert.AlertType.WARNING
            );

            alerta.setTitle(
                    "Filtro incompleto"
            );

            alerta.setHeaderText(null);

            alerta.setContentText(
                    "Debe seleccionar un año."
            );

            alerta.showAndWait();

            return;
        }

        listaPagos.addAll(
                historialLogic.filtrarPagos(
                        casa,
                        mes,
                        anio
                )
        );

        tblPagos.setItems(
                listaPagos
        );

        tblPagos.getSortOrder().clear();

        tblPagos.getSortOrder().add(
                colFecha
        );

        colFecha.setSortType(
                TableColumn.SortType.DESCENDING
        );

        tblPagos.sort();
    }

    private void cargarMeses() {

        String mesSeleccionado
                = cbMes.getValue();

        cbMes.getItems().clear();

        cbMes.getItems().add(
                "Todos los meses"
        );

        String[] meses = {
            "Enero",
            "Febrero",
            "Marzo",
            "Abril",
            "Mayo",
            "Junio",
            "Julio",
            "Agosto",
            "Septiembre",
            "Octubre",
            "Noviembre",
            "Diciembre"
        };

        String anioSeleccionado
                = cbAnio.getValue();

        int anioActual
                = LocalDate.now()
                        .getYear();

        int limiteMes;

        if (anioSeleccionado.equals(
                "Todos los años"
        )) {

            limiteMes
                    = LocalDate.now()
                            .getMonthValue();

        } else if (Integer.parseInt(anioSeleccionado)
                == anioActual) {

            limiteMes
                    = LocalDate.now()
                            .getMonthValue();

        } else {

            limiteMes = 12;
        }

        for (int i = 0;
                i < limiteMes;
                i++) {

            cbMes.getItems().add(
                    meses[i]
            );
        }

        if (mesSeleccionado != null
                && cbMes.getItems().contains(
                        mesSeleccionado
                )) {

            cbMes.setValue(
                    mesSeleccionado
            );

        } else {

            cbMes.setValue(
                    "Todos los meses"
            );
        }
    }
}
