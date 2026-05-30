package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.logic.CasaPendientePagoLogic;
import condominio.proyecto_condominio.model.CasasPendientesPago;
import condominio.proyecto_condominio.model.Sesion;
import condominio.proyecto_condominio.model.Usuario;
import condominio.proyecto_condominio.service.CorreoService;
import condominio.proyecto_condominio.service.ReporteService;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class CasasPendientesPagoController
        implements Initializable {

    private CasaPendientePagoLogic logic = new CasaPendientePagoLogic();

    @FXML
    private Label lblCPPFecha;

    @FXML
    private TableView<CasasPendientesPago> tbCasasPendientesPago;

    @FXML
    private TableColumn<CasasPendientesPago, Integer> colCPPNumeroCasa;

    @FXML
    private TableColumn<CasasPendientesPago, String> colCPPNombrePropietario;

    @FXML
    private TableColumn<CasasPendientesPago, String> colCPPTelefono;

    @FXML
    private TableColumn<CasasPendientesPago, String> colCPPFecha;

    @FXML
    private ChoiceBox<String> choiceBoxCPPFiltroMes;

    @FXML
    private ChoiceBox<Integer> choiceBoxCPPFiltroAno;

    @FXML
    private Label lblCPPCantidad;

    @FXML
    private Button btnCPPRegresar;

    @FXML
    private Button btnCPPGenerarReporte;

    @FXML
    private void generarReporteCPP() {

        try {

            String mesTexto = choiceBoxCPPFiltroMes.getValue();
            Integer anio = choiceBoxCPPFiltroAno.getValue();

            if (mesTexto == null || anio == null) {
                System.out.println("Filtros inválidos");
                return;
            }

            Usuario usuario = Sesion.getUsuarioActual();

            if (usuario == null) {
                System.out.println("No hay usuario en sesión");
                return;
            }

            String correoDestino = usuario.getCorreo();

            int mesNumero;

            if (mesTexto.equals("Ver todo")) {
                mesNumero = 0;
            } else {
                mesNumero = logic.convertirMesAInt(mesTexto);
            }

            ReporteService service = new ReporteService();

            String pdf = service.generarReportePagosPendientes(mesNumero, anio);

            CorreoService correoService = new CorreoService();

            correoService.enviarCorreoPagosPendientes(
                    pdf,
                    correoDestino,
                    mesTexto,
                    anio
            );

            System.out.println("Reporte enviado a: " + correoDestino);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void regresar(ActionEvent event) {
        try {

            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource(
                            "/condominio/proyecto_condominio/ui/Inicio.fxml"
                    )
            );

            Parent root = loader.load();

            Stage stage = (Stage) ((Node) event.getSource())
                    .getScene()
                    .getWindow();

            stage.getScene().setRoot(root);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ObservableList<CasasPendientesPago> listaCasas;

    private int[] limitesGlobales;

    @Override
    public void initialize(
            URL url,
            ResourceBundle rb
    ) {

        configurarColumnas();

        limitesGlobales = logic.obtenerLimitesFechas();

        inicializarFiltros();

        cargarDatos();
    }

    private void configurarColumnas() {

        colCPPNumeroCasa
                .setCellValueFactory(
                        new PropertyValueFactory<>("numeroCasa")
                );

        colCPPNombrePropietario
                .setCellValueFactory(
                        new PropertyValueFactory<>("nombrePropietario")
                );

        colCPPTelefono
                .setCellValueFactory(
                        new PropertyValueFactory<>("telefono")
                );

        colCPPFecha
                .setCellValueFactory(
                        new PropertyValueFactory<>("fecha")
                );
    }

    private void inicializarFiltros() {

        int anioActual
                = LocalDate.now().getYear();

        int anioMin
                = limitesGlobales[0];

        choiceBoxCPPFiltroAno.getItems().clear();

        for (int i = anioMin; i <= anioActual; i++) {

            choiceBoxCPPFiltroAno
                    .getItems()
                    .add(i);
        }

        choiceBoxCPPFiltroAno.setValue(
                anioActual
        );

        actualizarMesesDisponibles(
                anioActual
        );

        choiceBoxCPPFiltroAno
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, newV) -> {

                    if (newV != null) {

                        actualizarMesesDisponibles(newV);

                        cargarDatos();
                    }
                });

        choiceBoxCPPFiltroMes
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((obs, oldV, newV) -> {

                    if (newV != null) {

                        cargarDatos();
                    }
                });
    }

    private void actualizarMesesDisponibles(
            int anioSeleccionado
    ) {

        String mesPrevio
                = choiceBoxCPPFiltroMes.getValue();

        choiceBoxCPPFiltroMes
                .getItems()
                .clear();

        choiceBoxCPPFiltroMes
                .getItems()
                .add("Ver todo");

        int mesInicio = 1;

        int mesFin = 12;

        if (anioSeleccionado == limitesGlobales[0]) {

            mesInicio = limitesGlobales[1];
        }

        if (anioSeleccionado
                == LocalDate.now().getYear()) {

            mesFin
                    = LocalDate.now()
                            .getMonthValue();
        }

        String[] meses
                = logic.getNombresMeses();

        for (int i = mesInicio; i <= mesFin; i++) {

            choiceBoxCPPFiltroMes
                    .getItems()
                    .add(meses[i - 1]);
        }

        if (mesPrevio != null
                && choiceBoxCPPFiltroMes
                        .getItems()
                        .contains(mesPrevio)) {

            choiceBoxCPPFiltroMes
                    .setValue(mesPrevio);

        } else {

            choiceBoxCPPFiltroMes
                    .setValue(
                            meses[LocalDate.now()
                                    .getMonthValue() - 1]
                    );
        }
    }

    private void cargarDatos() {

        if (choiceBoxCPPFiltroMes.getValue() == null
                || choiceBoxCPPFiltroAno.getValue() == null) {

            return;
        }

        String mes
                = choiceBoxCPPFiltroMes.getValue();

        int anio
                = choiceBoxCPPFiltroAno.getValue();

        var datos
                = logic.obtenerDatos(
                        mes,
                        anio,
                        limitesGlobales
                );

        if (mes.equals("Ver todo")) {

            lblCPPFecha.setText(
                    "Pendientes del año "
                    + anio
            );

        } else {

            lblCPPFecha.setText(
                    "Cuota de "
                    + mes
                    + " del "
                    + anio
            );
        }

        listaCasas
                = FXCollections.observableArrayList(datos);

        tbCasasPendientesPago
                .setItems(listaCasas);

        lblCPPCantidad.setText(
                "Casas pendientes de pago: "
                + datos.size()
        );
    }
}
