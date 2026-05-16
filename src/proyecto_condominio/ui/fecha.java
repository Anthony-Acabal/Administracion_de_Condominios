/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package proyecto_condominio.ui;

import java.time.LocalDate;

/**
 * Clase utilitaria para gestionar las fechas del sistema.
 * @author yukiixD
 */
public class fecha {
    
    /**
     * Devuelve el mes y año actual en formato "Mes, Año" (ej: Mayo, 2026)
     * @return 
     */
    public static String obtenerFechaActualFormateada() {
        LocalDate fechaActual = LocalDate.now();
        int mes = fechaActual.getMonthValue();
        int anio = fechaActual.getYear();
        
        String nombreMes;
        nombreMes = switch (mes) {
            case 1 -> "Enero";
            case 2 -> "Febrero";
            case 3 -> "Marzo";
            case 4 -> "Abril";
            case 5 -> "Mayo";
            case 6 -> "Junio";
            case 7 -> "Julio";
            case 8 -> "Agosto";
            case 9 -> "Septiembre";
            case 10 -> "Octubre";
            case 11 -> "Noviembre";
            case 12 -> "Diciembre";
            default -> "Mes";
        };
        
        return nombreMes + ", " + anio;
    }
}