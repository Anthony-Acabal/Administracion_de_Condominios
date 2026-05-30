package condominio.proyecto_condominio.controller;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import javafx.event.ActionEvent;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;

import javafx.scene.control.cell.PropertyValueFactory;
import condominio.proyecto_condominio.dao.Conexion;

import condominio.proyecto_condominio.dao.EstadoCuentaDAO;
import condominio.proyecto_condominio.model.Cuota;
import condominio.proyecto_condominio.model.EstadoCuenta;

public class EstadoCuentaController
        implements Initializable {

    @FXML
    private Button btnBuscar;

    @FXML
    private ComboBox<String> cmbAnioFin;

    @FXML
    private ComboBox<String> cmbAnioInicio;

    @FXML
    private ComboBox<String> cmbMesFin;

    @FXML
    private ComboBox<String> cmbMesInicio;

    @FXML
    private ComboBox<Integer> cmbNumeroCasa;

    @FXML
    private TableColumn<Cuota, Integer> colAnio;

    @FXML
    private TableColumn<Cuota, Double> colCuota;

    @FXML
    private TableColumn<Cuota, String> colEstado;

    @FXML
    private TableColumn<Cuota, String> colMes;

    @FXML
    private TableView<Cuota> tbEstadosCuenta;

    @FXML
    private TextField txtPropietario;

    @FXML
    private TextField txtTelefono;

    @FXML
    void buscar(ActionEvent event) {

        cargarEstadoCuenta();
    }

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        configurarTabla();

        cargarMeses();

        cargarAnios();

        cargarCasas();

        cmbNumeroCasa.setOnAction(
                e -> cargarDatosCasa()
        );
    }

    private void configurarTabla() {

        colMes.setCellValueFactory(
                new PropertyValueFactory<>("mes")
        );

        colAnio.setCellValueFactory(
                new PropertyValueFactory<>("anio")
        );

        colCuota.setCellValueFactory(
                new PropertyValueFactory<>("montoCuota")
        );

        colEstado.setCellValueFactory(
                new PropertyValueFactory<>("estado")
        );
    }

    private void cargarMeses() {

        cmbMesInicio.getItems().addAll(
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
        );

        cmbMesFin.getItems().addAll(
                cmbMesInicio.getItems()
        );
    }

    private void cargarAnios() {

        for (int i = 2020; i <= 2035; i++) {

            cmbAnioInicio.getItems()
                    .add(String.valueOf(i));

            cmbAnioFin.getItems()
                    .add(String.valueOf(i));
        }
    }

    private void cargarCasas() {

        cmbNumeroCasa.getItems().addAll(
                1, 2, 3, 4, 5
        );
    }

    private void cargarDatosCasa() {

        Integer numeroCasa
                = cmbNumeroCasa.getValue();

        if (numeroCasa == null) {
            return;
        }

        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            EstadoCuentaDAO dao
                    = new EstadoCuentaDAO(conn);

            EstadoCuenta estado
                    = dao.obtenerEstadoCuenta(
                            numeroCasa
                    );

            if (estado != null
                    && estado.getCasa() != null) {

                txtPropietario.setText(
                        estado.getCasa()
                                .getPropietario()
                );

                txtTelefono.setText(
                        estado.getCasa()
                                .getTelefono()
                );
            }

        } catch (Exception e) {

            e.printStackTrace();
        }
    }

    private void cargarEstadoCuenta() {

        Integer numeroCasa
                = cmbNumeroCasa.getValue();

        if (numeroCasa == null) {
            return;
        }

        try {

            Connection conn = Conexion
                    .getInstancia()
                    .getConnection();

            EstadoCuentaDAO dao
                    = new EstadoCuentaDAO(conn);

            EstadoCuenta estado
                    = dao.obtenerEstadoCuenta(
                            numeroCasa
                    );

            ObservableList<Cuota> lista
                    = FXCollections
                            .observableArrayList();

            lista.addAll(
                    estado.getMesesPagados()
            );

            lista.addAll(
                    estado.getMesesPendientes()
            );

            tbEstadosCuenta.setItems(lista);

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}
