package Nomina;

/**
 * Clase abstracta Empleado
 * 
 * - Representa un empleado genérico.
 * - Es abstracta porque NO se pueden crear empleados directamente de aqui,
 *   solo se usan sus subclases (Asalariado, PorHoras, PorComision, etc).
 * - Aqui guardamos los datos comunes: nombre, apellido y NSS.
 */
public abstract class Empleado {

    // ================== Atributos ==================
    // Son privados con acceso solo por getters/setters.
    private String primerNombre;
    private String apellidoPaterno;
    private String numeroSeguroSocial;

    // ================== Constructor ==================
    public Empleado(String primerNombre, String apellidoPaterno, String numeroSeguroSocial) {
        this.primerNombre = primerNombre;
        this.apellidoPaterno = apellidoPaterno;
        this.numeroSeguroSocial = numeroSeguroSocial;
    }

    // ================== Getters y Setters ==================
    // controlamos el acceso a los atributos.
    public String getPrimerNombre() { return primerNombre; }
    public void setPrimerNombre(String primerNombre) { this.primerNombre = primerNombre; }

    public String getApellidoPaterno() { return apellidoPaterno; }
    public void setApellidoPaterno(String apellidoPaterno) { this.apellidoPaterno = apellidoPaterno; }

    public String getNumeroSeguroSocial() { return numeroSeguroSocial; }
    public void setNumeroSeguroSocial(String numeroSeguroSocial) { this.numeroSeguroSocial = numeroSeguroSocial; }

    // Metodo para devolver el nombre completo (nombre + apellido)
    public String getNombre() {
        // Si el primerNombre esta vacio, solo devuelve el apellido
        return (primerNombre == null || primerNombre.isBlank() ? "" : primerNombre + " ") + apellidoPaterno;
    }

    // ================== Metodos Abstractos ==================
    // Abstraccion y Polimorfismo: cada subclase debe implementar su propia version.

    /**
     * Cada tipo de empleado calcula su pago de forma diferente tal y como 
     * me pidieron en el pdf
     */
    public abstract double calcularPagoSemanal();

    //Cada tipo de empleado muestra como se hizo el cálculo.
     
    public abstract String detalleCalculo();

    // ================== Otros Metodos ==================
    // Devuelve el tipo de empleado 
    public String getTipo() {
        return this.getClass().getSimpleName();
    }
}
