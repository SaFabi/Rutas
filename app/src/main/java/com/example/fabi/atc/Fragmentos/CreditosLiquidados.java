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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class CreditosLiquidados extends Fragment implements Basic {
    private static final String ARG_POSITION = "position";
    private int mPosition;

    //CONTROLES
    TextView txtPunVenta;
    Spinner spinnerClaves;
    ListView listView;
    private ProgressDialog progressDialog;

    //ADAPTERS
    spinnerAdapter spinnerAdapter;
    AdapterClientes adapterClientes;

    //VARIABLES
    int claveCliente;
    String puntoVenta;
    int ordenID;

    private OnFragmentInteractionListener mListener;

    public CreditosLiquidados() {
        // Required empty public constructor
    }

    public static CreditosLiquidados newInstance(int position) {
        CreditosLiquidados fragment = new CreditosLiquidados();
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
       View vista = inflater.inflate(R.layout.fragment_creditos_liquidados, container, false);
        txtPunVenta = (TextView)vista.findViewById(R.id.puntoVentaCreditosLiquidados);
        spinnerClaves = (Spinner)vista.findViewById(R.id.spinnerClavesCreditosLiquidados);
        listView = (ListView)vista.findViewById(R.id.CreditoClientesCreditosLiquidados);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ordenID = (int)adapterClientes.getItemId(i);
                Toast.makeText(getContext(),String.valueOf(ordenID),Toast.LENGTH_SHORT).show();
            }
        });


        spinnerClaves.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                claveCliente = (int)spinnerAdapter.getItemId(i);
                //CONSULTA PARA OBTENER LOS CREDITOS DE CIERTO CLIENTE
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("En Proceso");
                progressDialog.setMessage("Un momento...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
                RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
                String consultaCreditos = "select distinct ord.id,cre.total,ord.folio,DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero)"+
                        " from orden ord,credito cre, punto_venta pv, cliente cli, clave_cliente cc"+
                        " where cre.orden_id = ord.id"+
                        " and ord.puntoVenta_id = pv.id"+
                        " and ord.cliente_id = cli.id"+
                        " and cc.cliente_id =cli.id"+
                        " and pv.id="+usuarioID+
                        " and cre.total>0"+
                        " and cre.estado=0"+
                        " and cli.id="+claveCliente; ;
                consultaCreditos = consultaCreditos.replace(" ", "%20");
                String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
                String url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
                Log.i("info", url);

                JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hide();
                        adapterClientes= new AdapterClientes(getContext(), ModeloClientes.sacarListaClientes(response));
                        listView.setAdapter(adapterClientes);

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queueCreditos.add(requestCreditos);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //PARA SELECCIONAR EL TIPO DE PUNTO DE VENTA

        //Se declara el progress dialog para ejecutar despues la consulta
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select tipo from punto_venta where id="+usuarioID;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();

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
                    txtPunVenta.setText(puntoVenta);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);

        //PARA LA CONSULTA DE LAS CLAVES DE LOS CLIENTES EN EL SPINNER
        RequestQueue queueClaveCliente = Volley.newRequestQueue(getContext());
        String consultaClaveCliente = "select cl.id, cc.numero "+
                " from cliente cl, clave_cliente cc, punto_venta pv"+
                " where cc.cliente_id = cl.id"+
                " and  cc.puntoVenta_id = pv.id"+
                " and pv.id ="+usuarioID;
        consultaClaveCliente = consultaClaveCliente.replace(" ", "%20");
        String cadenaClaveCliente = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaClaveCliente;
        String urlClaves = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
        Log.i("info", url);
        JsonArrayRequest requestClaveCliente = new JsonArrayRequest(Request.Method.GET, urlClaves, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                spinnerAdapter= new spinnerAdapter(getContext(), Modelo.ListaSpinner(response));
                spinnerClaves.setAdapter(spinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueClaveCliente.add(requestClaveCliente);


       return vista;
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