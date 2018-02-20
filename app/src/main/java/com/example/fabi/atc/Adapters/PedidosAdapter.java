package com.example.fabi.atc.Adapters;

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

import com.example.fabi.atc.Clases.ModeloPedidos;
import com.example.fabi.atc.Fragmentos.CarritoFragment;
import com.example.fabi.atc.Fragmentos.DetallesPedidos;
import com.example.fabi.atc.R;

import java.util.List;

/**
 * Created by Fabi on 23/01/2018.
 */

public class PedidosAdapter extends BaseAdapter {
    Context context;
    List<ModeloPedidos>elementos;
    FragmentManager fragmentManager;

    public PedidosAdapter(Context context, List<ModeloPedidos> elementos, FragmentManager fragmentManager) {
        this.context = context;
        this.elementos = elementos;
        this.fragmentManager = fragmentManager;
    }

    @Override
    public int getCount() {
        return elementos.size();
    }

    @Override
    public ModeloPedidos getItem(int i) {
        return elementos.get(i);
    }

    @Override
    public long getItemId(int i) {
        return elementos.get(i).getOrdenID();
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View vista = view;
        if (vista == null){
            vista = inflater.inflate(R.layout.modelopedidos,null);
        }
        TextView txtFolio = (TextView)vista.findViewById(R.id.foliopedido);
        TextView txtCliente = (TextView)vista.findViewById(R.id.clientepedido);
        TextView txtFecha = (TextView)vista.findViewById(R.id.fechapedido);
        TextView txtMonto = (TextView)vista.findViewById(R.id.Montopedido);
        Button btnTerminarVenta = (Button)vista.findViewById(R.id.btnTerminarVenta);

        btnTerminarVenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = CarritoFragment.newInstance(getItem(i).getOrdenID());
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }
        });

        txtFolio.setText("Folio: "+getItem(i).getFolio());
        txtCliente.setText("Cliente: "+getItem(i).getCliente());
        txtFecha.setText("Fecha de Pedido: "+getItem(i).getFecha());
        txtMonto.setText("Monto total $"+getItem(i).getTotal());

        return vista;
    }
}
