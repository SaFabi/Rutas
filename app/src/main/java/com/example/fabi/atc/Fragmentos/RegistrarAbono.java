package com.example.fabi.atc.Fragmentos;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterAbonos;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.CreditosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistrarAbono extends Fragment  implements Basic{
    //FRAGMENTO PROBADO. MUESTRA LOS ABONOS REALIZADOS DE UN DETERMINADO CREDITO Y PERMITE AGREGAR UNO NUEVO.
    //VARIABLES
    int ordenID;
    int MontoTotal;
    int ClienteID;
    int CreditoID;
    int SumaAbonos;
    View vistaAlertEliminar;

    //CONTROLES
    EditText edtMonto, edtAbono;
    ListView listView;
    ProgressDialog progressDialog;
    Button btnAgregarAbono;

    //ADAPTERS
   AdapterAbonos adapter;

    public RegistrarAbono() {
        // Required empty public constructor
    }

    public static RegistrarAbono newInstance(int OrdenID, int MontoTotal, int ClienteID, int CreditoID) {
        RegistrarAbono fragment = new RegistrarAbono();
        Bundle args = new Bundle();
       //RECIBIR LOS VALORES DEL OTRO FRAGMENTO
        args.putInt("ordenID",OrdenID);
        args.putInt("totalOrden",MontoTotal);
        args.putInt("ClienteID",ClienteID);
        args.putInt("CreditoID",CreditoID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //SE ASIGNAN LOS VALORES DEL OTRO FRAGMENTO A LAS VARIABLES LOCALES
            ordenID = getArguments().getInt("ordenID");
            MontoTotal = getArguments().getInt("totalOrden");
            ClienteID = getArguments().getInt("ClienteID");
            CreditoID = getArguments().getInt("CreditoID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //LA VISTA DEL FRAGMENTO
        View vista = inflater.inflate(R.layout.fragment_registrar_abono, container, false);

        //VERIFICAR QUE LOS DATOS SE ESTEN ENVIANDO CORRECTAMENTE
        //Toast.makeText(getContext(),String.valueOf("  "+ordenID)+String.valueOf("  "+MontoTotal)+String.valueOf("  "+ClienteID)+String.valueOf("  "+CreditoID),Toast.LENGTH_SHORT).show();
        //ASIGANCION DE VALORES A LOS CONTROLES
        edtMonto = (EditText)vista.findViewById(R.id.edtmontoTotal);
        edtAbono = (EditText)vista.findViewById(R.id.edtABono);
        listView = (ListView)vista.findViewById(R.id.Abonos);
        btnAgregarAbono = (Button)vista.findViewById(R.id.btnAgregarAbono);

        edtMonto.setText(String.valueOf("$"+MontoTotal));

        //SE LE DA FUNCIONALIDAD AL BOTON
        btnAgregarAbono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtAbono.getText().length() >0) {
                progressDialog = new ProgressDialog(getContext());
                progressDialog.setTitle("En Proceso");
                progressDialog.setMessage("Un momento...");
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.show();
                    //CONSULTA PARA INSERTAR EN BONO CREDITO
                    RequestQueue queueInsertar = Volley.newRequestQueue(getContext());
                    String consultaInsertar = "insert into bono_credito(cantidad,estado,fecha,credito_id)values(" + Integer.parseInt(edtAbono.getText().toString()) + "," + 1 + ",now()," + CreditoID + ")";
                    consultaInsertar = consultaInsertar.replace(" ", "%20");
                    String cadenaInsertar = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaInsertar;
                    String urlInsertar = SERVER + RUTA + "consultaGeneral.php" + cadenaInsertar;
                    Log.i("info", urlInsertar);
                    JsonArrayRequest requestInsertar = new JsonArrayRequest(Request.Method.GET, urlInsertar, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
                            RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
                            String consultaCreditos = "SELECT Distinct o.folio, DATE(bc.fecha),bc.cantidad" +
                                    " from credito c, orden o, bono_credito bc" +
                                    " WHERE c.orden_id = o.id" +
                                    " AND bc.credito_id = c.id" +
                                    " AND o.cliente_id =" + ClienteID +
                                    " AND c.orden_id =" + ordenID +
                                    " and bc.cantidad > 0"+
                                    " ORDER BY bc.id DESC";
                            consultaCreditos = consultaCreditos.replace(" ", "%20");
                            String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
                            final String url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
                            Log.i("info", url);
                            JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                                @Override
                                public void onResponse(JSONArray response) {
                                    adapter = new AdapterAbonos(response, getContext());
                                    listView.setAdapter(adapter);
                                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                                    dialogo1.setTitle("Importante");
                                    dialogo1.setCancelable(false);
                                    dialogo1.setMessage("Se agrego el abono correctamente");
                                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialogInterface, int i) {

                                        }
                                    });

                                    edtAbono.setText(" ");
                                    dialogo1.show();
                                    //CONSULTA PATA OBTENER EL MONTO ACTUAL DEL CREDITO
                                    RequestQueue queuesum = Volley.newRequestQueue(getContext());
                                    String consultasum = " select sum(bc.cantidad) from bono_credito bc where credito_id =" + CreditoID;
                                    consultasum = consultasum.replace(" ", "%20");
                                    String cadenasum = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultasum;
                                    String urlsum = SERVER + RUTA + "consultaGeneral.php" + cadenasum;
                                    Log.i("info", urlsum);
                                    JsonArrayRequest requestsum = new JsonArrayRequest(Request.Method.GET, urlsum, null, new Response.Listener<JSONArray>() {
                                        @Override
                                        public void onResponse(JSONArray response) {
                                            progressDialog.hide();
                                            JSONObject jsonObject;
                                            try {
                                                jsonObject = response.getJSONObject(0);

                                            } catch (Exception e) {
                                                jsonObject = new JSONObject();
                                            }
                                            try {
                                                SumaAbonos = Integer.parseInt(jsonObject.getString("0"));

                                            } catch (Exception e) {

                                                SumaAbonos = 0;
                                            }
                                            if (SumaAbonos != 0) {
                                                // Toast.makeText(getContext(),String.valueOf(SumaAbonos),Toast.LENGTH_SHORT).show();
                                                edtMonto.setText("$" + String.valueOf(MontoTotal - SumaAbonos));
                                                //COMPARA SI EL CREDITO SE LIQUIDO
                                                if ((MontoTotal - SumaAbonos) == 0) {


                                                //CONSULTA PARA ACTUALIZAR EL ESTADO DEL CREDITO A INACTIVO
                                                RequestQueue queueActualizar = Volley.newRequestQueue(getContext());
                                                String consultaActualizar = "update credito set estado=0 where id =" + CreditoID;
                                                consultaActualizar = consultaActualizar.replace(" ", "%20");
                                                String cadenaActualizar = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaActualizar;
                                                final String urlActualizar = SERVER + RUTA + "consultaGeneral.php" + cadenaActualizar;
                                                Log.i("info", urlActualizar);
                                                JsonArrayRequest requestActualizar = new JsonArrayRequest(Request.Method.GET, urlActualizar, null, new Response.Listener<JSONArray>() {
                                                    @Override
                                                    public void onResponse(JSONArray response) {
                                                        btnAgregarAbono.setEnabled(false);
                                                        edtAbono.setEnabled(false);
                                                        AlertDialog.Builder dialogo = new AlertDialog.Builder(getActivity());
                                                        dialogo.setTitle("Importante");
                                                        dialogo.setCancelable(false);
                                                        dialogo.setIcon(R.drawable.aceptar);
                                                        dialogo.setMessage("Se completó el monto del credito");
                                                        dialogo.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                                                            @Override
                                                            public void onClick(DialogInterface dialogInterface, int i) {

                                                            }
                                                        });
                                                        dialogo.show();


                                                    }
                                                }, new Response.ErrorListener() {
                                                    @Override
                                                    public void onErrorResponse(VolleyError error) {

                                                    }
                                                });
                                                queueActualizar.add(requestActualizar);
                                            }


                                                }
                                        }
                                    }, new Response.ErrorListener() {
                                        @Override
                                        public void onErrorResponse(VolleyError error) {

                                        }
                                    });
                                    queuesum.add(requestsum);

                                }
                            }, new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {

                                }
                            });
                            queueCreditos.add(requestCreditos);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queueInsertar.add(requestInsertar);


                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                    dialogo1.setTitle("Importante");
                    dialogo1.setCancelable(false);
                    dialogo1.setIcon(R.drawable.cancelar);
                    dialogo1.setMessage("Introduza una cantidad");
                    dialogo1.setView(vistaAlertEliminar);
                    dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialogo1.show();
                }

            }
        });


        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
        RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
        String consultaCreditos = "SELECT Distinct o.folio, DATE(bc.fecha),bc.cantidad"+
                " from credito c, orden o, bono_credito bc" +
                " WHERE c.orden_id = o.id" +
                " AND bc.credito_id = c.id" +
                " AND o.cliente_id =" +ClienteID+
                " AND c.orden_id =" +ordenID+
                " and bc.cantidad > 0"+
                " ORDER BY bc.id DESC";
        consultaCreditos = consultaCreditos.replace(" ", "%20");
        String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
        final String url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
        Log.i("info", url);
        JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                adapter = new AdapterAbonos(response,getContext());
                listView.setAdapter(adapter);
                //CONSULTA PATA OBTENER EL MONTO ACTUAL DEL CREDITO
                RequestQueue queuesum = Volley.newRequestQueue(getContext());
                String consultasum = " select sum(bc.cantidad) from bono_credito bc where credito_id ="+CreditoID;
                consultasum = consultasum.replace(" ","%20");
                String cadenasum = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultasum;
                String urlsum = SERVER + RUTA + "consultaGeneral.php" + cadenasum;
                Log.i("info", urlsum);
                JsonArrayRequest requestsum = new JsonArrayRequest(Request.Method.GET, urlsum, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        JSONObject jsonObject;
                        try {
                            jsonObject = response.getJSONObject(0);

                        }catch (Exception e){
                            jsonObject = new JSONObject();
                        }
                        try {
                            SumaAbonos = Integer.parseInt(jsonObject.getString("0"));

                        }catch (Exception e){

                           SumaAbonos = 0;
                        }
                        if (SumaAbonos != 0){
                           // Toast.makeText(getContext(),String.valueOf(SumaAbonos),Toast.LENGTH_SHORT).show();
                            edtMonto.setText("$"+String.valueOf(MontoTotal-SumaAbonos));

                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });
                queuesum.add(requestsum);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueCreditos.add(requestCreditos);


        return vista;
    }
}
