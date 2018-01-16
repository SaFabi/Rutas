package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 16/01/2018.
 */

public class CreditosAdapter extends BaseAdapter {
    Context context;
    List<ModeloCreditos>elementos;

    public CreditosAdapter(Context context, List<ModeloCreditos> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public ModeloCreditos getItem(int i) {
        return elementos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return elementos.get(i).getOrdenID();
    }

    public long getTotal(int i) {
        return elementos.get(i).getMontoTotal();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (view == null)
        view = inflater.inflate(R.layout.modelocreditosgeneral,null);

        TextView txtFolio = view.findViewById(R.id.folio);
        TextView txtTotal = view.findViewById(R.id.Total);
        TextView txtFecha = view.findViewById(R.id.fechaCredito);
        TextView txtClave = view.findViewById(R.id.ClaveCliente);

        txtFolio.setText(getItem(i).getFolio());
        txtClave.setText(getItem(i).getClaveCliente());
        txtTotal.setText(getItem(i).getMontoTotal());
        txtFecha.setText(getItem(i).getFecha());
        return view;
    }
}

