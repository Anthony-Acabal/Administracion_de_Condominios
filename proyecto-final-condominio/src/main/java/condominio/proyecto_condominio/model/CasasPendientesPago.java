package condominio.proyecto_condominio.model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class CasasPendientesPago {

    private final SimpleIntegerProperty numeroCasa;

    private final SimpleStringProperty nombrePropietario;

    private final SimpleStringProperty telefono;

    private final SimpleStringProperty fecha;

    public CasasPendientesPago(
            int numeroCasa,
            String nombrePropietario,
            String telefono,
            String fecha
    ) {

        this.numeroCasa
                = new SimpleIntegerProperty(numeroCasa);

        this.nombrePropietario
                = new SimpleStringProperty(nombrePropietario);

        this.telefono
                = new SimpleStringProperty(telefono);

        this.fecha
                = new SimpleStringProperty(fecha);
    }

    public int getNumeroCasa() {

        return numeroCasa.get();
    }

    public SimpleIntegerProperty numeroCasaProperty() {

        return numeroCasa;
    }

    public String getNombrePropietario() {

        return nombrePropietario.get();
    }

    public SimpleStringProperty nombrePropietarioProperty() {

        return nombrePropietario;
    }

    public String getTelefono() {

        return telefono.get();
    }

    public SimpleStringProperty telefonoProperty() {

        return telefono;
    }

    public String getFecha() {

        return fecha.get();
    }

    public SimpleStringProperty fechaProperty() {

        return fecha;
    }
}