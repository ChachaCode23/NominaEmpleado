package Nomina;

//clase de Empleado (HERENCIA).
//aqui Combinamos un salario base + comision por ventas + 10% extra del salario base.

public class AsalariadoPorComision extends Empleado {

    // Atributos especificos
    private double ventasBrutas;     // Ventas realizadas en la semana
    private double tarifaComision;   // Porcentaje de comisión (ej: 0.05 = 5%)
    private double salarioBase;      // Salario semanal base

    //este constructor inicia datos comunes y los de este tipo de empleado
    public AsalariadoPorComision(String primerNombre, String apellidoPaterno, String numeroSeguroSocial,
                                 double ventasBrutas, double tarifaComision, double salarioBase) {
        super(primerNombre, apellidoPaterno, numeroSeguroSocial);
        this.ventasBrutas = ventasBrutas;
        this.tarifaComision = tarifaComision;
        this.salarioBase = salarioBase;
    }

    // Getters y Setters
    public double getVentasBrutas() { return ventasBrutas; }
    public void setVentasBrutas(double ventasBrutas) { this.ventasBrutas = ventasBrutas; }

    public double getTarifaComision() { return tarifaComision; }
    public void setTarifaComision(double tarifaComision) { this.tarifaComision = tarifaComision; }

    public double getSalarioBase() { return salarioBase; }
    public void setSalarioBase(double salarioBase) { this.salarioBase = salarioBase; }

    // Implementamos el calculo 
    @Override
    public double calcularPagoSemanal() {
        // Formula del SRS:
        // Pago = (ventasBrutas * tarifaComision) + salarioBase + (salarioBase * 0.10)
        double comision = ventasBrutas * tarifaComision;
        double bono10 = salarioBase * 0.10;
        return comision + salarioBase + bono10;
    }

    // Detallamos el caculo para mostrar la formula en texto
    @Override
    public String detalleCalculo() {
        // Ejemplo: (50000 x 0.05) + 8000 + (8000 x 0.10)
        return "(" + ventasBrutas + " x " + tarifaComision + ") + " + salarioBase + " + (" + salarioBase + " x 0.10)";
    }
}
