package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/01/2018.
 */

public class ModeloInventarioPersonal {
    int CantidadID;
    String marca, modelo, precio, cantidad;

    public ModeloInventarioPersonal(int CantidadID,String marca, String modelo, String precio, String cantidad) {
        this.marca = marca;
        this.modelo = modelo;
        this.precio = precio;
        this.cantidad = cantidad;
        this.CantidadID = CantidadID;
    }


    public int getCantidadID() {
        return CantidadID;
    }

    public void setCantidadID(int cantidadID) {
        CantidadID = cantidadID;
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

    public static List<ModeloInventarioPersonal> sacarListaproductos(JSONArray array)
    {
        List<ModeloInventarioPersonal> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                ModeloInventarioPersonal cliente = new ModeloInventarioPersonal(Integer.parseInt(jsonObject.getString("0")), jsonObject.getString("1"), jsonObject.getString("2"),
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
