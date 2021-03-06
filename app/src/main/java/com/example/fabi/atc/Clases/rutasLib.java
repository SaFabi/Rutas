package com.example.fabi.atc.Clases;


import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import java.util.List;

/**
 * Created by Fabi on 22/12/2017.
 */

public class rutasLib implements  Basic {

    //CLASE QUE CONTIENE LOS METODOS NECESARIOS A IMPLEMENTAR
    //URL de la ubicacion de las imagenes en el servidor
    public static String URL = "http://192.168.1.91/CatalogoATC/img/";

    //Declaracion de Variables

    //Metodo para llenar un ViewPager con los titulos y los fragmentos
    public static CatalogoAdapter llenarViewPager(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titulos) {
        CatalogoAdapter adapter = new CatalogoAdapter(fragmentManager);
        adapter.agregarFragmentos(fragments, titulos);
        return adapter;
    }

    //METODO QUE VERIFICA SI EXISTE EL TOKEN DEL USUARIO DE RUTA
    public void verificarTokenUsuario(final int rutaID, final Context context){
        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(context);
        String consulta = "SELECT id FROM token_puntoventa WHERE puntoventa_id="+rutaID;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url= SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //VERIFICA SI NO EXISTE EL TOKEN
                if (response.length() == 0){
                    final String token = FirebaseInstanceId.getInstance().getToken();
                    //Inicia la peticion
                    RequestQueue queueInsertar = Volley.newRequestQueue(context);
                    String consultaInsertar = "INSERT INTO token_puntoventa(token,puntoventa_id)VALUES('"+token+"',"+rutaID+");";
                    consultaInsertar = consultaInsertar.replace(" ", "%20");
                    String cadenaInsertar = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaInsertar;
                    String urlInsertar= SERVER + RUTA + "consultaGeneral.php" + cadenaInsertar;
                    Log.i("info", urlInsertar);
                    JsonArrayRequest jsonArrayRequestInsertar = new JsonArrayRequest(Request.Method.GET, urlInsertar, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queueInsertar.add(jsonArrayRequestInsertar);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }



    //METODO QUE VERIFICA SI EXISTE EL TOKEN DEL CLIENTE
    public void verificarTokenCliente(final int clienteID, final Context context){
        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(context);
        String consulta = "SELECT id FROM token_cliente WHERE claveCliente_id="+clienteID;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url= SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //VERIFICA SI NO EXISTE EL TOKEN
                if (response.length() == 0){
                    final String token = FirebaseInstanceId.getInstance().getToken();
                    //Inicia la peticion
                    RequestQueue queueInsertar = Volley.newRequestQueue(context);
                    String consultaInsertar = "INSERT INTO token_cliente(token,claveCliente_id)VALUES('"+token+"',"+clienteID+");";
                    consultaInsertar = consultaInsertar.replace(" ", "%20");
                    String cadenaInsertar = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaInsertar;
                    String urlInsertar= SERVER + RUTA + "consultaGeneral.php" + cadenaInsertar;
                    Log.i("info", urlInsertar);
                    JsonArrayRequest jsonArrayRequestInsertar = new JsonArrayRequest(Request.Method.GET, urlInsertar, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queueInsertar.add(jsonArrayRequestInsertar);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(jsonArrayRequest);
    }

    public String sacarPuntoVenta(String cadena) {
        String ruta = "";
        if (cadena.length() > 2) {
            for (int i = 0; i < cadena.length() -1; i++) {
                ruta += String.valueOf(cadena.charAt(i));
            }
        }else{
            ruta = cadena;
        }
        return ruta;
    }
}