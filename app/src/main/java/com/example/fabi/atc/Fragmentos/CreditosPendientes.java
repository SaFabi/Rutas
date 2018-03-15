package com.example.fabi.atc.Fragmentos;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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
import com.example.fabi.atc.Adapters.CreditosAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class CreditosPendientes extends Fragment implements SwipeRefreshLayout.OnRefreshListener,Basic{
    //FRAGMENTO PROBADO.MUESTRA LOS CREDITOS QUE AUN ESTAN PENDIENTES. ESTA DENTRO DE CREDITOSCONTENEDOR
    //URL DE LA CONSULTA DEL PUNTO DE VENTA
    String url;

    //ASIGNACION DE VARIABLES CON SUS CONTROLES
    ListView listView;
    TextView txtPuntoVenta;
    Spinner spinnerCreditos;
    private ProgressDialog progressDialog;
    SwipeRefreshLayout contenedorCreditosP;

    //VARIABLES NORMALES
    String puntoVentaLogin;
    String puntoVentaVentas;
    int claveCliente;
    int ordenID;
    int Total;


    //ADAPTERS
    rutasLib rutasObj = new rutasLib();
    spinnerAdapter spinnerAdapter;
    CreditosAdapter creditosAdapter;

    public CreditosPendientes() {
        // Required empty public constructor
    }

    public static CreditosPendientes newInstance(int position) {
        CreditosPendientes fragment = new CreditosPendientes();
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
        View view= inflater.inflate(R.layout.fragment_creditos_pendientes, container, false);
        setHasOptionsMenu(true);
        listView = (ListView)view.findViewById(R.id.CreditoClientes);
        txtPuntoVenta = (TextView)view.findViewById(R.id.puntoVenta);
        spinnerCreditos = (Spinner)view.findViewById(R.id.spinnerClaves);
        puntoVentaVentas = rutasObj.sacarPuntoVenta(puntoVentaLogin);
        txtPuntoVenta.setText(puntoVentaVentas);
        contenedorCreditosP = (SwipeRefreshLayout)view.findViewById(R.id.contenedorCreditosPendientes);
        contenedorCreditosP.setOnRefreshListener(this);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ordenID = (int)creditosAdapter.getItemId(i);
                Total = (int)creditosAdapter.getTotal(i);
                Toast.makeText(getContext(),String.valueOf(ordenID),Toast.LENGTH_SHORT).show();
                Fragment fragment = detallesCreditosPendientes.newInstance(ordenID);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        //RECARGA EL LISTIVIEW SI LLEGA AL TOPE
        listView.setOnScrollListener(new AbsListView.OnScrollListener()
        {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState)
            {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
            {
                int top = (listView == null || listView.getChildCount() == 0) ? 0 : listView.getChildAt(0).getTop();
                contenedorCreditosP.setEnabled(firstVisibleItem == 0 && top >= 0);
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
                String consultaCreditos = "select distinct ord.id,ord.folio,DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero),cre.total, cli.id,cre.id" +
                " from orden ord,credito cre, punto_venta pv, cliente cli, clave_cliente cc"+
                " where cre.orden_id = ord.id"+
                " and ord.puntoVenta_id = pv.id"+
                " and ord.cliente_id = cli.id"+
                " and cc.cliente_id =cli.id"+
                " and pv.tipo='"+puntoVentaVentas+
                "' and cre.total>0"+
                " and cre.estado=1"+
                " and cli.id="+claveCliente;
                consultaCreditos = consultaCreditos.replace(" ", "%20");
                String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
                url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
                Log.i("info", url);
                JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progressDialog.hide();
                        creditosAdapter= new CreditosAdapter(getActivity().getSupportFragmentManager(),getContext(), ModeloCreditos.sacarListaClientes(response),ordenID,Total);
                        listView.setAdapter(creditosAdapter);

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
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
        //PARA LA CONSULTA DE LAS CLAVES DE LOS CLIENTES EN EL SPINNER
        RequestQueue queueClaveCliente = Volley.newRequestQueue(getContext());
        String consultaClaveCliente = "select cl.id, cc.numero "+
        " from cliente cl, clave_cliente cc, punto_venta pv"+
        " where cc.cliente_id = cl.id"+
        " and  cc.puntoVenta_id = pv.id"+
        " and pv.tipo ='"+puntoVentaVentas+"'";
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


    //Infla el menu para el carrito y el buscador
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        //inflater.inflate(R.menu.menu_buscador,menu);
        MenuItem buscador = menu.findItem(R.id.buscador2);
        MenuItem carrito = menu.findItem(R.id.carrito);
        carrito.setVisible(false);
        buscador.setVisible(false);
    }

    @Override
    public void onRefresh() {

        //CONSULTA PARA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
        RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
        String consultaCreditos = "select distinct ord.id,ord.folio,DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero),cre.total, cli.id,cre.id" +
                " from orden ord,credito cre, punto_venta pv, cliente cli, clave_cliente cc"+
                " where cre.orden_id = ord.id"+
                " and ord.puntoVenta_id = pv.id"+
                " and ord.cliente_id = cli.id"+
                " and cc.cliente_id =cli.id"+
                " and pv.tipo='"+puntoVentaVentas+
                "' and cre.total>0"+
                " and cre.estado=1"+
                " and cli.id="+claveCliente;
        consultaCreditos = consultaCreditos.replace(" ", "%20");
        String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
        url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
        Log.i("info", url);
        JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                creditosAdapter= new CreditosAdapter(getActivity().getSupportFragmentManager(),getContext(), ModeloCreditos.sacarListaClientes(response),ordenID,Total);
                listView.setAdapter(creditosAdapter);
                contenedorCreditosP.setRefreshing(false);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueCreditos.add(requestCreditos);

    }
}
