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
 * Created by Fabi on 19/01/2018.
 */

public class AdapterAbonos extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView txtFolio, txtFecha, txtAbono;
    String folio, fecha, abono;
    public AdapterAbonos(JSONArray array, Context context) {
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
            view = inflater.inflate(R.layout.modeloabonos,null);

        }
        txtFolio =(TextView)view.findViewById(R.id.txtfolio);
        txtFecha = (TextView)view.findViewById(R.id.txtFecha);
        txtAbono = (TextView)view.findViewById(R.id.txtAbono);

        try {
            folio = getItem(i).getString("0");
            fecha = getItem(i).getString("1");
            abono = getItem(i).getString("2");

        }catch (Exception e){
            folio = null;
            fecha = null;
            abono = null;
        }

        if (folio != null){
            txtFolio.setText(folio);
            txtFecha.setText(fecha);
            txtAbono.setText("Abono: "+abono);
        }


        return view;
    }
}
