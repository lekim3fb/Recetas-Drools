package sbc.example;

import java.io.Serializable;

/**
 * Clase que representa un ingrediente dentro de una receta con cantidad y unidad
 */
public class IngredienteReceta implements Serializable {
    private Ingrediente ingrediente;
    private double cantidad;
    private String unidad; // gramos, ml, unidades, cucharadas, etc.

    public IngredienteReceta() {
    }

    public IngredienteReceta(Ingrediente ingrediente, double cantidad, String unidad) {
        this.ingrediente = ingrediente;
        this.cantidad = cantidad;
        this.unidad = unidad;
    }

    // Getters y Setters
    public Ingrediente getIngrediente() {
        return ingrediente;
    }

    public void setIngrediente(Ingrediente ingrediente) {
        this.ingrediente = ingrediente;
    }

    public double getCantidad() {
        return cantidad;
    }

    public void setCantidad(double cantidad) {
        this.cantidad = cantidad;
    }

    public String getUnidad() {
        return unidad;
    }

    public void setUnidad(String unidad) {
        this.unidad = unidad;
    }

    @Override
    public String toString() {
        return "IngredienteReceta{" +
                "ingrediente=" + ingrediente.getNombre() +
                ", cantidad=" + cantidad +
                ", unidad='" + unidad + '\'' +
                '}';
    }
}
