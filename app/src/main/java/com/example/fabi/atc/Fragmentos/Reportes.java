package com.example.fabi.atc.Fragmentos;

import android.app.DatePickerDialog;
 import android.content.Context;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.fabi.atc.Adapters.spinnerSencilloAdapter;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.Modelo;
import com.example.fabi.atc.Clases.rutasLib;
import com.example.fabi.atc.R;

import java.util.ArrayList;


public class Reportes extends Fragment implements Basic {

    private static final String ARG_POSITION = "position";
    private int mPosition;
    Button btnFechaInicio, btnFechaFin, btnConsultar;
    EditText edtFechaInicio, edtFechaFin;
    private int dia,mes, ano,dayI, monthI, yearI,dayF, monthF, yearF;
    Spinner spinner;
     rutasLib rutasObj;
    ArrayList<Modelo> modelo;
    String fechaActual;
    ListView listView;
    String fechaInicial ="2017-08-12";
    String fechaFinal = fechaActual;
    String opcionSeleccionada;
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

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_reportes, container, false);
        //SE ASIGNAN LAS VARIABLES CON LOS CONTROLES DEL LAYOUT
        edtFechaFin = (EditText)view.findViewById(R.id.edtFechaFinal);
        edtFechaInicio= (EditText)view.findViewById(R.id.edtFechaInicial);
        btnFechaFin = (Button)view.findViewById(R.id.btnFechaFinal);
        btnFechaInicio = (Button)view.findViewById(R.id.btnFechaInicial);
        btnConsultar = (Button)view.findViewById(R.id.btnConsultar);
        spinner = (Spinner)view.findViewById(R.id.spinnerReportes);
        listView = (ListView)view.findViewById(R.id.listReportes);

        //ADAPTER DEL MENU DEL OPCIONES DEL SPINNER
        spinnerSencilloAdapter spinnerSencilloAdapter = new spinnerSencilloAdapter(listaReportes(),getContext());
        spinner.setAdapter(spinnerSencilloAdapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 0:
                        opcionSeleccionada = "Comisiones";
                        listView.setAdapter(rutasObj.ReporteComisiones(getContext(),fechaInicial,fechaFinal,usuarioID));
                        break;
                    case 1:
                        opcionSeleccionada="Ventas";
                        listView.setAdapter(rutasObj.ReporteVentas(getContext(),fechaInicial,fechaFinal,usuarioID));
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        //PARA OBTENER LA FECHA ACTUAL
        final Calendar c = Calendar.getInstance();
        dia=c.get(Calendar.DAY_OF_MONTH);
        mes = c.get(Calendar.MONTH) +1;
        ano=c.get(Calendar.YEAR);
        fechaActual = ano+"/"+mes+"/"+dia;
        //BOTONES PARA EL DATETIMEPICKER
        btnFechaFin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dayF = datePicker.getDayOfMonth();
                        monthF = datePicker.getMonth()+1;
                        yearF = datePicker.getYear();
                        fechaFinal = String.valueOf(yearF)+"/"+String.valueOf(monthF)+"/"+String.valueOf(dayF);
                        edtFechaFin.setText(fechaFinal);

                    }
                },ano,mes,dia);
                datePickerDialog.show();


            }
        });

        btnFechaInicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
                        dayI = datePicker.getDayOfMonth();
                        monthI = datePicker.getMonth()+1;
                        yearI = datePicker.getYear();
                        fechaInicial= String.valueOf(yearI)+"/"+String.valueOf(monthI)+"/"+String.valueOf(dayI);
                         edtFechaInicio.setText(fechaInicial);

                    }
                },ano,mes,dia);
                datePickerDialog.show();

            }
        });

        btnConsultar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(),"Fecha de hoy  "+fechaActual, Toast.LENGTH_SHORT).show();
                if (opcionSeleccionada.equals("Comisiones")){
                    listView.setAdapter(rutasObj.ReporteComisiones(getContext(),fechaInicial,fechaFinal,usuarioID));
                }else if (opcionSeleccionada.equals("Ventas")){
                    listView.setAdapter(rutasObj.ReporteVentas(getContext(),fechaInicial,fechaFinal,usuarioID));
                }
            }
        });



        return view;
    }

    public ArrayList<Modelo>listaReportes(){
        ArrayList<Modelo>lista = new ArrayList<>();
        String[] nombre = getResources().getStringArray(R.array.opcionesReportes);

        for (int i = 0; i <nombre.length; i++){
            lista.add(new Modelo(nombre[i]));
        }
        return lista;
    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }
/*
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
