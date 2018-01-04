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
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.R;

import org.json.JSONArray;


public class ChipsGeneral extends Fragment  implements Basic, Response.Listener<JSONArray>, Response.ErrorListener {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    String url;
    ListView listView;
    private ProgressDialog progressDialog;

    private OnFragmentInteractionListener mListener;

    public ChipsGeneral() {
        // Required empty public constructor
    }

    public static ChipsGeneral newInstance(int position) {
        ChipsGeneral fragment = new ChipsGeneral();
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
        View view =  inflater.inflate(R.layout.fragment_chips_general, container, false);

        listView= (ListView)view.findViewById(R.id.chipsGeneral);

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
                "and ta.nombre='Chip' " +
                "and ca.valor>0 "+
                "order by ma.nombre asc;";
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
*/
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }



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
        ProductosAdapter adapter = new ProductosAdapter(response,getContext());
        listView.setAdapter(adapter);

    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
