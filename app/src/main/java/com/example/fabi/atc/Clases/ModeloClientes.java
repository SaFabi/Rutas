package com.example.fabi.atc.Clases;

/**
 * Created by Fabi on 02/01/2018.
 */

public class ModeloClientes {
    String nombre, ciudad, telefono,claveR;

    public ModeloClientes(String nombre, String ciudad, String telefono, String claveR) {
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.telefono = telefono;
        this.claveR = claveR;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getClaveR() {
        return claveR;
    }

    public void setClaveR(String claveR) {
        this.claveR = claveR;
    }
}
