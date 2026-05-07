/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_condominio.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import java.time.LocalDate;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import proyecto_condominio.src.Config;
/**
 * FXML Controller class
 *
 * @author yecut
 */
public class RegistroPagoViewController implements Initializable {
    @FXML
    private ComboBox<String> cbCasa;

    @FXML
    private TextField txtNombre;

    @FXML
    private TextField txtApellido;

    @FXML
    private TextField txtTelefono;

    @FXML
    private TextField txtCuota;

    @FXML
    private ComboBox<String> cbMes;

    @FXML
    private ComboBox<String> cbAnio;

    @FXML
    private Button btnRegistrar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnImprimir;

    @FXML
    private Label lblEstado;

    @FXML
    private Button btnCerrar;
    /**
     * Initializes the controller class.
     */
    @Override
public void initialize(URL url, ResourceBundle rb) {

    cbMes.getItems().addAll(
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

    cbAnio.getItems().addAll(
        "2025",
        "2026",
        "2027",
        "2028"
    );

    LocalDate fechaActual = LocalDate.now();

    int mesActual = fechaActual.getMonthValue();
    cbMes.getSelectionModel().select(mesActual - 1);

    int anioActual = fechaActual.getYear();
    cbAnio.setValue(String.valueOf(anioActual));
    
    cargarCasas();
}
private void cargarCasas() {

    String sql = """
    SELECT numero_casa
    FROM propietario
    ORDER BY numero_casa ASC
    """;

    try (
        Connection conn = Config.getConexion();
        PreparedStatement ps = conn.prepareStatement(sql);
        ResultSet rs = ps.executeQuery();
    ) {

        while (rs.next()) {

            cbCasa.getItems().add(
                rs.getString("numero_casa")
            );

        }

    } catch (Exception e) {

        lblEstado.setText("Error al cargar casas");

        System.out.println(e.getMessage());
    }
}
    @FXML
private void seleccionarCasa() {

    String sql = """
        SELECT 
            primer_nombre,
            segundo_nombre,
            tercer_nombre,
            primer_apellido,
            segundo_apellido,
            telefono
        FROM propietario
        WHERE numero_casa = ?
    """;

    try (
        Connection conn = Config.getConexion();
        PreparedStatement ps = conn.prepareStatement(sql);
    ) {

        ps.setInt(
            1,
            Integer.parseInt(cbCasa.getValue())
        );

        ResultSet rs = ps.executeQuery();

        if (rs.next()) {

            String nombre = rs.getString("primer_nombre");

String segundoNombre = rs.getString("segundo_nombre");
String tercerNombre = rs.getString("tercer_nombre");

if (segundoNombre != null) {
    nombre += " " + segundoNombre;
}

if (tercerNombre != null) {
    nombre += " " + tercerNombre;
}

txtNombre.setText(nombre);

            String apellido = rs.getString("primer_apellido");

String segundoApellido = rs.getString("segundo_apellido");

if (segundoApellido != null) {
    apellido += " " + segundoApellido;
}

txtApellido.setText(apellido);

            txtTelefono.setText(
                rs.getString("telefono")
            );

            txtCuota.setText("Q1500");

        }

    } catch (Exception e) {

        lblEstado.setText(
            "Error al cargar propietario"
        );

        System.out.println(e.getMessage());
    }
}
}
