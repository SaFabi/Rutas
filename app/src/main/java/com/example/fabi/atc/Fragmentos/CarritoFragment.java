package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.InventarioPersonalAdapter;
import com.example.fabi.atc.Adapters.PedidosAdapter;
import com.example.fabi.atc.Adapters.carritoAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloCarrito;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.Clases.ModeloPedidos;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class CarritoFragment extends Fragment implements Basic {
    //FRAGMENTO EN PROCESO, IMPLEMENTAR CARRITO PARA REGISTRAR VENTAS Y TERMINAR PEDIDOS

    //VARIABLES
    int OrdenID;
    double precioUnitario;
    double Montototal;
    static ArrayList<ModeloInventarioPersonal> carritoFinal;

    //CONTROLES
    ProgressDialog progressDialog;
    ListView listView;
    Spinner spinnerClientes;
    TextView txtMonto;

    //ADAPTERS
    AdapterClientes adapter;
    carritoAdapter carritoAdapter;


    private OnFragmentInteractionListener mListener;

    public CarritoFragment() {
    }
    public static CarritoFragment newInstance(int OrdenID) {
        CarritoFragment fragment = new CarritoFragment();
        Bundle args = new Bundle();
        args.putInt("ordenID",OrdenID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            OrdenID = getArguments().getInt("ordenID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        //ASIGNACION DE VARIABLES CON SUS CONTROLES
        listView = (ListView)view.findViewById(R.id.listaproductoscarrito);
        spinnerClientes = (Spinner)view.findViewById(R.id.spinnerclientescarrito);
        txtMonto = (TextView)view.findViewById(R.id.montototalcarrito);

        Toast.makeText(getContext(),String.valueOf(OrdenID), Toast.LENGTH_SHORT).show();
        if (OrdenID == 0){
            //SI ENTRA POR LA PARTE DE VENTAS
            carritoFinal=new ArrayList<>();
            carritoFinal=InventarioPersonalAdapter.carrito;

            carritoAdapter = new carritoAdapter(carritoFinal,getContext());
            listView.setAdapter(carritoAdapter);

            //SI EL CARRITO TIENE PRODUCTOS SE CALCULA EL TOTAL
            if (carritoFinal.size() >0){
                for (int i=0;i<carritoFinal.size();i++){
                    precioUnitario= Double.parseDouble(carritoFinal.get(i).getPrecio()) *Double.parseDouble(carritoFinal.get(i).getCantidad());
                    Montototal+=precioUnitario;
                }
            }else{
                Toast.makeText(getContext(), "NO hay articulos en el carrito", Toast.LENGTH_SHORT).show();
                Montototal=0.0;
            }
            txtMonto.setText("Total: $"+String.valueOf(Montototal));
        }else{
            //SI ENTRA POR LA PARTE DE PEDIDO
            //SE INICIALIZA EL PROGRESS DIALOG
            progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("En Proceso");
            progressDialog.setMessage("Un momento...");
            progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progressDialog.show();
            //PARA LA CONSULTA DE LAS CLAVES DE LOS CLIENTES EN EL SPINNER
            RequestQueue queue = Volley.newRequestQueue(getContext());
            String consulta = " SELECT orddesc.id,ma.nombre,mo.nombre,orddesc.precio_final,orddesc.cantidad" +
                    " FROM orden ord, marca ma, modelo mo, orden_descripcion orddesc,cantidad ca,articulo art" +
                    " WHERE orddesc.orden_id = ord.id" +
                    " AND orddesc.tipoVentaId = ca.id" +
                    " AND ca.articulo_id = art.id" +
                    " AND art.modelo_id = mo.id" +
                    " AND mo.marca_id = ma.id" +
                    " AND ord.id ="+OrdenID;
            consulta = consulta.replace(" ", "%20");
            String cadenaClaveCliente = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
            String  url = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
            Log.i("info", url);
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    carritoFinal = ModeloInventarioPersonal.sacarListaproductos(response);
                    carritoAdapter= new carritoAdapter(carritoFinal,getContext());
                    listView.setAdapter(carritoAdapter);

                    RequestQueue queueSuma = Volley.newRequestQueue(getContext());
                    String consultaSuma = " SELECT sum(orddesc.cantidad * orddesc.precio_final)" +
                            " FROM orden ord, marca ma, modelo mo, orden_descripcion orddesc,cantidad ca,articulo art" +
                            " WHERE orddesc.orden_id = ord.id" +
                            " AND orddesc.tipoVentaId = ca.id" +
                            " AND ca.articulo_id = art.id" +
                            " AND art.modelo_id = mo.id" +
                            " AND mo.marca_id = ma.id" +
                            " AND ord.id ="+OrdenID;
                    consultaSuma = consultaSuma.replace(" ", "%20");
                    String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaSuma;
                    String  urlSuma = SERVER + RUTA + "consultaGeneral.php" + cadena;
                    Log.i("info", urlSuma);
                    final JsonArrayRequest requestSuma = new JsonArrayRequest(Request.Method.GET, urlSuma, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            String resultadoSuma;
                            JSONObject jsonObject;
                            try {
                                jsonObject =response.getJSONObject(0);
                            }catch (Exception e){
                                jsonObject = new JSONObject();
                            }


                            try {
                                resultadoSuma= jsonObject.getString("0");

                            }catch (Exception e){
                                resultadoSuma = null;
                            }
                            Toast.makeText(getContext(),resultadoSuma, Toast.LENGTH_SHORT).show();
                            if (resultadoSuma.equals("")){
                                txtMonto.setText("TOTAL: $0.0");
                                //SE ASIGNA EL RESULTADO DE LA CONSULTA  A UN EDITTEXT

                            }else{
                                txtMonto.setText("TOTAL: $"+resultadoSuma);
                            }

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queueSuma.add(requestSuma);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
            queue.add(request);

        }
        //CONSULTA PARA LLENAR EL SPINNER CON LOS CLIENTES
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select cl.id, cc.numero "+
                " from cliente cl, clave_cliente cc, punto_venta pv"+
                " where cc.cliente_id = cl.id"+
                " and  cc.puntoVenta_id = pv.id"+
                " and pv.id ="+usuarioID;
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db="+DB+ "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);
        JsonArrayRequest requestClaveCliente = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                SpinnerAdapter spinnerAdapter= new spinnerAdapter(getContext(), Modelo.ListaSpinner(response));
                spinnerClientes.setAdapter(spinnerAdapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(requestClaveCliente);

        return view;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);
    }
}
