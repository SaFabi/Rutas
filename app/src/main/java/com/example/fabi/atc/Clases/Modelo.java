package com.example.fabi.atc.Clases;

/**
 * Created by Fabi on 20/12/2017.
 */

public class Modelo {
    String correo, pass, imagen;

    public Modelo(String correo, String pass, String imagen) {
        this.correo = correo;
        this.pass = pass;
        this.imagen = imagen;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }
}
