package com.example.fabi.atc.Adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ProductosAdapter extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView titulo, subtitulo,precio, txtcantidad;
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
            view = inflater.inflate(R.layout.cardinventariogral,null);
        }
        titulo=(TextView)view.findViewById(R.id.marca);
        subtitulo = (TextView)view.findViewById(R.id.modelo);
        precio = (TextView)view.findViewById(R.id.precioGeneral);
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

