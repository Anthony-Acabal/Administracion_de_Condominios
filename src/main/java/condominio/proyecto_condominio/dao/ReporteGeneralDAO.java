package condominio.proyecto_condominio.dao;

import condominio.proyecto_condominio.model.ReporteGeneral;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class ReporteGeneralDAO {

    public List<ReporteGeneral> obtenerReporteGeneral(
            int mes,
            int anio
    ) {

        List<ReporteGeneral> lista = new ArrayList<>();

        String sql = """
            SELECT
                p.numero_casa,
                p.primer_nombre + ' ' + p.primer_apellido AS nombre_propietario,

                CASE
                    WHEN COUNT(
                        CASE
                            WHEN MONTH(pc.pago) = ?
                            AND YEAR(pc.pago) = ?
                            THEN 1
                        END
                    ) > 0
                    THEN 'Pagado'
                    ELSE 'Pendiente'
                END AS estado_actual,

                COALESCE(
                    SUM(
                        CASE
                            WHEN YEAR(pc.pago) = ?
                            THEN c.cuota
                            ELSE 0
                        END
                    ),
                    0
                ) AS total_pagado

            FROM propietario p

            LEFT JOIN pago_cuota pc
                ON p.id_propietario = pc.id_propietario

            LEFT JOIN cuota c
                ON pc.id_cuota = c.id_cuota

            GROUP BY
                p.numero_casa,
                p.primer_nombre,
                p.primer_apellido

            ORDER BY p.numero_casa ASC
        """;

        try (
                Connection conn = Conexion
                        .getInstancia()
                        .getConnection();

                PreparedStatement ps
                = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, mes);
            ps.setInt(2, anio);
            ps.setInt(3, anio);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {

                ReporteGeneral reporte
                        = new ReporteGeneral();

                reporte.setNumeroCasa(
                        rs.getInt("numero_casa")
                );

                reporte.setNombrePropietario(
                        rs.getString("nombre_propietario")
                );

                reporte.setEstadoActual(
                        rs.getString("estado_actual")
                );

                reporte.setTotalPagado(
                        rs.getDouble("total_pagado")
                );

                lista.add(reporte);
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return lista;
    }

    public double[] obtenerResumenMensual(
            int mes,
            int anio
    ) {

        double[] resumen = new double[2];

        String sql = """
            SELECT

            (
                SELECT ISNULL(SUM(c.cuota), 0)
                FROM pago_cuota pc
                INNER JOIN cuota c
                    ON pc.id_cuota = c.id_cuota
                WHERE MONTH(pc.pago) = ?
                AND YEAR(pc.pago) = ?
            ) AS recaudado,

            (
                SELECT COUNT(*)
                FROM propietario
            ) AS total_casas,

            (
                SELECT TOP 1 cuota
                FROM cuota
                ORDER BY id_cuota DESC
            ) AS ultima_cuota
        """;

        try (
                Connection conn = Conexion
                        .getInstancia()
                        .getConnection();

                PreparedStatement ps
                = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, mes);
            ps.setInt(2, anio);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                resumen[0]
                        = rs.getDouble("recaudado");

                int totalCasas
                        = rs.getInt("total_casas");

                double ultimaCuota
                        = rs.getDouble("ultima_cuota");

                resumen[1]
                        = totalCasas * ultimaCuota;
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return resumen;
    }

    public double[] obtenerResumenAnual(
            int mesHasta,
            int anio
    ) {

        double[] resumen = new double[2];

        String sql = """
            SELECT

            (
                SELECT ISNULL(SUM(c.cuota), 0)
                FROM pago_cuota pc
                INNER JOIN cuota c
                    ON pc.id_cuota = c.id_cuota
                WHERE YEAR(pc.pago) = ?
                AND MONTH(pc.pago) <= ?
            ) AS recaudado_anual,

            (
                SELECT COUNT(*)
                FROM propietario
            ) AS total_casas,

            (
                SELECT TOP 1 cuota
                FROM cuota
                ORDER BY id_cuota DESC
            ) AS ultima_cuota
        """;

        try (
                Connection conn = Conexion
                        .getInstancia()
                        .getConnection();

                PreparedStatement ps
                = conn.prepareStatement(sql)
        ) {

            ps.setInt(1, anio);
            ps.setInt(2, mesHasta);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {

                resumen[0]
                        = rs.getDouble("recaudado_anual");

                int totalCasas
                        = rs.getInt("total_casas");

                double ultimaCuota
                        = rs.getDouble("ultima_cuota");

                resumen[1]
                        = totalCasas
                        * ultimaCuota
                        * 12;
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
        }

        return resumen;
    }

    public int[] obtenerLimitesFechas() {

        int[] limites = new int[]{
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue(),
            LocalDate.now().getYear(),
            LocalDate.now().getMonthValue()
        };

        String sqlMin = """
            SELECT TOP 1
                YEAR(pago) AS anio,
                MONTH(pago) AS mes
            FROM pago_cuota
            ORDER BY pago ASC
        """;

        String sqlMax = """
            SELECT TOP 1
                YEAR(pago) AS anio,
                MONTH(pago) AS mes
            FROM pago_cuota
            ORDER BY pago DESC
        """;

        try (
                Connection conn = Conexion
                        .getInstancia()
                        .getConnection()
        ) {

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