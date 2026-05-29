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

    private Connection obtenerConexionSegura() {
        return Conexion.getConexion();
    }

    public String verificarDuplicados(int idPropietario, int idCasa, String telefono, String correo) {
        String sql = """
                     SELECT id_propietario, id_casa, telefono, correo, estado
                     FROM Propietario
                     WHERE (id_casa = ? OR telefono = ? OR correo = ?)
                     """;
        
        if (idPropietario > 0) {
            sql += " AND id_propietario <> ?";
        }

        Connection con = obtenerConexionSegura();
        if (con == null) return "Error: Conexión nula con la base de datos.";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idCasa);
            ps.setString(2, telefono);
            ps.setString(3, correo);
            if (idPropietario > 0) ps.setInt(4, idPropietario);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String estado = rs.getString("estado");
                    int idEncontrado = rs.getInt("id_propietario");

                    if ("Activo".equalsIgnoreCase(estado)) {
                        if (rs.getInt("id_casa") == idCasa) return "La casa número " + idCasa + " ya se encuentra asignada.";
                        if (rs.getString("telefono") != null && rs.getString("telefono").equalsIgnoreCase(telefono)) return "El teléfono ya existe registrado.";
                        if (rs.getString("correo") != null && rs.getString("correo").equalsIgnoreCase(correo)) return "El correo ya existe registrado.";
                    } else if ("Eliminado".equalsIgnoreCase(estado)) {
                        // AQUÍ ESTÁ EL FIX: Verificamos qué fue lo que coincidió
                        boolean coincideTelefono = rs.getString("telefono") != null && rs.getString("telefono").equalsIgnoreCase(telefono);
                        boolean coincideCorreo = rs.getString("correo") != null && rs.getString("correo").equalsIgnoreCase(correo);
                        
                        // Si coincidió el teléfono o correo, pedimos reactivar. 
                        // Si solo coincidió la casa, no hace nada y deja seguir el guardado.
                        if (coincideTelefono || coincideCorreo) {
                            return "REACTIVAR:" + idEncontrado;
                        }
                    }
                }
            }
        } catch (Exception e) {
            return "Error al validar duplicados: " + e.getMessage();
        }
        return null;
    }

    public boolean guardarPropietario(Propietario propietario) {
        if (propietario.getIdPropietario() > 0) return actualizarPropietario(propietario);
        
        String sql = """
                     INSERT INTO Propietario
                     (primer_nombre, segundo_nombre, tercer_nombre, primer_apellido, segundo_apellido,
                      id_casa, telefono, correo, fecha_creacion, id_usuario_creacion, estado)
                     VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'Activo')
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, propietario.getPrimerNombre());
            ps.setString(2, propietario.getSegundoNombre());
            ps.setString(3, propietario.getTercerNombre());
            ps.setString(4, propietario.getPrimerApellido());
            ps.setString(5, propietario.getSegundoApellido());
            ps.setInt(6, propietario.getIdCasa());
            ps.setString(7, propietario.getTelefono());
            ps.setString(8, propietario.getCorreoElectronico());
            ps.setDate(9, Date.valueOf(LocalDate.now()));
            ps.setInt(10, 1);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error Insert: " + e.getMessage());
        }
        return false;
    }

    private boolean actualizarPropietario(Propietario propietario) {
        String sql = """
                     UPDATE Propietario SET
                     primer_nombre = ?, segundo_nombre = ?, tercer_nombre = ?,
                     primer_apellido = ?, segundo_apellido = ?, id_casa = ?,
                     telefono = ?, correo = ?
                     WHERE id_propietario = ?
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, propietario.getPrimerNombre());
            ps.setString(2, propietario.getSegundoNombre());
            ps.setString(3, propietario.getTercerNombre());
            ps.setString(4, propietario.getPrimerApellido());
            ps.setString(5, propietario.getSegundoApellido());
            ps.setInt(6, propietario.getIdCasa());
            ps.setString(7, propietario.getTelefono());
            ps.setString(8, propietario.getCorreoElectronico());
            ps.setInt(9, propietario.getIdPropietario());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error Update: " + e.getMessage());
        }
        return false;
    }

    public List<Propietario> obtenerTodosLosPropietarios() {
        List<Propietario> lista = new ArrayList<>();
        String sql = "SELECT * FROM Propietario WHERE estado = 'Activo' ORDER BY id_propietario DESC";

        Connection con = obtenerConexionSegura();
        if (con == null) return lista;

        try (PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Propietario p = new Propietario();
                p.setIdPropietario(rs.getInt("id_propietario"));
                p.setPrimerNombre(rs.getString("primer_nombre"));
                p.setSegundoNombre(rs.getString("segundo_nombre"));
                p.setTercerNombre(rs.getString("tercer_nombre"));
                p.setPrimerApellido(rs.getString("primer_apellido"));
                p.setSegundoApellido(rs.getString("segundo_apellido"));
                p.setIdCasa(rs.getInt("id_casa")); 
                p.setTelefono(rs.getString("telefono"));
                p.setCorreoElectronico(rs.getString("correo"));
                p.setEstado(rs.getString("estado"));
                lista.add(p);
            }
        } catch (Exception e) {
            System.out.println("Error Select All: " + e.getMessage());
        }
        return lista;
    }

    public List<Integer> obtenerCasasOcupadas(int idPropietarioExistente) {
        List<Integer> casasOcupadas = new ArrayList<>();
        String sql = "SELECT id_casa FROM Propietario WHERE estado = 'Activo'"; 
        if (idPropietarioExistente != -1) sql += " AND id_propietario <> ?";

        Connection con = obtenerConexionSegura();
        if (con == null) return casasOcupadas;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            if (idPropietarioExistente != -1) ps.setInt(1, idPropietarioExistente);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    casasOcupadas.add(rs.getInt("id_casa")); 
                }
            }
        } catch (Exception e) {
            System.out.println("Error Obtener Casas: " + e.getMessage());
        }
        return casasOcupadas;
    }

    public boolean darBajaLogicaPropietario(int idPropietario) {
        String sql = "UPDATE Propietario SET estado = 'Eliminado' WHERE id_propietario = ?";
        Connection con = obtenerConexionSegura();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idPropietario);
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error Baja: " + e.getMessage());
        }
        return false;
    }

    public boolean reactivarPropietario(Propietario propietario) {
        String sql = """
                     UPDATE Propietario SET
                     primer_nombre = ?, segundo_nombre = ?, tercer_nombre = ?,
                     primer_apellido = ?, segundo_apellido = ?, id_casa = ?,
                     telefono = ?, correo = ?, estado = 'Activo'
                     WHERE id_propietario = ?
                     """;

        Connection con = obtenerConexionSegura();
        if (con == null) return false;

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, propietario.getPrimerNombre());
            ps.setString(2, propietario.getSegundoNombre());
            ps.setString(3, propietario.getTercerNombre());
            ps.setString(4, propietario.getPrimerApellido());
            ps.setString(5, propietario.getSegundoApellido());
            ps.setInt(6, propietario.getIdCasa());
            ps.setString(7, propietario.getTelefono());
            ps.setString(8, propietario.getCorreoElectronico());
            ps.setInt(9, propietario.getIdPropietario());
            return ps.executeUpdate() > 0;
        } catch (Exception e) {
            System.out.println("Error al reactivar: " + e.getMessage());
        }
        return false;
    }
}