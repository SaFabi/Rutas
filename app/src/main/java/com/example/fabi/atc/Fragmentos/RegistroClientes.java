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
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistroClientes extends Fragment implements Basic, Response.Listener<JSONArray>, Response.ErrorListener{

    Spinner spinner;
    String url;
    String urlClave;
    String ciudadID;
    private static final String ARG_POSITION = "POSITION";
    private ProgressDialog progressDialog;

    private int mPosition;

    private OnFragmentInteractionListener mListener;
    public RegistroClientes() {
        // Required empty public constructor
    }
    public static RegistroClientes newInstance(int position) {
        RegistroClientes fragment = new RegistroClientes();
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
                             final Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.fragment_registro_clientes, container, false);
        spinner = (Spinner)view.findViewById(R.id.Ciudades);
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               String position = spinner.getSelectedItem().toString();
               //Toast.makeText(getContext(),position,Toast.LENGTH_SHORT).show();
           }
           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        //Se declara el progress dialog para ejecutar despues la consulta
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion para llenar el Spinner  con las ciudades
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select id,nombre from ciudad order by nombre asc";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Inicia la peticion para obtener la ultima  clave del cliente
        RequestQueue queueClave = Volley.newRequestQueue(getContext());
        String consultaClave ="select CONCAT(pv.tipo,'-',cc.numero%2B1) "+
       "from clave_cliente cc, punto_Venta pv "+
        "where cc.puntoVenta_id  = pv.id "+
        "and pv.id ="+usuarioID+
        " order by cc.numero DESC limit 1";
        consultaClave = consultaClave.replace(" ", "%20");
        String cadenaClave = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaClave;
        urlClave = SERVER + RUTA + "consultaGeneral.php" + cadenaClave;
        Log.i("info", urlClave);

        //Hace la petición String para la consulta de las ciudades
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Para el proceso de obtencion de la ultima clave del cliente
        JsonArrayRequest requestClave = new JsonArrayRequest(Request.Method.GET, urlClave, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               // Toast.makeText(getContext(), urlClave, Toast.LENGTH_SHORT).show();
                EditText edtClave = (EditText)view.findViewById(R.id.edtClave);
                JSONObject jsonObject;
                try {
                    jsonObject =response.getJSONObject(0);
                }catch (Exception e){
                    jsonObject = new JSONObject();
                }

                String clave;
                try {
                    clave = jsonObject.getString("0");
                }catch (Exception e){
                    clave = null;
                }
                if (clave != null){
                    edtClave.setText(clave);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), urlClave, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);
        queueClave.add(requestClave);


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
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),  "Telefonos    "+url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        // Toast.makeText(getContext(), "Telefonos    "+url, Toast.LENGTH_SHORT).show();
        spinnerAdapter adapter = new spinnerAdapter(response,getContext());
        spinner.setAdapter(adapter);
        JSONObject jsonObject;
        try {
            jsonObject =response.getJSONObject(0);
        }catch (Exception e){
            jsonObject = new JSONObject();
        }
        try {
            ciudadID = jsonObject.getString("0");
        }catch (Exception e){
            ciudadID= null;
        }
        Toast.makeText(getContext(),ciudadID, Toast.LENGTH_SHORT).show();


    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
