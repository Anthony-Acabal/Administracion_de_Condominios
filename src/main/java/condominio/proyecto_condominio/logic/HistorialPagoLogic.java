package condominio.proyecto_condominio.logic;

public class HistorialPagoLogic {

    public boolean validarMesAnio(String mes, String anio) {

        if (mes != null && !mes.equals("Todos los meses")
                && anio.equals("Todos los años")) {
            return false;
        }

        return true;
    }

    public Integer parseCasa(String casa) {

        if (casa == null || casa.equals("Todas las casas")) {
            return null;
        }

        return Integer.parseInt(casa);
    }

    public Integer parseMes(String mesIndex) {

        if (mesIndex == null) return null;
        return Integer.parseInt(mesIndex);
    }

    public Integer parseAnio(String anio) {

        if (anio == null || anio.equals("Todos los años")) {
            return null;
        }

        return Integer.parseInt(anio);
    }
}