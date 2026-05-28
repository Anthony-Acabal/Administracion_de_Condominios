package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RegistroPropietarioDAO;
import condominio.proyecto_condominio.model.Propietario;
import java.util.ArrayList;
import java.util.List;

public class RegistroPropietarioLogic {

    private final RegistroPropietarioDAO dao = new RegistroPropietarioDAO();

    /**
     * Valida las reglas de negocio del propietario (campos obligatorios, formatos de texto y correo).
     */
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
                propietario.getNumeroCasa(),
                propietario.getTelefono(),
                propietario.getCorreoElectronico()
        );
    }

    /**
     * Envía el modelo al DAO para su inserción o actualización.
     */
    public boolean guardarPropietario(Propietario propietario) {
        return dao.guardarPropietario(propietario);
    }

    /**
     * Sirve de puente con el DAO para ejecutar la baja lógica de un propietario.
     */
    public boolean darBajaLogicaPropietario(int idPropietario) {
        return dao.darBajaLogicaPropietario(idPropietario);
    }

    /**
     * Procesa un string para aplicar formato de nombre propio (Ej: "bRyAn" -> "Bryan").
     */
    public String formatearNombre(String texto) {
        if (texto == null || texto.trim().isEmpty()) {
            return "";
        }
        texto = texto.trim();
        return texto.substring(0, 1).toUpperCase() + texto.substring(1).toLowerCase();
    }

    /**
     * Regla de negocio que calcula dinámicamente las casas libres dentro del rango de 30 viviendas.
     */
    public List<String> obtenerCasasDisponibles(int idPropietarioExistente, String casaActualAlEditar) {
        List<String> casasOcupadas = dao.obtenerCasasOcupadas(idPropietarioExistente);
        List<String> casasDisponibles = new ArrayList<>();

        // Lógica algorítmica: El condominio tiene un límite estricto de 30 casas
        for (int i = 1; i <= 30; i++) {
            String casaStr = String.valueOf(i);
            if (!casasOcupadas.contains(casaStr)) {
                casasDisponibles.add(casaStr);
            }
        }

        // Si se está editando, volvemos a inyectar la casa actual a las opciones visuales
        if (casaActualAlEditar != null && !casasDisponibles.contains(casaActualAlEditar)) {
            casasDisponibles.add(casaActualAlEditar);
        }

        return casasDisponibles;
    }
}