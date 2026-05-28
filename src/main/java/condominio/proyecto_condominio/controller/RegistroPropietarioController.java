package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.model.Propietario;
import condominio.proyecto_condominio.logic.RegistroPropietarioLogic;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.stage.Stage;

public class RegistroPropietarioController {

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

    // Instancia única a la capa lógica. La UI ya no conoce al DAO.
    private final RegistroPropietarioLogic logic = new RegistroPropietarioLogic();

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
        
        // Pide la lista limpia a la lógica sin procesar algoritmos visuales aquí
        List<String> casasDisponibles = logic.obtenerCasasDisponibles(idPropietarioExistente, casaActualAlEditar);
        
        // El controlador solo dibuja en pantalla
        cmbNumeroCasa.getItems().addAll(casasDisponibles);
    }

    @FXML
    void actionGuardar(ActionEvent event) {
        // Formateo delegado por completo a la capa Logic
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
        if (idPropietarioExistente != -1) {
            propietario.setIdPropietario(idPropietarioExistente);
        }
        propietario.setPrimerNombre(primerNombre);
        propietario.setSegundoNombre(segundoNombre);
        propietario.setTercerNombre(tercerNombre);
        propietario.setPrimerApellido(primerApellido);
        propietario.setSegundoApellido(segundoApellido);
        propietario.setNumeroCasa(numeroCasa);
        propietario.setTelefono(telefonoClean);
        propietario.setCorreoElectronico(correo);

        String errorValidacion = logic.validarPropietario(propietario);
        if (errorValidacion != null && !errorValidacion.isEmpty()) {
            mostrarAlerta("Validación de Registro", errorValidacion, AlertType.WARNING);
            return;
        }

        boolean exitoso = logic.guardarPropietario(propietario);
        if (exitoso) {
            if (idPropietarioExistente == -1) {
                mostrarAlerta("Registro Exitoso", "El propietario ha sido registrado correctamente.", AlertType.INFORMATION);
            } else {
                mostrarAlerta("Actualización Exitosa", "Los datos del propietario han sido actualizados.", AlertType.INFORMATION);
            }
            actionLimpiar(event);
            actionRegresar(event);
        } else {
            mostrarAlerta("Error de Operación", "No se pudo procesar la solicitud en la base de datos.", AlertType.ERROR);
        }
    }

    public boolean eliminarPropietarioLogico(int idPropietario, String nombreCompleto) {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Baja Lógica");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas pasar al estado 'Eliminado' al propietario: " + nombreCompleto + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            
            // Llamada 100% aislada a través de Logic. Cero instancias directas al DAO.
            boolean eliminado = logic.darBajaLogicaPropietario(idPropietario);
            
            if (eliminado) {
                mostrarAlerta("Baja Procesada", "El propietario ha sido cambiado a estado: Eliminado.", AlertType.INFORMATION);
                return true;
            } else {
                mostrarAlerta("Error de Eliminación", "No se pudo cambiar el estado en la base de datos.", AlertType.ERROR);
            }
        }
        return false;
    }

    @FXML
    void actionCancelar(ActionEvent event) {
        actionLimpiar(event);
        actionRegresar(event);
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
            
            if (urlVista == null) {
                mostrarAlerta("Error de Ruta", "¡Java no encuentra el archivo! Verifica la ruta.", AlertType.ERROR);
                return;
            }

            FXMLLoader loader = new FXMLLoader(urlVista);
            Parent root = loader.load();
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Panel de Inicio");
            stage.show();
            
        } catch (IOException e) { 
            mostrarAlerta("Error de Navegación", "No se pudo abrir la ventana: " + e.getMessage(), AlertType.ERROR);
        }   
    }

    private void mostrarAlerta(String titulo, String mensaje, AlertType tipo) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

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
            
            if (numeros.length() > 8) {
                numeros = numeros.substring(0, 8);
            }
            
            String formateado = numeros;
            if (numeros.length() > 4) {
                formateado = numeros.substring(0, 4) + "-" + numeros.substring(4);
            }
            
            if (!newValue.equals(formateado)) {
                txtNumeroTelefono.setText(formateado);
            }
        });
    }

    private void configurarFiltroSoloLetras(TextField campo) {
        campo.setTextFormatter(new TextFormatter<>(change -> {
            String nuevoTexto = change.getText();
            if (nuevoTexto.matches("^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]*$")) {
                return change;
            }
            return null;
        }));
    }
}