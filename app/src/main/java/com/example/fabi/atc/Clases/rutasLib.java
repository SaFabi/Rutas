package com.example.fabi.atc.Clases;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.util.Log;
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
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.Contenedor;
import com.example.fabi.atc.Fragmentos.Inicio;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/12/2017.
 */

public class rutasLib implements  Basic {

    //URL de la ubicacion de las imagenes en el servidor
      public static  String URL = "http://192.168.1.91/CatalogoATC/img/";

    //Ruta para las consultas
    public static String Consulta = "http://192.168.1.91/CatalogoATC/";

    //Declaracion de Variables
     private static ClientesAdapter adapter ;
     private static  ProgressDialog progressDialog;


    //Metodo para llenar un ViewPager con los titulos y los fragmentos
    public  static CatalogoAdapter llenarViewPager(FragmentManager fragmentManager, List<Fragment> fragments,List<String> titulos) {
        CatalogoAdapter adapter = new CatalogoAdapter(fragmentManager);
            adapter.agregarFragmentos(fragments,titulos);
        return adapter;
    }
    //Consulta para regresar los clientes que estan activos de una ruta en especifico
    public static ClientesAdapter ConsultaClientesActivos(final Context context) {

        //Inicializa el progres dialog
        progressDialog = new ProgressDialog(context);
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(context);
         String consulta = "select cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
                "from cliente cl, clave_cliente cc, punto_venta pv " +
                "where cc.puntoVenta_id = pv.id " +
                "and cc.cliente_id = cl.id " +
                " and pv.id="+usuarioID+" and cc.activo = true";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
         final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                 //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
               adapter = new ClientesAdapter(response,context);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(context, "Error en el WebService", Toast.LENGTH_SHORT).show();
                Toast.makeText(context,  "Activos   "+url, Toast.LENGTH_SHORT).show();

            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);
        return adapter;
    }

}