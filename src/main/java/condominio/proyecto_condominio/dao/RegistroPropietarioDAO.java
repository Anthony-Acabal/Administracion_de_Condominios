package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Conexion;
import condominio.proyecto_condominio.model.Propietario;
import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RegistroPropietarioDAO {

    /**
     * Método auxiliar interno que garantiza que la conexión use el método 
     * estático real de tu clase Conexion.
     */
    private Connection obtenerConexionSegura() {
        return Conexion.getConexion();
    }

    public String verificarDuplicados(String nroCasa, String telefono, String correo) {
        String sql = """
                     SELECT id_casa, telefono, correo
                     FROM Propietario
                     WHERE (id_casa = ? OR telefono = ? OR correo = ?)
                     AND estado = 'Activo'
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) {
            return "Error: No se pudo establecer comunicación con la base de datos (Conexión nula).";
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, nroCasa);
            ps.setString(2, telefono);
            ps.setString(3, correo);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    if (rs.getString("id_casa") != null && rs.getString("id_casa").equalsIgnoreCase(nroCasa)) {
                        return "El número de casa (" + nroCasa + ") ya se encuentra registrado.";
                    }
                    if (rs.getString("telefono") != null && rs.getString("telefono").equalsIgnoreCase(telefono)) {
                        return "El teléfono (" + telefono + ") ya existe.";
                    }
                    if (rs.getString("correo") != null && rs.getString("correo").equalsIgnoreCase(correo)) {
                        return "El correo (" + correo + ") ya existe.";
                    }
                }
            }
        } catch (Exception e) {
            return "Error al validar duplicados: " + e.getMessage();
        }
        return null;
    }

    /**
     * Guarda o actualiza un propietario dependiendo de si posee o no un ID asignado.
     */
    public boolean guardarPropietario(Propietario propietario) {
        // Si el ID es mayor a 0, significa que el registro ya existe; se ejecuta un UPDATE
        if (propietario.getIdPropietario() > 0) {
            return actualizarPropietario(propietario);
        }
        
        // Si no tiene ID, es un registro completamente nuevo; se ejecuta un INSERT
        String sql = """
                     INSERT INTO Propietario
                     (primer_nombre, segundo_nombre, tercer_nombre, primer_apellido, segundo_apellido,
                      id_casa, telefono, correo, fecha_creacion, id_usuario_creacion, estado)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Activo')
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) {
            System.out.println("Error en insertar: Conexión perdida con la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, propietario.getPrimerNombre());
            ps.setString(2, propietario.getSegundoNombre());
            ps.setString(3, propietario.getTercerNombre());
            ps.setString(4, propietario.getPrimerApellido());
            ps.setString(5, propietario.getSegundoApellido());
            ps.setString(6, propietario.getNumeroCasa()); 
            ps.setString(7, propietario.getTelefono());
            ps.setString(8, propietario.getCorreoElectronico());
            ps.setDate(9, Date.valueOf(LocalDate.now()));
            ps.setInt(10, 1);

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al intentar insertar el propietario: " + e.getMessage());
        }
        return false;
    }

    /**
     * Modifica los datos de un propietario existente en la base de datos.
     */
    private boolean actualizarPropietario(Propietario propietario) {
        String sql = """
                     UPDATE Propietario SET
                     primer_nombre = ?, segundo_nombre = ?, tercer_nombre = ?,
                     primer_apellido = ?, segundo_apellido = ?, id_casa = ?,
                     telefono = ?, correo = ?
                     WHERE id_propietario = ?
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) {
            System.out.println("Error en actualizar: Conexión perdida con la base de datos.");
            return false;
        }

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, propietario.getPrimerNombre());
            ps.setString(2, propietario.getSegundoNombre());
            ps.setString(3, propietario.getTercerNombre());
            ps.setString(4, propietario.getPrimerApellido());
            ps.setString(5, propietario.getSegundoApellido());
            ps.setString(6, propietario.getNumeroCasa());
            ps.setString(7, propietario.getTelefono());
            ps.setString(8, propietario.getCorreoElectronico());
            ps.setInt(9, propietario.getIdPropietario());

            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al intentar actualizar el propietario: " + e.getMessage());
        }
        return false;
    }

    public List<String> obtenerCasasOcupadas(int idPropietarioExistente) {
        List<String> casasOcupadas = new ArrayList<>();
        String sql = "SELECT id_casa FROM Propietario WHERE estado = 'Activo'";
        if (idPropietarioExistente != -1) {
            sql += " AND id_propietario <> ?";
        }

        Connection con = obtenerConexionSegura();
        if (con == null) {
            return casasOcupadas;
        }

        // Se declara el recurso directamente en el try (...) para auto-cierre y eliminar advertencias
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (idPropietarioExistente != -1) {
                ps.setInt(1, idPropietarioExistente);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    casasOcupadas.add(rs.getString("id_casa"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error en DAO al obtener casas: " + e.getMessage());
        }
        return casasOcupadas;
    }

    public boolean darBajaLogicaPropietario(int idPropietario) {
        String sql = "UPDATE Propietario SET estado = 'Eliminado' WHERE id_propietario = ?";
        
        Connection con = obtenerConexionSegura();
        if (con == null) {
            return false;
        }

        // Estructura directa con un solo bloque try para limpiar el código de advertencias
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropietario);
            int afectadas = ps.executeUpdate();
            return afectadas > 0;
        } catch (Exception e) {
            System.out.println("Error en DAO al eliminar propietario: " + e.getMessage());
        }
        return false;
    }
}