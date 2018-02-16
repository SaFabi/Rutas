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


public class CreditosContenedor extends Fragment {
    //FRAGMENTO PROBADO. CONTIENE LOS FRAGMENTOS DE CREDITO
    private OnFragmentInteractionListener mListener;
    //VARIABLES
    View vista;
    List<Fragment> fragmentosCreditos = new ArrayList<>();
    List<String> titulosCreditos = new ArrayList<>();

    //CONTROLES
    private AppBarLayout appBAr;
    private TabLayout pestanas;
    private ViewPager viewPager;

    //ADAPTERS
    rutasLib rutasObj;



    public CreditosContenedor() {
        // Required empty public constructor
    }

    public static CreditosContenedor newInstance(String param1, String param2) {
        CreditosContenedor fragment = new CreditosContenedor();
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
        vista= inflater.inflate(R.layout.fragment_creditos_contenedor, container, false);
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

                viewPager =vista.findViewById(R.id.ViewPagerCreditos);

                //llena los Arrays de los fragmentos

                fragmentosCreditos.add(new CreditosPendientes());
                fragmentosCreditos.add(new CreditosLiquidados());
                //Manda los titulos
                titulosCreditos.add("PENDIENTES");
                titulosCreditos.add("LIQUIDADOS");

                //Pone el Adapter en el ViewPager
                viewPager.setAdapter(rutasObj.llenarViewPager(getFragmentManager(),fragmentosCreditos,titulosCreditos));
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBAr.removeView(pestanas);
        }
    }

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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
