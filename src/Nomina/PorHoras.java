package Nomina;

/**
 * Clase PorHoras
 * 
 * - clase hija de Empleado
 * - Representa a un empleado que cobra segun las horas trabajadas.
 * - Si trabaja más de 40 horas, las extras se pagan a 1.5 veces la tarifa (horas extra).
 */
public class PorHoras extends Empleado {

    // Atributos especificos
    private double sueldoPorHora;
    private double horasTrabajadas;

    // Constructor inicia con apellido y NSS, mas sueldo y horas
    // Nota: aqui el primerNombre se setea aparte si se quiere mostrar
    public PorHoras(String apellidoPaterno, String numeroSeguroSocial, double sueldoPorHora, double horasTrabajadas) {
        super("", apellidoPaterno, numeroSeguroSocial); // "" porque no pedimos primerNombre aqui
        this.sueldoPorHora = sueldoPorHora;
        this.horasTrabajadas = horasTrabajadas;
    }

    // Getters y Setters
    public double getSueldoPorHora() { return sueldoPorHora; }
    public void setSueldoPorHora(double sueldoPorHora) { this.sueldoPorHora = sueldoPorHora; }

    public double getHorasTrabajadas() { return horasTrabajadas; }
    public void setHorasTrabajadas(double horasTrabajadas) { this.horasTrabajadas = horasTrabajadas; }

    // Implementamos el calculo 
    @Override
    public double calcularPagoSemanal() {
        if (horasTrabajadas <= 40) {
            // Caso normal todas las horas sueldo normal
            return sueldoPorHora * horasTrabajadas;
        } else {
            // Caso con horas extra 40 normales + las extra al 1.5
            double horasExtra = horasTrabajadas - 40;
            return (sueldoPorHora * 40) + (sueldoPorHora * 1.5 * horasExtra);
        }
    }

    // Detalle del calculo devuelve la formula como texto
    @Override
    public String detalleCalculo() {
        if (horasTrabajadas <= 40) {
            return "(" + sueldoPorHora + " x " + horasTrabajadas + ")";
        } else {
            double horasExtra = horasTrabajadas - 40;
            return "(" + sueldoPorHora + " x 40) + (" + sueldoPorHora + " x 1.5 x " + horasExtra + ")";
        }
    }
}
