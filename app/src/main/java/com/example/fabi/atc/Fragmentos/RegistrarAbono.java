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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Adapters.ClientesAdapter;
import com.example.fabi.atc.Adapters.CreditosAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.R;

import org.json.JSONArray;

public class RegistrarAbono extends Fragment  implements Basic{
    int ordenID;
    int MontoTotal;
    int ClienteID;
    EditText edtMonto, edtAbono;
    ListView listView;
   AdapterClientes adapter;

    ProgressDialog progressDialog;


    private OnFragmentInteractionListener mListener;

    public RegistrarAbono() {
        // Required empty public constructor
    }

    public static RegistrarAbono newInstance(int OrdenID, int MontoTotal, int ClienteID) {
        RegistrarAbono fragment = new RegistrarAbono();
        Bundle args = new Bundle();

        args.putInt("ordenID",OrdenID);
        args.putInt("totalOrden",MontoTotal);
        args.putInt("ClienteID",ClienteID);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ordenID = getArguments().getInt("ordenID");
            MontoTotal = getArguments().getInt("totalOrden");
            ClienteID = getArguments().getInt("ClienteID");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registrar_abono, container, false);
        edtMonto = (EditText)vista.findViewById(R.id.edtmontoTotal);
        edtMonto.setText(String.valueOf(MontoTotal));
        listView = (ListView)vista.findViewById(R.id.Abonos);

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("En Proceso");
        progressDialog.setMessage("Un momento...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        //CONSULTA PATA OBTENER TODOS LOS CREDITOS REGISTRADOS DE UN CLIENTE EN ESPECIFICO
        RequestQueue queueCreditos = Volley.newRequestQueue(getContext());
        String consultaCreditos = "SELECT Distinct c.id, o.folio, DATE(o.fecha) AS fecha,bc.cantidad ,(c.total - " +
                " (SELECT Sum(bc.cantidad) as total_abonos" +
                " FROM credito c, orden o, bono_credito bc" +
                " WHERE c.orden_id = o.id" +
                " AND bc.credito_id = c.id" +
                " AND o.cliente_id = 207" +
                " AND bc.credito_id = 4" +
                " AND c.id = bc.credito_id)) as total_momento" +
                " from credito c, orden o, bono_credito bc" +
                " WHERE c.orden_id = o.id" +
                " AND bc.credito_id = c.id" +
                " AND o.cliente_id =" +ClienteID+
                " AND c.orden_id =" +ordenID+
                " and bc.cantidad > 0; ";
        consultaCreditos = consultaCreditos.replace(" ", "%20");
        String cadenaCreditos = "?host=" + HOST + "&db=" + DB + "&usuario=" + USER + "&pass=" + PASS + "&consulta=" + consultaCreditos;
        String url = SERVER + RUTA + "consultaGeneral.php" + cadenaCreditos;
        Log.i("info", url);
        JsonArrayRequest requestCreditos = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progressDialog.hide();
                adapter = new AdapterClientes(getContext(),ModeloClientes.sacarListaClientes(response));
                listView.setAdapter(adapter);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queueCreditos.add(requestCreditos);


        return vista;
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
