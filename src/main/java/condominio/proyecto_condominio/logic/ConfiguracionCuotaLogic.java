package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.ConfiguracionCuotaDAO;
import condominio.proyecto_condominio.model.Cuota;

public class ConfiguracionCuotaLogic {

    private ConfiguracionCuotaDAO cuotaDAO = new ConfiguracionCuotaDAO();

    public String validarCuota(
            String nuevaCuota,
            double cuotaActual
    ) {

        if (nuevaCuota == null
                || nuevaCuota.trim().isEmpty()) {

            return "Debe ingresar una cuota.";
        }

        double monto = Double.parseDouble(nuevaCuota);

        if (monto <= 0) {

            return "La cuota debe ser mayor a 0.";
        }

        if (monto > 100000) {

            return "La cuota no puede exceder Q100,000.";
        }

        if (monto == cuotaActual) {

            return "La cuota ingresada ya existe.";
        }

        return null;
    }

    public boolean guardarCuota(
            double monto,
            int idUsuario
    ) {

        Cuota cuota = new Cuota();

        cuota.setMontoCuota(monto);

        return cuotaDAO.guardarCuota(
                cuota,
                idUsuario
        );
    }

    public double obtenerCuotaActual() {

        return cuotaDAO.obtenerUltimaCuota();
    }

    public double calcularRecaudacion() {

        double cuota = cuotaDAO.obtenerUltimaCuota();

        int totalCasas = cuotaDAO.obtenerTotalCasas();

        return cuota * totalCasas;
    }
}