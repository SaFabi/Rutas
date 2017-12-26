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

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Catalogo.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Catalogo#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Catalogo extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    ListView listView;
    rutasLib rutasObj;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Catalogo() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static Catalogo newInstance(String param1, String param2) {
        Catalogo fragment = new Catalogo();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_catalogo, container, false);

        listView= (ListView)view.findViewById(R.id.lvCatalogo);
        EnviarRecibirDatos(rutasObj.Consulta+"consultaTelefonos.php");
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
