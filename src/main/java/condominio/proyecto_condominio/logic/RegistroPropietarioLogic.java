package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RegistroPropietarioDAO;
import condominio.proyecto_condominio.model.Propietario;
import java.util.ArrayList;
import java.util.List;

public class RegistroPropietarioLogic {

    private final RegistroPropietarioDAO dao = new RegistroPropietarioDAO();

    public String validarPropietario(Propietario propietario) {
        if (propietario.getPrimerNombre().isEmpty() || propietario.getPrimerApellido().isEmpty()
                || propietario.getTelefono().isEmpty() || propietario.getCorreoElectronico().isEmpty()
                || propietario.getIdCasa() <= 0) {
            return "Complete todos los campos obligatorios.";
        }

        String reglaLetras = "^[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+$";
        boolean nombresValidos = propietario.getPrimerNombre().matches(reglaLetras)
                && propietario.getPrimerApellido().matches(reglaLetras);

        if (propietario.getSegundoNombre() != null && !propietario.getSegundoNombre().isEmpty() && !propietario.getSegundoNombre().matches(reglaLetras)) nombresValidos = false;
        if (propietario.getTercerNombre() != null && !propietario.getTercerNombre().isEmpty() && !propietario.getTercerNombre().matches(reglaLetras)) nombresValidos = false;
        if (propietario.getSegundoApellido() != null && !propietario.getSegundoApellido().isEmpty() && !propietario.getSegundoApellido().matches(reglaLetras)) nombresValidos = false;

        if (!nombresValidos) return "Los nombres y apellidos solo deben contener letras.";

        String formatoCorreo = "^[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}$";
        if (!propietario.getCorreoElectronico().matches(formatoCorreo)) return "Ingrese un correo válido.";

        return dao.verificarDuplicados(
                propietario.getIdPropietario(),
                propietario.getIdCasa(),
                propietario.getTelefono(),
                propietario.getCorreoElectronico()
        );
    }

    public boolean guardarPropietario(Propietario propietario) {
        return dao.guardarPropietario(propietario);
    }

    public List<Propietario> obtenerTodosLosPropietarios() {
        return dao.obtenerTodosLosPropietarios();
    }

    public boolean darBajaLogicaPropietario(int idPropietario) {
        return dao.darBajaLogicaPropietario(idPropietario);
    }

    public String formatearNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) return "";
        texto = texto.trim();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    public List<String> obtenerCasasDisponibles(int idPropietarioExistente, String casaActualAlEditar) {
        List<Integer> casasOcupadas = dao.obtenerCasasOcupadas(idPropietarioExistente);
        List<String> casasDisponibles = new ArrayList<>();

        for (int i = 1; i <= 30; i++) {
            if (!casasOcupadas.contains(i)) {
                casasDisponibles.add(String.valueOf(i)); // Puro número
            }
        }

        if (casaActualAlEditar != null && !casaActualAlEditar.trim().isEmpty()) {
            String numeroCasaActual = casaActualAlEditar.replaceAll("[^0-9]", "");
            if (!casasDisponibles.contains(numeroCasaActual) && !numeroCasaActual.isEmpty()) {
                casasDisponibles.add(numeroCasaActual);
            }
        }
        return casasDisponibles;
    }
}