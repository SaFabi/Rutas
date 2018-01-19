package com.example.fabi.atc.Adapters;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabi.atc.Clases.ModeloCreditos;
import com.example.fabi.atc.Fragmentos.RegistrarAbono;
import com.example.fabi.atc.Fragmentos.detallesCreditosPendientes;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 16/01/2018.
 */

public class CreditosAdapter extends BaseAdapter {
    Context context;
    List<ModeloCreditos>elementos;
    FragmentManager fragmentManager;
    int OrdenID;
    int Monto;

    public CreditosAdapter(FragmentManager fragmentManager, Context context, List<ModeloCreditos> elementos, int Orden, int total) {
        this.context = context;
        this.elementos = elementos;
        this.fragmentManager = fragmentManager;
        this.OrdenID = Orden;
        this.Monto = total;
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
    public long getClienteID (int i){
        return elementos.get(i).getClienteID();
    }
    public  long getCreditoID(int i){
        return elementos.get(i).getCreditoID();
    }


    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (view == null)
        view = inflater.inflate(R.layout.modelocreditosgeneral,null);

        TextView txtFolio = view.findViewById(R.id.folio);
        TextView txtTotal = view.findViewById(R.id.Total);
        TextView txtFecha = view.findViewById(R.id.fechaCredito);
        TextView txtClave = view.findViewById(R.id.ClaveCliente);
        Button btnregistrarAbono = view.findViewById(R.id.registrarAbono);

        btnregistrarAbono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(context,"Agregar abono",Toast.LENGTH_SHORT).show();
                 Fragment fragment = RegistrarAbono.newInstance((int)getItemId(i),(int)getTotal(i), (int)getClienteID(i), (int)getCreditoID(i));
                FragmentTransaction fragmentTransaction =fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });

        txtFolio.setText("Folio "+getItem(i).getFolio());
        txtClave.setText(getItem(i).getClaveCliente());
        txtTotal.setText(String.valueOf("Monto Total $"+getItem(i).getMontoTotal()));
        txtFecha.setText(getItem(i).getFecha());
        return view;
    }
}

