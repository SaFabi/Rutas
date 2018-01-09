package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by Fabi on 09/01/2018.
 */

public class spinnerSencilloAdapter extends BaseAdapter {
    ArrayList<Modelo> elementos;
    Context context;

    public spinnerSencilloAdapter(ArrayList<Modelo> elementos, Context context) {
        this.elementos = elementos;
        this.context = context;
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
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.modelo_spinner,null);
        }

        TextView opcion = (TextView)view.findViewById(R.id.spinnerCiudad);
        opcion.setText(getItem(i).getCiudad());


        return view;
    }
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = convertView;
        if(convertView == null){
            view = inflater.inflate(R.layout.modelo_spinner, null);
        }
        //Relaciona cada elemento con su view
        TextView titulo = (TextView)view.findViewById(R.id.spinnerCiudad);


        //Coloca los valores de cada elemento
        titulo.setText(getItem(position).getCiudad());

        return view;
    }
}
