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

import org.json.JSONArray;
import org.json.JSONException;

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
}