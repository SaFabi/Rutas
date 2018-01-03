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

    private OnFragmentInteractionListener mListener;
    View vista;
    private AppBarLayout appBArClientes;
    private TabLayout pestanasClientes;
    private ViewPager viewPagerClientes;
    rutasLib rutasObj;
    List<Fragment> fragmentos = new ArrayList<>();
    List<String> titulos = new ArrayList<>();


    public ClientesContenedor() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static ClientesContenedor newInstance(String param1, String param2) {
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
                appBArClientes = parent.findViewById(R.id.appBar);
                pestanasClientes =  new TabLayout(getActivity());
                pestanasClientes.setTabTextColors(Color.parseColor("#FFFFFF"),Color.parseColor("#FFFFFF"));
                appBArClientes.addView(pestanasClientes);

                viewPagerClientes =vista.findViewById(R.id.ViewPagerClientes);

                //llena los Arrays de los fragmentos
                fragmentos.add(new RegistroClientes());
                fragmentos.add(new ClientesInactivos());
                fragmentos.add(new Clientes());
                //Manda los titulos
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

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
    public void onDestroyView() {
        super.onDestroyView();
        if (Utilidades.rotacion == 0){
            appBArClientes.removeView(pestanasClientes);
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

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
