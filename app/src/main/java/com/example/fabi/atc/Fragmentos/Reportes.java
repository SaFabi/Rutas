package com.example.fabi.atc.Fragmentos;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.ReportesAdapter;
import com.example.fabi.atc.Adapters.spinnerSencilloAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloSpinnerGeneral;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;

import java.util.ArrayList;


public class Reportes extends Fragment implements Basic {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    Button btnFechaInicio, btnFechaFin, btnConsultar;
    EditText edtFechaInicio, edtFechaFin;
    private int dia,mes, ano,dayI, monthI, yearI,dayF, monthF, yearF;
    Spinner spinner;
     rutasLib rutasObj;
    ArrayList<Modelo> modelo;
    String fechaActual;
    ListView listView;
    String fechaInicial ="2017-08-12";
    String fechaFinal;
    String opcionSeleccionada;
    ProgressDialog progressDialog;
    ReportesAdapter adapter;
    private OnFragmentInteractionListener mListener;

    public Reportes() {
        // Required empty public constructor
    }


    public static Reportes newInstance(int position) {
        Reportes fragment = new Reportes();
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reportes, container, false);
        //SE ASIGNAN LAS VARIABLES CON LOS CONTROLES DEL LAYOUT
        edtFechaFin = (EditText)view.findViewById(R.id.edtFechaFinal);
        edtFechaInicio= (EditText)view.findViewById(R.id.edtFechaInicial);
        btnFechaFin = (Button)view.findViewById(R.id.btnFechaFinal);
        btnFechaInicio = (Button)view.findViewById(R.id.btnFechaInicial);
        btnConsultar = (Button)view.findViewById(R.id.btnConsultar);
        spinner = (Spinner)view.findViewById(R.id.spinnerReportes);
        listView = (ListView)view.findViewById(R.id.listReportes);

        //PARA OBTENER LA FECHA ACTUAL
        final Calendar c = Calendar.getInstance();
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH) +1;
        ano=c.get(Calendar.YEAR);
        fechaActual = ano+"/"+mes+"/"+dia;
        fechaFinal = fechaActual;


        //ADAPTER DEL MENU DEL OPCIONES DEL SPINNER
        spinnerSencilloAdapter spinnerSencilloAdapter = new spinnerSencilloAdapter(listaReportes(),getContext());

        spinner.setAdapter(spinnerSencilloAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        opcionSeleccionada = "Comisiones";
                        //Inicializa el progres dialog
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("En Proceso");
                        progressDialog.setMessage("Un momento...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        //Inicia la peticion
                        RequestQueue queue = Volley.newRequestQueue(getContext());
                        String consulta = "select ord.folio,CONCAT('$',tac.total),DATE(ord.fecha),pv.tipo " +
                                "from totalarticulo_comision tac,orden ord,punto_venta pv " +
                                "where tac.orden_id=ord.id " +
                                "and ord.puntoVenta_id=pv.id " +
                                "and pv.id="+usuarioID+
                                " and tac.total>0"+
                                " and DATE(ord.fecha)>"+"'"+fechaInicial+"'"+
                                " and DATE(ord.fecha)<"+"'"+fechaFinal+"'";
                        consulta = consulta.replace(" ", "%20");
                        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                        final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                        Log.i("info", url);

                        //Hace la petición String
                        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressDialog.hide();
                                //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                                adapter = new ReportesAdapter(response,getContext());
                                listView.setAdapter(adapter);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

                            }
                        });

                        //Agrega y ejecuta la cola
                        queue.add(request);
                        break;
                    case 1:
                        opcionSeleccionada="Ventas";
                        //Inicializa el progres dialog
                        progressDialog = new ProgressDialog(getContext());
                        progressDialog.setTitle("En Proceso");
                        progressDialog.setMessage("Un momento...");
                        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        progressDialog.show();

                        //Inicia la peticion
                        RequestQueue queueVentas = Volley.newRequestQueue(getContext());
                        String consultaVentas = "select distinct ord.folio, CONCAT('$',ordc.total),DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero) " +
                                "from orden ord, orden_completa ordc, punto_venta pv, cliente cli, clave_cliente cc " +
                                "where ordc.orden_id = ord.id " +
                                "and ord.puntoVenta_id = pv.id " +
                                "and ord.cliente_id = cli.id " +
                                "and cc.cliente_id =cli.id " +
                                " and pv.id="+usuarioID+
                                " and ordc.total>0"+
                                " and DATE(ord.fecha)>"+"'"+fechaInicial+"'"+
                                " and DATE(ord.fecha)<"+"'"+fechaFinal+"'";
                        consultaVentas = consultaVentas.replace(" ", "%20");
                        String cadenaVentas = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaVentas;
                        final String urlVentas = SERVER + RUTA + "consultaGeneral.php" + cadenaVentas;
                        Log.i("info", urlVentas);

                        //Hace la petición String
                        JsonArrayRequest requestVentas = new JsonArrayRequest(Request.Method.GET, urlVentas, null, new Response.Listener<JSONArray>() {
                            @Override
                            public void onResponse(JSONArray response) {
                                progressDialog.hide();
                                //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                                adapter = new ReportesAdapter(response,getContext());
                                listView.setAdapter(adapter);

                            }
                        }, new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                progressDialog.hide();
                                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                                Toast.makeText(getContext(),  "Activos   "+urlVentas, Toast.LENGTH_SHORT).show();

                            }
                        });

                        //Agrega y ejecuta la cola
                        queueVentas.add(requestVentas);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //BOTONES PARA EL DATETIMEPICKER
        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dayF = datePicker.getDayOfMonth();
                        monthF = datePicker.getMonth()+1;
                        yearF = datePicker.getYear();
                        fechaFinal = String.valueOf(yearF)+"/"+String.valueOf(monthF)+"/"+String.valueOf(dayF);
                        edtFechaFin.setText(fechaFinal);

                    }
                },ano,mes,dia);
                datePickerDialog.show();


            }
        });

        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dayI = datePicker.getDayOfMonth();
                        monthI = datePicker.getMonth()+1;
                        yearI = datePicker.getYear();
                        fechaInicial= String.valueOf(yearI)+"/"+String.valueOf(monthI)+"/"+String.valueOf(dayI);
                         edtFechaInicio.setText(fechaInicial);

                    }
                },ano,mes,dia);
                datePickerDialog.show();

            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),fechaInicial+ "    "+fechaFinal, Toast.LENGTH_SHORT).show();
                if (opcionSeleccionada.equals("Comisiones")) {
                    //Inicializa el progres dialog
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("En Proceso");
                    progressDialog.setMessage("Un momento...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    //Inicia la peticion
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String consulta = "select ord.folio,CONCAT('$',tac.total),DATE(ord.fecha),pv.tipo " +
                            "from totalarticulo_comision tac,orden ord,punto_venta pv " +
                            "where tac.orden_id=ord.id " +
                            "and ord.puntoVenta_id=pv.id " +
                            "and pv.id="+usuarioID+
                            " and tac.total>0"+
                            " and DATE(ord.fecha)>"+"'"+fechaInicial+"'"+
                            " and DATE(ord.fecha)<"+"'"+fechaFinal+"'";
                    consulta = consulta.replace(" ", "%20");
                    String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                    final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                    Log.i("info", url);

                    //Hace la petición String
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.hide();
                            //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                            adapter = new ReportesAdapter(response,getContext());
                            listView.setAdapter(adapter);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

                        }
                    });

                    //Agrega y ejecuta la cola
                    queue.add(request);
                }else if (opcionSeleccionada.equals("Ventas")){
                    //Inicializa el progres dialog
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("En Proceso");
                    progressDialog.setMessage("Un momento...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();

                    //Inicia la peticion
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String consulta = "select distinct ord.folio, CONCAT('$',ordc.total),DATE(ord.fecha),CONCAT(pv.tipo,'-',cc.numero) " +
                            "from orden ord, orden_completa ordc, punto_venta pv, cliente cli, clave_cliente cc " +
                            "where ordc.orden_id = ord.id " +
                            "and ord.puntoVenta_id = pv.id " +
                            "and ord.cliente_id = cli.id " +
                            "and cc.cliente_id =cli.id " +
                            " and pv.id="+usuarioID+
                            " and ordc.total>0"+
                            " and DATE(ord.fecha)>"+"'"+fechaInicial+"'"+
                            " and DATE(ord.fecha)<"+"'"+fechaFinal+"'";
                    consulta = consulta.replace(" ", "%20");
                    String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                    final String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                    Log.i("info", url);

                    //Hace la petición String
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            progressDialog.hide();
                            //Toast.makeText(context, "RutasLib    "+url, Toast.LENGTH_SHORT).show();
                            adapter = new ReportesAdapter(response,getContext());
                            listView.setAdapter(adapter);

                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            progressDialog.hide();
                            Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getContext(),  "Activos   "+url, Toast.LENGTH_SHORT).show();

                        }
                    });

                    //Agrega y ejecuta la cola
                    queue.add(request);
                }
            }
        });



        return view;
    }

    public ArrayList<ModeloSpinnerGeneral>listaReportes(){
        ArrayList<ModeloSpinnerGeneral>lista = new ArrayList<>();
        String[] nombre = getResources().getStringArray(R.array.opcionesReportes);

        for (int i = 0; i <nombre.length; i++){
            lista.add(new ModeloSpinnerGeneral(nombre[i]));
        }
        return lista;
    }
    // TODO: Rename method, update argument and hook method into UI event
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
