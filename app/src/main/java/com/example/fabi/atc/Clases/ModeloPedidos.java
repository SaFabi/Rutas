package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 23/01/2018.
 */

public class ModeloPedidos {
    int ordenID;
    String folio, Cliente, fecha, total;

    public ModeloPedidos(int ordenID, String folio, String cliente, String fecha, String total) {
        this.ordenID = ordenID;
        this.folio = folio;
        Cliente = cliente;
        this.fecha = fecha;
        this.total = total;
    }

    public int getOrdenID() {
        return ordenID;
    }

    public void setOrdenID(int ordenID) {
        this.ordenID = ordenID;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public static List<ModeloPedidos> sacarListaClientes(JSONArray array)
    {
        List<ModeloPedidos> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                ModeloPedidos cliente = new ModeloPedidos(Integer.parseInt(jsonObject.getString("0")), jsonObject.getString("1"), jsonObject.getString("2"),
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


