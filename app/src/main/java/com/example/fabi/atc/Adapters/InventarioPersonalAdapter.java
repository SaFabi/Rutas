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
import com.example.fabi.atc.Fragmentos.CarritoFragment;
import com.example.fabi.atc.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/01/2018.
 */

public class InventarioPersonalAdapter extends BaseAdapter {
    Context context;
    List<ModeloInventarioPersonal> elementos;
     public static  ArrayList<ModeloInventarioPersonal>carrito =new ArrayList<>();;
    String fragmento;
    int Cantidad;
    String marca,modelo,cantidad,precio;
    int CantidadID;

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

    public ArrayList<ModeloInventarioPersonal> regresarcarrito(){
        return carrito;
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

        marca = getItem(i).getMarca();
        modelo = getItem(i).getModelo();
        precio = getItem(i).getPrecio();
        cantidad = getItem(i).getCantidad();
        CantidadID = getItem(i).getCantidadID();

        txtMarca.setText(marca);
        txtModelo.setText(modelo);
        txtPrecio.setText("$"+precio);
        txtCantidad.setText("Cantidad disponible: "+cantidad);

        if (fragmento.equals("Chips")){
            //SI LOS ARTICULOS A AGREGAR SON CHIPS SE HACE ESTE PROCEDIMIENTO
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
                                Cantidad =Integer.parseInt(textoBusqueda.getText().toString());
                                AlertDialog.Builder dialogoCantidad = new AlertDialog.Builder(context);
                                final EditText cantidad = new EditText(context);
                                dialogoCantidad.setIcon(R.drawable.agregar);
                                dialogoCantidad.setTitle("Cantidad");
                                cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                                dialogoCantidad.setView(cantidad);
                                dialogoCantidad.setCancelable(false);
                                dialogoCantidad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        precio = cantidad.getText().toString();
                                        carrito.add(new ModeloInventarioPersonal(CantidadID,marca,modelo,precio,String.valueOf(Cantidad)));
                                        Toast.makeText(context,marca+" "+modelo,Toast.LENGTH_SHORT).show();
                                        Toast.makeText(context, "Se agrego al carrito ", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(context,String.valueOf(Cantidad), Toast.LENGTH_SHORT).show();

                                    }
                                });
                                dialogoCantidad.show();

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

            //SI LOS ARTICULOS NO SON CHIPS SE SIGUE ESTE PROCEDIMIENTO
            btncarrito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context,String.valueOf(CantidadID), Toast.LENGTH_SHORT).show();
                    numerPickerDialog();
                }
            });
        }

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
                Cantidad = i1;
            }
        };
        numberPicker.setOnValueChangedListener(valueChangeListener);
        AlertDialog.Builder builder = new AlertDialog.Builder(context).setView(numberPicker);
        builder.setTitle("Cantidad");
        builder.setIcon(R.drawable.agregar);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AlertDialog.Builder dialogoCantidad = new AlertDialog.Builder(context);
                final EditText cantidad = new EditText(context);
                dialogoCantidad.setIcon(R.drawable.agregar);
                dialogoCantidad.setTitle("Inserte el precio");
                cantidad.setInputType(InputType.TYPE_CLASS_NUMBER);
                cantidad.setText(precio);
                dialogoCantidad.setView(cantidad);
                dialogoCantidad.setCancelable(false);
                dialogoCantidad.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        precio = cantidad.getText().toString();
                        carrito.add(new ModeloInventarioPersonal(CantidadID,marca,modelo,precio,String.valueOf(Cantidad)));
                        Toast.makeText(context,marca+" "+modelo,Toast.LENGTH_SHORT).show();
                        Toast.makeText(context, "Se agrego al carrito ", Toast.LENGTH_SHORT).show();
                        Toast.makeText(context,String.valueOf(Cantidad), Toast.LENGTH_SHORT).show();

                    }
                });
                dialogoCantidad.show();
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

