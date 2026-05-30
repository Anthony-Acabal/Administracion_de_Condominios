package condominio.proyecto_condominio.logic;

import java.util.ArrayList;
import java.util.List;

import condominio.proyecto_condominio.dao.EstadoCuentaDAO;

import condominio.proyecto_condominio.model.Cuota;
import condominio.proyecto_condominio.model.EstadoCuenta;

public class EstadoCuentaLogic {

    private EstadoCuentaDAO dao;

    public EstadoCuentaLogic(
            EstadoCuentaDAO dao
    ) {

        this.dao = dao;
    }

    public EstadoCuenta obtenerEstadoCuenta(
            int numeroCasa
    ) {

        return dao.obtenerEstadoCuenta(
                numeroCasa
        );
    }

    public List<Cuota> obtenerCuotas(
            int numeroCasa
    ) {

        EstadoCuenta estado =
                dao.obtenerEstadoCuenta(
                        numeroCasa
                );

        List<Cuota> lista =
                new ArrayList<>();

        lista.addAll(
                estado.getMesesPagados()
        );

        lista.addAll(
                estado.getMesesPendientes()
        );

        return lista;
    }
}