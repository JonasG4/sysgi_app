package com.jonasgarcia.sys_gestion_incidentes.models;

public class Incidente {
    private int id_incidente;
    private String tipo;
    private String descripcion;
    private String imagen;
    private String fechaIngreso;
    private String estado;
    private String nota;
    private int resolve_by;


    private int id_usuario;

    public Incidente(int id_incidente, String tipo, String descripcion, String imagen, String fechaIngreso, String estado, String nota, int id_usuario) {
        this.id_incidente = id_incidente;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.imagen = imagen;
        this.fechaIngreso = fechaIngreso;
        this.estado = estado;
        this.nota = nota;
        this.id_usuario = id_usuario;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public int getId_incidente() {
        return id_incidente;
    }

    public void setId_incidente(int id_incidente) {
        this.id_incidente = id_incidente;
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

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(String fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
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

    public int getResolve_by() {
        return resolve_by;
    }

    public void setResolve_by(int resolve_by) {
        this.resolve_by = resolve_by;
    }
}
