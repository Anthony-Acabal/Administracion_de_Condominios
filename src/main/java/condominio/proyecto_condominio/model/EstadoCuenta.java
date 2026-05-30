package condominio.proyecto_condominio.model;

import java.util.ArrayList;
import java.util.List;

public class EstadoCuenta {

    private Casa casa;

    private List<Cuota> mesesPagados
            = new ArrayList<>();

    private List<Cuota> mesesPendientes
            = new ArrayList<>();

    private double totalPagado;

    public EstadoCuenta() {

    }

    public Casa getCasa() {
        return casa;
    }

    public void setCasa(Casa casa) {
        this.casa = casa;
    }

    public List<Cuota> getMesesPagados() {
        return mesesPagados;
    }

    public void setMesesPagados(
            List<Cuota> mesesPagados
    ) {
        this.mesesPagados = mesesPagados;
    }

    public List<Cuota> getMesesPendientes() {
        return mesesPendientes;
    }

    public void setMesesPendientes(
            List<Cuota> mesesPendientes
    ) {
        this.mesesPendientes = mesesPendientes;
    }

    public double getTotalPagado() {
        return totalPagado;
    }

    public void setTotalPagado(
            double totalPagado
    ) {
        this.totalPagado = totalPagado;
    }
}
