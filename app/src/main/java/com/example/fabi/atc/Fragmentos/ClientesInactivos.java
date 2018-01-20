package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class ClientesInactivos extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Basic{

    private static final String ARG_POSITION = "POSITION";
    String url;
    ListView listView;
    AdapterClientes adapter;
    int clienteID;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout contenedorClientesI;
    View vistaAlertActivar;

    // TODO: Rename and change types of parameters
    private int mPosition;

    private OnFragmentInteractionListener mListener;

    public ClientesInactivos() {
        // Required empty public constructor
    }
    public static ClientesInactivos newInstance(int position) {
        ClientesInactivos fragment = new ClientesInactivos();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //Crea la vista
       View view = inflater.inflate(R.layout.fragment_clientes_inactivos, container, false);
        //Se declaran los elementos con su id
        contenedorClientesI = (SwipeRefreshLayout)view.findViewById(R.id.contenedorClientesInactivos);
        contenedorClientesI.setOnRefreshListener(this);
        listView = (ListView)view.findViewById(R.id.clientesInactivos);

        //PARA LENAR EL LISTVIEW CON LOS CLIENTES QUE ESTAN DESHABILITADOS
        //Se declara el progress dialog para ejecutar despues la consulta
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
                " and pv.id="+usuarioID+" and cc.activo = false";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                // Toast.makeText(getContext(), "Telefonos    "+url, Toast.LENGTH_SHORT).show();
                adapter= new AdapterClientes(getContext(),ModeloClientes.sacarListaClientes(response));
                listView.setAdapter(adapter);
                contenedorClientesI.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressDialog.hide();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(),  "Inactivos   "+url, Toast.LENGTH_SHORT).show();
            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);





        //PARA AEJECUTAR LAS ACCIONES CUANDO SE DEJA PRESIONADO UN ITEM DEL LISTVIEW
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                //Hace la consulta para sacar el id del cliente que se va a eliminar

                clienteID = (int)adapter.getItemId(i);
                //Toast.makeText(getContext(), "ID: " + String.valueOf(clienteID), Toast.LENGTH_SHORT).show();


                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("Importante");
                dialogo1.setIcon(R.drawable.aceptar);
                dialogo1.setMessage("¿Desea Restaurar este elemento?");
                dialogo1.setView(vistaAlertActivar);
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


                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String consulta = "update clave_cliente set activo=1 where id="+clienteID;
                        consulta = consulta.replace(" ", "%20");
                        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                        final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                        Log.i("info", url);

                        //Inicia la peticion para actualizar el estado del cliente a activo
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                //Inicia la peticion
                                RequestQueue queue = Volley.newRequestQueue(getContext());
                                String consulta = "select cc.id,cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
                                        "from cliente cl, clave_cliente cc, punto_venta pv " +
                                        "where cc.puntoVenta_id = pv.id " +
                                        "and cc.cliente_id = cl.id " +
                                        " and pv.id="+usuarioID+" and cc.activo =false";
                                consulta = consulta.replace(" ", "%20");
                                String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                                final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                                Log.i("info", url);

                                //Hace la petición String para mostrar los clientes que siguen inactivos
                                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                    @Override
                                    public void onResponse(JSONArray response) {
                                        progressDialog.hide();
                                        //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                                        adapter = new AdapterClientes(getContext(),ModeloClientes.sacarListaClientes(response));
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
                                Toast.makeText(getContext(),"Se activo correctamente",Toast.LENGTH_SHORT).show();

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
                });
                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                dialogo1.show();

                return false;
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




    @Override
    public void onRefresh() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select cc.id,cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
                "from cliente cl, clave_cliente cc, punto_venta pv " +
                "where cc.puntoVenta_id = pv.id " +
                "and cc.cliente_id = cl.id " +
                " and pv.id="+usuarioID+" and cc.activo = false";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);
        //Para el proceso de obtencion de la ultima clave del cliente
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // Toast.makeText(getContext(), "Telefonos    "+url, Toast.LENGTH_SHORT).show();
                adapter= new AdapterClientes(getContext(),ModeloClientes.sacarListaClientes(response));
                listView.setAdapter(adapter);
                contenedorClientesI.setRefreshing(false);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), url, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
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
