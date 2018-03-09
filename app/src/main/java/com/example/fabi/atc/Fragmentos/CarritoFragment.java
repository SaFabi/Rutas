package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.security.spec.ECField;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;


public class CarritoFragment extends Fragment implements Basic {
    //FRAGMENTO EN PROCESO, IMPLEMENTAR CARRITO PARA REGISTRAR VENTAS Y TERMINAR PEDIDOS

    //VARIABLES
    int OrdenID;
    double precioUnitario;
    double Montototal;
    static ArrayList<ModeloInventarioPersonal> carritoFinal;
    rutasLib rutasObj =new rutasLib();
    int clienteID;
    int usuarioRuta;
    int precioCliente;
    int requerimientoID;
    int ganancia;
    String opcionCompra;
    int calculoGanancia;
    String puntoVenta;


    //CONTROLES
    ProgressDialog progressDialog;
    ListView listView;
    Spinner spinnerClientes;
    TextView txtMonto;
    Button btnTerminarVenta;
    TextView txtfolio;

    //ADAPTERS
    spinnerAdapter spinnerAdapter;
    AdapterClientes adapter;
    carritoAdapter carritoAdapter;

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
        btnTerminarVenta = (Button)view.findViewById(R.id.btnterminarcompra);
        txtfolio = (TextView)view.findViewById(R.id.txtfoliocarrito);

        //PARA OBTENER LA FECHA Y HORA ACTUAL
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();

        final String fecha = dateFormat.format(date);
        Toast.makeText(getContext(),fecha, Toast.LENGTH_SHORT).show();

        //PARA SACAR EL ID DEL CLIENTE QUE ESTE SELECCIONADO

        spinnerClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                clienteID = (int) spinnerAdapter.getItemId(i);
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

        //PARA TERMINAR LA VENTA
        btnTerminarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //VERIFICA QUE EL CARRITO TENGA PRODUCTOS AGREGADOS
                if (carritoFinal.size() > 0) {
                    //PARA SABER SI LA COMPRA SERA A CREDITO O CONTADO
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                    dialogo1.setIcon(R.drawable.reporte);
                    dialogo1.setMessage("¿La compra sera a contado?");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            opcionCompra = "Contado";

                        }
                    });
                    dialogo1.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            opcionCompra = "Credito";
                        }
                    });
                    dialogo1.show();
                    //INICIA LA CONSULTA PARA SACAR EL ULTIMO FOLIO
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String consulta = "select folio from orden ORDER BY id desc LIMIT 1;";
                    consulta = consulta.replace(" ", "%20");
                    String cadenaClaveCliente = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                    String url = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
                    Log.i("info", url);
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @RequiresApi(api = Build.VERSION_CODES.N)
                        @Override
                        public void onResponse(JSONArray response) {
                            String folio;
                            final String nuevoFolio;
                            JSONObject jsonObject;
                            try {
                                jsonObject = response.getJSONObject(0);
                            } catch (Exception e) {
                                jsonObject = new JSONObject();
                            }
                            try {
                                folio = jsonObject.getString("0");

                            } catch (Exception e) {
                                folio = null;
                            }
                            //SE GENERA EL FOLIO NUEVO
                            puntoVenta = rutasObj.sacarPuntoVenta(PUNTOVENTA);
                            nuevoFolio = rutasObj.generarFolio(folio, getContext(), puntoVenta);
                            txtfolio.setText(nuevoFolio);


                            //SE MANDA LLAMAR EL PROCEDIMIENTO PARA LA ORDEN

                            RequestQueue queue = Volley.newRequestQueue(getContext());
                            String consulta = "CALL procesoOrden('"+nuevoFolio+"',"+usuarioID+","+clienteID+");";
                            consulta = consulta.replace(" ", "%20");
                            String cadenaClaveCliente = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                            String url = SERVER + RUTA + "consultaGeneral.php" + cadenaClaveCliente;
                            Log.i("info", url);
                            JsonArrayRequest JsonprocesoOrden = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    JSONObject jsonOrden;
                                    try {
                                        jsonOrden = response.getJSONObject(0);
                                    }catch (Exception e){
                                        jsonOrden=new JSONObject();

                                    }
                                    try {
                                        OrdenID =Integer.parseInt(jsonOrden.getString("0"));

                                    }catch (Exception e){
                                        OrdenID = 0;

                                    }

                                        //INICIA EL CICLO PARA RECORRER EL ARREGLO DEL CARRITO Y SE MANDA LLAMAR EL PROCESO DE ORDEN DESCRIPCION
                                        for (int i =0 ;i<carritoFinal.size();i++){
                                            calculoGanancia = Integer.parseInt(carritoFinal.get(i).getCantidad()) * Integer.parseInt(carritoFinal.get(i).getPrecio());

                                            //Toast.makeText(getContext(), "Hubo erores", Toast.LENGTH_SHORT).show();
                                            RequestQueue queueOrdenDesc = Volley.newRequestQueue(getContext());
                                            String consultaOrdenDesc = "CALL procesoOrdenDescripcionRutas("+OrdenID+","+usuarioID+","+clienteID+","+
                                                    carritoFinal.get(i).getCantidadID()+","+carritoFinal.get(i).getCantidad()+","+
                                                    carritoFinal.get(i).getPrecio()+","+calculoGanancia+",'Articulo');";
                                            consultaOrdenDesc = consultaOrdenDesc.replace(" ", "%20");
                                            String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaOrdenDesc;
                                            String urlOrdenDesc = SERVER + RUTA + "consultaGeneral.php" + cadena;
                                            Log.i("info", urlOrdenDesc);
                                            JsonArrayRequest JsonOrdenDesc = new JsonArrayRequest(Request.Method.GET, urlOrdenDesc, null, new Response.Listener<JSONArray>() {
                                                @Override
                                                public void onResponse(JSONArray response) {
                                                    if (response.length() ==0){
                                                        Toast.makeText(getContext(), "No se puede completar la venta", Toast.LENGTH_SHORT).show();

                                                    }
                                                    if (opcionCompra.equals("Contado")){
                                                        Toast.makeText(getContext(), opcionCompra, Toast.LENGTH_SHORT).show();
                                                        RequestQueue queueContado = Volley.newRequestQueue(getContext());
                                                        String consultaContado = "CALL terminarVentaContado("+OrdenID+");";
                                                        consultaContado = consultaContado.replace(" ","%20");
                                                        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaContado;
                                                        String urlContado = SERVER + RUTA + "consultaGeneral.php" + cadena;
                                                        Log.i("info", urlContado);
                                                        JsonArrayRequest jsonContado = new JsonArrayRequest(Request.Method.GET, urlContado, null, new Response.Listener<JSONArray>() {
                                                            @Override
                                                            public void onResponse(JSONArray response) {
                                                                Toast.makeText(getContext(), "Se termino la venta", Toast.LENGTH_SHORT).show();

                                                            }
                                                        }, new Response.ErrorListener() {
                                                            @Override
                                                            public void onErrorResponse(VolleyError error) {

                                                            }
                                                        });
                                                        //TERMINA LA CONSULTA PARA TERMINAR UNA VENTA DE CONTADO
                                                        queueContado.add(jsonContado);

                                                    }else if (opcionCompra.equals("Credito")){
                                                        Toast.makeText(getContext(), opcionCompra, Toast.LENGTH_SHORT).show();
                                                                if (response.length() == 0){
                                                                    Toast.makeText(getContext(), "Hubo un problema", Toast.LENGTH_SHORT).show();
                                                                }else{
                                                                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                                                                    dialogo1.setIcon(R.drawable.reporte);
                                                                    dialogo1.setMessage("Abono Inicial");
                                                                    final EditText cantidad = new EditText(getContext());
                                                                    cantidad.setText("0");
                                                                    dialogo1.setView(cantidad);
                                                                    cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                                    dialogo1.setCancelable(false);
                                                                    dialogo1.setPositiveButton("SI", new DialogInterface.OnClickListener() {
                                                                        @Override
                                                                        public void onClick(DialogInterface dialogInterface, int i) {
                                                                            RequestQueue queueCredito = Volley.newRequestQueue(getContext());
                                                                            String consultaCredito = "CALL terminarVentaCredito("+OrdenID+","+cantidad.getText().toString()+");";
                                                                            consultaCredito = consultaCredito.replace(" ","%20");
                                                                            String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCredito;
                                                                            String urlContado = SERVER + RUTA + "consultaGeneral.php" + cadena;
                                                                            Log.i("info", urlContado);
                                                                            JsonArrayRequest jsonCredito = new JsonArrayRequest(Request.Method.GET, urlContado, null, new Response.Listener<JSONArray>() {
                                                                                @Override
                                                                                public void onResponse(JSONArray response) {
                                                                                    Toast.makeText(getContext(), "Se termino la venta", Toast.LENGTH_SHORT).show();
                                                                                }
                                                                            }, new Response.ErrorListener() {
                                                                                @Override
                                                                                public void onErrorResponse(VolleyError error) {

                                                                                }
                                                                            });
                                                                            //TERMINA LA CONSULTA PARA TERMINAR UNA VENTA DE CREDITO
                                                                            queueCredito.add(jsonCredito);
                                                                                }


                                                                    });
                                                                    dialogo1.show();

                                                                }
                                                    }


                                                }
                                            }, new Response.ErrorListener() {
                                                @Override
                                                public void onErrorResponse(VolleyError error) {

                                                }
                                            });
                                            //TERMINA EL PROCESO DE ORDEN DESCRIPCION
                                            queueOrdenDesc.add(JsonOrdenDesc);
                                        }




                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            //TERMINA EL PROCESO DE ORDEN
                            queue.add(JsonprocesoOrden);
                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    //TERMINA LA CONSULTA PARA SACAR EL ULTIMO FOLIO
                    queue.add(request);


                }else{
                    //SI NO HAY ARTICULOS AGREGADOS MANDA UNN ALERT DIALOG
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getContext());
                    dialogo1.setIcon(R.drawable.cancelar);
                    dialogo1.setTitle("Importante");
                    dialogo1.setMessage("No hay articulos agregados");
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialogo1.show();

                }

            }


        });


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

            //EJECUTA LA CONSULTA PARA OBTENER LOS DETALLES DE UN PEDIDO
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

                    //EJECUTA LA CONSULTA PARA SUMAR EL TOTAL DE ESE PEDIDO
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
                    JsonArrayRequest requestSuma = new JsonArrayRequest(Request.Method.GET, urlSuma, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //CALCULA EL TOTAL DE ESE PEDIDO
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
                            //Toast.makeText(getContext(),resultadoSuma, Toast.LENGTH_SHORT).show();
                            if (resultadoSuma.equals("")){
                                //SE ASIGNA EL RESULTADO DE LA CONSULTA  A UN EDITTEXT
                                txtMonto.setText("TOTAL: $0.0");
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
                spinnerAdapter= new spinnerAdapter(getContext(), Modelo.ListaSpinner(response));
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

}
