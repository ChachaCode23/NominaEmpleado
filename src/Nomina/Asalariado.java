package Nomina;

// Es una clase hija de empleado.
 
public class Asalariado extends Empleado {

    // Atributo específico de este tipo de empleado
    private double salarioSemanal;

    // este constructor inicializa los datos comunes + el salario
    public Asalariado(String primerNombre, String apellidoPaterno, String numeroSeguroSocial, double salarioSemanal) {
        // Usamos super(...) para llamar al constructor de la clase padre (Empleado)
        super(primerNombre, apellidoPaterno, numeroSeguroSocial);
        this.salarioSemanal = salarioSemanal;
    }

    // Getter y Setter (encapsulamiento)
    public double getSalarioSemanal() { return salarioSemanal; }
    public void setSalarioSemanal(double salarioSemanal) { this.salarioSemanal = salarioSemanal; }

    // Implementamos el cálculo (POLIMORFISMO)
    @Override
    public double calcularPagoSemanal() {
        // Para asalariado es simple: siempre el mismo salario fijo
        return salarioSemanal;
    }

    // Implementamos el detalle del cálculo
    @Override
    public String detalleCalculo() {
        return "Salario fijo = " + salarioSemanal;
    }
}
