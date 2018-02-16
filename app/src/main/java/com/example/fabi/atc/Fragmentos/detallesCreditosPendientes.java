package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.AdapterDetallesCreditos;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class detallesCreditosPendientes extends Fragment  implements Basic{

    //FRAGMENTO PROBADO. MUESTRA LOS DETALLES DE UN CREDITO PENDIENTE, SE LLAM DENTRO DE CREDITOS PENDIENTES

    //VARIABLES
    int ordenID;

    //CONTROLES
    ListView listView;
    TextView txtMonto;
    ProgressDialog progressDialog;

    //ADAPTERS
    AdapterDetallesCreditos adapter;

    private OnFragmentInteractionListener mListener;

    public detallesCreditosPendientes() {
        // Required empty public constructor
    }

    public static detallesCreditosPendientes newInstance(int ordenID) {
        detallesCreditosPendientes fragment = new detallesCreditosPendientes();
        Bundle args = new Bundle();
        args.putInt("ordenID",ordenID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
            ordenID = getArguments().getInt("ordenID");

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista =  inflater.inflate(R.layout.fragment_detalles_creditos_pendientes, container, false);
        txtMonto = (TextView)vista.findViewById(R.id.txtTotalCreditos);

        //INICIALIZA EL PROGRESS DIALOG
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        listView = (ListView)vista.findViewById(R.id.detallesCreditos);

        //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
        RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
        String consultaCreditos = "select distinct ta.nombre,ma.nombre, mo.nombre, orddesc.cantidad,(orddesc.precio_final * orddesc.cantidad) " +
                "from tipo_articulo ta, modelo mo, marca ma, articulo a, orden_descripcion orddesc, orden ord, cantidad ca " +
                "where ta.id = a.tipoArticulo_id " +
                "and a.modelo_id = mo.id " +
                "and mo.marca_id = ma.id " +
                "and ca.id = orddesc.tipoVentaId " +
                "and ca.articulo_id = a.id " +
                "and orddesc.orden_id = ord.id " +
                "and ord.id="+ordenID;
        consultaCreditos = consultaCreditos.replace(" ", "%20");
        String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
        final String url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
        Log.i("info", url);
        JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
               adapter = new AdapterDetallesCreditos(response,getContext());
                listView.setAdapter(adapter);
                //CONSULTA PARA OBTENER EL TOTAL DEL CREDITO
                RequestQueue queue = Volley.newRequestQueue(getContext());
                String consulta = "select sum(orddesc.precio_final * orddesc.cantidad) " +
                        "from orden_descripcion orddesc, orden ord " +
                        "where orddesc.orden_id = ord.id " +
                        "and ord.id="+ordenID;
                consulta = consulta.replace(" ", "%20");
                String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                String url1 = SERVER + RUTA + "consultaGeneral.php" + cadena;
                Log.i("info", url1);
                JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hide();
                        String  puntoVenta;
                        JSONObject jsonObject;
                        try {
                            jsonObject =response.getJSONObject(0);
                        }catch (Exception e){
                            jsonObject = new JSONObject();
                        }

                        try {
                            puntoVenta= jsonObject.getString("0");

                        }catch (Exception e){
                            puntoVenta = null;
                        }
                        if (puntoVenta != null){
                            txtMonto.setText("TOTAL: $"+puntoVenta);
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
            queue.add(request);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueCreditos.add(requestCreditos);
        //Toast.makeText(getContext(),String.valueOf(ordenID),Toast.LENGTH_SHORT).show();


        return vista;
    }
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
