/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package condominio.proyecto_condominio.model;

public class HistorialPago {

    private int casa;

    private String propietario;

    private String fecha;

    private double monto;

    private String comprobante;

    private int idPropietario;
    private int idPagoCuota;

    public HistorialPago(
            int casa,
            String propietario,
            String fecha,
            double monto,
            String comprobante,
            int idPropietario,
            int idPagoCuota
    ) {

        this.casa = casa;

        this.propietario = propietario;

        this.fecha = fecha;

        this.monto = monto;

        this.comprobante = comprobante;
        
        this.idPropietario = idPropietario;
        
        this.idPagoCuota = idPagoCuota;
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

    public String getComprobante() {
        return comprobante;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdPagoCuota() {
        return idPagoCuota;
    }

    public void setIdPagoCuota(int idPagoCuota) {
        this.idPagoCuota = idPagoCuota;
    }
}
