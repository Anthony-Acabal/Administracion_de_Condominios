package condominio.proyecto_condominio.logic;

public class InicioLogic {
    
    /**
     * Construye el mensaje de bienvenida formateado.
     * Mover la manipulación de cadenas a la lógica cumple con el patrón MVC.
     */
    public String generarMensajeBienvenida(String usuario) {
        if (usuario == null || usuario.trim().isEmpty()) {
            return "BIENVENIDO USUARIO";
        }
        return "BIENVENIDO " + usuario.trim().toUpperCase();
    }
}