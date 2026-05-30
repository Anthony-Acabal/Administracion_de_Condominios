package condominio.proyecto_condominio.logic;

import condominio.proyecto_condominio.dao.RegistroPagoCuotaDAO;
import condominio.proyecto_condominio.model.PagoCuota;
import condominio.proyecto_condominio.model.Propietario;
import java.time.LocalDate;
import java.util.ArrayList;

public class RegistroPagoCuotaLogic {

    private RegistroPagoCuotaDAO pagoDAO = new RegistroPagoCuotaDAO();

    public double obtenerCuotaActual() {

        return pagoDAO.obtenerUltimaCuota();
    }

    private final String[] meses = {
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

    public ArrayList<String> obtenerCasas() {

        return pagoDAO.obtenerCasas();
    }

    public Propietario obtenerPropietario(
            int numeroCasa
    ) {

        return pagoDAO.obtenerPropietario(numeroCasa);
    }

    public ArrayList<String> obtenerMesesPendientes(int numeroCasa, int anio) {

        ArrayList<String> pendientes = new ArrayList<>();

        ArrayList<Integer> pagados = pagoDAO.obtenerMesesPagados(numeroCasa, anio);

        int mesActual = LocalDate.now().getMonthValue();
        int anioActual = LocalDate.now().getYear();

        // si no es el año actual, no limitamos por mes
        if (anio != anioActual) {
            mesActual = 12;
        }

        for (int i = 1; i <= mesActual; i++) {
            if (!pagados.contains(i)) {
                pendientes.add(meses[i - 1]);
            }
        }

        return pendientes;
    }

    public int obtenerNumeroMes(
            String mes
    ) {

        for (int i = 0; i < meses.length; i++) {

            if (meses[i].equals(mes)) {

                return i + 1;
            }
        }

        return 0;
    }

    public String validarPago(
            int numeroCasa,
            int mes,
            int anio
    ) {

        boolean existePago
                = pagoDAO.existePago(
                        numeroCasa,
                        mes,
                        anio
                );

        if (existePago) {

            return "El pago ya existe.";
        }

        int mesAnterior = mes - 1;
        int anioAnterior = anio;

        if (mesAnterior == 0) {

            mesAnterior = 12;
            anioAnterior--;
        }

        boolean pagoAnterior
                = pagoDAO.existePago(
                        numeroCasa,
                        mesAnterior,
                        anioAnterior
                );

        if (!pagoAnterior && mes != 1) {

            return "Debe pagar primero el mes anterior.";
        }

        return null;
    }

    public int registrarPago(
            int numeroCasa,
            int mes,
            int anio
    ) {

        Propietario propietario
                = pagoDAO.obtenerPropietario(
                        numeroCasa
                );

        if (propietario == null) {

            return -1;
        }

        PagoCuota pago = new PagoCuota();

        pago.setIdPropietario(
                propietario.getIdPropietario()
        );

        pago.setIdCuota(8);

        pago.setFechaPago(
                LocalDate.of(anio, mes, 1)
        );

        pago.setImprimeComprobante("S");

        pago.setIdUsuarioCreacion(1);

        int idPagoCuota = pagoDAO.registrarPago(pago);

        return idPagoCuota;
    }
}
