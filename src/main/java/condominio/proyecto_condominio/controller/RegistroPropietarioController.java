package condominio.proyecto_condominio.controller;

import condominio.proyecto_condominio.model.Conexion;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
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
        
        String sql = "SELECT numero_casa FROM Propietario WHERE estado = 'Activo'";
        if (idPropietarioExistente != -1) {
            sql += " AND id_propietario <> ?";
        }

        List<String> casasOcupadas = new ArrayList<>();
        
        try (Connection con = Conexion.getConexion()) {
            if (con != null) {
                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    if (idPropietarioExistente != -1) {
                        ps.setInt(1, idPropietarioExistente);
                    }
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            casasOcupadas.add(rs.getString("numero_casa"));
                        }
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al filtrar casas: " + e.getMessage());
        }

        for (int i = 1; i <= 30; i++) {
            String casaStr = String.valueOf(i);
            if (!casasOcupadas.contains(casaStr)) {
                cmbNumeroCasa.getItems().add(casaStr);
            }
        }
        
        if (casaActualAlEditar != null && !cmbNumeroCasa.getItems().contains(casaActualAlEditar)) {
            cmbNumeroCasa.getItems().add(casaActualAlEditar);
        }
    }

    private String verificarDuplicados(String nroCasa, String telefonoClean, String correo) {
        String sql = "SELECT id_propietario, numero_casa, telefono, correo FROM Propietario " +
                     "WHERE (numero_casa = ? OR telefono = ? OR correo = ?) AND estado = 'Activo'";
        
        if (idPropietarioExistente != -1) {
            sql += " AND id_propietario <> ?";
        }
        
        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                return "Error: No se pudo establecer conexión con el servidor de Base de Datos.";
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, nroCasa);
                ps.setString(2, telefonoClean);
                ps.setString(3, correo);
                
                if (idPropietarioExistente != -1) {
                    ps.setInt(4, idPropietarioExistente);
                }
                
                try (ResultSet rs = ps.executeQuery()) {
                    if (rs.next()) {
                        if (rs.getString("numero_casa").equalsIgnoreCase(nroCasa)) {
                            return "El número de casa (" + nroCasa + ") ya se encuentra ocupado por otro propietario activo.";
                        }
                        if (rs.getString("telefono").equalsIgnoreCase(telefonoClean)) {
                            return "El número de teléfono (" + telefonoClean + ") ya está asignado a otro propietario activo.";
                        }
                        if (rs.getString("correo").equalsIgnoreCase(correo)) {
                            return "El correo electrónico (" + correo + ") ya está registrado a otro propietario activo.";
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Error al validar datos duplicados en el servidor: " + e.getMessage();
        }
        return null;
    }

    private String formatearNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }
        texto = texto.trim();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    @FXML
    void actionGuardar(ActionEvent event) {
        String primerNombre = formatearNombre(txtPrimerNombre.getText());
        String segundoNombre = formatearNombre(txtSegundoNombre.getText());
        String tercerNombre = formatearNombre(txtTercerNombre.getText());
        String primerApellido = formatearNombre(txtPrimerApellido.getText());
        String segundoApellido = formatearNombre(txtSegundoApellido.getText());
        
        String telefonoConGuion = txtNumeroTelefono.getText().trim();
        String telefonoClean = telefonoConGuion.replace("-", ""); 
        
        String correo = txtCorreoElectronico.getText().trim();
        Object casaSeleccionada = cmbNumeroCasa.getValue();

        if (primerNombre.isEmpty() || primerApellido.isEmpty() || telefonoClean.isEmpty() || correo.isEmpty() || casaSeleccionada == null) {
            mostrarAlerta("Campos Incompletos", "Por favor, complete todos los campos obligatorios del formulario.", AlertType.WARNING);
            return;
        }
        
        if (telefonoClean.length() < 8) {
            mostrarAlerta("Teléfono Inválido", "Por favor, ingrese un número de teléfono completo de 8 dígitos.", AlertType.WARNING);
            return;
        }

        String formatoCorreo = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!correo.matches(formatoCorreo)) {
            mostrarAlerta("Correo Inválido", "Por favor, ingrese un correo electrónico válido (ejemplo: usuario@gmail.com).", AlertType.WARNING);
            return;
        }

        String numeroCasaTexto = casaSeleccionada.toString(); 
        String numeroCasa = numeroCasaTexto.replaceAll("[^0-9]", ""); 

        String mensajeDuplicado = verificarDuplicados(numeroCasa, telefonoClean, correo);
        if (mensajeDuplicado != null) {
            mostrarAlerta("Validación de Registro", mensajeDuplicado, AlertType.ERROR);
            return;
        }

        String sql;
        if (idPropietarioExistente == -1) {
            sql = "INSERT INTO Propietario (primer_nombre, segundo_nombre, tercer_nombre, " +
                  "primer_apellido, segundo_apellido, numero_casa, telefono, correo, " +
                  "fecha_creacion, id_usuario_creacion, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Activo')";
        } else {
            sql = "UPDATE Propietario SET primer_nombre=?, segundo_nombre=?, tercer_nombre=?, " +
                  "primer_apellido=?, segundo_apellido=?, numero_casa=?, telefono=?, correo=? " +
                  "WHERE id_propietario=?";
        }

        try (Connection con = Conexion.getConexion()) {
            if (con == null) {
                mostrarAlerta("Error de Conexión", "No se pudo conectar a la Base de Datos.", AlertType.ERROR);
                return;
            }
            try (PreparedStatement ps = con.prepareStatement(sql)) {
                ps.setString(1, primerNombre);
                ps.setString(2, segundoNombre.isEmpty() ? null : segundoNombre);
                ps.setString(3, tercerNombre.isEmpty() ? null : tercerNombre);
                ps.setString(4, primerApellido);
                ps.setString(5, segundoApellido.isEmpty() ? null : segundoApellido);
                ps.setString(6, numeroCasa);
                ps.setString(7, telefonoClean); 
                ps.setString(8, correo);

                if (idPropietarioExistente == -1) {
                    ps.setDate(9, Date.valueOf(LocalDate.now()));
                    ps.setInt(10, 1); 
                } else {
                    ps.setInt(9, idPropietarioExistente);
                }

                int filasAfectadas = ps.executeUpdate();
                if (filasAfectadas > 0) {
                    if (idPropietarioExistente == -1) {
                        mostrarAlerta("Registro Exitoso", "El propietario ha sido registrado correctamente.", AlertType.INFORMATION);
                    } else {
                        mostrarAlerta("Actualización Exitosa", "Los datos del propietario han sido actualizados.", AlertType.INFORMATION);
                    }
                    actionLimpiar(event);
                    actionRegresar(event);
                }
            }
        } catch (Exception e) {
            mostrarAlerta("Error de Operación", "No se pudo procesar en la base de datos: " + e.getMessage(), AlertType.ERROR);
        }
    }

    public boolean eliminarPropietarioLogico(int idPropietario, String nombreCompleto) {
        Alert confirmacion = new Alert(AlertType.CONFIRMATION);
        confirmacion.setTitle("Confirmar Baja Lógica");
        confirmacion.setHeaderText(null);
        confirmacion.setContentText("¿Seguro que deseas pasar al estado 'Eliminado' al propietario: " + nombreCompleto + "?");

        Optional<ButtonType> resultado = confirmacion.showAndWait();
        if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
            String sql = "UPDATE Propietario SET estado = 'Eliminado' WHERE id_propietario = ?";
            
            try (Connection con = Conexion.getConexion()) {
                if (con != null) {
                    try (PreparedStatement ps = con.prepareStatement(sql)) {
                        ps.setInt(1, idPropietario);
                        int afectadas = ps.executeUpdate();
                        
                        if (afectadas > 0) {
                            mostrarAlerta("Baja Procesada", "El propietario ha sido cambiado a estado: Eliminado.", AlertType.INFORMATION);
                            return true;
                        }
                    }
                }
            } catch (Exception e) {
                mostrarAlerta("Error de Eliminación", "No se pudo cambiar el estado: " + e.getMessage(), AlertType.ERROR);
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
            // CORREGIDO: Ruta absoluta desde la raíz de recursos para entornos Maven
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