import model.Propietario;
import negocio.GestorUsuarios;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Clase de prueba para verificar la funcionalidad del módulo de Propietarios
 * sin necesidad de conexión a base de datos
 */
public class TestPropietario {
    
    public static void main(String[] args) {
        System.out.println("=== INICIANDO PRUEBAS DEL MÓDULO PROPIETARIO ===\n");
        
        // Test 1: Creación de objeto Propietario
        testCreacionPropietario();
        
        // Test 2: Validación de getters
        testGetters();
        
        // Test 3: Validación de setters
        testSetters();
        
        // Test 4: Validación de campos obligatorios
        testValidacionCamposObligatorios();
        
        System.out.println("\n=== PRUEBAS COMPLETADAS ===");
    }
    
    private static void testCreacionPropietario() {
        System.out.println("TEST 1: Creación de objeto Propietario");
        try {
            List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios", "editar_predios"));
            List<String> lugaresProduccion = new ArrayList<>();
            
            Propietario p = new Propietario(
                "PROP-123456789",
                "Propietario",
                permisos,
                "CC",
                "123456789",
                "Juan Pérez García",
                "3001234567",
                "juan.perez@email.com",
                "ICA-001",
                lugaresProduccion
            );
            
            if (p != null) {
                System.out.println("✅ ÉXITO: Propietario creado correctamente");
                System.out.println("   - ID Usuario: " + p.getIdUsuario());
                System.out.println("   - Número Identificación: " + p.getNumeroIdentificacion());
                System.out.println("   - Nombres: " + p.getNombresCompletos());
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private static void testGetters() {
        System.out.println("TEST 2: Validación de métodos Getter");
        try {
            List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios", "editar_predios"));
            List<String> lugaresProduccion = new ArrayList<>();
            
            Propietario p = new Propietario(
                "PROP-987654321",
                "Propietario",
                permisos,
                "NIT",
                "987654321",
                "María López Rodríguez",
                "3109876543",
                "maria.lopez@email.com",
                "ICA-002",
                lugaresProduccion
            );
            
            boolean todosCorrectos = true;
            
            if (!p.getIdUsuario().equals("PROP-987654321")) {
                System.out.println("❌ ERROR: getIdUsuario() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getRol().equals("Propietario")) {
                System.out.println("❌ ERROR: getRol() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getTipoIdentificacion().equals("NIT")) {
                System.out.println("❌ ERROR: getTipoIdentificacion() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getNumeroIdentificacion().equals("987654321")) {
                System.out.println("❌ ERROR: getNumeroIdentificacion() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getNombresCompletos().equals("María López Rodríguez")) {
                System.out.println("❌ ERROR: getNombresCompletos() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getTelefonoContacto().equals("3109876543")) {
                System.out.println("❌ ERROR: getTelefonoContacto() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getCorreoElectronico().equals("maria.lopez@email.com")) {
                System.out.println("❌ ERROR: getCorreoElectronico() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (!p.getCodigoICAPredio().equals("ICA-002")) {
                System.out.println("❌ ERROR: getCodigoICAPredio() no retorna el valor correcto");
                todosCorrectos = false;
            }
            
            if (todosCorrectos) {
                System.out.println("✅ ÉXITO: Todos los getters funcionan correctamente");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private static void testSetters() {
        System.out.println("TEST 3: Validación de métodos Setter");
        try {
            List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios"));
            List<String> lugaresProduccion = new ArrayList<>();
            
            Propietario p = new Propietario(
                "PROP-111111111",
                "Propietario",
                permisos,
                "CC",
                "111111111",
                "Nombre Original",
                "3001111111",
                "original@email.com",
                "ICA-003",
                lugaresProduccion
            );
            
            // Modificar valores usando setters
            p.setTipoIdentificacion("CE");
            p.setNumeroIdentificacion("222222222");
            p.setNombresCompletos("Nombre Modificado");
            p.setTelefonoContacto("3002222222");
            p.setCorreoElectronico("modificado@email.com");
            p.setCodigoICAPredio("ICA-004");
            
            boolean todosCorrectos = true;
            
            if (!p.getTipoIdentificacion().equals("CE")) {
                System.out.println("❌ ERROR: setTipoIdentificacion() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (!p.getNumeroIdentificacion().equals("222222222")) {
                System.out.println("❌ ERROR: setNumeroIdentificacion() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (!p.getNombresCompletos().equals("Nombre Modificado")) {
                System.out.println("❌ ERROR: setNombresCompletos() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (!p.getTelefonoContacto().equals("3002222222")) {
                System.out.println("❌ ERROR: setTelefonoContacto() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (!p.getCorreoElectronico().equals("modificado@email.com")) {
                System.out.println("❌ ERROR: setCorreoElectronico() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (!p.getCodigoICAPredio().equals("ICA-004")) {
                System.out.println("❌ ERROR: setCodigoICAPredio() no funciona correctamente");
                todosCorrectos = false;
            }
            
            if (todosCorrectos) {
                System.out.println("✅ ÉXITO: Todos los setters funcionan correctamente");
            }
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
    
    private static void testValidacionCamposObligatorios() {
        System.out.println("TEST 4: Validación de campos obligatorios");
        try {
            List<String> permisos = new ArrayList<>(Arrays.asList("acceso_predios"));
            List<String> lugaresProduccion = new ArrayList<>();
            
            // Test con número de identificación vacío
            Propietario p1 = new Propietario(
                "PROP-000",
                "Propietario",
                permisos,
                "CC",
                "",  // Vacío
                "Nombre Completo",
                "3001234567",
                "email@test.com",
                "ICA-005",
                lugaresProduccion
            );
            
            if (p1.getNumeroIdentificacion().isEmpty()) {
                System.out.println("✅ ÉXITO: Se puede crear propietario con identificación vacía (validación debe hacerse en capa de negocio)");
            }
            
            // Test con nombres vacíos
            Propietario p2 = new Propietario(
                "PROP-001",
                "Propietario",
                permisos,
                "CC",
                "123456789",
                "",  // Vacío
                "3001234567",
                "email@test.com",
                "ICA-006",
                lugaresProduccion
            );
            
            if (p2.getNombresCompletos().isEmpty()) {
                System.out.println("✅ ÉXITO: Se puede crear propietario con nombres vacíos (validación debe hacerse en capa de negocio)");
            }
            
            System.out.println("✅ NOTA: Las validaciones de campos obligatorios deben implementarse en GestorUsuarios");
            
        } catch (Exception e) {
            System.out.println("❌ ERROR: " + e.getMessage());
            e.printStackTrace();
        }
        System.out.println();
    }
}
