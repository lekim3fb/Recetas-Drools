package sbc.example;

import java.io.Serializable;

/**
 * Clase para almacenar el resultado de una recomendación
 */
public class Recomendacion implements Serializable, Comparable<Recomendacion> {
    private Receta receta;
    private double puntuacion;
    private String estado; // recomendada, alternativa, descartada
    private String motivo; // razón por la cual fue descartada o recomendada

    public Recomendacion() {
    }

    public Recomendacion(Receta receta, double puntuacion, String estado, String motivo) {
        this.receta = receta;
        this.puntuacion = puntuacion;
        this.estado = estado;
        this.motivo = motivo;
    }

    // Getters y Setters
    public Receta getReceta() {
        return receta;
    }

    public void setReceta(Receta receta) {
        this.receta = receta;
    }

    public double getPuntuacion() {
        return puntuacion;
    }

    public void setPuntuacion(double puntuacion) {
        this.puntuacion = puntuacion;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getMotivo() {
        return motivo;
    }

    public void setMotivo(String motivo) {
        this.motivo = motivo;
    }

    @Override
    public int compareTo(Recomendacion otra) {
        // Ordenar de mayor a menor puntuación
        return Double.compare(otra.puntuacion, this.puntuacion);
    }

    @Override
    public String toString() {
        return "Recomendacion{" +
                "receta='" + receta.getNombre() + '\'' +
                ", puntuacion=" + puntuacion +
                ", estado='" + estado + '\'' +
                ", motivo='" + motivo + '\'' +
                '}';
    }
}
