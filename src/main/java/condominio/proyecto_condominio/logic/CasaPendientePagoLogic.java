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

        int anioActual = LocalDate.now().getYear();
        int mesActual = LocalDate.now().getMonthValue();

        if (mesSeleccionado.equals("Ver todo")) {

            int mesInicio = 1;
            int mesFin;

            if (anio == limites[0]) {
                mesInicio = limites[1];
            }

            if (anio == anioActual) {
                mesFin = mesActual;
            } else {
                mesFin = 12;
            }

            if (mesInicio > mesFin) {
                return List.of();
            }

            return dao.obtenerCasasPendientesAnual(anio, mesInicio, mesFin);
        }

        int mes = obtenerNumeroMes(mesSeleccionado);

        return dao.obtenerCasasPendientes(mes, anio);
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

    public int convertirMesAInt(String mes) {

        return switch (mes) {
            case "Enero" ->
                1;
            case "Febrero" ->
                2;
            case "Marzo" ->
                3;
            case "Abril" ->
                4;
            case "Mayo" ->
                5;
            case "Junio" ->
                6;
            case "Julio" ->
                7;
            case "Agosto" ->
                8;
            case "Septiembre" ->
                9;
            case "Octubre" ->
                10;
            case "Noviembre" ->
                11;
            case "Diciembre" ->
                12;
            default ->
                throw new IllegalArgumentException("Mes inválido: " + mes);
        };
    }
}
