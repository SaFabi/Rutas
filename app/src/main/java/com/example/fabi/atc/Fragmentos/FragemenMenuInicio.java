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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FragemenMenuInicio.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FragemenMenuInicio#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragemenMenuInicio extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public FragemenMenuInicio() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FragemenMenuInicio.
     */
    // TODO: Rename and change types and number of parameters
    public static FragemenMenuInicio newInstance(String param1, String param2) {
        FragemenMenuInicio fragment = new FragemenMenuInicio();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        View view= inflater.inflate(R.layout.fragment_fragemen_menu_inicio, container, false);
        /*
        final Animation animScale = AnimationUtils.loadAnimation(getContext(),R.anim.scaele_animacion);
        Button btnCatalogo = (Button)view.findViewById(R.id.idCatalogo);
        Button btnPedidos = (Button)view.findViewById(R.id.idPedidos);
        Button btnReportes = (Button)view.findViewById(R.id.idReportes);
        Button btnClientes= (Button)view.findViewById(R.id.idClientes);

        btnCatalogo.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);

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
            }});

        btnClientes.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View arg0) {
                arg0.startAnimation(animScale);
            }});*/
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
