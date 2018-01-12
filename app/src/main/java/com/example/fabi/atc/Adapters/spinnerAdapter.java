package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

/**
 * Created by Fabi on 03/01/2018.
 */

public class spinnerAdapter extends BaseAdapter {
    Context context;
    List<Modelo> elementos;
    TextView ciudad;

    public spinnerAdapter(Context context, List<Modelo> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public Modelo getItem(int i) {
        return elementos.get(i);
    }


    @Override
    public long getItemId(int i) {
        return  elementos.get(i).getCiudadID();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.modelo_spinner,null);
        }
        ciudad=(TextView)view.findViewById(R.id.spinnerCiudad);

        ciudad.setText(getItem(i).getCiudad());
        return view;

    }
}
