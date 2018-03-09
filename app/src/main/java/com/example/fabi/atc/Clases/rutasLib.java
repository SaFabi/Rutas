package com.example.fabi.atc.Clases;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Adapters.ReportesAdapter;
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.Contenedor;
import com.example.fabi.atc.Fragmentos.Inicio;
import com.example.fabi.atc.Fragmentos.Reportes;
import com.example.fabi.atc.R;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String generarFolio(String folio, Context context, String puntoVenta){
        //PARA OBTENER LA FECHA ACTUAL
        final Calendar c = Calendar.getInstance();
        int ano=c.get(Calendar.YEAR);

        String nuevoFolio="";
        String parte1;
        String year;
        String []partes =folio.split("/");
        String parteNumerica;
        int tamañoCadena;

        //VERIFICA QUE EL TAMAÑO DEL FOLIO SOLO SE DIVIDA EN DOS PARTES
        if (partes.length == 2){
            parte1 = partes[0];
            year = partes[1];
        }else{
            parte1 = partes[1];
            year = partes[2];
        }

        //VERIFICA QUE EL AÑO ACTUAL SEA EL MISMO AL DEL FOLIO
        if (year == String.valueOf(ano)){

            //GENERA LA PARTE NUMERICA DEL FOLIO
            parteNumerica = String.valueOf(Integer.parseInt(parte1)+1);
            tamañoCadena = parteNumerica.length();

            //GENERA LA PARTE DE LOS CEROS

                    for (int i=0; i<6;i++){
                        if (tamañoCadena < 6) {
                            parteNumerica = "0"+parteNumerica;
                        }
                        tamañoCadena = parteNumerica.length();

                    }
            nuevoFolio = puntoVenta+"/"+parteNumerica+"/"+year;
        }else{
            parteNumerica =String.valueOf(Integer.parseInt(parte1)+1);
            tamañoCadena = parteNumerica.length();

            //GENERA LA PARTE DE LOS CEROS

            for (int i=0; i<6;i++){
                if (tamañoCadena < 6) {
                    parteNumerica = "0"+parteNumerica;
                }
                tamañoCadena = parteNumerica.length();

            }


            nuevoFolio = puntoVenta+"/"+parteNumerica+"/"+ano;
        }

        return nuevoFolio;
    }

    //METODO QUE VERIFICA SI EXISTE EL TOKEN DEL USUARIO DE RUTA
    public void verificarTokenUsuario(final int rutaID, final Context context){
        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(context);
        String consulta = "SELECT id FROM token_usuarios WHERE usuario_id="+rutaID;
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
                    String consultaInsertar = "INSERT INTO token_usuario(token,usuario_id)VALUES('"+token+"',"+rutaID+");";
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
        String consulta = "SELECT id FROM token_clientes WHERE claveCliente_id="+clienteID;
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
                    String consultaInsertar = "INSERT INTO token_clientes(token,claveCliente_id)VALUES('"+token+"',"+clienteID+");";
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