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
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Creditos extends Fragment implements Basic,  Response.Listener<JSONArray>, Response.ErrorListener{
    private static final String ARG_POSITION = "position";
    private int mPosition;
    String url;
    ListView listView;
    TextView txtPuntoVenta;
    String puntoVenta;
    Spinner spinnerCreditos;
    Button btnConsultar;
    rutasLib rutasObj;
    String claveCliente ="034";
    private ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public Creditos() {
        // Required empty public constructor
    }

    public static Creditos newInstance(int position) {
        Creditos fragment = new Creditos();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION,position);
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
        View view= inflater.inflate(R.layout.fragment_creditos, container, false);
        listView = (ListView)view.findViewById(R.id.CreditoClientes);
        txtPuntoVenta = (TextView)view.findViewById(R.id.puntoVenta);
        spinnerCreditos = (Spinner)view.findViewById(R.id.spinnerClaves);
        btnConsultar = (Button)view.findViewById(R.id.buscarCreditos);
        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                     listView.setAdapter(rutasObj.ReporteCreditos(getContext(), claveCliente,usuarioID));
            }
        });
        spinnerCreditos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

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


        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select pv.tipo, cc.numero "+
        "from punto_venta pv, clave_cliente cc "+
        "where cc.puntoVenta_id = pv.id "+
        "and pv.id="+usuarioID;
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


    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        //spinnerAdapter spinnerAdapter= new spinnerAdapter(response,getContext());
        //spinnerCreditos.setAdapter(spinnerAdapter);

        JSONObject jsonObject;
        try {
            jsonObject = response.getJSONObject(0);

        }catch (Exception e){
            jsonObject = new JSONObject();
        }
        try {
            puntoVenta = jsonObject.getString("0");

        }catch (Exception e){

            puntoVenta = null;
        }
        if (puntoVenta != null){
            txtPuntoVenta.setText(puntoVenta);
        }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
