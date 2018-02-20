package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.PedidosAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloPedidos;
import com.example.fabi.atc.R;

import org.json.JSONArray;


public class PedidosFragment extends Fragment implements Basic {
    //FRAGMENTO PROBADO.MUETSRA LA LISTA DE LOS PEDIDOS QUE LOS CLIENTES REALIZAN
    // VARIABLES
    int pedidoID;

    //CONTROLES
    ListView listView;
    ProgressDialog progressDialog;

    //ADAPTERS
    PedidosAdapter pedidosAdapter;


    private OnFragmentInteractionListener mListener;

    public PedidosFragment() {
        // Required empty public constructor
    }

    public static PedidosFragment newInstance(int position) {
        PedidosFragment fragment = new PedidosFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_pedidos, container, false);

        //MUESTRA EL MENU DE OPCIONES EN LA TOOLBAR
        setHasOptionsMenu(true);

        listView = (ListView)view.findViewById(R.id.listaPedidos);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                pedidoID = (int)pedidosAdapter.getItemId(i);
                Fragment fragment = DetallesPedidos.newInstance(pedidoID);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        //SE INICIALIZA EL PROGRESS DIALOG
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //PARA LA CONSULTA DE LAS CLAVES DE LOS CLIENTES EN EL SPINNER
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = " SELECT distinct o.id,o.folio,CONCAT(pv.tipo,'-',cc.numero),DATE(o.fecha),"+
        " (SELECT SUM( od.precio_final * od.cantidad)"+
        " FROM orden_descripcion od, orden ord"+
        " where od.orden_id = ord.id"+
        " AND ord.id = o.id)"+
        " from orden o, punto_venta pv, clave_cliente cc, cliente cli, orden_descripcion od "+
        " where o.id not in(Select orden_id from orden_completa)"+
        " and o.puntoVenta_id = pv.id"+
        " and od.orden_id = o.id "+
        " and o.cliente_id = cli.id"+
        " and cc.cliente_id=cli.id"+
        " and o.puntoVenta_id ="+usuarioID+
        " and od.precio_final * od.cantidad > 0"  +
        " order by o.fecha desc;";
        consulta = consulta.replace(" ", "%20");
        String cadenaClaveCliente = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
       String  url = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
        Log.i("info", url);
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                pedidosAdapter = new PedidosAdapter(getContext(), ModeloPedidos.sacarListaClientes(response),getFragmentManager());
                listView.setAdapter(pedidosAdapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(request);
        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //INFLA EL MENU DE OPCIONES
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_buscador,menu);
        MenuItem buscador = menu.findItem(R.id.buscador2);
        MenuItem carrito = menu.findItem(R.id.carrito);
        carrito.setVisible(false);
        buscador.setVisible(false);
    }
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
