package com.example.fabi.atc.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Fabi on 20/12/2017.
 */

public class ProductosAdapter extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView titulo, subtitulo,precio;
    rutasLib rutasObj;
    Button carrito;
    //ImageView imageView;
    public ProductosAdapter(JSONArray array, Context context) {
        this.array= array;
        this.context = context;
    }


    @Override
    public int getCount() {
        return array.length();
    }

    @Override
    public JSONObject getItem(int i) {
        JSONObject jsonObject;

        try
        {
            jsonObject = array.getJSONObject(i);
        }
        catch (JSONException e)
        {
            jsonObject = null;
        }

        return jsonObject;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, final View convertView, final ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.card,null);
        }
        titulo=(TextView)view.findViewById(R.id.titulocard);
        subtitulo = (TextView)view.findViewById(R.id.subtitulocard);
        precio = (TextView)view.findViewById(R.id.precio);
        carrito = (Button)view.findViewById(R.id.btnCarrito);
        carrito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                final EditText textoBusqueda = new EditText(context);
                textoBusqueda.setInputType(1);
                builder.setTitle("Cantidad");   // Título
                builder.setView(textoBusqueda);
                builder.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(context,"Se agregaron al carrito",Toast.LENGTH_SHORT).show();
                       // Log.i("Algo", textoBusqueda.getText().toString());
                    }
                });
                builder .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogo1, int id) {
                    }
                });
                builder.show();

            }
        });
      //  imageView = (ImageView)view.findViewById(R.id.imagencard);
        String titulo2, subtitulo2, imagen;
        try
        {
            titulo2= getItem(i).getString("0");
            subtitulo2 = getItem(i).getString("1");
            imagen = getItem(i).getString("2");
        }
        catch (JSONException e)
        {
            titulo2= null;
            subtitulo2 = null;
            imagen= null;
        }
        if (titulo2 != null) {
            titulo.setText(titulo2);
            subtitulo.setText(subtitulo2);
            precio.setText("$"+imagen);
            // String http = imagen;
/*

            Glide.with(viewGroup.getContext())
                    .load(rutasObj.URL + http)
                    .crossFade()
                    .centerCrop()
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .thumbnail(0.5f)
                    .into(imageView);
        */
        }
        return view;
    }

/*
    public void setFilter(ArrayList<Modelo>listamodelos){
        this.modelo= new ArrayList<>();
        this.modelo.addAll(listamodelos);
        notifyDataSetChanged();


    }
    */

}

