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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.fabi.atc.Adapters.CatalogoAdapter;
import com.example.fabi.atc.Clases.Utilidades;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import java.util.ArrayList;
import java.util.List;

public class Contenedor extends Fragment {

    private OnFragmentInteractionListener mListener;
    View vista;
    private AppBarLayout appBAr;
    private TabLayout pestanas;
    private ViewPager viewPager;
    rutasLib rutasObj;
    List<Fragment> fragmentosCatalogo = new ArrayList<>();
    List<String> titulosCatalogo = new ArrayList<>();


    public Contenedor() {
        // Required empty public constructor
    }



    public static Contenedor newInstance(String param1, String param2) {
        Contenedor fragment = new Contenedor();
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
        vista = inflater.inflate(R.layout.fragment_contenedor, container, false);
        if (Utilidades.rotacion == 0){

            View parent = (View) container.getParent();
            if (appBAr==null){
                //asigna los elementos
                appBAr = parent.findViewById(R.id.appBar);
                pestanas =  new TabLayout(getActivity());
                //color del texto de las tabs
                pestanas.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                //Agrega los titulos a las tabs
                appBAr.addView(pestanas);

                viewPager =vista.findViewById(R.id.ViewPagerInformacion);

                //llena los Arrays de los fragmentos

               fragmentosCatalogo.add(new Catalogo());
                fragmentosCatalogo.add(new Inicio());
                fragmentosCatalogo.add(new Clientes());
                //Manda los titulos
                titulosCatalogo.add("Telefonos");
                titulosCatalogo.add("Chips");
                titulosCatalogo.add("Accesorios");

                //Pone el Adapter en el ViewPager
                viewPager.setAdapter(rutasObj.llenarViewPager(getFragmentManager(),fragmentosCatalogo,titulosCatalogo));

                viewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
                    @Override
                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                        super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                    }
                });
                pestanas.setupWithViewPager(viewPager);

            }
            pestanas.setTabGravity(TabLayout.GRAVITY_FILL);


        }else {
            Utilidades.rotacion = 1;
        }


        return vista;
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBAr.removeView(pestanas);
        }
        }
//Guardar informaccion cuando interactua con otro fragmento
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
