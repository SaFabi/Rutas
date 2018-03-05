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

import com.example.fabi.atc.Clases.Utilidades;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import java.util.ArrayList;
import java.util.List;

public class ContenedorInventarioGeneral extends Fragment {
    //FRAGMENTO PROBADO.CONTIENE LOS FRAGMENTOS DEL INVENTARIO GENERAL
    //VARIABLES
    List<Fragment> fragmentosInventario = new ArrayList<>();
    List<String> titulosInventario = new ArrayList<>();

    //CONTROLES
    private AppBarLayout appBArInventario;
    private TabLayout pestanasInventario;
    private ViewPager viewPagerINventario;

    //ADAPTERS
    View vista;
    rutasLib rutasObj;

    public ContenedorInventarioGeneral() {

    }

    public static ContenedorInventarioGeneral newInstance(String param1, String param2) {
        ContenedorInventarioGeneral fragment = new ContenedorInventarioGeneral();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
      vista =  inflater.inflate(R.layout.fragment_contenedor_inventario_general, container, false);
        if (Utilidades.rotacion == 0){

            View parent = (View) container.getParent();
            if (appBArInventario==null){
                //asigna los elementos
                appBArInventario = parent.findViewById(R.id.appBar);
                pestanasInventario =  new TabLayout(getActivity());
                //color del texto de las tabs
                pestanasInventario.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                //Agrega los titulos a las tabs
                appBArInventario.addView(pestanasInventario);

                viewPagerINventario =vista.findViewById(R.id.ViewPagerInventario);

                //llena los Arrays de los fragmentos

                fragmentosInventario.add(new TelefonosGeneral());
                fragmentosInventario.add(new ChipsGeneral());
                fragmentosInventario.add(new AccesoriosGeneral());
                //Manda los titulos
                titulosInventario.add("Telefonos");
                titulosInventario.add("Chips");
                titulosInventario.add("Accesorios");

                //Pone el Adapter en el ViewPager
                viewPagerINventario.setAdapter(rutasObj.llenarViewPager(getFragmentManager(),fragmentosInventario,titulosInventario));

                viewPagerINventario.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanasInventario.setupWithViewPager(viewPagerINventario);

            }
            pestanasInventario.setTabGravity(TabLayout.GRAVITY_FILL);


        }else {
            Utilidades.rotacion = 1;
        }


        return vista;
    }
    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBArInventario.removeView(pestanasInventario);
        }
    }
}
