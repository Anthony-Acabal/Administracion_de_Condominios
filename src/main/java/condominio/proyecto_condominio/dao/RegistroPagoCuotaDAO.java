package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.PagoCuota;
import condominio.proyecto_condominio.model.Propietario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

public class RegistroPagoCuotaDAO {

    public double obtenerUltimaCuota() {
        String sql = """
            SELECT cuota
            FROM cuota
            WHERE id_estado = 2
            ORDER BY id_cuota DESC
        """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            if (rs.next()) {
                return rs.getInt("cuota");
            }
        } catch (Exception e) {
            System.out.println("Error al obtener cuota:");
            System.out.println(e.getMessage());
        }
        return 0;
    }

    public ArrayList<String> obtenerCasas() {
        ArrayList<String> casas = new ArrayList<>();
        String sql = """
            SELECT id_casa
            FROM propietario
            ORDER BY id_casa ASC
        """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql); ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                casas.add(String.valueOf(rs.getInt("id_casa")));
            }
        } catch (Exception e) {
            System.out.println("Error al obtener casas:");
            System.out.println(e.getMessage());
        }
        return casas;
    }

    public Propietario obtenerPropietario(int numeroCasa) {
        String sql = """
            SELECT 
                id_propietario,
                primer_nombre,
                segundo_nombre,
                tercer_nombre,
                primer_apellido,
                segundo_apellido,
                telefono,
                correo
            FROM propietario
            WHERE id_casa = ?
        """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Propietario propietario = new Propietario();
                    propietario.setIdPropietario(rs.getInt("id_propietario"));
                    propietario.setPrimerNombre(rs.getString("primer_nombre"));
                    propietario.setSegundoNombre(rs.getString("segundo_nombre"));
                    propietario.setTercerNombre(rs.getString("tercer_nombre"));
                    propietario.setPrimerApellido(rs.getString("primer_apellido"));
                    propietario.setSegundoApellido(rs.getString("segundo_apellido"));
                    propietario.setTelefono(rs.getString("telefono"));
                    propietario.setCorreoElectronico(rs.getString("correo"));
                    return propietario;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener propietario:");
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean existePago(int numeroCasa, int mes, int anio) {
        String sql = """
            SELECT COUNT(*)
            FROM pago_cuota pc
            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario
            WHERE p.id_casa = ?
            AND MONTH(pc.fecha_pago) = ?
            AND YEAR(pc.fecha_pago) = ?
        """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            ps.setInt(2, mes);
            ps.setInt(3, anio);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        } catch (Exception e) {
            System.out.println("Error al validar pago:");
            System.out.println(e.getMessage());
        }
        return false;
    }

    public int registrarPago(PagoCuota pago) {

        String sql = """
        INSERT INTO pago_cuota
        (
            id_propietario,
            id_cuota,
            fecha_pago,
            pago,
            imprime_comprobante,
            id_usuario_creacion
        )
        VALUES (?, ?, GETDATE(), ?, ?, ?);
    """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            ps.setInt(1, pago.getIdPropietario());
            ps.setInt(2, pago.getIdCuota());
            ps.setDate(3,java.sql.Date.valueOf(pago.getFechaPago()));
            ps.setString(4, pago.getImprimeComprobante());
            ps.setInt(5, pago.getIdUsuarioCreacion());

            int affectedRows = ps.executeUpdate();

            if (affectedRows == 0) {
                return -1;
            }

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1); // 👈 ID REAL
                }
            }

        } catch (Exception e) {
            System.out.println("Error al registrar pago:");
            System.out.println(e.getMessage());
        }

        return -1;
    }

    public ArrayList<Integer> obtenerMesesPagados(int numeroCasa, int anio) {
        ArrayList<Integer> meses = new ArrayList<>();
        String sql = """
            SELECT MONTH(fecha_pago) mes
            FROM pago_cuota pc
            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario
            WHERE p.id_casa = ?
            AND YEAR(fecha_pago) = ?
        """;

        try (
                Connection conn = Conexion.getInstancia().getConnection(); PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, numeroCasa);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    meses.add(rs.getInt("mes"));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al obtener meses pagados:");
            System.out.println(e.getMessage());
        }
        return meses;
    }
}
