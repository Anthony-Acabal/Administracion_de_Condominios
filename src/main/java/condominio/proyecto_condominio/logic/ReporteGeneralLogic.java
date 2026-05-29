package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.ReporteGeneralDAO;
import condominio.proyecto_condominio.model.ReporteGeneral;

import java.util.List;

public class ReporteGeneralLogic {

    private ReporteGeneralDAO dao
            = new ReporteGeneralDAO();

    public List<ReporteGeneral> obtenerReporteGeneral(
            int mes,
            int anio
    ) {

        return dao.obtenerReporteGeneral(
                mes,
                anio
        );
    }

    public double[] obtenerResumenMensual(
            int mes,
            int anio
    ) {

        return dao.obtenerResumenMensual(
                mes,
                anio
        );
    }

    public double[] obtenerResumenAnual(
            int mesHasta,
            int anio
    ) {

        return dao.obtenerResumenAnual(
                mesHasta,
                anio
        );
    }

    public int[] obtenerLimitesFechas() {

        return dao.obtenerLimitesFechas();
    }
}