package com.example.fabi.atc;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import com.example.fabi.atc.Fragmentos.Catalogo;
import com.example.fabi.atc.Fragmentos.Clientes;
import com.example.fabi.atc.Fragmentos.ClientesContenedor;
import com.example.fabi.atc.Fragmentos.Contenedor;
import com.example.fabi.atc.Fragmentos.ContenedorInventarioGeneral;
import com.example.fabi.atc.Fragmentos.CreditosContenedor;
import com.example.fabi.atc.Fragmentos.CreditosPendientes;
import com.example.fabi.atc.Fragmentos.Inicio;
import com.example.fabi.atc.Fragmentos.Reportes;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        Inicio.OnFragmentInteractionListener,Catalogo.OnFragmentInteractionListener,
        Clientes.OnFragmentInteractionListener,
        Contenedor.OnFragmentInteractionListener, ClientesContenedor.OnFragmentInteractionListener,
        ContenedorInventarioGeneral.OnFragmentInteractionListener, Reportes.OnFragmentInteractionListener,
        CreditosContenedor.OnFragmentInteractionListener{
    MenuItem itemBuscar;
    MenuItem itemCarrito;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);
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
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.menu_buscador,menu);
        itemBuscar =menu.findItem(R.id.buscador2);
        itemCarrito = menu.findItem(R.id.carrito);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                return true;
            case R.id.buscador2:
                return true;
            case R.id.carrito:
                return true;
        }


        /*
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment miFragment =null;
        boolean fragmentSeleccionado=false;

        if (id == R.id.Inicio){
            miFragment = new Inicio();
            fragmentSeleccionado = true;
            itemBuscar.setVisible(false);
            itemCarrito.setVisible(false);
        } else if (id == R.id.Catalogo) {
            miFragment = new Contenedor();
            fragmentSeleccionado=true;
            itemBuscar.setVisible(true);
            itemCarrito.setVisible(true);
        } else if (id == R.id.Clientes) {
            miFragment = new ClientesContenedor();
            fragmentSeleccionado = true;
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(true);

        } else if (id == R.id.Creditos) {
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(false);
            miFragment = new CreditosContenedor();
            fragmentSeleccionado = true;


        } else if (id == R.id.Reportes) {
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(true);
            miFragment = new Reportes();
            fragmentSeleccionado = true;


        } else if (id == R.id.Ayuda) {
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(false);


        } else if (id == R.id.Contacto) {
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(false);


        }else if (id == R.id.InventarioG) {
            miFragment = new ContenedorInventarioGeneral();
            fragmentSeleccionado = true;
            itemCarrito.setVisible(false);
            itemBuscar.setVisible(true);


        }

        if (fragmentSeleccionado == true){
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main,miFragment).commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
