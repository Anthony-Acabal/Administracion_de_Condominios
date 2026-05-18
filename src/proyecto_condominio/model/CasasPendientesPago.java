package proyecto_condominio.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CasasPendientesPago {
    private final SimpleIntegerProperty numeroCasa;
    private final SimpleStringProperty nombrePropietario;
    private final SimpleStringProperty telefono;

    public CasasPendientesPago(int numeroCasa, String nombrePropietario, String telefono) {
        this.numeroCasa = new SimpleIntegerProperty(numeroCasa);
        this.nombrePropietario = new SimpleStringProperty(nombrePropietario);
        this.telefono = new SimpleStringProperty(telefono);
    }

    public int getNumeroCasa() { return numeroCasa.get(); }
    public SimpleIntegerProperty numeroCasaProperty() { return numeroCasa; }

    public String getNombrePropietario() { return nombrePropietario.get(); }
    public SimpleStringProperty nombrePropietarioProperty() { return nombrePropietario; }

    public String getTelefono() { return telefono.get(); }
    public SimpleStringProperty telefonoProperty() { return telefono; }
}
