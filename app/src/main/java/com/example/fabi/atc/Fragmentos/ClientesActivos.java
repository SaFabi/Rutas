package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;

public class ClientesActivos extends Fragment implements Basic{

    private static final String ARG_POSITION = "param1";
    String url;
    ListView listView;
    rutasLib rutasObj;
    private ProgressDialog progressDialog;


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
        //Se declaran los elementos con su id
        listView = (ListView)view.findViewById(R.id.clientesActivos);
        listView.setAdapter(rutasObj.ConsultaClientesActivos(getContext()));
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
               //Hace la consulta para sacar el id del cliente que se va a eliminar


                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Desea eliminar este elemento?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    //
                    public void onClick(DialogInterface dialogo1, int id) {
                        Fragment nuevoFragmento = new ClientesInactivos();
                        FragmentTransaction transaction = getFragmentManager().beginTransaction();
                        transaction.replace(R.id.content_main, nuevoFragmento);
                        transaction.addToBackStack(null);

                        // Commit a la transacción
                        transaction.commit();
                        /*
                        //Inicia la peticion para actualizar el estado del cliente a inactivo
                        RequestQueue queueUpdate = Volley.newRequestQueue(getContext());

                        //Asigna a la variable la consulta que se va a ejecutar
                        String consultaUpdate = "select cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
                                "from cliente cl, clave_cliente cc, punto_venta pv " +
                                "where cc.puntoVenta_id = pv.id " +
                                "and cc.cliente_id = cl.id " +
                                " and pv.id="+usuarioID+" and cc.activo = true";

                        //Reemplaza los espacios de a consulta por %20
                        consultaUpdate = consultaUpdate.replace(" ", "%20");

                        //Arma la cadena que se va a mandar al navegador
                        String cadenaUpdate = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaUpdate;

                        //Asigna la url que se ejecutara en el navegador
                        url = SERVER + RUTA + "consultaGeneral.php" + cadenaUpdate;
                        //Para obtener la respuesta de la consulta anterior
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {

                            }
                        });
                        queueUpdate.add(request);
                        //Log.i("info", url);  */
                        Toast.makeText(getContext(),"Se elimino correctamente",Toast.LENGTH_SHORT).show();
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

/*
        //Se declara el progress dialog para ejecutar despues la consulta
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select cl.nombre, cl.direccion,cl.telefono,CONCAT(pv.tipo,'-',cc.numero) " +
                "from cliente cl, clave_cliente cc, punto_venta pv " +
                "where cc.puntoVenta_id = pv.id " +
                "and cc.cliente_id = cl.id " +
                " and pv.id="+usuarioID+" and cc.activo = true";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Agrega y ejecuta la cola
        queue.add(request);

*/
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
/*
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        // Toast.makeText(getContext(), "Telefonos    "+url, Toast.LENGTH_SHORT).show();
        ClientesAdapter adapter = new ClientesAdapter(response,getContext());
        listView.setAdapter(adapter);

    }
*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
