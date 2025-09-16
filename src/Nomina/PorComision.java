package Nomina;

/**
 * 
 * - clase hija de Empleado (HERENCIA).
 * - Representa a un empleado que cobra un porcentaje de sus ventas brutas.
 */
public class PorComision extends Empleado {

    // Atributos especificos
    private double ventasBrutas;      // Monto de las ventas realizadas
    private double tarifaComision;    // Porcentaje de comisióo (ej: 0.07 = 7%)

    // Constructor que inicia los datos comunes y los de comision
    public PorComision(String primerNombre, String apellidoPaterno, String numeroSeguroSocial,
                       double ventasBrutas, double tarifaComision) {
        super(primerNombre, apellidoPaterno, numeroSeguroSocial);
        this.ventasBrutas = ventasBrutas;
        this.tarifaComision = tarifaComision;
    }

    // Getters y Setters
    public double getVentasBrutas() { return ventasBrutas; }
    public void setVentasBrutas(double ventasBrutas) { this.ventasBrutas = ventasBrutas; }

    public double getTarifaComision() { return tarifaComision; }
    public void setTarifaComision(double tarifaComision) { this.tarifaComision = tarifaComision; }

    // Implementamos el calculo
    @Override
    public double calcularPagoSemanal() {
        // Pago = ventas x comision
        return ventasBrutas * tarifaComision;
    }

    // Detalle del calculo
    @Override
    public String detalleCalculo() {
        return "(" + ventasBrutas + " x " + tarifaComision + ")";
    }
}
