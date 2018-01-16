package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.textclassifier.TextClassification;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.fabi.atc.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class AdapterDetallesCreditos  extends BaseAdapter{
    private JSONArray array;
    private Context context;
    TextView tipo,marca,modelo,cantidad,monto;


    public AdapterDetallesCreditos(JSONArray array, Context context) {
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
            view = inflater.inflate(R.layout.modelodetallescreditos,null);
        }
        tipo = (TextView)view.findViewById(R.id.tipoArticulo);
        marca = (TextView)view.findViewById(R.id.marca);
        modelo = (TextView)view.findViewById(R.id.modelo);
        cantidad = (TextView)view.findViewById(R.id.Cantidad);
        monto = (TextView)view.findViewById(R.id.monto);

        String Stipo, Smarca, Smodelo, Scantidad, Smonto;
        try {

            Stipo = getItem(i).getString("0");
            Smarca = getItem(i).getString("1");
            Smodelo = getItem(i).getString("2");
            Scantidad = getItem(i).getString("3");
            Smonto = getItem(i).getString("4");
        }catch (JSONException e){
            Stipo = null;
            Smarca = null;
            Smodelo = null;
            Scantidad = null;
            Smonto = null;
        }
            if (Stipo != null) {
            tipo.setText(Stipo);
            marca.setText(Smarca);
            modelo.setText(Smodelo);
            cantidad.setText("Cantidad: "+Scantidad);
            monto.setText("Monto total: "+Smonto);

            }
        return view;
    }
}
