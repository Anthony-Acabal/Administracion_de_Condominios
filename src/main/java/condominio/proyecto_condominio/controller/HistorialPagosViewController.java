package condominio.proyecto_condominio.controller;

import java.net.URL;
<<<<<<< HEAD
import java.text.SimpleDateFormat;
=======
>>>>>>> origin/main
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
<<<<<<< HEAD
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Label;

import javafx.scene.control.cell.PropertyValueFactory;

import condominio.proyecto_condominio.model.HistorialPago;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import condominio.proyecto_condominio.dao.Conexion;

import javafx.stage.Stage;

=======
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import javafx.scene.control.cell.PropertyValueFactory;

import javafx.stage.Stage;

import condominio.proyecto_condominio.logic.HistorialPagoLogic;
import condominio.proyecto_condominio.model.HistorialPago;

>>>>>>> origin/main
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

    private ObservableList<HistorialPago> listaPagos
            = FXCollections.observableArrayList();

<<<<<<< HEAD
=======
    private HistorialPagoLogic historialLogic
            = new HistorialPagoLogic();

>>>>>>> origin/main
    @FXML
    private void cerrarVentana() {

        Stage stage
                = (Stage) btnRegresar
                        .getScene()
                        .getWindow();

        stage.close();
    }

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {
<<<<<<< HEAD
        tblPagos.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
=======

        tblPagos.setColumnResizePolicy(
                TableView.CONSTRAINED_RESIZE_POLICY
        );
>>>>>>> origin/main

        colCasa.setCellValueFactory(
                new PropertyValueFactory<>("casa")
        );
<<<<<<< HEAD
        colCasa.setReorderable(false);

        colPropietario.setReorderable(false);

        colFecha.setReorderable(false);

        colMonto.setReorderable(false);

        colComprobante.setSortable(false);

        colComprobante.setReorderable(false);

=======

        colCasa.setReorderable(false);

>>>>>>> origin/main
        colPropietario.setCellValueFactory(
                new PropertyValueFactory<>("propietario")
        );

<<<<<<< HEAD
=======
        colPropietario.setReorderable(false);

>>>>>>> origin/main
        colFecha.setCellValueFactory(
                new PropertyValueFactory<>("fecha")
        );

<<<<<<< HEAD
        colFecha.setCellFactory(
                columna -> new TableCell<
        HistorialPago, String>() {
=======
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
>>>>>>> origin/main

            @Override
            protected void updateItem(
                    String fecha,
                    boolean empty
            ) {

                super.updateItem(
                        fecha,
                        empty
                );

<<<<<<< HEAD
                if (empty || fecha == null) {
=======
                if (empty
                        || fecha == null) {
>>>>>>> origin/main

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

<<<<<<< HEAD
        colMonto.setCellValueFactory(
                new PropertyValueFactory<>("monto")
        );

        colComprobante.setCellValueFactory(
                new PropertyValueFactory<>("comprobante")
        );

        colComprobante.setCellFactory(
                columna -> new TableCell<
                HistorialPago, String>() {

            private final Button btnImprimir
                    = new Button(
                            "Imprimir"
                    );

            {

                btnImprimir.setOnAction(
                        event -> {

                            HistorialPago pago
                            = getTableView()
                                    .getItems()
                                    .get(getIndex());

                            // TODO:
                            // Agregar aquí la lógica del botón de impresión
                        }
                );
            }

            @Override
            protected void updateItem(
                    String item,
                    boolean empty
            ) {

                super.updateItem(
                        item,
                        empty
                );

                if (empty) {

                    setGraphic(null);

                } else {

                    setGraphic(
                            btnImprimir
                    );

                    setStyle(
                            "-fx-alignment: CENTER;"
                    );
                }
            }
        }
        );

=======
>>>>>>> origin/main
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

<<<<<<< HEAD
=======
        colComprobante.setCellFactory(
                columna -> new TableCell<
                HistorialPago, String>() {

            private final Button btnImprimir
                    = new Button("Imprimir");

            {

                btnImprimir.setOnAction(
                        event -> {

                            HistorialPago pago
                                    = getTableView()
                                            .getItems()
                                            .get(getIndex());

                            // TODO:
                            // Lógica impresión
                        }
                );
            }

            @Override
            protected void updateItem(
                    String item,
                    boolean empty
            ) {

                super.updateItem(
                        item,
                        empty
                );

                if (empty) {

                    setGraphic(null);

                } else {

                    setGraphic(btnImprimir);

                    setStyle(
                            "-fx-alignment: CENTER;"
                    );
                }
            }
        }
        );

>>>>>>> origin/main
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

<<<<<<< HEAD
        cbAnio.setOnAction(event -> {

            cargarMeses();
        });
=======
        cbAnio.setOnAction(
                event -> cargarMeses()
        );
>>>>>>> origin/main
    }

    private void cargarCombos() {

        cbCasa.getItems().add(
                "Todas las casas"
        );

<<<<<<< HEAD
        String sqlCasas = """

            SELECT DISTINCT numero_casa

            FROM propietario

            ORDER BY numero_casa ASC
        """;

        try (
                Connection conn
                = Conexion.getConnection(); PreparedStatement ps
                = conn.prepareStatement(
                        sqlCasas
                ); ResultSet rs
                = ps.executeQuery()) {

            while (rs.next()) {

                cbCasa.getItems().add(
                        String.valueOf(
                                rs.getInt(
                                        "numero_casa"
                                )
                        )
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
=======
        cbCasa.getItems().addAll(
                historialLogic.obtenerCasas()
        );
>>>>>>> origin/main

        cbCasa.getSelectionModel()
                .selectFirst();

        cbAnio.getItems().add(
                "Todos los años"
        );

        int anioActual
                = LocalDate.now()
                        .getYear();

<<<<<<< HEAD
        for (int i = 2026;
                i <= anioActual;
                i++) {
=======
        for (
                int i = 2026;
                i <= anioActual;
                i++
        ) {
>>>>>>> origin/main

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

<<<<<<< HEAD
        String sql = """
            SELECT

                p.numero_casa,

                p.primer_nombre
                + ' ' +
                p.segundo_nombre
                + ' ' +
                p.primer_apellido
                + ' ' +
                p.segundo_apellido
                AS propietario,

                pc.fecha_pago,

                c.cuota,

                pc.imprime_comprobante

            FROM pago_cuota pc

            INNER JOIN propietario p
                ON pc.id_propietario =
                   p.id_propietario

            INNER JOIN cuota c
                ON pc.id_cuota =
                   c.id_cuota

            WHERE 1=1
        """;

        String casa
                = cbCasa.getValue();

        String mes
                = cbMes.getValue();

        String anio
                = cbAnio.getValue();

        if (mes != null
                && !mes.equals(
                        "Todos los meses"
                )
                && anio.equals(
                        "Todos los años"
                )) {
=======
        String casa = cbCasa.getValue();

        String mes = cbMes.getValue();

        String anio = cbAnio.getValue();

        if (!historialLogic.validarMesAnio(
                mes,
                anio
        )) {
>>>>>>> origin/main

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

<<<<<<< HEAD
        if (casa != null
                && !casa.equals(
                        "Todas las casas"
                )) {

            sql
                    += " AND p.numero_casa = ?";
        }

        if (!mes.equals(
                "Todos los meses"
        )) {

            sql
                    += " AND MONTH(pc.fecha_pago) = ?";
        }

        if (!anio.equals(
                "Todos los años"
        )) {

            sql
                    += " AND YEAR(pc.fecha_pago) = ?";
        }
        try (
                Connection conn
                = Conexion.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql)) {

            int index = 1;

            if (casa != null
                    && !casa.equals(
                            "Todas las casas"
                    )) {

                ps.setInt(
                        index++,
                        Integer.parseInt(casa)
                );
            }

            if (!mes.equals(
                    "Todos los meses"
            )) {

                int numeroMes
                        = cbMes.getSelectionModel()
                                .getSelectedIndex();

                ps.setInt(
                        index++,
                        numeroMes
                );
            }

            if (!anio.equals(
                    "Todos los años"
            )) {

                ps.setInt(
                        index++,
                        Integer.parseInt(anio)
                );
            }

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                String fecha
                        = new SimpleDateFormat(
                                "dd/MM/yyyy"
                        ).format(
                                rs.getTimestamp(
                                        "fecha_pago"
                                )
                        );
                listaPagos.add(
                        new HistorialPago(
                                rs.getInt(
                                        "numero_casa"
                                ),
                                rs.getString(
                                        "propietario"
                                ),
                                fecha,
                                rs.getDouble(
                                        "cuota"
                                ),
                                rs.getString(
                                        "imprime_comprobante"
                                )
                        )
                );
            }

            tblPagos.setItems(
                    listaPagos
            );
            tblPagos.getSortOrder().add(colFecha);

            colFecha.setSortType(
                    TableColumn.SortType.DESCENDING
            );

            tblPagos.sort();

        } catch (Exception e) {

            e.printStackTrace();
        }
=======
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
>>>>>>> origin/main
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
<<<<<<< HEAD
        } else if (Integer.parseInt(
                anioSeleccionado
        ) == anioActual) {
=======

        } else if (
                Integer.parseInt(anioSeleccionado)
                == anioActual
        ) {
>>>>>>> origin/main

            limiteMes
                    = LocalDate.now()
                            .getMonthValue();
<<<<<<< HEAD
=======

>>>>>>> origin/main
        } else {

            limiteMes = 12;
        }

<<<<<<< HEAD
        for (int i = 0;
                i < limiteMes;
                i++) {
=======
        for (
                int i = 0;
                i < limiteMes;
                i++
        ) {
>>>>>>> origin/main

            cbMes.getItems().add(
                    meses[i]
            );
        }

        if (mesSeleccionado != null
<<<<<<< HEAD
                && cbMes.getItems()
                        .contains(
                                mesSeleccionado
                        )) {
=======
                && cbMes.getItems().contains(
                        mesSeleccionado
                )) {
>>>>>>> origin/main

            cbMes.setValue(
                    mesSeleccionado
            );

        } else {

            cbMes.setValue(
                    "Todos los meses"
            );
        }
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
