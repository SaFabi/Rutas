package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 16/01/2018.
 */

public class ModeloCreditos {
    int ordenID;
    int montoTotal;
    String folio;
    String Fecha;
    String claveCliente;
    int ClienteID;

    public ModeloCreditos(int ordenID ,String folio, String fecha, String claveCliente,int montoTotal, int ClienteID) {
        this.montoTotal = montoTotal;
        this.folio = folio;
        Fecha = fecha;
        this.claveCliente = claveCliente;
        this.ordenID = ordenID;
        this.ClienteID = ClienteID;
    }

    public int getMontoTotal() {
        return montoTotal;
    }

    public void setMontoTotal(int montoTotal) {
        this.montoTotal = montoTotal;
    }

    public String getFolio() {
        return folio;
    }

    public void setFolio(String folio) {
        this.folio = folio;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public String getClaveCliente() {
        return claveCliente;
    }

    public void setClaveCliente(String claveCliente) {
        this.claveCliente = claveCliente;
    }


    public int getOrdenID() {
        return ordenID;
    }

    public void setOrdenID(int ordenID) {
        this.ordenID = ordenID;
    }

    public int getClienteID() {
        return ClienteID;
    }

    public void setClienteID(int clienteID) {
        ClienteID = clienteID;
    }

    public static List<ModeloCreditos> sacarListaClientes(JSONArray array)
    {
        List<ModeloCreditos> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                ModeloCreditos cliente = new ModeloCreditos(Integer.parseInt(jsonObject.getString("0")), jsonObject.getString("1"), jsonObject.getString("2"),
                        jsonObject.getString("3"), (Integer.parseInt(jsonObject.getString("4"))),jsonObject.getInt("5"));
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
