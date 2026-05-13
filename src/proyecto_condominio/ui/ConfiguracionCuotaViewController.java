/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_condominio.ui;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import proyecto_condominio.model.Cuota;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import proyecto_condominio.src.Config;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author yecut
 */
public class ConfiguracionCuotaViewController
implements Initializable {

    private Cuota cuota = new Cuota();

    @FXML
    private TextField txtCuotaMensual;

    @FXML
    private TextField txtNuevaCuota;

    @FXML
    private TextField txtRecaudacionMensual;

    @FXML
    private DatePicker dpFechaModificacion;

    @FXML
    private TextField lblHora;

    @FXML
    private Button btnGuardar;

    @FXML
    private Button btnLimpiar;

    @FXML
    private Button btnCerrar;

    @FXML
    private ComboBox<String> cbUsuario;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(
        URL url,
        ResourceBundle rb
    ) {

        cargarCuotaActual();

        cargarUsuarios();

        txtNuevaCuota
            .textProperty()
            .addListener(

            (observable, oldValue, newValue) -> {

                if (
                    !newValue.matches("\\d*")
                ) {

                    txtNuevaCuota.setText(
                        oldValue
                    );
                }
            }
        );

        dpFechaModificacion.setValue(
            java.time.LocalDate.now()
        );

        lblHora.setText(
            java.time.LocalTime.now()
                .withNano(0)
                .toString()
        );

        txtCuotaMensual.setEditable(false);

        txtCuotaMensual
            .setFocusTraversable(false);

        txtCuotaMensual.deselect();

        txtRecaudacionMensual
            .setEditable(false);

        txtRecaudacionMensual
            .setFocusTraversable(false);

        dpFechaModificacion
            .setDisable(true);

        lblHora.setEditable(false);

        btnGuardar.requestFocus();
    }

    @FXML
    private void guardarConfiguracion() {

        btnGuardar.setDisable(true);

        if (
            txtNuevaCuota.getText() == null
            || txtNuevaCuota
                .getText()
                .trim()
                .isEmpty()
        ) {

            mostrarAdvertencia(
                "Campo Vacío",
                "No se ingresó una cuota",
                "Debe ingresar un monto válido para actualizar la cuota."
            );

            btnGuardar.setDisable(false);

            return;
        }

        if (
            cbUsuario.getValue() == null
        ) {

            mostrarAdvertencia(
                "Usuario requerido",
                "No se seleccionó un usuario",
                "Debe seleccionar un usuario antes de guardar la configuración."
            );

            btnGuardar.setDisable(false);

            return;
        }

        cuota.setMontoCuota(
            Double.parseDouble(
                txtNuevaCuota.getText()
            )
        );

        if (
            cuota.getMontoCuota() <= 0
        ) {

            mostrarAdvertencia(
                "Monto inválido",
                "La cuota no es válida",
                "La cuota debe ser mayor a 0."
            );

            btnGuardar.setDisable(false);

            return;
        }

        if (
            cuota.getMontoCuota() > 100000
        ) {

            mostrarAdvertencia(
                "Monto demasiado alto",
                "La cuota excede el límite permitido",
                "Ingrese una cuota razonable."
            );

            btnGuardar.setDisable(false);

            return;
        }

        if (
            cuota.getMontoCuota()
            ==
            Double.parseDouble(
                txtCuotaMensual
                    .getText()
                    .replace("Q", "")
            )
        ) {

            mostrarAdvertencia(
                "Cuota sin cambios",
                "La cuota ingresada ya existe",
                "Debe ingresar un monto diferente\nal actual para realizar una actualización."
            );

            btnGuardar.setDisable(false);

            return;
        }

        int idUsuario = 0;

        String sqlUsuario = """
            SELECT id_usuario
            FROM usuario
            WHERE usuario = ?
        """;

        try (

            Connection conn =
                Config.getConexion();

            PreparedStatement ps =
                conn.prepareStatement(sqlUsuario);

        ) {

            ps.setString(
                1,
                cbUsuario.getValue()
            );

            ResultSet rs =
                ps.executeQuery();

            if (rs.next()) {

                idUsuario =
                    rs.getInt(
                        "id_usuario"
                    );
            }

        } catch (Exception e) {

            System.out.println(
                e.getMessage()
            );
        }

        if (idUsuario == 0) {

            mostrarAdvertencia(
                "Usuario inválido",
                "No se encontró el usuario",
                "Seleccione un usuario válido."
            );

            btnGuardar.setDisable(false);

            return;
        }

        String sql = """
            INSERT INTO cuota
            (
                cuota,
                id_estado,
                id_usuario_creacion,
                fecha_creacion
            )
            VALUES (?, ?, ?, GETDATE())
        """;

        try (

            Connection conn =
                Config.getConexion();

            PreparedStatement ps =
                conn.prepareStatement(sql);

        ) {

            ps.setDouble(
                1,
                cuota.getMontoCuota()
            );

            ps.setInt(2, 18);

            ps.setInt(3, idUsuario);

            ps.executeUpdate();

            mostrarExito();

            cargarCuotaActual();

            dpFechaModificacion.setValue(
                java.time.LocalDate.now()
            );

            lblHora.setText(
                java.time.LocalTime.now()
                    .withNano(0)
                    .toString()
            );

            txtNuevaCuota.clear();

            cbUsuario.setValue(null);

            btnGuardar.setDisable(false);

            btnGuardar.requestFocus();

        } catch (Exception e) {

            System.out.println(
                e.getMessage()
            );

            btnGuardar.setDisable(false);
        }
    }

    @FXML
    private void limpiarCampos() {

        txtNuevaCuota.clear();

        cbUsuario.setValue(null);

        txtNuevaCuota.requestFocus();
    }

    private void cargarCuotaActual() {

        String sql = """
            SELECT TOP 1 cuota
            FROM cuota
            ORDER BY id_cuota DESC
        """;

        try (

            Connection conn =
                Config.getConexion();

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

        ) {

            if (rs.next()) {

                cuota.setMontoCuota(
                    rs.getDouble(
                        "cuota"
                    )
                );

                txtCuotaMensual.setText(
                    "Q" +
                    cuota.getMontoCuota()
                );

                calcularRecaudacion();

            } else {

                txtCuotaMensual.setText(
                    "Q0"
                );

                calcularRecaudacion();
            }

        } catch (Exception e) {

            System.out.println(
                e.getMessage()
            );
        }
    }

    private void calcularRecaudacion() {

        String sql = """
            SELECT COUNT(*) AS total_casas
            FROM propietario
        """;

        try (

            Connection conn =
                Config.getConexion();

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

        ) {

            if (rs.next()) {

                int totalCasas =
                    rs.getInt(
                        "total_casas"
                    );

                double recaudacion =
                    totalCasas
                    * cuota.getMontoCuota()
                    * 12;

                txtRecaudacionMensual
                    .setText(
                        "Q" +
                        String.format(
                            "%,.2f",
                            recaudacion
                        )
                    );
            }

        } catch (Exception e) {

            System.out.println(
                e.getMessage()
            );
        }
    }

    private void mostrarAdvertencia(
        String titulo,
        String encabezado,
        String mensaje
    ) {

        Alert alerta =
            new Alert(
                Alert.AlertType.WARNING
            );

        alerta.setTitle(titulo);

        alerta.setHeaderText(encabezado);

        alerta.setContentText(
            mensaje +
            "\n\nEsta ventana se cerrará en 5 segundos."
        );

        alerta.show();

        Timeline timeline =
            new Timeline(
                new KeyFrame(
                    Duration.seconds(5),
                    event -> alerta.close()
                )
            );

        timeline.setCycleCount(1);

        timeline.play();
    }

    private void mostrarExito() {

        Alert alerta =
            new Alert(
                Alert.AlertType.INFORMATION
            );

        alerta.setTitle(
            "Cuota Actualizada"
        );

        alerta.setHeaderText(
            "Actualización exitosa"
        );

        alerta.setContentText(
            "La cuota mensual fue actualizada correctamente."
        );

        alerta.show();

        Timeline timeline =
            new Timeline(
                new KeyFrame(
                    Duration.seconds(5),
                    event -> alerta.close()
                )
            );

        timeline.setCycleCount(1);

        timeline.play();
    }

    private void cargarUsuarios() {

        cbUsuario.getItems().clear();

        String sql = """
            SELECT usuario
            FROM usuario
        """;

        try (

            Connection conn =
                Config.getConexion();

            PreparedStatement ps =
                conn.prepareStatement(sql);

            ResultSet rs =
                ps.executeQuery();

        ) {

            while (rs.next()) {

                cbUsuario
                    .getItems()
                    .add(
                        rs.getString(
                            "usuario"
                        )
                    );
            }

            if (
                cbUsuario
                    .getItems()
                    .isEmpty()
            ) {

                mostrarAdvertencia(
                    "Sin usuarios",
                    "No hay usuarios registrados",
                    "Debe existir al menos un usuario en el sistema."
                );
            }

        } catch (Exception e) {

            System.out.println(
                e.getMessage()
            );
        }
    }
}