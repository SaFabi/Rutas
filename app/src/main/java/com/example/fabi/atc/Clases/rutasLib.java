package com.example.fabi.atc.Clases;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.Inicio;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 22/12/2017.
 */

public class rutasLib {

    public  static CatalogoAdapter llenarViewPager(FragmentManager fragmentManager, List<Fragment> fragments,List<String> titulos) {
        CatalogoAdapter adapter = new CatalogoAdapter(fragmentManager);
            adapter.agregarFragmentos(fragments,titulos);



        return adapter;

    }

}
