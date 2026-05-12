/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_condominio.model;

import java.time.LocalDate;

public class PagoCuota {

    private int idPagoCuota;

    private int idPropietario;

    private int idCuota;

    private LocalDate fechaPago;

    private String imprimeComprobante;

    private int idUsuarioCreacion;

    public PagoCuota() {

    }

    public int getIdPagoCuota() {
        return idPagoCuota;
    }

    public void setIdPagoCuota(int idPagoCuota) {
        this.idPagoCuota = idPagoCuota;
    }

    public int getIdPropietario() {
        return idPropietario;
    }

    public void setIdPropietario(int idPropietario) {
        this.idPropietario = idPropietario;
    }

    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public LocalDate getFechaPago() {
        return fechaPago;
    }

    public void setFechaPago(LocalDate fechaPago) {
        this.fechaPago = fechaPago;
    }

    public String getImprimeComprobante() {
        return imprimeComprobante;
    }

    public void setImprimeComprobante(String imprimeComprobante) {
        this.imprimeComprobante = imprimeComprobante;
    }

    public int getIdUsuarioCreacion() {
        return idUsuarioCreacion;
    }

    public void setIdUsuarioCreacion(int idUsuarioCreacion) {
        this.idUsuarioCreacion = idUsuarioCreacion;
    }
}
