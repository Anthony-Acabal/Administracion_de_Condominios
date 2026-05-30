package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RecuperacionDAO;
import condominio.proyecto_condominio.model.Usuario;
import condominio.proyecto_condominio.service.CorreoService;

public class RecuperacionLogic {

    private final RecuperacionDAO dao = new RecuperacionDAO();
    private final CorreoService correoService = new CorreoService();

    public String recuperarContrasena(String usuario, String correo) {

        Usuario user = dao.obtenerUsuario(usuario, correo);

        if (user == null) {
            return null;
        }

        try {
            correoService.enviarCredencialesBD(
                    user.getCorreo(),
                    user.getUsuario(),
                    user.getContrasena() // 🔥 LA DE BD DIRECTA
            );
        } catch (Exception e) {
            System.out.println("Error enviando correo");
            e.printStackTrace();
        }

        return user.getContrasena();
    }
}