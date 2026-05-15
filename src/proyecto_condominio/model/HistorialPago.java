/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_condominio.model;

public class HistorialPago {

    private int casa;

    private String propietario;

    private String fecha;

    private double monto;

    private String hora;

    private String comprobante;

    public HistorialPago(
            int casa,
            String propietario,
            String fecha,
            String hora,
            double monto,
            String comprobante
    ) {

        this.casa = casa;

        this.propietario = propietario;

        this.fecha = fecha;

        this.hora = hora;

        this.monto = monto;

        this.comprobante = comprobante;
    }

    public int getCasa() {
        return casa;
    }

    public String getPropietario() {
        return propietario;
    }

    public String getFecha() {
        return fecha;
    }

    public double getMonto() {
        return monto;
    }

    public String getHora() {
        return hora;
    }

    public String getComprobante() {
        return comprobante;
    }
}
