package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

/**
 * Created by Fabi on 02/01/2018.
 */

public class ClientesAdapter extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView Nombre, Ciudad,Telefono;


    public ClientesAdapter(JSONArray array, Context context) {
        this.array = array;
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
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.modelo_clientes,null);
        }
        Nombre = (TextView)view.findViewById(R.id.nombreCliente);
        Telefono = (TextView)view.findViewById(R.id.telefonoCliente);
        Ciudad = (TextView)view.findViewById(R.id.ciudadCliente);

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
            Nombre.setText(titulo2);
            Ciudad.setText(subtitulo2);
            Telefono.setText(imagen);
        }
        return view;
    }
}
