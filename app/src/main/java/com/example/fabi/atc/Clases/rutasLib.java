package com.example.fabi.atc.Clases;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.Inicio;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/12/2017.
 */

public class rutasLib {

    //URL de la ubicacion de las imagenes en el servidor
      public static  String URL = "http://192.168.1.91/CatalogoATC/img/";

    //Ruta para las consultas
    public static String Consulta = "http://192.168.1.91/CatalogoATC/";

    //Metodo para llenar un ViewPager con los titulos y los fragmentos
    public  static CatalogoAdapter llenarViewPager(FragmentManager fragmentManager, List<Fragment> fragments,List<String> titulos) {
        CatalogoAdapter adapter = new CatalogoAdapter(fragmentManager);
            adapter.agregarFragmentos(fragments,titulos);
        return adapter;
    }
/*
    //Metodo para ejecutar la consulta  con Volley y poner el resultado en un listView
    public void EnviarRecibirDatos(final Context context, String URL, final ArrayList<Modelo>lista){
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length() > 0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        Log.i("SizeJson",""+ja.length());
                        CargarListView(ja,context,lista);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",""+error.getMessage());
            }
        });
        queue.add(stringRequest);
    }
    /*
    //Metodo para generar adapters con el resultado de las consultas
    public ProductosAdapter CargarListView(JSONArray ja, Context context, ArrayList<Modelo>lista ){
        //ArrayList<Modelo> lista = new ArrayList<>();

        for (int i = 0; i<ja.length();i+=ja.length()){
            try {
                lista.add(new Modelo(ja.getString(i),ja.getString(i+1),ja.getString(i+2)));

            }catch(JSONException e){
                e.printStackTrace();
            }

        }
        ProductosAdapter adapter= new ProductosAdapter(lista,context);


        return adapter;
    }
    */
}
