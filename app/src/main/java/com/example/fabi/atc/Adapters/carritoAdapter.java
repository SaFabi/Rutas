package com.example.fabi.atc.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.R;

import java.util.ArrayList;

/**
 * Created by Fabi on 15/02/2018.
 */

public class carritoAdapter extends BaseAdapter {
    ArrayList<ModeloInventarioPersonal>carrito = new ArrayList<>();
     Context context;
    Button btnEliminar;
    Button btnsumar;
    Button btnrestar;

    public carritoAdapter(ArrayList<ModeloInventarioPersonal> carrito, Context context) {
        this.carrito = carrito;
        this.context = context;
    }

    @Override
    public int getCount() {
        return carrito.size();
    }

    @Override
    public ModeloInventarioPersonal getItem(int i) {
        return carrito.get(i);
    }

    @Override
    public long getItemId(int i) {
        return carrito.get(i).getCantidadID();
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View vista = view;
        if (vista == null) {
            vista = inflater.inflate(R.layout.cardcarrito, null);
        }

            TextView marca = (TextView)vista.findViewById(R.id.txtTipoArt);
            TextView modelo = (TextView)vista.findViewById(R.id.txtMarcayModelo);
            TextView precio = (TextView)vista.findViewById(R.id.txtPrecioCarrito);
            final EditText cantidad = (EditText)vista.findViewById(R.id.edtCantidadCarrito);

            btnsumar = (Button)vista.findViewById(R.id.btnSumarCarrito);
            btnrestar = (Button)vista.findViewById(R.id.btnRestarCarrito);
            btnEliminar = (Button)vista.findViewById(R.id.eliminarCarrito);

            marca.setText(getItem(position).getMarca());
            modelo.setText(getItem(position).getModelo());
            precio.setText(getItem(position).getPrecio());
            cantidad.setText(getItem(position).getCantidad());

            btnsumar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int CantidadActual = Integer.parseInt(cantidad.getText().toString());
                    CantidadActual= CantidadActual+1;
                    cantidad.setText(String.valueOf(CantidadActual));
                }
            });

            btnrestar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int CantidadActual = Integer.parseInt(cantidad.getText().toString());
                    if (CantidadActual == 0){

                    }else{
                        CantidadActual= CantidadActual-1;
                        cantidad.setText(String.valueOf(CantidadActual));
                    }
                }
            });

        btnEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                dialogo1.setIcon(R.drawable.cancelar);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Desea eliminar este elemento?");
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        carrito.remove(position);

                    }
                });

                dialogo1.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialogo1.show();
                notifyDataSetChanged();

            }
        });
        if (carrito.size() == 0){
            Toast.makeText(context, "No hay articulos en el carrito", Toast.LENGTH_SHORT).show();
        }
        return vista;
    }
}
