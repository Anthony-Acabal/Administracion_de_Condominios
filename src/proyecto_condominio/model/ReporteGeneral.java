package proyecto_condominio.model;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class ReporteGeneral {
    private final SimpleIntegerProperty numeroCasa;
    private final SimpleStringProperty nombrePropietario;
    private final SimpleStringProperty estadoActual;
    private final SimpleDoubleProperty totalPagado;

    public ReporteGeneral(int numeroCasa, String nombrePropietario, String estadoActual, double totalPagado) {
        this.numeroCasa = new SimpleIntegerProperty(numeroCasa);
        this.nombrePropietario = new SimpleStringProperty(nombrePropietario);
        this.estadoActual = new SimpleStringProperty(estadoActual);
        this.totalPagado = new SimpleDoubleProperty(totalPagado);
    }

    public int getNumeroCasa() { return numeroCasa.get(); }
    public SimpleIntegerProperty numeroCasaProperty() { return numeroCasa; }

    public String getNombrePropietario() { return nombrePropietario.get(); }
    public SimpleStringProperty nombrePropietarioProperty() { return nombrePropietario; }

    public String getEstadoActual() { return estadoActual.get(); }
    public SimpleStringProperty estadoActualProperty() { return estadoActual; }

    public double getTotalPagado() { return totalPagado.get(); }
    public SimpleDoubleProperty totalPagadoProperty() { return totalPagado; }
}
