package com.jonasgarcia.sys_gestion_incidentes.Indicentes;

import java.util.Date;

public class Incidente {
    private String tipo;
    private String descripcion;
    private int imagen;
    private String fechaIngreso;
    private String estado;
    private String nota;

    public Incidente(String tipo, String descripcion, int imagen, String fechaIngreso, String estado, String nota) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
        this.nota = nota;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public String getFechaString() {
        return fechaIngreso;
    }

    public void setFechaString(String fechaString) {
        this.fechaIngreso = fechaString;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
