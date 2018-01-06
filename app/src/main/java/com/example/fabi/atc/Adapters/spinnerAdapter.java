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
 * Created by Fabi on 03/01/2018.
 */

public class spinnerAdapter extends BaseAdapter {
    private JSONArray array;
    private Context context;
    TextView ciudad;

    public spinnerAdapter(JSONArray array, Context context) {
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
        return  i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = convertView;
        if (convertView == null){
            view = inflater.inflate(R.layout.modelo_spinner,null);
        }
        ciudad=(TextView)view.findViewById(R.id.spinnerCiudad);

        String ciudadtxt;
        try
        {
            ciudadtxt= getItem(i).getString("1");
        }
        catch (JSONException e)
        {
            ciudadtxt = null;
        }
        if (ciudadtxt != null) {
            ciudad.setText(ciudadtxt);

        }
        return view;

    }
}
