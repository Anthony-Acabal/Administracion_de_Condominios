package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.LoginDAO;
import condominio.proyecto_condominio.model.Usuario;

public class LoginLogic {

    private final LoginDAO dao = new LoginDAO();

    public Usuario validarLogin(
            String correo,
            String contrasena
    ) {

        return dao.validarLogin(
                correo,
                contrasena
        );
    }
}