package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.R;

import java.util.ArrayList;

/**
 * Created by Fabi on 20/12/2017.
 */

public class ProductosAdapter extends BaseAdapter {
    private ArrayList<Modelo> modelo;
    private Context context;
    TextView titulo, subtitulo;
    ImageView imageView;
    String url = "http://192.168.1.71/CatalogoATC/img/";
    public ProductosAdapter(ArrayList<Modelo> modelo, Context context) {
        this.modelo = modelo;
        this.context = context;
    }


    @Override
    public int getCount() {
        return modelo.size();
    }

    @Override
    public Modelo getItem(int i) {
        return modelo.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.card,null);
        }
        titulo=(TextView)view.findViewById(R.id.titulocard);
        subtitulo = (TextView)view.findViewById(R.id.subtitulocard);
        imageView = (ImageView)view.findViewById(R.id.imagencard);

        titulo.setText(getItem(i).getCorreo());
        subtitulo.setText(getItem(i).getPass());
        String http = getItem(i).getImagen();


        Glide.with(viewGroup.getContext())
                .load(url+http)
                .crossFade()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .thumbnail(0.5f)
                .into(imageView);
        return view;
    }
}
