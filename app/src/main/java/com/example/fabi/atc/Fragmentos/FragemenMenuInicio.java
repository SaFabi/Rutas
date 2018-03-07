package com.example.fabi.atc.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.example.fabi.atc.R;


public class FragemenMenuInicio extends Fragment {

    public FragemenMenuInicio() {
        // Required empty public constructor
    }
    public static FragemenMenuInicio newInstance(String param1, String param2) {
        FragemenMenuInicio fragment = new FragemenMenuInicio();
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


        View view= inflater.inflate(R.layout.fragment_fragemen_menu_inicio, container, false);

        final Animation animScale = AnimationUtils.loadAnimation(getContext(),R.anim.scaele_animacion);
        Button btnCatalogo = view.findViewById(R.id.idBtnCatalogo);
        Button btnPedidos = view.findViewById(R.id.idBtnPedidos);
        Button btnReportes = view.findViewById(R.id.idBtnReportes);
        Button btnClientes= view.findViewById(R.id.idBtnClientes);

        btnCatalogo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);
                Fragment fragment = Contenedor.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }});

        btnPedidos.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);

                Fragment fragment = PedidosFragment.newInstance(0);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();

            }});
        btnReportes.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);

                Fragment fragment = Reportes.newInstance(0);
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }});

        btnClientes.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);

                Fragment fragment = ClientesContenedor.newInstance();
                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.content_main,fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }});
        return view;
    }
}
