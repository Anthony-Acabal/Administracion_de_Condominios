package condominio.proyecto_condominio.model;

public class ReporteGeneral {

    private int numeroCasa;
    private String nombrePropietario;
    private String estadoActual;
    private double totalPagado;

    public ReporteGeneral() {
    }

    public ReporteGeneral(
            int numeroCasa,
            String nombrePropietario,
            String estadoActual,
            double totalPagado
    ) {

        this.numeroCasa = numeroCasa;
        this.nombrePropietario = nombrePropietario;
        this.estadoActual = estadoActual;
        this.totalPagado = totalPagado;
    }

    public int getNumeroCasa() {
        return numeroCasa;
    }

    public void setNumeroCasa(int numeroCasa) {
        this.numeroCasa = numeroCasa;
    }

    public String getNombrePropietario() {
        return nombrePropietario;
    }

    public void setNombrePropietario(String nombrePropietario) {
        this.nombrePropietario = nombrePropietario;
    }

    public String getEstadoActual() {
        return estadoActual;
    }

    public void setEstadoActual(String estadoActual) {
        this.estadoActual = estadoActual;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(double totalPagado) {
        this.totalPagado = totalPagado;
    }
}