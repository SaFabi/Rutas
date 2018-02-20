package com.example.fabi.atc.Clases;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.res.TypedArray;
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

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/12/2017.
 */

public class rutasLib implements  Basic {

    //URL de la ubicacion de las imagenes en el servidor
    public static String URL = "http://192.168.1.91/CatalogoATC/img/";


    //Declaracion de Variables
    private static ReportesAdapter adapter;
    private static ProgressDialog progressDialog;

    //Metodo para llenar un ViewPager con los titulos y los fragmentos
    public static CatalogoAdapter llenarViewPager(FragmentManager fragmentManager, List<Fragment> fragments, List<String> titulos) {
        CatalogoAdapter adapter = new CatalogoAdapter(fragmentManager);
        adapter.agregarFragmentos(fragments, titulos);
        return adapter;
    }

    public String generarFolio(String folio, Context context){
        String nuevoFolio="";
        int position=0;
        for (int i=0; i<folio.length();i++){
           if (folio.charAt(i) == '/'){
               Toast.makeText(context,String.valueOf(i), Toast.LENGTH_SHORT).show();

           }

        }

        return nuevoFolio;
    }
}