package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RegistroPropietarioDAO;
import condominio.proyecto_condominio.model.Propietario;

public class RegistroPropietarioLogic {

    private final RegistroPropietarioDAO dao = new RegistroPropietarioDAO();

    public String validarPropietario(Propietario propietario) {

        if (propietario.getPrimerNombre().isEmpty()
                || propietario.getPrimerApellido().isEmpty()
                || propietario.getTelefono().isEmpty()
                || propietario.getCorreoElectronico().isEmpty()) {

            return "Complete todos los campos obligatorios.";
        }

        String reglaLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";

        boolean nombresValidos = propietario.getPrimerNombre().matches(reglaLetras)
                && propietario.getPrimerApellido().matches(reglaLetras);

        if (propietario.getSegundoNombre() != null
                && !propietario.getSegundoNombre().isEmpty()
                && !propietario.getSegundoNombre().matches(reglaLetras)) {

            nombresValidos = false;
        }

        if (propietario.getTercerNombre() != null
                && !propietario.getTercerNombre().isEmpty()
                && !propietario.getTercerNombre().matches(reglaLetras)) {

            nombresValidos = false;
        }

        if (propietario.getSegundoApellido() != null
                && !propietario.getSegundoApellido().isEmpty()
                && !propietario.getSegundoApellido().matches(reglaLetras)) {

            nombresValidos = false;
        }

        if (!nombresValidos) {
            return "Los nombres y apellidos solo deben contener letras.";
        }

        String formatoCorreo = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";

        if (!propietario.getCorreoElectronico().matches(formatoCorreo)) {
            return "Ingrese un correo válido.";
        }

        return dao.verificarDuplicados(
                String.valueOf(propietario.getNumeroCasa()),
                propietario.getTelefono(),
                propietario.getCorreoElectronico()
        );
    }

    public boolean guardarPropietario(Propietario propietario) {
        return dao.guardarPropietario(propietario);
    }
}