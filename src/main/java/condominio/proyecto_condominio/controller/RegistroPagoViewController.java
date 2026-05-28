
package condominio.proyecto_condominio.controller;

import java.net.URL;
<<<<<<< HEAD
import java.util.ResourceBundle;

import javafx.fxml.Initializable;
import javafx.fxml.FXML;

import javafx.scene.control.ComboBox;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
=======
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;

import javafx.scene.Node;

import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

import javafx.stage.Stage;

import javafx.event.ActionEvent;
>>>>>>> origin/main

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;

import javafx.util.Duration;

<<<<<<< HEAD
import javafx.stage.Stage;

import javafx.scene.Node;

import javafx.event.ActionEvent;

import java.time.LocalDate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import condominio.proyecto_condominio.dao.Conexion;

import condominio.proyecto_condominio.model.Propietario;
import condominio.proyecto_condominio.model.PagoCuota;
import condominio.proyecto_condominio.model.Cuota;
import condominio.proyecto_condominio.model.Casa;

/**
 * FXML Controller class
 *
 * @author yecut
 */
=======
import condominio.proyecto_condominio.logic.RegistroPagoCuotaLogic;

import condominio.proyecto_condominio.model.Propietario;
import condominio.proyecto_condominio.model.PagoCuota;

>>>>>>> origin/main
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
    private Button btnRegresar;

<<<<<<< HEAD
=======
        private RegistroPagoCuotaLogic registroLogic
                = new RegistroPagoCuotaLogic();

    private final String[] meses = {
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

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        int anioActual = LocalDate.now().getYear();

        cbAnio.setValue(
                String.valueOf(anioActual)
        );

        cargarCasas();
    }

>>>>>>> origin/main
    @FXML
    private void registrarPago(ActionEvent event) {

        if (cbCasa.getValue() == null) {

            mostrarAdvertencia(
                    "Validación de Registro",
                    "No se ha seleccionado una casa",
                    "Por favor, seleccione una casa antes de registrar el pago."
            );

            return;
        }

<<<<<<< HEAD
        if (cbMes.getValue() == null || cbAnio.getValue() == null) {
=======
        if (cbMes.getValue() == null
                || cbAnio.getValue() == null) {
>>>>>>> origin/main

            mostrarAdvertencia(
                    "Validación de Registro",
                    "Mes o año no seleccionado",
                    "Por favor, seleccione un mes y un año antes de registrar el pago."
            );

            return;
        }

<<<<<<< HEAD
        String sqlValidar = """
        SELECT COUNT(*) 
        FROM pago_cuota pc
        INNER JOIN propietario p
            ON pc.id_propietario = p.id_propietario
        WHERE p.numero_casa = ?
        AND MONTH(pc.fecha_pago) = ?
        AND YEAR(pc.fecha_pago) = ?
    """;
=======
        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );
>>>>>>> origin/main

        int mesSeleccionado
                = obtenerNumeroMes(
                        cbMes.getValue()
                );

        int anioSeleccionado
<<<<<<< HEAD
                = Integer.parseInt(cbAnio.getValue());

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sqlValidar);) {

            ps.setInt(
                    1,
                    Integer.parseInt(cbCasa.getValue())
            );

            ps.setInt(2, mesSeleccionado);

            ps.setInt(3, anioSeleccionado);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                int cantidad = rs.getInt(1);

                if (cantidad > 0) {

                    return;
                }
            }

            int mesAnterior = mesSeleccionado - 1;
            int anioAnterior = anioSeleccionado;

            if (mesAnterior == 0) {

                mesAnterior = 12;
                anioAnterior--;
            }

            String sqlMesAnterior = """
            SELECT COUNT(*)
            FROM pago_cuota pc
            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario
            WHERE p.numero_casa = ?
            AND MONTH(pc.fecha_pago) = ?
            AND YEAR(pc.fecha_pago) = ?
        """;

            PreparedStatement psAnterior
                    = conn.prepareStatement(sqlMesAnterior);

            psAnterior.setInt(
                    1,
                    Integer.parseInt(cbCasa.getValue())
            );

            psAnterior.setInt(2, mesAnterior);

            psAnterior.setInt(3, anioAnterior);

            ResultSet rsAnterior
                    = psAnterior.executeQuery();

            if (rsAnterior.next()) {

                int pagosAnteriores
                        = rsAnterior.getInt(1);

                if (pagosAnteriores == 0
                        && mesSeleccionado != 1) {

                    mostrarAdvertencia(
                            "Pago Pendiente Detectado",
                            "Existen meses anteriores pendientes",
                            "Debe registrar primero los meses pendientes anteriores antes de continuar con el pago seleccionado."
                    );

                    return;
                }
            }

            String sqlPropietario = """
            SELECT id_propietario
            FROM propietario
            WHERE numero_casa = ?
        """;

            PreparedStatement psPropietario
                    = conn.prepareStatement(sqlPropietario);

            psPropietario.setInt(
                    1,
                    Integer.parseInt(cbCasa.getValue())
            );

            ResultSet rsPropietario
                    = psPropietario.executeQuery();

            int idPropietario = 0;

            if (rsPropietario.next()) {

                idPropietario
                        = rsPropietario.getInt("id_propietario");
            }

            PagoCuota pago
                    = new PagoCuota();
            pago.setIdPropietario(
                    idPropietario
            );

            String sqlUltimaCuota = """
SELECT TOP 1 id_cuota
FROM cuota
ORDER BY fecha_creacion DESC
""";

            PreparedStatement psCuota
                    = conn.prepareStatement(sqlUltimaCuota);

            ResultSet rsCuota
                    = psCuota.executeQuery();

            int idCuotaActual = 0;

            if (rsCuota.next()) {

                idCuotaActual
                        = rsCuota.getInt("id_cuota");
            }

            pago.setIdCuota(idCuotaActual);

            LocalDate fechaPago
                    = LocalDate.of(
                            anioSeleccionado,
                            mesSeleccionado,
                            1
                    );

            pago.setFechaPago(
                    fechaPago
            );

            pago.setImprimeComprobante("S");

            pago.setIdUsuarioCreacion(1);
            String sqlInsertar = """
            INSERT INTO pago_cuota
            (
                id_propietario,
                id_cuota,
                fecha_pago,
                imprime_comprobante,
                id_usuario_creacion
            )
            VALUES (?, ?, ?, ?, ?)
        """;

            PreparedStatement psInsertar
                    = conn.prepareStatement(sqlInsertar);

            psInsertar.setInt(
                    1,
                    pago.getIdPropietario()
            );

            psInsertar.setInt(
                    2,
                    pago.getIdCuota()
            );

            psInsertar.setDate(
                    3,
                    java.sql.Date.valueOf(
                            pago.getFechaPago()
                    )
            );

            psInsertar.setString(
                    4,
                    pago.getImprimeComprobante()
            );

            psInsertar.setInt(
                    5,
                    pago.getIdUsuarioCreacion()
            );

            psInsertar.executeUpdate();
=======
                = Integer.parseInt(
                        cbAnio.getValue()
                );
        String validacion
        = registroLogic.validarPago(
                numeroCasa,
                mesSeleccionado,
                anioSeleccionado
        );

if (validacion != null) {

    mostrarAdvertencia(
            "Validación",
            "No se pudo registrar el pago",
            validacion
    );

    return;
}

boolean registrado
        = registroLogic.registrarPago(
                numeroCasa,
                mesSeleccionado,
                anioSeleccionado
        );

        if (registrado) {
>>>>>>> origin/main

            Alert alerta = new Alert(
                    Alert.AlertType.CONFIRMATION
            );

            alerta.setTitle(
                    "Registro Exitoso"
            );

            alerta.setHeaderText(
                    "¡Registro Exitoso!"
            );

            alerta.setContentText(
                    "¿Desea imprimir el recibo?"
            );

            ButtonType btnSi
                    = new ButtonType("Sí");

            ButtonType btnNo
                    = new ButtonType("No");

            alerta.getButtonTypes().setAll(
                    btnSi,
                    btnNo
            );

            alerta.showAndWait();
<<<<<<< HEAD
            cargarMesesPendientes();

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        LocalDate fechaActual
                = LocalDate.now();

        int anioActual
                = fechaActual.getYear();

        cbAnio.setValue(
                String.valueOf(anioActual)
        );

        cargarCasas();
    }

    private void cargarCasas() {

        String sql = """
    SELECT numero_casa
    FROM propietario
    ORDER BY numero_casa ASC
    """;

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery();) {

            while (rs.next()) {
                Casa casa
                        = new Casa();
                casa.setNumeroCasa(
                        rs.getInt("numero_casa")
                );

                cbCasa.getItems().add(
                        String.valueOf(
                                casa.getNumeroCasa()
                        )
                );

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }
    private final String[] meses = {
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
=======

            cargarMesesPendientes();

        } else {

            mostrarAdvertencia(
                    "Error",
                    "No se pudo registrar el pago",
                    "Ocurrió un error al guardar el pago."
            );
        }
    }

    private void cargarCasas() {

        cbCasa.getItems().addAll(
                registroLogic.obtenerCasas()
        );
    }
>>>>>>> origin/main

    private void cargarMesesPendientes() {

        cbMes.getItems().clear();

        if (cbCasa.getValue() == null) {
<<<<<<< HEAD
            return;
        }

        int anioActual
=======

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        int anio
>>>>>>> origin/main
                = Integer.parseInt(
                        cbAnio.getValue()
                );

<<<<<<< HEAD
        LocalDate fechaActual
                = LocalDate.now();

        int mesActual
                = fechaActual.getMonthValue();

        String sql = """
        SELECT MONTH(fecha_pago) AS mes
        FROM pago_cuota pc
        INNER JOIN propietario p
            ON pc.id_propietario = p.id_propietario
        WHERE p.numero_casa = ?
        AND YEAR(fecha_pago) = ?
    """;

        try (
                Connection conn = Conexion.getConnection(); PreparedStatement ps
                = conn.prepareStatement(sql);) {

            ps.setInt(
                    1,
                    Integer.parseInt(cbCasa.getValue())
            );

            ps.setInt(2, anioActual);

            ResultSet rs
                    = ps.executeQuery();

            boolean[] mesesPagados
                    = new boolean[12];

            while (rs.next()) {

                int mesPagado
                        = rs.getInt("mes");

                mesesPagados[mesPagado - 1] = true;
            }

            for (int i = 0; i < mesActual; i++) {

                if (!mesesPagados[i]) {

                    cbMes.getItems().add(
                            meses[i]
                    );
                }
            }
            if (cbMes.getItems().isEmpty()) {

                Alert alerta = new Alert(Alert.AlertType.INFORMATION);

                alerta.setTitle("Sin cuotas pendientes");

                alerta.setHeaderText("No quedan cuotas pendientes");

                alerta.setContentText(
                        "No quedan cuotas pendientes por pagar."
                );

                alerta.getDialogPane().setPrefWidth(400);

                Timeline timeline = new Timeline(
                        new KeyFrame(
                                Duration.seconds(5),
                                event -> alerta.close()
                        )
                );

                timeline.setCycleCount(1);

                timeline.play();

                alerta.showAndWait();
            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

    }

    @FXML
    private void cerrarVentana(ActionEvent event) {

        Stage stage = (Stage) ((Node) event.getSource())
                .getScene()
                .getWindow();

        stage.close();
=======
        cbMes.getItems().addAll(
        registroLogic.obtenerMesesPendientes(
                numeroCasa,
                anio
        )
        );

        if (cbMes.getItems().isEmpty()) {

            Alert alerta = new Alert(
                    Alert.AlertType.INFORMATION
            );

            alerta.setTitle(
                    "Sin cuotas pendientes"
            );

            alerta.setHeaderText(
                    "No quedan cuotas pendientes"
            );

            alerta.setContentText(
                    "No quedan cuotas pendientes por pagar."
            );

            alerta.getDialogPane().setPrefWidth(400);

            Timeline timeline = new Timeline(
                    new KeyFrame(
                            Duration.seconds(5),
                            event -> alerta.close()
                    )
            );

            timeline.setCycleCount(1);

            timeline.play();

            alerta.showAndWait();
        }
>>>>>>> origin/main
    }

    @FXML
    private void seleccionarCasa() {

<<<<<<< HEAD
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
                Connection conn = Conexion.getConnection(); PreparedStatement ps = conn.prepareStatement(sql);) {

            ps.setInt(
                    1,
                    Integer.parseInt(cbCasa.getValue())
            );

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                Propietario propietario
                        = new Propietario();

                propietario.setPrimerNombre(
                        rs.getString("primer_nombre")
                );

                propietario.setSegundoNombre(
                        rs.getString("segundo_nombre")
                );

                propietario.setTercerNombre(
                        rs.getString("tercer_nombre")
                );

                propietario.setPrimerApellido(
                        rs.getString("primer_apellido")
                );

                propietario.setSegundoApellido(
                        rs.getString("segundo_apellido")
                );

                propietario.setTelefono(
                        rs.getString("telefono")
                );

                String nombre
                        = propietario.getPrimerNombre();

                if (propietario.getSegundoNombre() != null) {

                    nombre += " "
                            + propietario.getSegundoNombre();
                }

                if (propietario.getTercerNombre() != null) {

                    nombre += " "
                            + propietario.getTercerNombre();
                }

                String apellido
                        = propietario.getPrimerApellido();

                if (propietario.getSegundoApellido() != null) {

                    apellido += " "
                            + propietario.getSegundoApellido();
                }
                txtNombre.setText(nombre);

                txtApellido.setText(apellido);

                String telefono
                        = propietario.getTelefono();

                if (telefono != null && telefono.length() == 8) {

                    telefono = telefono.substring(0, 4)
                            + "-"
                            + telefono.substring(4);
                }

                txtTelefono.setText(telefono);

                String sqlCuota = """
SELECT TOP 1 cuota
FROM cuota
ORDER BY id_cuota DESC
""";

                PreparedStatement psCuota
                        = conn.prepareStatement(sqlCuota);

                ResultSet rsCuota
                        = psCuota.executeQuery();

                if (rsCuota.next()) {

                    double montoCuota
                            = rsCuota.getDouble("cuota");

                    txtCuota.setText(
                            "Q" + String.format("%.0f", montoCuota)
                    );
                }
                cargarMesesPendientes();
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }
    }

    private int obtenerNumeroMes(
            String mes
    ) {
=======
        if (cbCasa.getValue() == null) {

            return;
        }

        int numeroCasa
                = Integer.parseInt(
                        cbCasa.getValue()
                );

        Propietario propietario
                = registroLogic.obtenerPropietario(
                        numeroCasa
                );

        if (propietario == null) {

            return;
        }

        String nombre
                = propietario.getPrimerNombre();

        if (propietario.getSegundoNombre() != null) {

            nombre += " "
                    + propietario.getSegundoNombre();
        }

        if (propietario.getTercerNombre() != null) {

            nombre += " "
                    + propietario.getTercerNombre();
        }

        String apellido
                = propietario.getPrimerApellido();

        if (propietario.getSegundoApellido() != null) {

            apellido += " "
                    + propietario.getSegundoApellido();
        }

        txtNombre.setText(nombre);

        txtApellido.setText(apellido);

        String telefono
                = propietario.getTelefono();

        if (telefono != null
                && telefono.length() == 8) {

            telefono = telefono.substring(0, 4)
                    + "-"
                    + telefono.substring(4);
        }

        txtTelefono.setText(telefono);

        double cuotaActual
        = registroLogic.obtenerCuotaActual();

txtCuota.setText(
        "Q"
        + String.format(
                "%,.2f",
                cuotaActual
        )
);

        cargarMesesPendientes();
    }

    @FXML
    private void cerrarVentana(ActionEvent event) {

        Stage stage
                = (Stage) ((Node) event.getSource())
                        .getScene()
                        .getWindow();

        stage.close();
    }

    private int obtenerNumeroMes(String mes) {
>>>>>>> origin/main

        for (int i = 0; i < meses.length; i++) {

            if (meses[i].equals(mes)) {

                return i + 1;
            }
        }

        return 0;
    }

    private void mostrarAdvertencia(
            String titulo,
            String encabezado,
            String mensaje
    ) {

        Alert alerta = new Alert(
                Alert.AlertType.WARNING
        );

        alerta.setTitle(titulo);

        alerta.setHeaderText(encabezado);

        alerta.setContentText(
                mensaje
<<<<<<< HEAD
                + "\n"
                + "\n"
=======
                + "\n\n"
>>>>>>> origin/main
                + "La ventana se cerrará automáticamente en 5 segundos."
                + "\n"
                + "También puede presionar OK para continuar."
        );
<<<<<<< HEAD
        alerta.getDialogPane().setPrefWidth(450);

        Timeline timeline
                = new Timeline(
                        new KeyFrame(
                                Duration.seconds(5),
                                event -> alerta.close()
                        )
                );
=======

        alerta.getDialogPane().setPrefWidth(450);

        Timeline timeline = new Timeline(
                new KeyFrame(
                        Duration.seconds(5),
                        event -> alerta.close()
                )
        );
>>>>>>> origin/main

        timeline.setCycleCount(1);

        timeline.play();

        alerta.showAndWait();
    }
<<<<<<< HEAD
}
=======
}
>>>>>>> origin/main
