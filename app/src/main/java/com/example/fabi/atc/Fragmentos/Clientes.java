package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
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
import org.json.JSONObject;

import java.util.ArrayList;

public class Clientes extends Fragment implements Basic, Response.Listener<JSONArray>, Response.ErrorListener{

    //Fragmento para los accesorios
    private static final String ARG_POSITION= "POSITION";

    ListView listView;
    rutasLib rutasObj;
    String url;
    private ProgressDialog progressDialog;
    private int  mPosition;
    private OnFragmentInteractionListener mListener;

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance(int position) {
        Clientes fragment = new Clientes();
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

        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        listView= (ListView)view.findViewById(R.id.lvFClientes);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select ca.id,ta.nombre, ma.nombre,a.precio,ca.valor " +
                "from marca ma, modelo mo, articulo a, punto_venta pv, cantidad ca, tipo_articulo ta " +
                "where a.modelo_id = mo.id " +
                "and mo.marca_id = ma.id " +
                "and ca.puntoVenta_id = pv.id " +
                "and ca.articulo_id = a.id " +
                "and a.tipoArticulo_id = ta.id " +
                "and pv.id = "+usuarioID+" and ta.nombre !='Teléfono' " +
                "and ta.nombre !='Chip' " +
                "and ca.valor > 0 "+
                "order by ta.nombre asc;";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Hace la petición String
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Agrega y ejecuta la cola
        queue.add(request);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(),"Error en el webservice", Toast.LENGTH_SHORT).show();
        Toast.makeText(getContext(), "Accesorios  "+url, Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        //Toast.makeText(getContext(), "Accesorios  "+url, Toast.LENGTH_SHORT).show();
        InventarioPersonalAdapter adapter = new InventarioPersonalAdapter(getContext(), ModeloInventarioPersonal.sacarListaproductos(response));
        listView.setAdapter(adapter);

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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
