package com.example.fabi.atc.Fragmentos;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.example.fabi.atc.Clases.Utilidades;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import java.util.ArrayList;
import java.util.List;

public class ClientesContenedor extends Fragment {
    //FRAGMENTO PROBADO. ES EL CONTENEDOR DE LO RELACIONADO CON LOS CLIENTES

    //VARIABLES
    List<Fragment> fragmentos = new ArrayList<>();
    List<String> titulos = new ArrayList<>();
    View vista;

    //CONTROLES
    private AppBarLayout appBArClientes;
    private TabLayout pestanasClientes;
    private ViewPager viewPagerClientes;

    //ADAPTERS
    rutasLib rutasObj;


    public ClientesContenedor() {
        // Required empty public constructor
    }

    public static ClientesContenedor newInstance() {
        ClientesContenedor fragment = new ClientesContenedor();
        Bundle args = new Bundle();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       vista =  inflater.inflate(R.layout.fragment_clientes_contenedor, container, false);
        if (Utilidades.rotacion == 0){
            View parent = (View) container.getParent();
            if (appBArClientes==null){
                //PERSONALIZAR LA BARRA
                appBArClientes = parent.findViewById(R.id.appBar);
                pestanasClientes =  new TabLayout(getActivity());
                pestanasClientes.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                appBArClientes.addView(pestanasClientes);

                viewPagerClientes =vista.findViewById(R.id.ViewPagerClientes);

                //LLENA EL ARRAY DE LOS FRAGMENTOS
                fragmentos.add(new RegistroClientes());
                fragmentos.add(new ClientesActivos());
                fragmentos.add(new ClientesInactivos());
                //LLENA EL ARRAY DE LOS TITULOS
                titulos.add("Registrar");
                titulos.add("Activos");
                titulos.add("Inactivos");

                viewPagerClientes.setAdapter(rutasObj.llenarViewPager(getFragmentManager(),fragmentos,titulos));

                viewPagerClientes.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanasClientes.setupWithViewPager(viewPagerClientes);

            }
            pestanasClientes.setTabGravity(TabLayout.GRAVITY_FILL);
        }else {
            Utilidades.rotacion = 1;
        }
        return vista;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBArClientes.removeView(pestanasClientes);
        }
    }
}
