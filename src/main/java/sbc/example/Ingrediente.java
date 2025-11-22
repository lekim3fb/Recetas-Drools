package sbc.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que representa un ingrediente en el sistema
 */
public class Ingrediente implements Serializable {
    private String nombre;
    private String categoria; // vegetal, proteína, carbohidrato, especia, lácteo
    private List<String> alergias; // gluten, lactosa, frutos secos, etc.
    private double proteina; // gramos
    private double carbohidratos; // gramos
    private double grasas; // gramos
    private double calorias; // kcal por 100g
    private boolean disponible;

    public Ingrediente() {
        this.alergias = new ArrayList<>();
        this.disponible = true;
    }

    public Ingrediente(String nombre, String categoria) {
        this();
        this.nombre = nombre;
        this.categoria = categoria;
    }

    public Ingrediente(String nombre, String categoria, double proteina, double carbohidratos, double grasas, double calorias) {
        this();
        this.nombre = nombre;
        this.categoria = categoria;
        this.proteina = proteina;
        this.carbohidratos = carbohidratos;
        this.grasas = grasas;
        this.calorias = calorias;
    }

    // Getters y Setters
    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public List<String> getAlergias() {
        return alergias;
    }

    public void setAlergias(List<String> alergias) {
        this.alergias = alergias;
    }

    public void addAlergia(String alergia) {
        if (!this.alergias.contains(alergia)) {
            this.alergias.add(alergia);
        }
    }

    public double getProteina() {
        return proteina;
    }

    public void setProteina(double proteina) {
        this.proteina = proteina;
    }

    public double getCarbohidratos() {
        return carbohidratos;
    }

    public void setCarbohidratos(double carbohidratos) {
        this.carbohidratos = carbohidratos;
    }

    public double getGrasas() {
        return grasas;
    }

    public void setGrasas(double grasas) {
        this.grasas = grasas;
    }

    public double getCalorias() {
        return calorias;
    }

    public void setCalorias(double calorias) {
        this.calorias = calorias;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    @Override
    public String toString() {
        return "Ingrediente{" +
                "nombre='" + nombre + '\'' +
                ", categoria='" + categoria + '\'' +
                ", alergias=" + alergias +
                ", calorias=" + calorias +
                ", disponible=" + disponible +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingrediente that = (Ingrediente) o;
        return nombre.equals(that.nombre);
    }

    @Override
    public int hashCode() {
        return nombre.hashCode();
    }
}
