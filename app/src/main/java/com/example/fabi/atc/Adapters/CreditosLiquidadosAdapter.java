package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 24/01/2018.
 */

public class CreditosLiquidadosAdapter extends BaseAdapter {

    Context context;
    List<ModeloCreditos>elementos;

    public CreditosLiquidadosAdapter(Context context, List<ModeloCreditos> elementos) {
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
        return elementos.get(i).getCreditoID();
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
        Button btnregistrarAbono = view.findViewById(R.id.registrarAbono);
        btnregistrarAbono.setVisibility(View.INVISIBLE);

        txtFolio.setText("Folio "+getItem(i).getFolio());
        txtClave.setText(getItem(i).getClaveCliente());
        txtTotal.setText(String.valueOf("Monto Total $"+getItem(i).getMontoTotal()));
        txtFecha.setText(getItem(i).getFecha());
        return view;
    }
}
