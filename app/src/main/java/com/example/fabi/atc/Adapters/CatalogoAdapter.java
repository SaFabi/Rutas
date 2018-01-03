package com.example.fabi.atc.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fabi on 18/12/2017.
 */

public class CatalogoAdapter extends FragmentStatePagerAdapter {
    //Adaptador para fragmentos con TABS

    private  List<Fragment> listaFragments = new ArrayList<>();
    private  List<String> listaTitulos=new ArrayList<>();

    public CatalogoAdapter(FragmentManager fm) {
        super(fm);
    }

    public void agregarFragmentos(List<Fragment> fragments, List<String> titulos){
        listaFragments=fragments;
        listaTitulos=titulos;

    }

    public void addFragment(Fragment fragment, String titulo){
        listaFragments.add(fragment);
        listaTitulos.add(titulo);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return listaTitulos.get(position);
    }

    @Override
    public Fragment getItem(int position) {
        return listaFragments.get(position);
    }

    @Override
    public int getCount() {
        return listaFragments.size();
    }
}
