package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.CasasPendientesPago;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class CasasPendientesPagoDAO {

    public List<CasasPendientesPago> obtenerCasasPendientes(
            int mes,
            int anio
    ) {

        List<CasasPendientesPago> lista
                = new ArrayList<>();

        String sql = """
            SELECT 
                p.numero_casa,
                p.primer_nombre 
                + ' '
                + ISNULL(p.segundo_nombre + ' ', '')
                + p.primer_apellido nombre_propietario,
                p.telefono
            FROM propietario p
            WHERE NOT EXISTS
            (
                SELECT 1
                FROM pago_cuota pc
                WHERE pc.id_propietario = p.id_propietario
                AND MONTH(pc.pago) = ?
                AND YEAR(pc.pago) = ?
            )
            ORDER BY p.numero_casa ASC
        """;

        String fechaFormato
                = String.format(
                        "%02d/%d",
                        mes,
                        anio
                );

        try {

            Connection conn
                    = Conexion.getInstancia()
                            .getConnection();

            PreparedStatement ps
                    = conn.prepareStatement(sql);

            ps.setInt(1, mes);

            ps.setInt(2, anio);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                lista.add(
                        new CasasPendientesPago(
                                rs.getInt("numero_casa"),
                                rs.getString("nombre_propietario"),
                                rs.getString("telefono"),
                                fechaFormato
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return lista;
    }

    public List<CasasPendientesPago> obtenerCasasPendientesAnual(
            int anio,
            int mesInicio,
            int mesFin
    ) {

        List<CasasPendientesPago> lista
                = new ArrayList<>();

        String sql = """
            WITH Meses AS
            (
                SELECT 1 mes
                UNION ALL SELECT 2
                UNION ALL SELECT 3
                UNION ALL SELECT 4
                UNION ALL SELECT 5
                UNION ALL SELECT 6
                UNION ALL SELECT 7
                UNION ALL SELECT 8
                UNION ALL SELECT 9
                UNION ALL SELECT 10
                UNION ALL SELECT 11
                UNION ALL SELECT 12
            )

            SELECT
                p.numero_casa,

                p.primer_nombre
                + ' '
                + ISNULL(p.segundo_nombre + ' ', '')
                + p.primer_apellido nombre_propietario,

                p.telefono,

                m.mes

            FROM propietario p

            CROSS JOIN Meses m

            WHERE m.mes >= ?
            AND m.mes <= ?

            AND NOT EXISTS
            (
                SELECT 1
                FROM pago_cuota pc
                WHERE pc.id_propietario = p.id_propietario
                AND MONTH(pc.pago) = m.mes
                AND YEAR(pc.pago) = ?
            )

            ORDER BY
                m.mes ASC,
                p.numero_casa ASC
        """;

        try {

            Connection conn
                    = Conexion.getInstancia()
                            .getConnection();

            PreparedStatement ps
                    = conn.prepareStatement(sql);

            ps.setInt(1, mesInicio);

            ps.setInt(2, mesFin);

            ps.setInt(3, anio);

            ResultSet rs
                    = ps.executeQuery();

            while (rs.next()) {

                int mes = rs.getInt("mes");

                lista.add(
                        new CasasPendientesPago(
                                rs.getInt("numero_casa"),
                                rs.getString("nombre_propietario"),
                                rs.getString("telefono"),
                                String.format(
                                        "%02d/%d",
                                        mes,
                                        anio
                                )
                        )
                );
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return lista;
    }

    public int[] obtenerLimitesFechas() {

        int[] limites = {
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue(),
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue()
        };

        String sqlMin = """
            SELECT TOP 1
                YEAR(pago) anio,
                MONTH(pago) mes
            FROM pago_cuota
            ORDER BY pago ASC
        """;

        String sqlMax = """
            SELECT TOP 1
                YEAR(pago) anio,
                MONTH(pago) mes
            FROM pago_cuota
            ORDER BY pago DESC
        """;

        try {

            Connection conn
                    = Conexion.getInstancia()
                            .getConnection();

            PreparedStatement psMin
                    = conn.prepareStatement(sqlMin);

            ResultSet rsMin
                    = psMin.executeQuery();

            if (rsMin.next()) {

                limites[0]
                        = rsMin.getInt("anio");

                limites[1]
                        = rsMin.getInt("mes");
            }

            PreparedStatement psMax
                    = conn.prepareStatement(sqlMax);

            ResultSet rsMax
                    = psMax.executeQuery();

            if (rsMax.next()) {

                limites[2]
                        = rsMax.getInt("anio");

                limites[3]
                        = rsMax.getInt("mes");
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return limites;
    }
}