package com.example.fabi.atc.Fragmentos;

import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.fabi.atc.R;

public class Reportes extends Fragment {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    Button btnFechaInicio, btnFechaFin;
    EditText edtFechaInicio, edtFechaFin;
    private int day, month, year;

    private OnFragmentInteractionListener mListener;

    public Reportes() {
        // Required empty public constructor
    }


    public static Reportes newInstance(int position) {
        Reportes fragment = new Reportes();
        Bundle args = new Bundle();
        args.putInt(ARG_POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mPosition = getArguments().getInt(ARG_POSITION);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reportes, container, false);
        edtFechaFin = (EditText)view.findViewById(R.id.edtFechaInicial);
        edtFechaInicio= (EditText)view.findViewById(R.id.edtFechaFinal);
        btnFechaFin = (Button)view.findViewById(R.id.btnFechaFinal);
        btnFechaInicio = (Button)view.findViewById(R.id.btnFechaInicial);

        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });
        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });



        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
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
