package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.InventarioPersonalAdapter;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

public class Inicio extends Fragment  implements SwipeRefreshLayout.OnRefreshListener,SearchView.OnQueryTextListener, Basic, Response.Listener<JSONArray>, Response.ErrorListener {

    //Fragmento para los chips

    private static final String ARG_POSITION= "POSITION";
    private int mPosition;
    String url;
    ListView listView;
    rutasLib rutasObj;
    private ProgressDialog progressDialog;
    List<ModeloInventarioPersonal> lista;
    InventarioPersonalAdapter inventarioPersonalAdapter;
    int cantidadID;
    SwipeRefreshLayout contenedorClientesA;

    private OnFragmentInteractionListener mListener;

    public Inicio() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static Inicio newInstance(int position) {
        Inicio fragment = new Inicio();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition= getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_inicio, container, false);
        setHasOptionsMenu(true);
        listView= (ListView)view.findViewById(R.id.lvInicio);

        contenedorClientesA = (SwipeRefreshLayout)view.findViewById(R.id.contenedorCatalogoChips);
        contenedorClientesA.setOnRefreshListener(this);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                cantidadID = (int)inventarioPersonalAdapter.getItemId(i);
                Toast.makeText(getContext(),String.valueOf(cantidadID),Toast.LENGTH_SHORT).show();
            }
        });
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select ca.id,ma.nombre,mo.nombre,a.precio,ca.valor " +
                "from marca ma, modelo mo, articulo a, punto_venta pv, cantidad ca, tipo_articulo ta " +
                "where a.modelo_id = mo.id " +
                "and mo.marca_id = ma.id " +
                "and ca.puntoVenta_id = pv.id " +
                "and ca.articulo_id = a.id " +
                "and a.tipoArticulo_id = ta.id " +
                "and ca.valor > 0 "+
                "and pv.id = "+usuarioID+
                " and ta.nombre = 'Chip' "+
                "order by ma.nombre asc ";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url= SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Agrega y ejecuta la cola
        queue.add(request);

        //Parte que recarga el listview solamente si llega al tope
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int top = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                contenedorClientesA.setEnabled(firstVisibleItem == 0 && top >= 0);
            }
        });

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    //Infla el menu para el carrito y el buscador
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_buscador,menu);
        MenuItem buscador = menu.findItem(R.id.buscador2);
        SearchView searchView = (SearchView) MenuItemCompat.getActionView(buscador);
        searchView.setOnQueryTextListener(this);
        MenuItemCompat.setOnActionExpandListener(buscador, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                return true;
            }

            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                inventarioPersonalAdapter.setFilter(lista);
                return true;
            }
        });

    }


    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        lista = ModeloInventarioPersonal.sacarListaproductos(response);
       // Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();
        inventarioPersonalAdapter = new InventarioPersonalAdapter(getContext(),lista,"Chips");
        listView.setAdapter(inventarioPersonalAdapter);

    }

    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            List<ModeloInventarioPersonal>listafiltrada =filter(lista,s);
            inventarioPersonalAdapter.setFilter(listafiltrada);
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }
    private List<ModeloInventarioPersonal>filter(List<ModeloInventarioPersonal>notas,String texto){
        List<ModeloInventarioPersonal>listaFiltrada= new ArrayList<>();
        try {
            texto=texto.toLowerCase();
            for (ModeloInventarioPersonal nota:notas){
                String nota2 = nota.getMarca().toLowerCase();
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

    @Override
    public void onRefresh()

    {
        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select ca.id,ma.nombre,mo.nombre,a.precio,ca.valor " +
                "from marca ma, modelo mo, articulo a, punto_venta pv, cantidad ca, tipo_articulo ta " +
                "where a.modelo_id = mo.id " +
                "and mo.marca_id = ma.id " +
                "and ca.puntoVenta_id = pv.id " +
                "and ca.articulo_id = a.id " +
                "and a.tipoArticulo_id = ta.id " +
                "and ca.valor > 0 " +
                "and pv.id = " + usuarioID +
                " and ta.nombre = 'Chip' " +
                "order by ma.nombre asc ";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                lista = ModeloInventarioPersonal.sacarListaproductos(response);
                // Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();
                inventarioPersonalAdapter = new InventarioPersonalAdapter(getContext(),lista,"Chips");
                listView.setAdapter(inventarioPersonalAdapter);
                contenedorClientesA.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();

            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
