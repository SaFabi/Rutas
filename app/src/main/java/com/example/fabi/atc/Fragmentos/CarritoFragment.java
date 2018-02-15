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
import com.example.fabi.atc.Adapters.carritoAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.R;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class CarritoFragment extends Fragment implements Basic {
    ProgressDialog progressDialog;
    ListView listView;
    AdapterClientes adapter;
    carritoAdapter carritoAdapter;
    int Montototal;

    private static final String ARG_POSITION = "POSITION";
    private int mPosition;
     static ArrayList<ModeloInventarioPersonal> carritoFinal = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public CarritoFragment() {
    }
    public static CarritoFragment newInstance(ArrayList<ModeloInventarioPersonal>carrito) {
        CarritoFragment fragment = new CarritoFragment();
        Bundle args = new Bundle();
        carritoFinal =carrito;
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
       View view = inflater.inflate(R.layout.fragment_carrito, container, false);
        listView = (ListView)view.findViewById(R.id.listaproductoscarrito);
        TextView txtMonto = (TextView)view.findViewById(R.id.montototalcarrito);
            carritoAdapter = new carritoAdapter(InventarioPersonalAdapter.carrito,getContext());
            listView.setAdapter(carritoAdapter);
        for (int i=0;i<carritoFinal.size();i++){
            Montototal += Integer.parseInt(carritoFinal.get(i).getPrecio());

        }
        txtMonto.setText(String.valueOf(Montototal));



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
