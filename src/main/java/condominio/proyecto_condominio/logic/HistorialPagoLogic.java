package condominio.proyecto_condominio.logic;

<<<<<<< HEAD
public class HistorialPagoLogic {

    public boolean validarMesAnio(String mes, String anio) {

        if (mes != null && !mes.equals("Todos los meses")
                && anio.equals("Todos los años")) {
=======
import condominio.proyecto_condominio.dao.HistorialPagoDAO;
import condominio.proyecto_condominio.model.HistorialPago;

import java.util.List;

public class HistorialPagoLogic {

    private HistorialPagoDAO historialDAO
            = new HistorialPagoDAO();

    public List<String> obtenerCasas() {

        return historialDAO.obtenerCasas();
    }

    public boolean validarMesAnio(
            String mes,
            String anio
    ) {

        if (mes != null
                && !mes.equals("Todos los meses")
                && anio.equals("Todos los años")) {

>>>>>>> origin/main
            return false;
        }

        return true;
    }

<<<<<<< HEAD
    public Integer parseCasa(String casa) {

        if (casa == null || casa.equals("Todas las casas")) {
=======
    public Integer parseCasa(
            String casa
    ) {

        if (casa == null
                || casa.equals("Todas las casas")) {

>>>>>>> origin/main
            return null;
        }

        return Integer.parseInt(casa);
    }

<<<<<<< HEAD
    public Integer parseMes(String mesIndex) {

        if (mesIndex == null) return null;
        return Integer.parseInt(mesIndex);
    }

    public Integer parseAnio(String anio) {

        if (anio == null || anio.equals("Todos los años")) {
=======
    public Integer obtenerNumeroMes(
            String mes
    ) {

        if (mes == null
                || mes.equals("Todos los meses")) {

            return null;
        }

        String[] meses = {
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

        for (int i = 0; i < meses.length; i++) {

            if (meses[i].equals(mes)) {

                return i + 1;
            }
        }

        return null;
    }

    public Integer parseAnio(
            String anio
    ) {

        if (anio == null
                || anio.equals("Todos los años")) {

>>>>>>> origin/main
            return null;
        }

        return Integer.parseInt(anio);
    }
<<<<<<< HEAD
=======

    public List<HistorialPago> filtrarPagos(
            String casa,
            String mes,
            String anio
    ) {

        Integer numeroCasa
                = parseCasa(casa);

        Integer numeroMes
                = obtenerNumeroMes(mes);

        Integer numeroAnio
                = parseAnio(anio);

        return historialDAO.filtrarPagos(
                numeroCasa == null
                        ? null
                        : String.valueOf(numeroCasa),

                numeroMes == null
                        ? null
                        : String.valueOf(numeroMes),

                numeroAnio == null
                        ? null
                        : String.valueOf(numeroAnio)
        );
    }
>>>>>>> origin/main
}