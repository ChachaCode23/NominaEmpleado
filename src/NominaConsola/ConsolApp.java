package NominaConsola;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import Nomina.*; 
// Importamos todas clases: Empleado, Asalariado, PorHoras, PorComision, AsalariadoPorComision

/**
 * ConsolaApp
 * -----------
 * Esta clase es la "interfaz" por consola de la aplicacion de nomina.
 * Aqui NO se hace el cálculo (eso ya lo hacen las subclases de Empleado).
 * 
 * aqui se hacemos:
 * - Mantiene una "BD" en memoria con un Map<Integer, Empleado>.
 * - Muestra un menu simple (listar, agregar, editar, eliminar, reporte).
 * - Usa Scanner para leer datos del usuario.
 * - Demuestra POLIMORFISMO: guardamos distintos tipos de Empleado y
 *   llamamos calcularPagoSemanal()/detalleCalculo() sin importar el tipo.
 */
public class ConsolApp {

    // ====== "Base de datos" en memoria (ID autoincremental -> Empleado) ======
    private static final Map<Integer, Empleado> DB = new LinkedHashMap<>();
    private static final AtomicInteger SEQ = new AtomicInteger(1000); // para generar IDs 1001, 1002, ...

    // Para leer desde la consola
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        seed();   // Carga algunos empleados de ejemplo que agregue para verificar que todo este bien
        menu();   // Arranca el menu principal
    }

    // ====== MENÚUPRINCIPAL ======
    private static void menu() {
        while (true) {
            System.out.println("\n=== NOMINA ===");
            System.out.println("1) Lista de empleados");
            System.out.println("2) Agregar empleado");
            System.out.println("3) Editar empleado");
            System.out.println("4) Eliminar empleado");
            System.out.println("5) Reporte semanal");
            System.out.println("0) Salir");
            System.out.print("Digite el numero de la opcion que desea: ");

            String op = sc.nextLine().trim();

            switch (op) {
                case "1" -> lista();
                case "2" -> agregar();
                case "3" -> editar();
                case "4" -> eliminar();
                case "5" -> reporte();
                case "0" -> { 
                    System.out.println("¡Adios!"); 
                    return; // sale del programa
                }
                default -> System.out.println("Opcion invalida.");
            }
        }
    }

    // ====== 1) LISTA DE EMPLEADOS ======
    private static void lista() {
        if (DB.isEmpty()) {
            System.out.println("No hay empleados.");
            return;
        }
        // Encabezado de tabla simple
        System.out.printf("%-6s %-25s %-24s%n", "ID", "Nombre", "Tipo");
        System.out.println("----------------------------------------------------");
        for (var entry : DB.entrySet()) {
            int id = entry.getKey();
            Empleado e = entry.getValue();
            // getNombre() y getTipo()  Empleado + subclases
            System.out.printf("%-6d %-25s %-24s%n", id, e.getNombre(), e.getTipo());
        }
    }

    // ====== 2) AGREGAR EMPLEADO ======
    private static void agregar() {
        System.out.println("\nTipos válidos: Asalariado | PorHoras | PorComision | AsalariadoPorComision");
        String tipo = ask("Tipo").trim();

        // Datos comunes
        String pn  = ask("Primer nombre (dejar vacio si no aplica)");
        String ap  = ask("Apellido paterno");
        String nss = ask("Numero de Seguro Social");

        Empleado e; 
        try {
            // Segun el tipo de empleado, pedimos los campos especificos
            switch (tipo) {
                case "Asalariado" -> {
                    double salario = nonNeg(ask("Salario semanal"));
                    e = new Asalariado(pn, ap, nss, salario);
                }
                case "PorHoras" -> {
                    double sueldo = nonNeg(ask("Sueldo por hora"));
                    double horas  = nonNeg(ask("Horas trabajadas"));
                    e = new PorHoras(ap, nss, sueldo, horas);
                    e.setPrimerNombre(pn); // opcional: asi getNombre muestra "pn + ap"
                }
                case "PorComision" -> {
                    double ventas    = nonNeg(ask("Ventas brutas"));
                    double tarifaPct = inRange(ask("Tarifa comision (%)"), 0, 100);
                    e = new PorComision(pn, ap, nss, ventas, tarifaPct / 100.0); // convertimos a decimal
                }
                case "AsalariadoPorComision" -> {
                    double ventas    = nonNeg(ask("Ventas brutas"));
                    double tarifaPct = inRange(ask("Tarifa comision (%)"), 0, 100);
                    double base      = nonNeg(ask("Salario base"));
                    e = new AsalariadoPorComision(pn, ap, nss, ventas, tarifaPct / 100.0, base);
                }
                default -> {
                    System.out.println("Tipo no soportado.");
                    return; // no seguimos si el tipo no es valido
                }
            }
        } catch (NumberFormatException ex) {
            // Si el usuario escribió algo que no es número o fuera de rango
            System.out.println("Entrada numerica invalida. Intenta de nuevo.");
            return;
        }

        int id = SEQ.incrementAndGet(); // genera un nuevo ID
        DB.put(id, e);                  // guarda en memoria
        System.out.println("Empleado agregado con ID: " + id);
    }

    // ====== 3) EDITAR EMPLEADO ======
    // mantenemos el "tipo" original del empleado.
    private static void editar() {
        int id = (int) nonNeg(ask("ID a editar"));
        Empleado prev = DB.get(id);
        if (prev == null) {
            System.out.println("No existe ese ID.");
            return;
        }

        System.out.println("Editando: " + prev.getNombre() + " (" + prev.getTipo() + ")");
        String tipo = prev.getTipo(); // dejamos el mismo tipo

        // Si el usuario presiona Enter, mantenemos el valor anterior 
        String pn  = defOr(prev.getPrimerNombre(), ask("Primer nombre [" + nvl(prev.getPrimerNombre()) + "]"));
        String ap  = defOr(prev.getApellidoPaterno(), ask("Apellido paterno [" + nvl(prev.getApellidoPaterno()) + "]"));
        String nss = defOr(prev.getNumeroSeguroSocial(), ask("NSS [" + nvl(prev.getNumeroSeguroSocial()) + "]"));

        Empleado e;
        try {
            switch (tipo) {
                case "Asalariado" -> {
                    Asalariado a = (Asalariado) prev;
                    double salario = defOr(a.getSalarioSemanal(), ask("Salario semanal [Enter = igual]"));
                    e = new Asalariado(pn, ap, nss, salario);
                }
                case "PorHoras" -> {
                    PorHoras ph = (PorHoras) prev;
                    double sueldo = defOr(ph.getSueldoPorHora(), ask("Sueldo por hora [Enter = igual]"));
                    double horas  = defOr(ph.getHorasTrabajadas(), ask("Horas trabajadas [Enter = igual]"));
                    e = new PorHoras(ap, nss, sueldo, horas);
                    e.setPrimerNombre(pn);
                }
                case "PorComision" -> {
                    PorComision pc = (PorComision) prev;
                    double ventas    = defOr(pc.getVentasBrutas(), ask("Ventas brutas [Enter = igual]"));
                    double tarifaPct = defOr(pc.getTarifaComision() * 100.0, ask("Tarifa comisión (%) [Enter = igual]"));
                    e = new PorComision(pn, ap, nss, ventas, tarifaPct / 100.0);
                }
                case "AsalariadoPorComision" -> {
                    AsalariadoPorComision ac = (AsalariadoPorComision) prev;
                    double ventas    = defOr(ac.getVentasBrutas(), ask("Ventas brutas [Enter = igual]"));
                    double tarifaPct = defOr(ac.getTarifaComision() * 100.0, ask("Tarifa comision (%) [Enter = igual]"));
                    double base      = defOr(ac.getSalarioBase(), ask("Salario base [Enter = igual]"));
                    e = new AsalariadoPorComision(pn, ap, nss, ventas, tarifaPct / 100.0, base);
                }
                default -> {
                    System.out.println("Tipo no soportado.");
                    return;
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Entrada numerica invalida. Cambios cancelados.");
            return;
        }

        DB.put(id, e); // guardamos el reemplazo
        System.out.println("Empleado actualizado.");
    }

    // ====== 4) ELIMINAR EMPLEADO ======
    private static void eliminar() {
        int id = (int) nonNeg(ask("ID a eliminar"));
        Empleado removed = DB.remove(id);
        System.out.println(removed == null ? "No existe ese ID." : "Empleado eliminado.");
    }

    // ====== 5) REPORTE SEMANAL (RF-4) ======
    // Muestra: ID, Nombre, Tipo, detalle de la formula, y el Pago total
    private static void reporte() {
        if (DB.isEmpty()) {
            System.out.println("No hay empleados.");
            return;
        }

        double total = 0;
        System.out.printf("%-6s %-22s %-22s %-40s %12s%n", "ID", "Nombre", "Tipo", "Detalle", "Pago");
        System.out.println("---------------------------------------------------------------------------------------------------------------");

        for (var entry : DB.entrySet()) {
            int id = entry.getKey();
            Empleado e = entry.getValue();

 
            // calcularPagoSemanal() y detalleCalculo() se comportan segun el tipo real (Asalariado, PorHoras, etc.)
            double pago = e.calcularPagoSemanal();
            total += pago;

            System.out.printf("%-6d %-22s %-22s %-40s %,12.2f%n",
                    id, e.getNombre(), e.getTipo(), nvl(e.detalleCalculo()), pago);
        }

        System.out.println("---------------------------------------------------------------------------------------------------------------");
        System.out.printf("%-6s %-22s %-22s %-40s %,12.2f%n", "", "", "", "TOTAL", total);
    }


    // Pide un dato al usuario con un mensaje
    private static String ask(String label) {
        System.out.print(label + ": ");
        return sc.nextLine().trim();
    }

    // Si el usuario presiona Enter, deja el valor por defecto; si no, usa el nuevo
    private static String defOr(String def, String input) {
        return (input == null || input.isBlank()) ? def : input.trim();
    }

    // Igual que arriba pero para numeros (≥ 0)
    private static double defOr(double def, String input) throws NumberFormatException {
        if (input == null || input.isBlank()) return def;
        return nonNeg(input);
    }

    // Convierte a double y valida que sea >= 0 (para sueldos, horas, ventas, etc.)
    private static double nonNeg(String input) throws NumberFormatException {
        double v = parseNum(input);
        if (v < 0) throw new NumberFormatException("Debe ser >= 0");
        return v;
    }

    // Convierte a double y valida un rango (por ejemplo, 0 a 100 para % comisión)
    private static double inRange(String input, double min, double max) throws NumberFormatException {
        double v = parseNum(input);
        if (v < min || v > max) throw new NumberFormatException("Debe estar entre " + min + " y " + max);
        return v;
    }

    // Acepta "12,5" o "12.5" como numero
    private static double parseNum(String input) throws NumberFormatException {
        return Double.parseDouble(input.replace(",", ".").trim());
    }

    private static String nvl(String s) { return (s == null) ? "" : s; }

    // Carga 4 empleados de ejemplo lo hice para comprobar que todo marche bien
    private static void seed() {
        
        DB.put(SEQ.incrementAndGet(), new Asalariado("Ana",   "Gomez",  "001-123", 15000));
        DB.put(SEQ.incrementAndGet(), new PorHoras("Perez",   "002-456", 300, 45));
        DB.put(SEQ.incrementAndGet(), new PorComision("Carmen","Lopez","003-789", 100000, 0.07));
        DB.put(SEQ.incrementAndGet(), new AsalariadoPorComision("Diego","Mixto","004-000", 50000, 0.05, 8000));
    }
}
