package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RecuperacionDAO;

public class RecuperacionLogic {

    private final RecuperacionDAO dao
            = new RecuperacionDAO();

    public String generarClaveTemporal(
            String usuario,
            String correo
    ) {

        return dao.generarClaveTemporal(
                usuario,
                correo
        );
    }
}
