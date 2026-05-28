package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.CasasPendientesPagoDAO;
import condominio.proyecto_condominio.model.CasasPendientesPago;
import java.time.LocalDate;
import java.util.List;

public class CasaPendientePagoLogic {

    private CasasPendientesPagoDAO dao
            = new CasasPendientesPagoDAO();

    private final String[] nombresMeses = {
        "Enero",
        "Febrero",
        "Marzo",
        "Abril",
        "Mayo",
        "Junio",
        "Julio",
        "Agosto",
        "Septiembre",
        "Octubre",
        "Noviembre",
        "Diciembre"
    };

    public int[] obtenerLimitesFechas() {

        return dao.obtenerLimitesFechas();
    }

    public List<CasasPendientesPago> obtenerDatos(
            String mesSeleccionado,
            int anio,
            int[] limites
    ) {

        if (mesSeleccionado.equals("Ver todo")) {

            int mesInicio = 1;

            int mesFin = 12;

            if (anio == limites[0]) {

                mesInicio = limites[1];
            }

            if (anio == LocalDate.now().getYear()) {

                mesFin = LocalDate.now().getMonthValue();
            }

            return dao.obtenerCasasPendientesAnual(
                    anio,
                    mesInicio,
                    mesFin
            );
        }

        int mes = obtenerNumeroMes(
                mesSeleccionado
        );

        return dao.obtenerCasasPendientes(
                mes,
                anio
        );
    }

    public int obtenerNumeroMes(
            String nombreMes
    ) {

        for (int i = 0; i < nombresMeses.length; i++) {

            if (nombresMeses[i].equals(nombreMes)) {

                return i + 1;
            }
        }

        return 1;
    }

    public String[] getNombresMeses() {

        return nombresMeses;
    }
}