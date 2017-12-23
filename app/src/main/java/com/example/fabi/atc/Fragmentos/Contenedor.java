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
import com.example.fabi.atc.R;

public class Contenedor extends Fragment {

    private OnFragmentInteractionListener mListener;
    View vista;
    private AppBarLayout appBAr;
    private TabLayout pestanas;
    private ViewPager viewPager;

    public Contenedor() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Contenedor.
     */
    // TODO: Rename and change types and number of parameters
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
        // Inflate the layout for this fragment

        vista = inflater.inflate(R.layout.fragment_contenedor, container, false);
        if (Utilidades.rotacion == 0){

            View parent = (View) container.getParent();
            if (appBAr==null){
                appBAr = parent.findViewById(R.id.appBar);
                pestanas =  new TabLayout(getActivity());
                pestanas.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                appBAr.addView(pestanas);

                viewPager =vista.findViewById(R.id.ViewPagerInformacion);
                llenarViewPager(viewPager);
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

    private void llenarViewPager(ViewPager viewPager) {
        CatalogoAdapter adapter = new CatalogoAdapter(getFragmentManager());
        adapter.addFragment(new Catalogo(),"Telefonos");
        adapter.addFragment(new Clientes(),"Chips");
        adapter.addFragment(new Inicio(),"Accesorios");
        viewPager.setAdapter(adapter);

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
