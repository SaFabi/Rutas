package com.example.fabi.atc.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.example.fabi.atc.Clases.ModeloClientes;

import java.util.List;

/**
 * Created by Fabi on 13/01/2018.
 */

public class AdapterCreditos extends BaseAdapter {
    Context context;
    List<ModeloClientes> elementos;

    public AdapterCreditos(Context context, List<ModeloClientes> elementos) {
        this.context = context;
        this.elementos = elementos;
    }

    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        return null;
    }
}
