package com.example.fabi.atc;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.fabi.atc.Adapters.AdapterClientes;
import com.example.fabi.atc.Clases.Basic;
import com.example.fabi.atc.Clases.ModeloClientes;
import com.example.fabi.atc.Fragmentos.CarritoFragment;
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.ClientesContenedor;
import com.example.fabi.atc.Fragmentos.Contenedor;
import com.example.fabi.atc.Fragmentos.ContenedorInventarioGeneral;
import com.example.fabi.atc.Fragmentos.CreditosContenedor;
import com.example.fabi.atc.Fragmentos.CreditosPendientes;
import com.example.fabi.atc.Fragmentos.FragemenMenuInicio;
import com.example.fabi.atc.Fragmentos.Inicio;
import com.example.fabi.atc.Fragmentos.PedidosFragment;
import com.example.fabi.atc.Fragmentos.Reportes;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;

public class MainActivity extends AppCompatActivity
        implements Basic, NavigationView.OnNavigationItemSelectedListener{
    MenuItem itemBuscar;
    MenuItem itemCarrito;
    Fragment miFragment =null;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //RECIBE UN STRING PARA SABER QUE LLEGO UNA NOTIFICACION
        String identificador = getIntent().getStringExtra("identificador");
        //Toast.makeText(this, identificador, Toast.LENGTH_SHORT).show();

        //PARA SABER SI ENTRA POR EL LADO DE LA NOTIFICACION
        if (identificador != null){
            //Toast.makeText(this, identificador, Toast.LENGTH_SHORT).show();
            if (identificador.equals("Notificacion")){
                miFragment = new Contenedor();
                getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).addToBackStack(null).commit();
            }
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);



        Fragment miFragment = new FragemenMenuInicio();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).addToBackStack(null).commit();


    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
         getMenuInflater().inflate(R.menu.menu_buscador,menu);
        MenuItem itembuscar =menu.findItem(R.id.buscador2);
         MenuItem itemcarrito = menu.findItem(R.id.carrito);
        itemcarrito.setVisible(false);
        itembuscar.setVisible(false);
        return true;
    }



    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        boolean fragmentSeleccionado=false;

        if (id == R.id.Inicio){
            miFragment = new FragemenMenuInicio();
            fragmentSeleccionado = true;
        } else if (id == R.id.Catalogo) {
            miFragment = new Contenedor();
            fragmentSeleccionado=true;
        } else if (id == R.id.Clientes) {
            miFragment = new ClientesContenedor();
            fragmentSeleccionado = true;

        } else if (id == R.id.Creditos) {
            miFragment = new CreditosContenedor();
            fragmentSeleccionado = true;


        } else if (id == R.id.Reportes) {
            miFragment = new Reportes();
            fragmentSeleccionado = true;


        } else if (id == R.id.Ayuda) {


        }else if (id == R.id.InventarioG) {
            miFragment = new ContenedorInventarioGeneral();
            fragmentSeleccionado = true;


        }else if (id == R.id.Pedidos){
            miFragment = new PedidosFragment();
            fragmentSeleccionado = true;
        }else if (id == R.id.Localizacion){

        }


        if (fragmentSeleccionado == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).addToBackStack(null).commit();
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
