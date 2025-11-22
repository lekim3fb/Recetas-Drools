package sbc.example;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;
import org.kie.api.runtime.rule.QueryResults;
import org.kie.api.runtime.rule.QueryResultsRow;

import java.util.*;

/**
 * Clase principal del sistema de recomendación de recetas
 * Integra todos los módulos de Drools para proporcionar recomendaciones
 * Versión INTERACTIVA: solicita información del usuario por pantalla
 */
public class Main {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("\n" + "█".repeat(80));
        System.out.println("█ SISTEMA DE RECOMENDACIÓN DE RECETAS DE COCINA                           █");
        System.out.println("█ Basado en Drools - Sistemas Basados en el Conocimiento                 █");
        System.out.println("█ VERSIÓN INTERACTIVA - Ingrese sus datos paso a paso                   █");
        System.out.println("█".repeat(80) + "\n");

        try {
            // Inicializar KieServices
            KieServices ks = KieServices.Factory.get();
            KieContainer kContainer = ks.getKieClasspathContainer();

            // Recopilar información del usuario interactivamente
            Usuario usuario = recopilarDatosUsuario();

            // Ejecutar el sistema con la información recopilada
            if (usuario != null) {
                ejecutarSistemaRecomendacion(kContainer, usuario);
            } else {
                System.out.println("❌ No se pudieron recopilar los datos del usuario.");
            }

        } catch (Exception e) {
            System.out.println("\n✗ ERROR AL EJECUTAR EL SISTEMA:");
            System.out.println("  " + e.getMessage());
            e.printStackTrace();
        } finally {
            scanner.close();
        }
    }

    /**
     * Recopila los datos del usuario interactivamente
     */
    private static Usuario recopilarDatosUsuario() {
        System.out.println("\n" + "═".repeat(80));
        System.out.println("INGRESE SUS DATOS PERSONALES");
        System.out.println("═".repeat(80));

        // 1. Nombre del usuario
        String nombre = null;
        while (true) {
            System.out.print("\n▶ ¿Cuál es su nombre? ");
            nombre = scanner.nextLine().trim();
            if (!nombre.isEmpty()) break;
            System.out.println("❌ El nombre no puede estar vacío. Por favor, introduzca su nombre.");
        }

        Usuario usuario = new Usuario(nombre);

        // 2. Nivel de habilidad en cocina
        System.out.println("\n▶ Nivel de habilidad en cocina:");
        System.out.println("  1. Básico (principiante)");
        System.out.println("  2. Intermedio");
        System.out.println("  3. Avanzado");
        String nivel = null;
        while (true) {
            System.out.print("  Seleccione (1-3): ");
            String nivelInput = scanner.nextLine().trim();
            nivel = mapearNivel(nivelInput);
            if (nivel != null) break;
            System.out.println("  ❌ Opción inválida. Introduzca 1, 2 o 3.");
        }
        usuario.setNivelHabilidad(nivel);
        System.out.println("  ✓ Nivel seleccionado: " + nivel);

        // 3. Tiempo disponible
        int tiempoMin = 0;
        while (true) {
            System.out.print("\n▶ ¿Cuántos minutos tiene disponibles para cocinar? ");
            String tiempoStr = scanner.nextLine().trim();
            try {
                tiempoMin = Integer.parseInt(tiempoStr);
                if (tiempoMin < 5) {
                    System.out.println("  ⚠ Tiempo mínimo recomendado: 5 minutos. Intente de nuevo.");
                    continue;
                }
                break;
            } catch (NumberFormatException e) {
                System.out.println("  ❌ Valor inválido. Introduzca un número entero de minutos.");
            }
        }
        usuario.setTiempoMaximoDisponible(tiempoMin);
        System.out.println("  ✓ Tiempo registrado: " + tiempoMin + " minutos");

        // 4. Ingredientes disponibles
        System.out.println("\n" + "═".repeat(80));
        System.out.println("INGREDIENTES DISPONIBLES");
        System.out.println("═".repeat(80));
        mostrarIngredientesDisponibles();
        System.out.println("\n▶ Ingrese los números de los ingredientes que tiene (separados por comas)");
        System.out.println("  Ejemplo: 1,2,3,5");
        boolean ingredientesOk = false;
        while (!ingredientesOk) {
            System.out.print("  Su selección: ");
            String ingInput = scanner.nextLine().trim();
            ingredientesOk = agregarIngredientesDelUsuario(usuario, ingInput);
            if (!ingredientesOk) {
                System.out.println("  ❌ Selección inválida. Debe escoger al menos un ingrediente válido (números separados por comas).");
            }
        }
        System.out.println("  ✓ " + usuario.getIngredientesDisponibles().size() + " ingredientes registrados");

        // 5. Preferencias gastronómicas
        System.out.println("\n" + "═".repeat(80));
        System.out.println("PREFERENCIAS GASTRONÓMICAS");
        System.out.println("═".repeat(80));
        mostrarPreferenciasDisponibles();
        System.out.println("\n▶ Ingrese los números de sus preferencias (separados por comas)");
        while (true) {
            System.out.print("  Su selección (deje en blanco si no desea agregar): ");
            String prefInput = scanner.nextLine().trim();
            if (prefInput.isEmpty()) break;
            boolean ok = agregarPreferenciasDelUsuario(usuario, prefInput);
            if (ok) break;
            System.out.println("  ❌ Selección inválida. Introduzca números separados por comas o deje en blanco.");
        }
        System.out.println("  ✓ " + usuario.getPreferenciasWebs().size() + " preferencias registradas");

        // 6. Restricciones dietéticas
        System.out.println("\n" + "═".repeat(80));
        System.out.println("RESTRICCIONES DIETÉTICAS");
        System.out.println("═".repeat(80));
        mostrarRestriccionesDisponibles();
        System.out.println("\n▶ Ingrese los números de sus restricciones (separados por comas)");
        System.out.println("  (Deje en blanco si no tiene restricciones)");
        while (true) {
            System.out.print("  Su selección (deje en blanco si no tiene restricciones): ");
            String restInput = scanner.nextLine().trim();
            if (restInput.isEmpty()) break;
            boolean ok = agregarRestriccionesDelUsuario(usuario, restInput);
            if (ok) break;
            System.out.println("  ❌ Selección inválida. Introduzca números separados por comas o deje en blanco.");
        }
        System.out.println("  ✓ " + usuario.getRestricciones().size() + " restricciones registradas");

        System.out.println("\n" + "═".repeat(80));
        System.out.println("✅ DATOS DEL USUARIO REGISTRADOS CORRECTAMENTE");
        System.out.println("═".repeat(80));

        return usuario;
    }

    private static String mapearNivel(String input) {
        switch (input) {
            case "1": return "básico";
            case "2": return "intermedio";
            case "3": return "avanzado";
            default: return null; // devolver null para indicar opción inválida y forzar reintento
        }
    }

    private static void mostrarIngredientesDisponibles() {
        String[] ingredientes = {
            "pasta", "tomate", "cebolla", "ajo", "aceite de oliva",
            "sal", "huevo", "pechuga de pollo", "salmón", "brócoli",
            "zanahoria", "patata", "leche", "queso", "pan",
            "arroz", "carne molida"
        };
        for (int i = 0; i < ingredientes.length; i++) {
            System.out.println("  " + (i + 1) + ". " + ingredientes[i]);
        }
    }

    private static boolean agregarIngredientesDelUsuario(Usuario usuario, String input) {
        String[] ingredientes = {
            "pasta", "tomate", "cebolla", "ajo", "aceite de oliva",
            "sal", "huevo", "pechuga de pollo", "salmón", "brócoli",
            "zanahoria", "patata", "leche", "queso", "pan",
            "arroz", "carne molida"
        };
        usuario.getIngredientesDisponibles().clear();
        if (input == null || input.isEmpty()) return false;
        try {
            String[] indices = input.split(",");
            for (String idx : indices) {
                int i = Integer.parseInt(idx.trim()) - 1;
                if (i >= 0 && i < ingredientes.length) {
                    usuario.addIngredienteDisponible(ingredientes[i]);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("  ❌ Formato inválido. Ingrese números separados por comas.");
            usuario.getIngredientesDisponibles().clear();
            return false;
        }
        return !usuario.getIngredientesDisponibles().isEmpty();
    }

    private static void mostrarPreferenciasDisponibles() {
        String[] preferencias = {
            "italiana", "española", "francesa", "asiática", "mexicana",
            "vegetariana", "picante", "ligera", "tradicional", "moderna"
        };
        for (int i = 0; i < preferencias.length; i++) {
            System.out.println("  " + (i + 1) + ". " + preferencias[i]);
        }
    }

    private static boolean agregarPreferenciasDelUsuario(Usuario usuario, String input) {
        String[] preferencias = {
            "italiana", "española", "francesa", "asiática", "mexicana",
            "vegetariana", "picante", "ligera", "tradicional", "moderna"
        };
        usuario.getPreferenciasWebs().clear();
        if (input == null || input.isEmpty()) return true;
        try {
            String[] indices = input.split(",");
            for (String idx : indices) {
                int i = Integer.parseInt(idx.trim()) - 1;
                if (i >= 0 && i < preferencias.length) {
                    usuario.addPreferencia(preferencias[i]);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("  ❌ Formato inválido.");
            usuario.getPreferenciasWebs().clear();
            return false;
        }
        return true;
    }

    private static void mostrarRestriccionesDisponibles() {
        String[] restricciones = {
            "vegetariano", "vegano", "sin gluten", "sin lactosa", "alérgico a frutos secos",
            "diabético", "hipertensión", "alérgico a mariscos", "alérgico a huevo", "kosher"
        };
        for (int i = 0; i < restricciones.length; i++) {
            System.out.println("  " + (i + 1) + ". " + restricciones[i]);
        }
    }

    private static boolean agregarRestriccionesDelUsuario(Usuario usuario, String input) {
        String[] restricciones = {
            "vegetariano", "vegano", "sin gluten", "sin lactosa", "alérgico a frutos secos",
            "diabético", "hipertensión", "alérgico a mariscos", "alérgico a huevo", "kosher"
        };
        usuario.getRestricciones().clear();
        if (input == null || input.isEmpty()) return true;
        try {
            String[] indices = input.split(",");
            for (String idx : indices) {
                int i = Integer.parseInt(idx.trim()) - 1;
                if (i >= 0 && i < restricciones.length) {
                    usuario.addRestriccion(restricciones[i]);
                }
            }
        } catch (NumberFormatException e) {
            System.out.println("  ❌ Formato inválido.");
            usuario.getRestricciones().clear();
            return false;
        }
        return true;
    }

    /**
     * Ejecuta el sistema completo de recomendación
     */
    private static void ejecutarSistemaRecomendacion(KieContainer kContainer, Usuario usuario) {
        try {
            // Crear sesión principal
            KieSession kSession = kContainer.newKieSession("InformeSession");

            // Insertar usuario
            kSession.insert(usuario);

            // Disparar las reglas
            System.out.println("\n[PROCESANDO SOLICITUD...]");
            int reglasDisparadas = kSession.fireAllRules();
            System.out.println("[REGLAS DISPARADAS: " + reglasDisparadas + "]\n");

            // Ejecutar queries para obtener resultados
            mostrarResultadosQueries(kSession);

            // Mostrar evidencias
            mostrarEvidencias(kSession, usuario);

            kSession.dispose();

        } catch (Exception e) {
            System.out.println("Error durante la ejecución: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Muestra los resultados usando queries definidas en las reglas
     */
    private static void mostrarResultadosQueries(KieSession kSession) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("RESULTADOS DETALLADOS DE QUERIES");
        System.out.println("=".repeat(80));

        // Query 1: Obtener recetas recomendadas con explicación
        System.out.println("\n[QUERY] Recomendaciones con explicación:");
        try {
            QueryResults resultados = kSession.getQueryResults("ObtenerRecomendacionesConExplicacion");
            if (resultados.size() > 0) {
                for (QueryResultsRow row : resultados) {
                    String nombre = (String) row.get("$nombre");
                    Double puntuacion = (Double) row.get("$puntuacion");
                    String estado = (String) row.get("$estado");
                    List<String> razones = (List<String>) row.get("$razones");

                    System.out.println("\n  ├─ Receta: " + nombre);
                    System.out.println("  ├─ Estado: " + estado);
                    System.out.println("  ├─ Puntuación: " + String.format("%.1f", puntuacion));
                    if (razones != null && !razones.isEmpty()) {
                        System.out.println("  ├─ Razones:");
                        for (String razon : razones) {
                            System.out.println("  │  └─ " + razon);
                        }
                    }
                }
            } else {
                System.out.println("  └─ No hay recomendaciones disponibles");
            }
        } catch (Exception e) {
            System.out.println("  └─ Query no disponible: " + e.getMessage());
        }

        // Query 2: Obtener recetas descartadas
        System.out.println("\n[QUERY] Recetas descartadas:");
        try {
            QueryResults descartadas = kSession.getQueryResults("ObtenerRecetasDescartadas");
            if (descartadas.size() > 0) {
                System.out.println("  Encontradas " + descartadas.size() + " recetas descartadas");
                for (QueryResultsRow row : descartadas) {
                    Recomendacion rec = (Recomendacion) row.get("$recomendacion");
                    System.out.println("  ├─ " + rec.getReceta().getNombre() + ": " + rec.getMotivo());
                }
            } else {
                System.out.println("  └─ No hay recetas descartadas (excelente)");
            }
        } catch (Exception e) {
            System.out.println("  └─ Query no disponible: " + e.getMessage());
        }

        // Query 3: Obtener recetas recomendadas ordenadas por puntuación
        System.out.println("\n[QUERY] Ranking de recetas recomendadas:");
        try {
            QueryResults recomendadas = kSession.getQueryResults("ObtenerRecetasRecomendadas");
            if (recomendadas.size() > 0) {
                int posicion = 1;
                for (QueryResultsRow row : recomendadas) {
                    Recomendacion rec = (Recomendacion) row.get("$recomendacion");
                    System.out.println("  " + posicion + ". " + rec.getReceta().getNombre() +
                            " (Puntuación: " + String.format("%.1f", rec.getPuntuacion()) + ")");
                    posicion++;
                }
            }
        } catch (Exception e) {
            System.out.println("  └─ Query no disponible: " + e.getMessage());
        }
    }

    /**
     * Muestra las evidencias que han llevado a la recomendación
     */
    private static void mostrarEvidencias(KieSession kSession, Usuario usuario) {
        System.out.println("\n" + "=".repeat(80));
        System.out.println("EVIDENCIAS DEL PROCESO DE RECOMENDACIÓN");
        System.out.println("=".repeat(80));

        System.out.println("\n[DATOS DEL USUARIO]");
        System.out.println("├─ Nombre: " + usuario.getNombre());
        System.out.println("├─ Ingredientes disponibles (" + usuario.getIngredientesDisponibles().size() + "): ");
        for (String ing : usuario.getIngredientesDisponibles()) {
            System.out.println("│  ├─ " + ing);
        }
        System.out.println("├─ Preferencias gastronómicas: " + usuario.getPreferenciasWebs());
        System.out.println("├─ Restricciones dietéticas: " + usuario.getRestricciones());
        System.out.println("├─ Tiempo disponible: " + usuario.getTiempoMaximoDisponible() + " minutos");
        System.out.println("└─ Nivel de habilidad: " + usuario.getNivelHabilidad());

        System.out.println("\n[VALIDACIÓN DE DATOS]");
        if (usuario.validarDatos()) {
            for (String tip : usuario.getTipsvalidacion()) {
                System.out.println("├─ " + tip);
            }
        }

        System.out.println("\n[PROCESO DE FILTRADO]");
        System.out.println("├─ Se aplican reglas de:\n" +
                "│  ├─ Filtrado por alergia\n" +
                "│  ├─ Filtrado por tiempo insuficiente\n" +
                "│  ├─ Filtrado por incompatibilidad dietética\n" +
                "│  └─ Penalización por dificultad excesiva");

        System.out.println("\n[PROCESO DE PUNTUACIÓN]");
        System.out.println("├─ Puntuación base por disponibilidad de ingredientes (% ≥ 70%)\n" +
                "├─ Puntuación alternativa (% 50-70%)\n" +
                "├─ Bonificación por preferencia gastronómica (+15)\n" +
                "├─ Bonificación por compatibilidad dietética (+20)\n" +
                "├─ Bonificación por rapidez (+10)\n" +
                "└─ Bonificación por nivel de dificultad apropiado (+5)");

        System.out.println("\n[EXPLICACIÓN]");
        System.out.println("├─ Se generan explicaciones basadas en:\n" +
                "│  ├─ Disponibilidad de ingredientes\n" +
                "│  ├─ Ingredientes faltantes y sustitutos\n" +
                "│  ├─ Compatibilidad con restricciones\n" +
                "│  └─ Factores de bonificación aplicados");

        System.out.println("\n" + "=".repeat(80));
    }
}