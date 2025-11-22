package sbc.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un usuario del sistema
 */
public class Usuario implements Serializable {
    private String nombre;
    private List<String> ingredientesDisponibles; // nombres de ingredientes disponibles
    private List<String> preferenciasWebs; // italianas, españolas, asiáticas, etc.
    private List<String> restricciones; // alergias o tipo de dieta
    private int tiempoMaximoDisponible; // en minutos
    private String nivelHabilidad; // principiante, intermedio, avanzado
    private List<String> tipsvalidacion; // para almacenar feedback de validación

    public Usuario() {
        this.ingredientesDisponibles = new ArrayList<>();
        this.preferenciasWebs = new ArrayList<>();
        this.restricciones = new ArrayList<>();
        this.tipsvalidacion = new ArrayList<>();
    }

    public Usuario(String nombre) {
        this();
        this.nombre = nombre;
    }

    // Validación de datos
    public boolean validarDatos() {
        this.tipsvalidacion.clear();

        // Validar nombre
        if (nombre == null || nombre.trim().isEmpty()) {
            this.tipsvalidacion.add("ERROR: El nombre del usuario no puede estar vacío");
            return false;
        }

        // Validar tiempo
        if (tiempoMaximoDisponible < 5) {
            this.tipsvalidacion.add("ADVERTENCIA: El tiempo disponible debe ser al menos 5 minutos");
        }

        // Validar nivel de habilidad
        if (nivelHabilidad != null && !nivelHabilidad.matches("principiante|intermedio|avanzado")) {
            this.tipsvalidacion.add("ERROR: El nivel de habilidad debe ser 'principiante', 'intermedio' o 'avanzado'");
            return false;
        }

        if (this.tipsvalidacion.isEmpty()) {
            this.tipsvalidacion.add("VALIDACIÓN: Datos del usuario válidos");
        }

        return true;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<String> getIngredientesDisponibles() {
        return ingredientesDisponibles;
    }

    public void setIngredientesDisponibles(List<String> ingredientesDisponibles) {
        this.ingredientesDisponibles = ingredientesDisponibles;
    }

    public void addIngredienteDisponible(String ingrediente) {
        if (!this.ingredientesDisponibles.contains(ingrediente)) {
            this.ingredientesDisponibles.add(ingrediente);
        }
    }

    public List<String> getPreferenciasWebs() {
        return preferenciasWebs;
    }

    public void setPreferenciasWebs(List<String> preferenciasWebs) {
        this.preferenciasWebs = preferenciasWebs;
    }

    public void addPreferencia(String preferencia) {
        if (!this.preferenciasWebs.contains(preferencia)) {
            this.preferenciasWebs.add(preferencia);
        }
    }

    public List<String> getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(List<String> restricciones) {
        this.restricciones = restricciones;
    }

    public void addRestriccion(String restriccion) {
        if (!this.restricciones.contains(restriccion)) {
            this.restricciones.add(restriccion);
        }
    }

    public int getTiempoMaximoDisponible() {
        return tiempoMaximoDisponible;
    }

    public void setTiempoMaximoDisponible(int tiempoMaximoDisponible) {
        this.tiempoMaximoDisponible = tiempoMaximoDisponible;
    }

    public String getNivelHabilidad() {
        return nivelHabilidad;
    }

    public void setNivelHabilidad(String nivelHabilidad) {
        this.nivelHabilidad = nivelHabilidad;
    }

    public List<String> getTipsvalidacion() {
        return tipsvalidacion;
    }

    public void setTipsvalidacion(List<String> tipsvalidacion) {
        this.tipsvalidacion = tipsvalidacion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "nombre='" + nombre + '\'' +
                ", ingredientes=" + ingredientesDisponibles.size() +
                ", restricciones=" + restricciones +
                ", tiempoMaximo=" + tiempoMaximoDisponible +
                ", habilidad='" + nivelHabilidad + '\'' +
                '}';
    }
}
