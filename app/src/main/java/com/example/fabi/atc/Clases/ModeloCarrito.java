package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 20/02/2018.
 */

public class ModeloCarrito {
    int OrdenDescID;
    String marca,modelo,precio,cantidad;

    public ModeloCarrito(int ordenDescID, String marca, String modelo, String precio, String cantidad) {
        OrdenDescID = ordenDescID;
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.cantidad = cantidad;
    }

    public int getOrdenDescID() {
        return OrdenDescID;
    }

    public void setOrdenDescID(int ordenDescID) {
        OrdenDescID = ordenDescID;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getPrecio() {
        return precio;
    }

    public void setPrecio(String precio) {
        this.precio = precio;
    }

    public String getCantidad() {
        return cantidad;
    }

    public void setCantidad(String cantidad) {
        this.cantidad = cantidad;
    }

    public static List<ModeloClientes> sacarListaCarrito(JSONArray array)
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
