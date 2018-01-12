package com.example.fabi.atc.Clases;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 20/12/2017.
 */

public class Modelo {
    int ciudadID;
    String ciudad;

    public Modelo(int ciudadID, String ciudad) {
        this.ciudadID = ciudadID;
        this.ciudad = ciudad;
    }

    public int getCiudadID() {
        return ciudadID;
    }

    public void setCiudadID(int ciudadID) {
        this.ciudadID = ciudadID;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public static List<Modelo>ListaSpinner(JSONArray array)
    {
        List<Modelo> lista = new ArrayList<>();
        try
        {
            for (int i = 0; i < array.length(); i++)
            {
                JSONObject jsonObject = array.getJSONObject(i);
                Modelo cliente = new Modelo(Integer.parseInt(jsonObject.getString("0")), jsonObject.getString("1"));
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
