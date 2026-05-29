/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package condominio.proyecto_condominio.model;

import java.time.LocalDate;

public class Cuota {

    private int idCuota;

    private String mes;

    private int anio;

    private int montoCuota;

    private String estado;

    private LocalDate fechaPago;

    private double recaudacionMensual;

    private String usuarioModificacion;

    public Cuota() {

    }

    public Cuota(
            int idCuota,
            String mes,
            int anio,
            int montoCuota,
            String estado,
            LocalDate fechaPago,
            double recaudacionMensual,
            String usuarioModificacion
    ) {

        this.idCuota = idCuota;
        this.mes = mes;
        this.anio = anio;
        this.montoCuota = montoCuota;
        this.estado = estado;
        this.fechaPago = fechaPago;
        this.recaudacionMensual = recaudacionMensual;
        this.usuarioModificacion = usuarioModificacion;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public int getAnio() {
        return anio;
    }

    public void setAnio(int anio) {
        this.anio = anio;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(int montoCuota) {
        this.montoCuota = montoCuota;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public double getRecaudacionMensual() {
        return recaudacionMensual;
    }

    public void setRecaudacionMensual(
            double recaudacionMensual
    ) {
        this.recaudacionMensual = recaudacionMensual;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(
            String usuarioModificacion
    ) {
        this.usuarioModificacion = usuarioModificacion;
    }

    @Override
    public String toString() {

        return "Cuota{"
                + "idCuota=" + idCuota
                + ", mes=" + mes
                + ", anio=" + anio
                + ", montoCuota=" + montoCuota
                + ", estado=" + estado
                + ", fechaPago=" + fechaPago
                + ", recaudacionMensual=" + recaudacionMensual
                + ", usuarioModificacion=" + usuarioModificacion
                + '}';
    }
}
