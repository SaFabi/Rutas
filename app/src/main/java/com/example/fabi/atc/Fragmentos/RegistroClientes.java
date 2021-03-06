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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
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
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Adapters.spinnerAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class RegistroClientes extends Fragment implements Basic, Response.Listener<JSONArray>, Response.ErrorListener{
    //FRAGMENTO PROBADO.PERMITE AÑADIR UN NUEVO CLIENTE A LA BASE DE DATOS
    //CONTROLES
    Spinner spinner;
    Button btnAgregar;
    EditText edtNombre, edtDireccion,edtTelefono,edtCorreo;
    EditText edtClave;
    private ProgressDialog progressDialog;
    //VARIABLES
    String url;
    String urlClave;
   int ciudadID;
    String Nuevaclave;
    String puntoVenta;
    String ultimoUsuario;
    //ADAPTERS
    rutasLib rutasObj = new rutasLib();
    spinnerAdapter adapter;

    public RegistroClientes() {
        // Required empty public constructor
    }
    public static RegistroClientes newInstance(int position) {
        RegistroClientes fragment = new RegistroClientes();
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
                             final Bundle savedInstanceState) {
       final View view= inflater.inflate(R.layout.fragment_registro_clientes, container, false);
        //ASIGNACION DE VARIABLES CON SUS CONTROLES
        setHasOptionsMenu(true);
        spinner = (Spinner)view.findViewById(R.id.Ciudades);
        btnAgregar = (Button)view.findViewById(R.id.btnAgregarUsuario);
        edtNombre = (EditText)view.findViewById(R.id.edtNombre);
        edtDireccion = (EditText)view.findViewById(R.id.edtDireccion);
        edtCorreo = (EditText)view.findViewById(R.id.edtCorreo);
        edtTelefono = (EditText)view.findViewById(R.id.edtTelefono);
        edtClave = (EditText)view.findViewById(R.id.edtClave);
        puntoVenta = rutasObj.sacarPuntoVenta(PUNTOVENTA);

        //FUNCIONALIDAD DEL BOTON PARA AGREGAR UN CLIENTE
        btnAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //PARA INSERTAR EN LA TABLA CLIENTE

                if (edtNombre.getText().length() >0 && edtDireccion.getText().length()>0 &&
                        edtTelefono.getText().length() > 0 && edtCorreo.getText().length() > 0 && edtClave.getText().length() >0){

                    //Inicializa el progres dialog
                    progressDialog = new ProgressDialog(getContext());
                    progressDialog.setTitle("En Proceso");
                    progressDialog.setMessage("Un momento...");
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.show();
                    //Inicia la peticion
                    //INICIA LA CONSULTA PARA REGISTRAR UN NUEVO CLIENTE
                    RequestQueue queue = Volley.newRequestQueue(getContext());
                    String consulta = "CALL registroCliente("+Nuevaclave+",'"+puntoVenta+"','"+edtNombre.getText().toString()+"','"+edtDireccion.getText().toString()
                            +"','"+edtTelefono.getText().toString()+"','"+edtCorreo.getText().toString()+"',"+1+","+ciudadID+");";
                    consulta = consulta.replace(" ", "%20");
                    String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
                    String url = SERVER + RUTA + "consultaGeneral.php" + cadena;
                    Log.i("info", url);
                    //Hace la petición String
                    JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                        @Override
                        public void onResponse(JSONArray response) {
                            JSONObject jsonObject;
                            try {
                                jsonObject =response.getJSONObject(0);
                            }catch (Exception e){
                                jsonObject = new JSONObject();
                            }


                            try {
                                Nuevaclave = jsonObject.getString("1");

                            }catch (Exception e){
                                puntoVenta = null;
                            }
                            if (Nuevaclave != null){
                                edtClave.setText(puntoVenta + "-" + Nuevaclave);
                            }


                        }
                    }, new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                        }
                    });
                    queue.add(request);

                }else{
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(getActivity());
                    dialogo1.setIcon(R.drawable.cancelar);
                    dialogo1.setTitle("Importante");
                    dialogo1.setMessage("Debes completar todos los campos");
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

        //OPCIONES DEL MENU DEL SPINNER
       spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
           @Override
           public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               ciudadID = (int)adapter.getItemId(i);
               //Toast.makeText(getContext(), "ID: " + String.valueOf(ciudadID), Toast.LENGTH_SHORT).show();
           }
           @Override
           public void onNothingSelected(AdapterView<?> adapterView) {

           }
       });
        //Se declara el progress dialog para ejecutar despues la consulta
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //Inicia la peticion para llenar el Spinner  con las ciudades
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String consulta = "select id,nombre from ciudad order by nombre asc";
        consulta = consulta.replace(" ", "%20");
        String cadena = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consulta;
        url = SERVER + RUTA + "consultaGeneral.php" + cadena;
        Log.i("info", url);

        //Inicia la peticion para obtener la ultima  clave del cliente
        RequestQueue queueClave = Volley.newRequestQueue(getContext());
        String consultaClave ="select pv.tipo,cc.numero%2B1 "+
       "from clave_cliente cc, punto_Venta pv "+
        "where cc.puntoVenta_id  = pv.id "+
        "and pv.tipo ='"+puntoVenta+
        "' order by cc.numero DESC limit 1";
        consultaClave = consultaClave.replace(" ", "%20");
        String cadenaClave = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaClave;
        urlClave = SERVER + RUTA + "consultaGeneral.php" + cadenaClave;
        Log.i("info", urlClave);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, this, this);

        //Para el proceso de obtencion de la ultima clave del cliente
        JsonArrayRequest requestClave = new JsonArrayRequest(Request.Method.GET, urlClave, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                JSONObject jsonObject;
                try {
                    jsonObject =response.getJSONObject(0);
                }catch (Exception e){
                    jsonObject = new JSONObject();
                }


                try {
                    puntoVenta = jsonObject.getString("0");
                    Nuevaclave = jsonObject.getString("1");

                }catch (Exception e){
                    Nuevaclave = null;
                    puntoVenta = null;
                }
                if (Nuevaclave != null){
                    edtClave.setText(puntoVenta + "-" + Nuevaclave);
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), urlClave, Toast.LENGTH_SHORT).show();
                Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
            }
        });

        //Agrega y ejecuta la cola
        queue.add(request);
        queueClave.add(requestClave);


        return view;
    }

    //INFLA EL MENU DE OPCIONES
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        MenuItem buscar, carrito;
             buscar = menu.findItem(R.id.buscador2);
            carrito = menu.findItem(R.id.carrito);

            buscar.setVisible(false);
            carrito.setVisible(false);



    }

    //RESPUESTA DE LA CONSULTA GENERAL
    @Override
    public void onErrorResponse(VolleyError error) {
        progressDialog.hide();
        Toast.makeText(getContext(), "Error en el WebService", Toast.LENGTH_SHORT).show();
        //Toast.makeText(getContext(),  "Telefonos    "+url, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResponse(JSONArray response) {
        progressDialog.hide();
        // Toast.makeText(getContext(), "Telefonos    "+url, Toast.LENGTH_SHORT).show();
        adapter = new spinnerAdapter(getContext(),Modelo.ListaSpinner(response));
        spinner.setAdapter(adapter);
    }

}
