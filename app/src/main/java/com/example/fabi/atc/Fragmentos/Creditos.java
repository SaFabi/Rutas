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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class Creditos extends Fragment implements Basic{
    private static final String ARG_POSITION = "position";
    private int mPosition;
    //URL DE LA CONSULTA DEL PUNTO DE VENTA
    String url;

    //ASIGNACION DE VARIABLES CON SUS CONTROLES
    ListView listView;
    TextView txtPuntoVenta;
    Spinner spinnerCreditos;
    Button btnConsultar;
    private ProgressDialog progressDialog;

    //VARIBALES NORMALES
    String puntoVenta;
    int claveCliente;

    //ADAPTERS
    rutasLib rutasObj;
    spinnerAdapter spinnerAdapter;
    AdapterClientes adapterClientes;

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
                     listView.setAdapter(rutasObj.ReporteCreditos(getContext(),String.valueOf(claveCliente),usuarioID));
            }
        });

        //PARA SACAR EL ID DEL CLIENTE
        spinnerCreditos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                claveCliente = (int)spinnerAdapter.getItemId(i);
                //Toast.makeText(getContext(),String.valueOf(claveCliente),Toast.LENGTH_SHORT).show();
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("En Proceso");
                progressDialog.setMessage("Un momento...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();

                //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
                RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
                String consultaCreditos = "select distinct ord.id,ord.folio,cre.total,DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero)"+
                " from orden ord,credito cre, punto_venta pv, cliente cli, clave_cliente cc"+
                " where cre.orden_id = ord.id"+
                " and ord.puntoVenta_id = pv.id"+
                " and ord.cliente_id = cli.id"+
                " and cc.cliente_id =cli.id"+
                " and pv.id="+usuarioID+
                " and cre.total>0"+
                " and cli.id="+claveCliente; ;
                consultaCreditos = consultaCreditos.replace(" ", "%20");
                String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
                url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
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
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;

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
                    txtPuntoVenta.setText(puntoVenta);
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
        url = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
        Log.i("info", url);
        JsonArrayRequest requestClaveCliente = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                spinnerAdapter= new spinnerAdapter(getContext(), Modelo.ListaSpinner(response));
                spinnerCreditos.setAdapter(spinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueClaveCliente.add(requestClaveCliente);
        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
