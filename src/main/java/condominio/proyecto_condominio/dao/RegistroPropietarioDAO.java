package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.Propietario;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;

public class RegistroPropietarioDAO {

    Connection con = Conexion.getInstancia().getConnection();

    public String verificarDuplicados(String nroCasa, String telefono, String correo) {

        
        String sql = """
                     SELECT id_casa, telefono, correo
                     FROM Propietario
                     WHERE id_casa = ?
                     OR telefono = ?
                     OR correo = ?
                     """;

        try (PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setString(1, nroCasa);
            ps.setString(2, telefono);
            ps.setString(3, correo);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    // CORREGIDO: Se lee la columna 'id_casa' en lugar de 'numero_casa'
                    if (rs.getString("id_casa").equalsIgnoreCase(nroCasa)) {
                        return "El número de casa (" + nroCasa + ") ya se encuentra registrado.";
                    }

                    if (rs.getString("telefono").equalsIgnoreCase(telefono)) {
                        return "El teléfono (" + telefono + ") ya existe.";
                    }

                    if (rs.getString("correo").equalsIgnoreCase(correo)) {
                        return "El correo (" + correo + ") ya existe.";
                    }
                }
            }

        } catch (Exception e) {
            return "Error al validar duplicados: " + e.getMessage();
        }

        return null;
    }

    public boolean guardarPropietario(Propietario propietario) {

        
        String sql = """
                     INSERT INTO Propietario
                     (
                     primer_nombre,
                     segundo_nombre,
                     tercer_nombre,
                     primer_apellido,
                     segundo_apellido,
                     id_casa,
                     telefono,
                     correo,
                     fecha_creacion,
                     id_usuario_creacion
                     )
                     VALUES
                     (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                     """;

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
            System.out.println("Error al intentar guardar el propietario: " + e.getMessage());
        }

        return false;
    }

}