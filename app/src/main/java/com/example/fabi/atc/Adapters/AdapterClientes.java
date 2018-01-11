package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 11/01/2018.
 */

public class AdapterClientes extends BaseAdapter {
    Context context;
    List<ModeloClientes> elementos;

    public AdapterClientes(Context context, List<ModeloClientes> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public ModeloClientes getItem(int i) {
        return elementos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return elementos.get(i).getId();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View vista = view;
        if (vista == null){
            vista = inflater.inflate(R.layout.modelo_clientes,null);
        }
        TextView txtNombre = (TextView)vista.findViewById(R.id.nombreCliente);
        TextView txtDireccion = (TextView)vista.findViewById(R.id.ciudadCliente);
        TextView txtTelefono = (TextView)vista.findViewById(R.id.telefonoCliente);
        TextView txtClave = (TextView)vista.findViewById(R.id.claveR);

        txtNombre.setText(getItem(i).getNombre());
        txtDireccion.setText(getItem(i).getCiudad());
        txtTelefono.setText(getItem(i).getTelefono());
        txtClave.setText(getItem(i).getClaveR());

        return vista;
    }
}
