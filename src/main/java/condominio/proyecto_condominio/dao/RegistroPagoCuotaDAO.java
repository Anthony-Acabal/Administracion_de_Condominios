package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.PagoCuota;
import condominio.proyecto_condominio.model.Propietario;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;

public class RegistroPagoCuotaDAO {

    public double obtenerUltimaCuota() {

        String sql = """
            SELECT TOP 1 cuota
            FROM cuota
            ORDER BY id_cuota DESC
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            if (rs.next()) {

                return rs.getDouble("cuota");
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al obtener cuota:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return 0;
    }

    public ArrayList<String> obtenerCasas() {

        ArrayList<String> casas = new ArrayList<>();

        String sql = """
            SELECT numero_casa
            FROM propietario
            ORDER BY numero_casa ASC
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql);
                ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                casas.add(
                        String.valueOf(
                                rs.getInt("numero_casa")
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al obtener casas:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return casas;
    }

    public Propietario obtenerPropietario(
            int numeroCasa
    ) {

        String sql = """
            SELECT 
                id_propietario,
                primer_nombre,
                segundo_nombre,
                tercer_nombre,
                primer_apellido,
                segundo_apellido,
                telefono
            FROM propietario
            WHERE numero_casa = ?
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, numeroCasa);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    Propietario propietario
                            = new Propietario();

                    propietario.setIdPropietario(
                            rs.getInt("id_propietario")
                    );

                    propietario.setPrimerNombre(
                            rs.getString("primer_nombre")
                    );

                    propietario.setSegundoNombre(
                            rs.getString("segundo_nombre")
                    );

                    propietario.setTercerNombre(
                            rs.getString("tercer_nombre")
                    );

                    propietario.setPrimerApellido(
                            rs.getString("primer_apellido")
                    );

                    propietario.setSegundoApellido(
                            rs.getString("segundo_apellido")
                    );

                    propietario.setTelefono(
                            rs.getString("telefono")
                    );

                    return propietario;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al obtener propietario:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return null;
    }

    public boolean existePago(
            int numeroCasa,
            int mes,
            int anio
    ) {

        String sql = """
            SELECT COUNT(*)
            FROM pago_cuota pc

            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario

            WHERE p.numero_casa = ?
            AND MONTH(pc.fecha_pago) = ?
            AND YEAR(pc.fecha_pago) = ?
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, numeroCasa);
            ps.setInt(2, mes);
            ps.setInt(3, anio);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return rs.getInt(1) > 0;
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al validar pago:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return false;
    }

    public boolean registrarPago(
            PagoCuota pago
    ) {

        String sql = """
            INSERT INTO pago_cuota
            (
                id_propietario,
                id_cuota,
                fecha_pago,
                imprime_comprobante,
                id_usuario_creacion
            )
            VALUES (?, ?, ?, ?, ?)
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(
                    1,
                    pago.getIdPropietario()
            );

            ps.setInt(
                    2,
                    pago.getIdCuota()
            );

            ps.setDate(
                    3,
                    java.sql.Date.valueOf(
                            pago.getFechaPago()
                    )
            );

            ps.setString(
                    4,
                    pago.getImprimeComprobante()
            );

            ps.setInt(
                    5,
                    pago.getIdUsuarioCreacion()
            );

            return ps.executeUpdate() > 0;

        } catch (Exception e) {

            System.out.println(
                    "Error al registrar pago:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return false;
    }

    public ArrayList<Integer> obtenerMesesPagados(
            int numeroCasa,
            int anio
    ) {

        ArrayList<Integer> meses = new ArrayList<>();

        String sql = """
            SELECT MONTH(fecha_pago) mes

            FROM pago_cuota pc

            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario

            WHERE p.numero_casa = ?
            AND YEAR(fecha_pago) = ?
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, numeroCasa);
            ps.setInt(2, anio);

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    meses.add(
                            rs.getInt("mes")
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al obtener meses pagados:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return meses;
    }
}