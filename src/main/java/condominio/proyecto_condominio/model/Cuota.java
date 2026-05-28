/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package condominio.proyecto_condominio.model;

public class Cuota {

    private int idCuota;

    private double montoCuota;

    private double recaudacionMensual;

    private String usuarioModificacion;

    public Cuota() {

    }

<<<<<<< HEAD
=======
    public Cuota(
            int idCuota,
            double montoCuota,
            double recaudacionMensual,
            String usuarioModificacion
    ) {

        this.idCuota = idCuota;
        this.montoCuota = montoCuota;
        this.recaudacionMensual = recaudacionMensual;
        this.usuarioModificacion = usuarioModificacion;
    }

>>>>>>> origin/main
    public int getIdCuota() {
        return idCuota;
    }

    public void setIdCuota(int idCuota) {
        this.idCuota = idCuota;
    }

    public double getMontoCuota() {
        return montoCuota;
    }

    public void setMontoCuota(double montoCuota) {
        this.montoCuota = montoCuota;
    }

    public double getRecaudacionMensual() {
        return recaudacionMensual;
    }

    public void setRecaudacionMensual(
            double recaudacionMensual
    ) {
        this.recaudacionMensual
                = recaudacionMensual;
    }

    public String getUsuarioModificacion() {
        return usuarioModificacion;
    }

    public void setUsuarioModificacion(
            String usuarioModificacion
    ) {
        this.usuarioModificacion
                = usuarioModificacion;
    }
<<<<<<< HEAD
=======

    @Override
    public String toString() {

        return "Cuota{"
                + "idCuota=" + idCuota
                + ", montoCuota=" + montoCuota
                + ", recaudacionMensual=" + recaudacionMensual
                + ", usuarioModificacion=" + usuarioModificacion
                + '}';
    }
>>>>>>> origin/main
}
