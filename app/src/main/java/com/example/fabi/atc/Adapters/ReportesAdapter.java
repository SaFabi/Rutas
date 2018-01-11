package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Fabi on 11/01/2018.
 */
//ADAPTER PARA LOS REPORTES EN GENERAL
public class ReportesAdapter extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView folio,total,fecha,otros;

    public ReportesAdapter(JSONArray array, Context context) {
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
        folio = (TextView)view.findViewById(R.id.nombreCliente);
       total = (TextView)view.findViewById(R.id.telefonoCliente);
        fecha = (TextView)view.findViewById(R.id.ciudadCliente);
        otros = (TextView)view.findViewById(R.id.claveR);

        String titulo2, subtitulo2, imagen, claveRuta;
        try
        {
            titulo2= getItem(i).getString("0");//Nombre del cliente
            subtitulo2 = getItem(i).getString("1");//Direccion
            imagen = getItem(i).getString("2");//Telefono
            claveRuta= getItem(i).getString("3");//Clave del cliente
        }
        catch (JSONException e)
        {
            titulo2= null;
            subtitulo2 = null;
            imagen= null;
            claveRuta = null;
        }
        if (titulo2 != null) {
            folio.setText(titulo2);
            total.setText(subtitulo2);
            fecha.setText(imagen);
            otros.setText(claveRuta);
        }
        return view;
    }
}
