package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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
import com.example.fabi.atc.Adapters.AdapterDetallesCreditos;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;


public class DetallesPedidos extends Fragment implements Basic {
    //FRAGMENTO PROBANDO. SE MUESTRAN LOS DETALLES DE UN PEDIDO.
    //VARIABLES
    int pedidoID;

    //CONTROLES
    ListView listView;
    TextView txtTotal;
    ProgressDialog progressDialog;

    //ADAPTERS
    AdapterDetallesCreditos adapterDetallesCreditos;

    public DetallesPedidos() {
    }


    public static DetallesPedidos newInstance(int pedidoID) {
        DetallesPedidos fragment = new DetallesPedidos();
        Bundle args = new Bundle();
        args.putInt("pedidoID",pedidoID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
           pedidoID = getArguments().getInt("pedidoID");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View  view= inflater.inflate(R.layout.fragment_detalles_pedidos, container, false);
        listView = (ListView)view.findViewById(R.id.listadetallesPedidos);
        txtTotal = (TextView)view.findViewById(R.id.txtTotalpedidos);

        Toast.makeText(getContext(),String.valueOf(pedidoID), Toast.LENGTH_SHORT).show();
        //Se declara el progress dialog para ejecutar despues la consulta
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select distinct ta.nombre,ma.nombre, mo.nombre, orddesc.cantidad,(orddesc.precio_final * orddesc.cantidad) "+
        " from tipo_articulo ta, modelo mo, marca ma, articulo a, orden_descripcion orddesc, orden ord, cantidad ca"+
        " where ta.id = a.tipoArticulo_id"+
        " and a.modelo_id = mo.id"+
        " and mo.marca_id = ma.id"+
        " and ca.id = orddesc.tipoVentaId"+
        " and ca.articulo_id = a.id"+
        " and orddesc.orden_id = ord.id"+
        " and ord.id ="+pedidoID;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                adapterDetallesCreditos = new AdapterDetallesCreditos(response,getContext());
                listView.setAdapter(adapterDetallesCreditos);
                RequestQueue queue1 = Volley.newRequestQueue(getContext());
                String consulta1 = "SELECT SUM(od.precio_final * od.cantidad)"+
                " from orden o, orden_descripcion od"+
                " where od.orden_id = o.id"+
               " and o.id="+pedidoID;
                consulta1 = consulta1.replace(" ", "%20");
                String cadena1 = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta1;
                String url1 = SERVER + RUTA + "consultaGeneral.php" + cadena1;
                JsonArrayRequest request1 = new JsonArrayRequest(Request.Method.GET, url1, null, new Response.Listener<JSONArray>() {
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
                            txtTotal.setText("TOTAL: $"+puntoVenta);
                        }


                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queue1.add(request1);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        return view;
    }

}
