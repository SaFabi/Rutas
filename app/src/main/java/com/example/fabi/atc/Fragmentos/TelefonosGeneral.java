package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloInventarioGeneral;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;


public class TelefonosGeneral extends Fragment implements SearchView.OnQueryTextListener,Basic, Response.Listener<JSONArray>, Response.ErrorListener {
    //FRAGMENTO PROBADO. ESTA DENTRO DE CONTENEDOR DE INVENTARIO GENERAL.MUESTRA LOS TELEFONOS

    //VARIABLES
    String url;
    List<ModeloInventarioGeneral>lista;

    //CONTROLES
    ListView listView;
    private ProgressDialog progressDialog;

    //ADAPTERS
    ProductosAdapter adapter;


    public TelefonosGeneral() {

    }

    public static TelefonosGeneral newInstance(int position) {
        TelefonosGeneral fragment = new TelefonosGeneral();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_telefonos_general, container, false);
        //MUESTRA EL MENU DE OPCIONES EN LA TOOLBAR
        setHasOptionsMenu(true);

        listView= (ListView)view.findViewById(R.id.telefonosGeneral);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select distinct ma.nombre,mo.nombre,a.precio " +
                "from marca ma,modelo mo,articulo a,punto_venta pv,cantidad ca,tipo_articulo ta,colocacion co,puntoventa_colocacion pvc "+
                "where a.modelo_id=mo.id " +
                "and mo.marca_id=ma.id " +
                "and ca.puntoVenta_id=pv.id " +
                "and ca.articulo_id =a.id " +
                "and a.tipoArticulo_id=ta.id " +
                "and pvc.colocacion_id=co.id " +
                "and co.tipo!='Local' " +
                "and ta.nombre='Teléfono' " +
                "and ca.valor>0 "+
                "order by ma.nombre asc ";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url= SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Agrega y ejecuta la cola
        queue.add(request);

        return view;
    }
    //INFLA EL MENU DE OPCIONES
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem carrito = menu.findItem(R.id.carrito);
        carrito.setVisible(false);
        MenuItem buscador = menu.findItem(R.id.buscador2);
        buscador.setVisible(true);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscador);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(buscador, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                adapter.setFilter(lista);
                return true;
            }
        });
    }
    //RESPUESTA DE
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();

    }
    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        lista =ModeloInventarioGeneral.listaProductos(response);
        // Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();
        adapter = new ProductosAdapter(lista,getContext());
        listView.setAdapter(adapter);

    }
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            List<ModeloInventarioGeneral>listafiltrada =filter(lista,s);
            adapter.setFilter(listafiltrada);
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }
    private List<ModeloInventarioGeneral>filter(List<ModeloInventarioGeneral>notas,String texto){
        List<ModeloInventarioGeneral>listaFiltrada= new ArrayList<>();
        try {
            texto=texto.toLowerCase();
            for (ModeloInventarioGeneral nota:notas){
                String nota2 = nota.getMarcaIG().toLowerCase();
                //Para saber si el texto se encuentra dentro de la nota
                if (nota2.contains(texto)){
                    listaFiltrada.add(nota);
                }
            }

        }catch (Exception e){
            e.printStackTrace();
        }
        return listaFiltrada;
    }

}
