package com.example.fabi.atc.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloInventarioPersonal;
import com.example.fabi.atc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/01/2018.
 */

public class InventarioPersonalAdapter extends BaseAdapter {
    Context context;
    List<ModeloInventarioPersonal> elementos;
    String fragmento;

    public InventarioPersonalAdapter(Context context, List<ModeloInventarioPersonal> elementos, String fragmento) {
        this.context = context;
        this.elementos = elementos;
        this.fragmento =fragmento;
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
        Button btncarrito = (Button)vista.findViewById(R.id.btnCarrito);
        if (fragmento.equals("Chips")){
            btncarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                    final EditText textoBusqueda = new EditText(context);
                    dialogo1.setIcon(R.drawable.agregar);
                    dialogo1.setTitle("Cantidad");
                    textoBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                    dialogo1.setView(textoBusqueda);
                    dialogo1.setCancelable(false);
                    dialogo1.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            if (textoBusqueda.getText().length() >0){
                                int  Cantidad =Integer.parseInt(textoBusqueda.getText().toString());
                               // Toast.makeText(context,String.valueOf(Cantidad),Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(context, "Introduzca una cantidad", Toast.LENGTH_SHORT).show();
                            }


                        }
                    });
                    dialogo1.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    dialogo1.show();
                }
            });

        }else{
            btncarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numerPickerDialog();
                }
            });
        }

        /*
        btncarrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder dialogo1 = new AlertDialog.Builder(context);
                final EditText textoBusqueda = new EditText(context);
                dialogo1.setIcon(R.drawable.cancelar);
                dialogo1.setTitle("Importante");
                dialogo1.setMessage("¿Desea eliminar este elemento?");
                textoBusqueda.setInputType(InputType.TYPE_CLASS_NUMBER);
                dialogo1.setView(textoBusqueda);
                dialogo1.setCancelable(false);
                dialogo1.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                dialogo1.show();
            }
        });
        */
        txtMarca.setText(getItem(i).getMarca());
        txtModelo.setText(getItem(i).getModelo());
        txtPrecio.setText("$"+getItem(i).getPrecio());
        txtCantidad.setText("Cantidad disponible: "+getItem(i).getCantidad());
        return vista;
    }

    public void numerPickerDialog(){
        NumberPicker numberPicker = new NumberPicker(context);
        numberPicker.setMaxValue(100);
        numberPicker.setMinValue(1);
        NumberPicker.OnValueChangeListener valueChangeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker numberPicker, int i, int i1) {
               // Toast.makeText(context,String.valueOf(i1),Toast.LENGTH_SHORT).show();
            }
        };
        numberPicker.setOnValueChangedListener(valueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(numberPicker);
        builder.setTitle("Cantidad");
        builder.setIcon(R.drawable.agregar);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();

    }

    public void setFilter(List<ModeloInventarioPersonal> lista){
        this.elementos = new ArrayList<>();
        this.elementos.addAll(lista);
        notifyDataSetChanged();

    }
}

