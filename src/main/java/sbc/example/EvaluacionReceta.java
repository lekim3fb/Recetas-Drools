package sbc.example;

import java.io.Serializable;

/**
 * Clase para evaluar la calidad de una receta usando decisiones
 * Se utiliza como puente entre Drools y el DMN
 */
public class EvaluacionReceta implements Serializable {
    private String nombreReceta;
    private double porcentajeIngredientes;
    private int tiempoMinutos;
    private String nivelDificultad;
    private String resultado; // "óptima", "buena", "aceptable", "rechazar"
    private int puntuacionFinal;

    public EvaluacionReceta() {
    }

    public EvaluacionReceta(String nombreReceta, double porcentajeIngredientes, 
                           int tiempoMinutos, String nivelDificultad) {
        this.nombreReceta = nombreReceta;
        this.porcentajeIngredientes = porcentajeIngredientes;
        this.tiempoMinutos = tiempoMinutos;
        this.nivelDificultad = nivelDificultad;
    }

    // Método para evaluar localmente si el DMN no está disponible
    public void evaluarLocalmente() {
        // Lógica de decisión basada en reglas
        if (porcentajeIngredientes >= 85 && tiempoMinutos <= 20) {
            resultado = "óptima";
            puntuacionFinal = 95;
        } else if (porcentajeIngredientes >= 70 && tiempoMinutos <= 30) {
            resultado = "buena";
            puntuacionFinal = 80;
        } else if (porcentajeIngredientes >= 50) {
            resultado = "aceptable";
            puntuacionFinal = 60;
        } else {
            resultado = "rechazar";
            puntuacionFinal = 0;
        }
    }

    // Getters y Setters
    public String getNombreReceta() {
        return nombreReceta;
    }

    public void setNombreReceta(String nombreReceta) {
        this.nombreReceta = nombreReceta;
    }

    public double getPorcentajeIngredientes() {
        return porcentajeIngredientes;
    }

    public void setPorcentajeIngredientes(double porcentajeIngredientes) {
        this.porcentajeIngredientes = porcentajeIngredientes;
    }

    public int getTiempoMinutos() {
        return tiempoMinutos;
    }

    public void setTiempoMinutos(int tiempoMinutos) {
        this.tiempoMinutos = tiempoMinutos;
    }

    public String getNivelDificultad() {
        return nivelDificultad;
    }

    public void setNivelDificultad(String nivelDificultad) {
        this.nivelDificultad = nivelDificultad;
    }

    public String getResultado() {
        return resultado;
    }

    public void setResultado(String resultado) {
        this.resultado = resultado;
    }

    public int getPuntuacionFinal() {
        return puntuacionFinal;
    }

    public void setPuntuacionFinal(int puntuacionFinal) {
        this.puntuacionFinal = puntuacionFinal;
    }

    @Override
    public String toString() {
        return "EvaluacionReceta{" +
                "nombreReceta='" + nombreReceta + '\'' +
                ", porcentajeIngredientes=" + porcentajeIngredientes +
                ", tiempoMinutos=" + tiempoMinutos +
                ", resultado='" + resultado + '\'' +
                ", puntuacionFinal=" + puntuacionFinal +
                '}';
    }
}
