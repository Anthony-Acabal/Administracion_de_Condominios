/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package proyecto_condominio.ui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Label;

/**
 * FXML Controller class
 *
 * @author yukiixD
 */
public class InicioController implements Initializable {
    @FXML
    private Label lblBienvenida;
    
    @FXML
    private Label lblFecha;
    
    @FXML
    private javafx.scene.layout.BorderPane vistasContenedor;

    /**
     * Initializes the controller class.
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        mostrarBienvenida("Usuario"); 
        cargarFechaActual();
    }    

    /**
     * [ITM-28] Este método permite recibir dinámicamente el nombre de la persona 
     * que inició sesión desde el LoginController.
     * @param administrador Nombre del usuario logueado
     */
    public void setUsuarioLogueado(String administrador) {
        mostrarBienvenida(administrador);
    }

    private void mostrarBienvenida(String administrador) {
        lblBienvenida.setText("Bienvenido, " + administrador);
    }

    private void cargarFechaActual() {
       lblFecha.setText(fecha.obtenerFechaActualFormateada());
    }



    @FXML
    private void irPropietarios(ActionEvent event) {
        System.out.println("Clic en ir a propietarios");
       }

    @FXML
    private void irPagos(ActionEvent event) {
        System.out.println("Clic en ir a pagos");
  }

    @FXML
    private void irCuota(ActionEvent event) {
        System.out.println("Clic en ir a cuotas");
    }

    @FXML
    private void irGastos(ActionEvent event) {
        System.out.println("Clic en ir a gastos");
    }

    @FXML
    private void irVisitantes(ActionEvent event) {
        System.out.println("Clic en ir a visitantes");
    }

    @FXML
    private void irReportes(ActionEvent event) {
        System.out.println("Clic en ir a reportes");
    }

    @FXML
    private void irEstadosCuenta(ActionEvent event) {
        System.out.println("Clic en ir a estados de cuenta");
    }

    @FXML
    private void irReporteGeneral(ActionEvent event) {
        System.out.println("Clic en ir a reporte general");
    }

    @FXML
    private void irCasasMorosas(ActionEvent event) {
        System.out.println("Clic en ir a casas morosas");
    }

    @FXML
    private void irInicio(ActionEvent event) {
        System.out.println("Clic en ir a inicio");
       vistasContenedor.setCenter(null);
    }

    @FXML
    private void cerrarSesion(ActionEvent event) {
        System.out.println("Clic en cerrar sesión");
    }
    
    @FXML
    private void irConfiguracion(ActionEvent event) {
        System.out.println("Clic en configuración");
    }
    
    /**
     * [ITM-31] Carga un archivo FXML dentro del espacio central del BorderPane.
     */
    private void cargarPagina(String fxmlArchivo) {
        try {
            javafx.scene.Parent pantalla = FXMLLoader.load(getClass().getResource(fxmlArchivo));
            Node pantacity = null;
            
           vistasContenedor.setCenter(pantacity);
            
        } catch (IOException e) {
            System.out.println("❌ Error al cambiar de pantalla: " + e.getMessage());
        }
    }
}