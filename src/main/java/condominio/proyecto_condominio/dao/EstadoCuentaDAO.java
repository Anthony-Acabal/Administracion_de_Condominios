package condominio.proyecto_condominio.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.time.LocalDate;

import java.time.format.TextStyle;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import condominio.proyecto_condominio.model.Casa;
import condominio.proyecto_condominio.model.Cuota;
import condominio.proyecto_condominio.model.EstadoCuenta;

public class EstadoCuentaDAO {

    private Connection conn;

    public EstadoCuentaDAO(Connection conn) {

        if (conn == null) {

            throw new IllegalArgumentException(
                    "La conexión a la base de datos es nula."
            );
        }

        this.conn = conn;
    }

    public EstadoCuenta obtenerEstadoCuenta(
            int idCasa
    ) {

        if (idCasa <= 0) {

            throw new IllegalArgumentException(
                    "El idCasa debe ser mayor a 0."
            );
        }

        EstadoCuenta estadoCuenta =
                new EstadoCuenta();

        String sql =
                "SELECT "
                + "a.id_pago_cuota, "
                + "b.cuota, "
                + "c.id_casa, "
                + "c.primer_nombre, "
                + "c.telefono, "
                + "c.correo, "
                + "CONVERT(date, fecha_pago) fecha_pago "
                + "FROM pago_cuota a "
                + "INNER JOIN cuota b "
                + "ON a.id_cuota = b.id_cuota "
                + "INNER JOIN propietario c "
                + "ON a.id_propietario = c.id_propietario "
                + "WHERE c.id_casa = ?";

        try (
                PreparedStatement ps =
                conn.prepareStatement(sql)
        ) {

            ps.setInt(1, idCasa);

            System.out.println(
                    "CONSULTANDO ID CASA: "
                    + idCasa
            );

            try (
                    ResultSet rs =
                    ps.executeQuery()
            ) {

                boolean encontrado = false;

                Casa casa = new Casa();

                List<Cuota> cuotasPagadas =
                        new ArrayList<>();

                while (rs.next()) {

                    encontrado = true;

                    int numeroCasa =
                            rs.getInt("id_casa");

                    if (rs.wasNull()) {

                        throw new SQLException(
                                "La columna id_casa vino NULL."
                        );
                    }

                    casa.setNumeroCasa(
                            numeroCasa
                    );

                    casa.setPropietario(
                            rs.getString(
                                    "primer_nombre"
                            )
                    );

                    casa.setTelefono(
                            rs.getString(
                                    "telefono"
                            )
                    );

                    casa.setCorreo(
                            rs.getString(
                                    "correo"
                            )
                    );

                    Cuota cuota =
                            new Cuota();

                    cuota.setIdCuota(
                            rs.getInt(
                                    "id_pago_cuota"
                            )
                    );

                    cuota.setMontoCuota(
                            rs.getInt("cuota")
                    );

                    LocalDate fechaPago =
                            rs.getDate(
                                    "fecha_pago"
                            ).toLocalDate();

                    cuota.setMes(
                            fechaPago.getMonth()
                                    .getDisplayName(
                                            TextStyle.FULL,
                                            new Locale(
                                                    "es",
                                                    "ES"
                                            )
                                    )
                    );

                    cuota.setAnio(
                            fechaPago.getYear()
                    );

                    cuota.setEstado(
                            "PAGADO"
                    );

                    cuotasPagadas.add(
                            cuota
                    );
                }

                estadoCuenta.setCasa(casa);

                estadoCuenta.setMesesPagados(
                        cuotasPagadas
                );

                if (!encontrado) {

                    throw new Exception(
                            "No se encontró información "
                            + "para la casa con ID: "
                            + idCasa
                    );
                }
            }

            System.out.println(
                    "FIN CONSULTA"
            );

        } catch (SQLException e) {

            System.err.println(
                    "ERROR SQL AL OBTENER "
                    + "ESTADO DE CUENTA:"
            );

            System.err.println(
                    "Mensaje: "
                    + e.getMessage()
            );

            e.printStackTrace();

        } catch (Exception e) {

            System.err.println(
                    "ERROR GENERAL: "
                    + e.getMessage()
            );

            e.printStackTrace();
        }

        return estadoCuenta;
    }
}