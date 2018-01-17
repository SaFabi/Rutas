package com.example.fabi.atc.Fragmentos;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.fabi.atc.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link RegistrarAbono.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link RegistrarAbono#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RegistrarAbono extends Fragment {
    int ordenID;
    int MontoTotal;


    private OnFragmentInteractionListener mListener;

    public RegistrarAbono() {
        // Required empty public constructor
    }

    public static RegistrarAbono newInstance(int OrdenID, int MontoTotal) {
        RegistrarAbono fragment = new RegistrarAbono();
        Bundle args = new Bundle();

        args.putInt("ordenID",OrdenID);
        args.putInt("totalOrden",MontoTotal);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            ordenID = getArguments().getInt("ordenID");
            MontoTotal = getArguments().getInt("totalOrden");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_registrar_abono, container, false);
        Toast.makeText(getContext(),String.valueOf(ordenID + "    "+ MontoTotal), Toast.LENGTH_SHORT).show();


        return vista;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
