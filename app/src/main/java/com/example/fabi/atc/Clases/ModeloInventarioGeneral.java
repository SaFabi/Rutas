package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ModeloInventarioGeneral {
    String marcaIG, modeloIG, precioIG;

    public ModeloInventarioGeneral(String marca, String modelo, String precio) {
        this.marcaIG = marca;
        this.modeloIG = modelo;
        this.precioIG = precio;
    }


    public String getMarcaIG() {
        return marcaIG;
    }

    public void setMarcaIG(String marcaIG) {
        this.marcaIG = marcaIG;
    }

    public String getModeloIG() {
        return modeloIG;
    }

    public void setModeloIG(String modeloIG) {
        this.modeloIG = modeloIG;
    }

    public String getPrecioIG() {
        return precioIG;
    }

    public void setPrecioIG(String precioIG) {
        this.precioIG = precioIG;
    }

    public static List<ModeloInventarioGeneral> listaProductos(JSONArray array)
    {
        List<ModeloInventarioGeneral> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
               ModeloInventarioGeneral cliente = new ModeloInventarioGeneral(jsonObject.getString("0"), jsonObject.getString("1"), jsonObject.getString("2"));
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
