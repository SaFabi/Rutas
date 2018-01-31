package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ClientesActivos extends Fragment implements SearchView.OnQueryTextListener,SwipeRefreshLayout.OnRefreshListener,Basic{

    private static final String ARG_POSITION = "param1";
    String url;
    ListView listView;
    rutasLib rutasObj;
    int clienteID;
    List<ModeloClientes> lista;

    private ProgressDialog progressDialog;
    AdapterClientes adapter;
    SwipeRefreshLayout contenedorClientesA;



    // TODO: Rename and change types of parameters
    private int mPosition;

    private OnFragmentInteractionListener mListener;

    public ClientesActivos() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ClientesActivos newInstance(int position) {
        ClientesActivos fragment = new ClientesActivos();
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
        //Crea la vista
        View view = inflater.inflate(R.layout.fragment_clientes_activos, container, false);
        setHasOptionsMenu(true);

        //Se declaran los elementos con su id
        listView = (ListView) view.findViewById(R.id.clientesActivos);
        contenedorClientesA = (SwipeRefreshLayout)view.findViewById(R.id.contenedorClientesActivos);
        contenedorClientesA.setOnRefreshListener(this);
        //Inicializa el progres dialog
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select cc.id,cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
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
                lista = ModeloClientes.sacarListaClientes(response);
                //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                adapter = new AdapterClientes(getContext(),lista);
                listView.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               //Hace la consulta para sacar el id del cliente que se va a eliminar

               clienteID = (int)adapter.getItemId(i);
                //Toast.makeText(getContext(), "ID: " + String.valueOf(clienteID), Toast.LENGTH_SHORT).show();


                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setIcon(R.drawable.cancelar);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Desea eliminar este elemento?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    //
                    public void onClick(DialogInterface dialogo1, int id) {

                        //Inicializa el progres dialog
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("En Proceso");
                        progressDialog.setMessage("Un momento...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        //Inicia la peticion
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String consulta = "update clave_cliente set activo=0 where id="+clienteID;
                        consulta = consulta.replace(" ", "%20");
                        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                        final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                        Log.i("info", url);

                        //Hace la petición String
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //Inicia la peticion
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                String consulta = "select cc.id,cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
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
                                        lista = ModeloClientes.sacarListaClientes(response);
                                        //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                                        adapter = new AdapterClientes(getContext(),lista);
                                        listView.setAdapter(adapter);

                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        progressDialog.hide();
                                        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

                                    }
                                });

                                //Agrega y ejecuta la cola
                                queue.add(request);
                                progressDialog.hide();
                                Toast.makeText(getContext(),"Se elimino correctamente",Toast.LENGTH_SHORT).show();

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

                            }
                        });

                        //Agrega y ejecuta la cola
                        queue.add(request);

                        //PARA PASAR A OTRO FRAGMENTO
                        /*Fragment nuevoFragmento = new ClientesInactivos();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_main, nuevoFragmento);
                        transaction.addToBackStack(null);

                        // Commit a la transacción
                        transaction.commit();
                        */

                    }
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
            }
        });
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    //Infla el menu para el carrito y el buscador
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
       // inflater.inflate(R.menu.menu_buscador,menu);
        MenuItem buscador = menu.findItem(R.id.buscador2);
        MenuItem carrito = menu.findItem(R.id.carrito);
        buscador.setVisible(true);
        carrito.setVisible(false);
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


    @Override
    public void onRefresh() {
        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select cc.id,cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
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
                lista = ModeloClientes.sacarListaClientes(response);
                //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                adapter = new AdapterClientes(getContext(),lista);
                listView.setAdapter(adapter);
                contenedorClientesA.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);
    }

    //PARA REALIZAR LAS BUSQUEDAS
    @Override
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        try {
            List<ModeloClientes>listafiltrada =filter(lista,s);
            adapter.setFilter(listafiltrada);
        }catch (Exception e){
            e.printStackTrace();

        }
        return false;
    }
    private List<ModeloClientes>filter(List<ModeloClientes>notas,String texto){
        List<ModeloClientes>listaFiltrada= new ArrayList<>();
        try {
            texto=texto.toLowerCase();
            for (ModeloClientes nota:notas){
                String nota2 = nota.getNombre().toLowerCase();
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
