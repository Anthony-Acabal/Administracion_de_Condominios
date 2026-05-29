package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.model.Propietario;
import condominio.proyecto_condominio.logic.RegistroPropietarioLogic;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class RegistroPropietarioController {

    @FXML private VBox vboxTabla;
    @FXML private VBox vboxFormulario;

    @FXML private TableView<Propietario> tablaPropietarios;
    @FXML private TableColumn<Propietario, String> colNombres;
    @FXML private TableColumn<Propietario, String> colApellidos;
    @FXML private TableColumn<Propietario, String> colCasa;
    @FXML private TableColumn<Propietario, String> colTelefono;
    @FXML private TableColumn<Propietario, String> colCorreo;

    @FXML private TextField txtPrimerNombre;
    @FXML private TextField txtSegundoNombre;
    @FXML private TextField txtTercerNombre;
    @FXML private TextField txtPrimerApellido;
    @FXML private TextField txtSegundoApellido;
    @FXML private ComboBox<String> cmbNumeroCasa;
    @FXML private TextField txtNumeroTelefono;
    @FXML private TextField txtCorreoElectronico;
    @FXML private Button btnGuardar;
    @FXML private Button btnCancelar;
    @FXML private Button btnLimpiar;
    @FXML private Button btnRegresar;
    
    private int idPropietarioExistente = -1;
    private String casaActualAlEditar = null; 

    private final RegistroPropietarioLogic logic = new RegistroPropietarioLogic();

    @FXML
    void initialize() {
        cargarCasasFiltroCoordinador();
        configurarFiltroSoloLetras(txtPrimerNombre);
        configurarFiltroSoloLetras(txtSegundoNombre);
        configurarFiltroSoloLetras(txtTercerNombre);
        configurarFiltroSoloLetras(txtPrimerApellido);
        configurarFiltroSoloLetras(txtSegundoApellido);

        txtNumeroTelefono.textProperty().addListener((observable, oldValue, newValue) -> {
            String numeros = newValue.replaceAll("[^\\d]", "");
            if (numeros.length() > 8) numeros = numeros.substring(0, 8);
            String formateado = numeros;
            if (numeros.length() > 4) formateado = numeros.substring(0, 4) + "-" + numeros.substring(4);
            if (!newValue.equals(formateado)) {
                int pos = txtNumeroTelefono.getCaretPosition();
                txtNumeroTelefono.setText(formateado);
                if (formateado.length() > newValue.length()) pos++;
                txtNumeroTelefono.positionCaret(pos);
            }
        });

        colNombres.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPrimerNombre() + " " + 
            (cellData.getValue().getSegundoNombre() != null ? cellData.getValue().getSegundoNombre() : "")
        ));
        
        colApellidos.setCellValueFactory(cellData -> new SimpleStringProperty(
            cellData.getValue().getPrimerApellido() + " " + 
            (cellData.getValue().getSegundoApellido() != null ? cellData.getValue().getSegundoApellido() : "")
        ));
        
        colCasa.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getIdCasa())));
        
        colTelefono.setCellValueFactory(cellData -> {
            String tel = cellData.getValue().getTelefono();
            if (tel != null && tel.length() >= 8) {
                return new SimpleStringProperty(tel.substring(0, 4) + "-" + tel.substring(4));
            }
            return new SimpleStringProperty(tel);
        });

        colCorreo.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCorreoElectronico()));

        cargarTabla();
    }

    private void cargarTabla() {
        try {
            List<Propietario> lista = logic.obtenerTodosLosPropietarios(); 
            ObservableList<Propietario> observableList = FXCollections.observableArrayList(lista);
            tablaPropietarios.setItems(observableList);
        } catch (Exception e) {
            System.out.println("No se pudo cargar la tabla.");
        }
    }

    @FXML
    void actionMostrarFormularioNuevo(ActionEvent event) {
        actionLimpiar(null); 
        vboxTabla.setVisible(false); 
        vboxFormulario.setVisible(true); 
    }

    @FXML
    void actionEditar(ActionEvent event) {
        Propietario seleccionado = tablaPropietarios.getSelectionModel().getSelectedItem();
        
        if (seleccionado != null) {
            cargarDatosPropietario(
                seleccionado.getIdPropietario(),
                seleccionado.getPrimerNombre(),
                seleccionado.getSegundoNombre(),
                seleccionado.getTercerNombre(),
                seleccionado.getPrimerApellido(),
                seleccionado.getSegundoApellido(),
                String.valueOf(seleccionado.getIdCasa()),
                seleccionado.getTelefono(),
                seleccionado.getCorreoElectronico()
            );
            vboxTabla.setVisible(false);
            vboxFormulario.setVisible(true);
        } else {
            mostrarAlerta("Seleccion Requerida", "Por favor, selecciona un propietario de la tabla para editar.", AlertType.WARNING);
        }
    }

    @FXML
    void actionEliminar(ActionEvent event) {
        Propietario seleccionado = tablaPropietarios.getSelectionModel().getSelectedItem();
        
        if (seleccionado != null) {
            String nombreCompleto = seleccionado.getPrimerNombre() + " " + seleccionado.getPrimerApellido();
            boolean eliminado = eliminarPropietarioLogico(seleccionado.getIdPropietario(), nombreCompleto);
            if (eliminado) {
                cargarTabla(); 
            }
        } else {
            mostrarAlerta("Seleccion Requerida", "Por favor, selecciona un propietario de la tabla para eliminar.", AlertType.WARNING);
        }
    }

    @FXML
    void actionGuardar(ActionEvent event) {
        String primerNombre = logic.formatearNombre(txtPrimerNombre.getText());
        String segundoNombre = logic.formatearNombre(txtSegundoNombre.getText());
        String tercerNombre = logic.formatearNombre(txtTercerNombre.getText());
        String primerApellido = logic.formatearNombre(txtPrimerApellido.getText());
        String segundoApellido = logic.formatearNombre(txtSegundoApellido.getText());
        
        String telefonoClean = txtNumeroTelefono.getText().trim().replace("-", ""); 
        String correo = txtCorreoElectronico.getText().trim();
        Object casaSeleccionada = cmbNumeroCasa.getValue();
        
        String numeroCasa = "0";
        if (casaSeleccionada != null) {
            numeroCasa = casaSeleccionada.toString().replaceAll("[^0-9]", "");
        }

        Propietario propietario = new Propietario();
        if (idPropietarioExistente != -1) propietario.setIdPropietario(idPropietarioExistente);
        propietario.setPrimerNombre(primerNombre);
        propietario.setSegundoNombre(segundoNombre);
        propietario.setTercerNombre(tercerNombre);
        propietario.setPrimerApellido(primerApellido);
        propietario.setSegundoApellido(segundoApellido);
        propietario.setIdCasa(Integer.parseInt(numeroCasa));
        propietario.setTelefono(telefonoClean);
        propietario.setCorreoElectronico(correo);

        String errorValidacion = logic.validarPropietario(propietario);
        if (errorValidacion != null && !errorValidacion.isEmpty()) {
            mostrarAlerta("Validacion de Registro", errorValidacion, AlertType.WARNING);
            return;
        }

        boolean exitoso = logic.guardarPropietario(propietario);
        if (exitoso) {
            mostrarAlerta("Operacion Exitosa", "Propietario guardado correctamente.", AlertType.INFORMATION);
            actionLimpiar(null);
            vboxFormulario.setVisible(false);
            vboxTabla.setVisible(true);
            cargarTabla(); 
        } else {
            mostrarAlerta("Error", "No se pudo procesar en la base de datos.", AlertType.ERROR);
        }
    }

    @FXML
    void actionCancelar(ActionEvent event) {
        actionLimpiar(null);
        vboxFormulario.setVisible(false);
        vboxTabla.setVisible(true);
    }
    
    @FXML
    void actionLimpiar(ActionEvent event) {
        txtPrimerNombre.clear();
        txtSegundoNombre.clear();
        txtTercerNombre.clear();
        txtPrimerApellido.clear();
        txtSegundoApellido.clear();
        txtNumeroTelefono.clear();
        txtCorreoElectronico.clear();
        cmbNumeroCasa.getSelectionModel().clearSelection();
        
        idPropietarioExistente = -1;
        casaActualAlEditar = null;
        btnGuardar.setText("Guardar");
        cargarCasasFiltroCoordinador();
    }

    @FXML
    void actionRegresar(ActionEvent event) {
        try {
            java.net.URL urlVista = getClass().getResource("/condominio/proyecto_condominio/ui/Inicio.fxml");
            FXMLLoader loader = new FXMLLoader(urlVista);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) { 
            mostrarAlerta("Error", "No se pudo volver a Inicio.", AlertType.ERROR);
        }   
    }

    public void cargarDatosPropietario(int id, String pNombre, String sNombre, String tNombre, 
                                      String pApellido, String sApellido, String casa, 
                                      String tel, String correo) {
        this.idPropietarioExistente = id;
        this.casaActualAlEditar = casa; 
        txtPrimerNombre.setText(pNombre);
        txtSegundoNombre.setText(sNombre == null ? "" : sNombre);
        txtTercerNombre.setText(tNombre == null ? "" : tNombre);
        txtPrimerApellido.setText(pApellido);
        txtSegundoApellido.setText(sApellido == null ? "" : sApellido);
        cargarCasasFiltroCoordinador();
        cmbNumeroCasa.setValue(casa);
        txtNumeroTelefono.setText(tel); 
        txtCorreoElectronico.setText(correo);
        btnGuardar.setText("Actualizar");
    }

    private void cargarCasasFiltroCoordinador() {
        cmbNumeroCasa.getItems().clear();
        List<String> casasDisponibles = logic.obtenerCasasDisponibles(idPropietarioExistente, casaActualAlEditar);
        cmbNumeroCasa.getItems().addAll(casasDisponibles);
    }

    public boolean eliminarPropietarioLogico(int idPropietario, String nombreCompleto) {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Baja");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas eliminar a: " + nombreCompleto + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            boolean eliminado = logic.darBajaLogicaPropietario(idPropietario);
            if (eliminado) {
                mostrarAlerta("Baja Procesada", "El propietario fue eliminado.", AlertType.INFORMATION);
                return true;
            } else {
                mostrarAlerta("Error", "No se pudo cambiar el estado en BD.", AlertType.ERROR);
            }
        }
        return false;
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void configurarFiltroSoloLetras(TextField campo) {
        campo.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) return;
            
            String textoFiltrado = newValue.replaceAll("[^a-zA-ZáéíóúÁÉÍÓÚñÑ]", "");
            
            if (!textoFiltrado.isEmpty()) {
                textoFiltrado = textoFiltrado.substring(0, 1).toUpperCase() + textoFiltrado.substring(1).toLowerCase();
            }

            if (!newValue.equals(textoFiltrado)) {
                int pos = campo.getCaretPosition();
                int diferencia = newValue.length() - textoFiltrado.length();
                campo.setText(textoFiltrado);
                campo.positionCaret(Math.max(0, pos - diferencia));
            }
        });
    }
}