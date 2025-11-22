package sbc.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa una receta en el sistema
 */
public class Receta implements Serializable {
    private String nombre;
    private List<IngredienteReceta> ingredientes; // Ingrediente + cantidad
    private int tiempoPreparacion; // en minutos
    private String dificultad; // fácil, medio, difícil
    private String tipoPlato; // entrante, principal, postre
    private List<String> dietasCompatibles; // vegano, vegetariano, keto, sin gluten
    private String region; // italiana, española, asiática, etc.
    private String descripcion;
    private double puntuacion; // puntuación del sistema
    private List<String> razonesRecomendacion; // explicación de por qué se recomienda

    public Receta() {
        this.ingredientes = new ArrayList<>();
        this.dietasCompatibles = new ArrayList<>();
        this.razonesRecomendacion = new ArrayList<>();
        this.puntuacion = 0;
    }

    public Receta(String nombre, int tiempoPreparacion, String dificultad, String tipoPlato) {
        this();
        this.nombre = nombre;
        this.tiempoPreparacion = tiempoPreparacion;
        this.dificultad = dificultad;
        this.tipoPlato = tipoPlato;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<IngredienteReceta> getIngredientes() {
        return ingredientes;
    }

    public void setIngredientes(List<IngredienteReceta> ingredientes) {
        this.ingredientes = ingredientes;
    }

    public void addIngrediente(Ingrediente ingrediente, double cantidad, String unidad) {
        this.ingredientes.add(new IngredienteReceta(ingrediente, cantidad, unidad));
    }

    public int getTiempoPreparacion() {
        return tiempoPreparacion;
    }

    public void setTiempoPreparacion(int tiempoPreparacion) {
        this.tiempoPreparacion = tiempoPreparacion;
    }

    public String getDificultad() {
        return dificultad;
    }

    public void setDificultad(String dificultad) {
        this.dificultad = dificultad;
    }

    public String getTipoPlato() {
        return tipoPlato;
    }

    public void setTipoPlato(String tipoPlato) {
        this.tipoPlato = tipoPlato;
    }

    public List<String> getDietasCompatibles() {
        return dietasCompatibles;
    }

    public void setDietasCompatibles(List<String> dietasCompatibles) {
        this.dietasCompatibles = dietasCompatibles;
    }

    public void addDietaCompatible(String dieta) {
        if (!this.dietasCompatibles.contains(dieta)) {
            this.dietasCompatibles.add(dieta);
        }
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public List<String> getRazonesRecomendacion() {
        return razonesRecomendacion;
    }

    public void setRazonesRecomendacion(List<String> razonesRecomendacion) {
        this.razonesRecomendacion = razonesRecomendacion;
    }

    public void addRazonRecomendacion(String razon) {
        this.razonesRecomendacion.add(razon);
    }

    public int getIngredientesDisponibles(Usuario usuario) {
        int count = 0;
        for (IngredienteReceta ir : this.ingredientes) {
            if (usuario.getIngredientesDisponibles().contains(ir.getIngrediente().getNombre())) {
                count++;
            }
        }
        return count;
    }

    public double getPorcentajeIngredientesDisponibles(Usuario usuario) {
        if (this.ingredientes.isEmpty()) return 0;
        return (double) getIngredientesDisponibles(usuario) / this.ingredientes.size() * 100;
    }

    @Override
    public String toString() {
        return "Receta{" +
                "nombre='" + nombre + '\'' +
                ", tiempoPreparacion=" + tiempoPreparacion +
                ", dificultad='" + dificultad + '\'' +
                ", tipoPlato='" + tipoPlato + '\'' +
                ", puntuacion=" + puntuacion +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receta receta = (Receta) o;
        return nombre.equals(receta.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
