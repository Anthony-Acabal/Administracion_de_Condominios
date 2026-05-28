package condominio.proyecto_condominio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.util.ArrayList;
import java.util.List;

import condominio.proyecto_condominio.model.HistorialPago;

public class HistorialPagoDAO {

    public List<String> obtenerCasas() {

        List<String> casas = new ArrayList<>();

        String sql = """
            SELECT DISTINCT numero_casa
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

    public List<HistorialPago> filtrarPagos(
            String casa,
            String mes,
            String anio
    ) {

        List<HistorialPago> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.numero_casa,

                p.primer_nombre + ' ' +
                p.segundo_nombre + ' ' +
                p.primer_apellido + ' ' +
                p.segundo_apellido
                AS propietario,

                pc.fecha_pago,

                c.cuota,

                pc.imprime_comprobante

            FROM pago_cuota pc

            INNER JOIN propietario p
                ON pc.id_propietario = p.id_propietario

            INNER JOIN cuota c
                ON pc.id_cuota = c.id_cuota

            WHERE 1 = 1
        """;

        if (casa != null) {

            sql += " AND p.numero_casa = ?";
        }

        if (mes != null) {

            sql += " AND MONTH(pc.fecha_pago) = ?";
        }

        if (anio != null) {

            sql += " AND YEAR(pc.fecha_pago) = ?";
        }

        sql += """
            
            ORDER BY pc.fecha_pago DESC
        """;

        try (
                Connection conn = Conexion.getConnection();
                PreparedStatement ps = conn.prepareStatement(sql)
        ) {

            int i = 1;

            if (casa != null) {

                ps.setInt(
                        i++,
                        Integer.parseInt(casa)
                );
            }

            if (mes != null) {

                ps.setInt(
                        i++,
                        Integer.parseInt(mes)
                );
            }

            if (anio != null) {

                ps.setInt(
                        i++,
                        Integer.parseInt(anio)
                );
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    lista.add(
                            new HistorialPago(
                                    rs.getInt("numero_casa"),
                                    rs.getString("propietario"),
                                    rs.getString("fecha_pago"),
                                    rs.getDouble("cuota"),
                                    rs.getString("imprime_comprobante")
                            )
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Error al filtrar pagos:"
            );

            System.out.println(
                    e.getMessage()
            );
        }

        return lista;
    }
}