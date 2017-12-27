package com.example.fabi.atc.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.ProductosAdapter;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Clientes extends Fragment {

    //Fragmento para los accesorios
    private static final String ARG_POSITION= "POSITION";

    ListView listView;
    rutasLib rutasObj;

    private int  mPosition;

    private OnFragmentInteractionListener mListener;

    public Clientes() {
        // Required empty public constructor
    }

    public static Clientes newInstance(int position) {
        Clientes fragment = new Clientes();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_clientes, container, false);
        listView= (ListView)view.findViewById(R.id.lvFClientes);
        EnviarRecibirDatos(rutasObj.Consulta+"consultaChips.php");
        return view;
    }
    public void EnviarRecibirDatos(String URL){
        RequestQueue queue = Volley.newRequestQueue(getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                response = response.replace("][",",");
                if (response.length() > 0){
                    try {
                        JSONArray ja = new JSONArray(response);
                        Log.i("SizeJson",""+ja.length());
                        CargarListView(ja);
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.i("error",""+error.getMessage());
            }
        });
        queue.add(stringRequest);
    }

    public void CargarListView(JSONArray ja){
        ArrayList<Modelo> lista = new ArrayList<>();

        for (int i = 0; i<ja.length();i+=3){
            try {
                lista.add(new Modelo(ja.getString(i),ja.getString(i+1),ja.getString(i+2)));

            }catch(JSONException e){
                e.printStackTrace();
            }

        }
        ProductosAdapter adapter= new ProductosAdapter(lista,getContext());
        listView.setAdapter(adapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }
*/
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
