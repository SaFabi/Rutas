package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
public class ModeloClientes {
    int id;
    String nombre, ciudad, telefono,claveR;

    public ModeloClientes(int id, String nombre, String ciudad, String telefono, String claveR) {
        this.id = id;
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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public static List<ModeloClientes> sacarListaClientes(JSONArray array)
    {
        List<ModeloClientes> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                ModeloClientes cliente = new ModeloClientes(Integer.parseInt(jsonObject.getString("0")), jsonObject.getString("1"), jsonObject.getString("2"),
                        jsonObject.getString("3"), jsonObject.getString("4"));
                lista.add(cliente);
            }
        }
        catch (JSONException e)
        {
            lista = null;
        }

        return lista;
    }
}
