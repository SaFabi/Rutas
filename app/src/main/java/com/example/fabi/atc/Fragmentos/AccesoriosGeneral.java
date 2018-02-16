package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
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


public class AccesoriosGeneral extends Fragment  implements SearchView.OnQueryTextListener,Basic, Response.Listener<JSONArray>, Response.ErrorListener {
    //FRAGMENTO PROBADO,ESTA DENTRO DEL CONTENEDOR DEL INVENTARIO GENERAL

    //VARIABLES
    private static final String ARG_POSITION = "POSITION";
    private int mPosition;
    String url;
    List<ModeloInventarioGeneral>lista;
    //CONTROLES
    ListView listView;
    private ProgressDialog progressDialog;
    //ADAPTERS
    ProductosAdapter adapter;



    private OnFragmentInteractionListener mListener;

    public AccesoriosGeneral() {
    }


    public static AccesoriosGeneral newInstance(int position) {
        AccesoriosGeneral fragment = new AccesoriosGeneral();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_accesorios_general, container, false);

        //ASIGNACION DE VARIABLES CON SUS CONTROLES
        listView= (ListView)view.findViewById(R.id.accesoriosGeneral);

        //PARA MOSTAR EL MENU DE OPCIONES
        setHasOptionsMenu(true);

        //INICIALIZAR EL PROGRESSDIALOG
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select distinct ta.nombre,mo.nombre,a.precio " +
                "from marca ma,modelo mo,articulo a,punto_venta pv,cantidad ca,tipo_articulo ta,colocacion co,puntoventa_colocacion pvc "+
                "where a.modelo_id=mo.id " +
                "and mo.marca_id=ma.id " +
                "and ca.puntoVenta_id=pv.id " +
                "and ca.articulo_id =a.id " +
                "and a.tipoArticulo_id=ta.id " +
                "and pvc.colocacion_id=co.id " +
                "and co.tipo!='Local' " +
                "and ta.nombre!='Télefono' " +
                "and ta.nombre!='Chip' "+
                "and ca.valor>0 "+
                "order by ta.nombre asc;";
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
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    //INFLA EL MENU
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

    //LA RESPUESTA DE LA CONSULTA
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        // Toast.makeText(getContext(),"Chips   "+ url, Toast.LENGTH_SHORT).show();
        lista = ModeloInventarioGeneral.listaProductos(response);
        adapter = new ProductosAdapter(lista,getContext());
        listView.setAdapter(adapter);

    }

    //PARFA LA IMPLEMENTACION DE LAS BUSQUEDAS
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
