package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 22/01/2018.
 */

public class InventarioPersonalAdapter extends BaseAdapter {
    Context context;
    List<ModeloInventarioPersonal> elementos;

    public InventarioPersonalAdapter(Context context, List<ModeloInventarioPersonal> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public ModeloInventarioPersonal getItem(int i) {
        return elementos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return elementos.get(i).getCantidadID();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View vista = view;
        if (vista == null){
            vista = inflater.inflate(R.layout.card,null);
        }
        TextView txtMarca = (TextView)vista.findViewById(R.id.titulocard);
        TextView txtModelo = (TextView)vista.findViewById(R.id.subtitulocard);
        TextView txtPrecio = (TextView)vista.findViewById(R.id.precio);
        TextView txtCantidad = (TextView)vista.findViewById(R.id.cantidadproductos);

        txtMarca.setText(getItem(i).getMarca());
        txtModelo.setText(getItem(i).getModelo());
        txtPrecio.setText(getItem(i).getPrecio());
        txtCantidad.setText("Cantidad disponible: "+getItem(i).getCantidad());
        return vista;
    }
}
