package com.example.fabi.atc.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.fabi.atc.Clases.ModeloInventarioGeneral;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProductosAdapter extends BaseAdapter {

   List<ModeloInventarioGeneral>elementos;
    Context context;
    TextView titulo, subtitulo,precio;

    public ProductosAdapter(List<ModeloInventarioGeneral> elementos, Context context) {
        this.elementos = elementos;
        this.context = context;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public ModeloInventarioGeneral getItem(int i) {
        return elementos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, final View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.cardinventariogral,null);
        }
        titulo=(TextView)view.findViewById(R.id.marca);
        subtitulo = (TextView)view.findViewById(R.id.modelo);
        precio = (TextView)view.findViewById(R.id.precioGeneral);

        titulo.setText(getItem(i).getMarcaIG());
        subtitulo.setText(getItem(i).getModeloIG());
        precio.setText(getItem(i).getPrecioIG());
        return view;
    }


    public void setFilter(List<ModeloInventarioGeneral>listamodelos){
        this.elementos= new ArrayList<>();
        this.elementos.addAll(listamodelos);
        notifyDataSetChanged();
    }


}

