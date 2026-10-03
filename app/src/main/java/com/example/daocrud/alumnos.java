package com.example.daocrud;

public class alumnos {
    private String matricula;
    private String nombre_completo;
    private String correo;
    private String telefono;
    private String uuid;

    public alumnos() {
    }

    public alumnos(String uuid, String nombre_completo, String matricula, String correo, String telefono) {
        this.nombre_completo = nombre_completo;
        this.matricula = matricula;
        this.correo = correo;
        this.telefono = telefono;
        this.uuid = uuid;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getNombre_completo() {
        return nombre_completo;
    }

    public void setNombre_completo(String nombre_completo) {
        this.nombre_completo = nombre_completo;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public alumnos(String uuid) {
        this.uuid = uuid;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Override
    public String toString() {
        return "matricula=" + matricula +
                ", nombre_completo=" + nombre_completo +
                ", correo=" + correo +
                ", telefono=" + telefono;
    }
}