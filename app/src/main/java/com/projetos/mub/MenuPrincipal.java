package com.projetos.mub;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import java.util.List;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    TextView tvNomeUsuario, tvEmailUsuario;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        tvNomeUsuario = (TextView) findViewById(R.id.tvMenuNomeUsuario);
        tvEmailUsuario = (TextView) findViewById(R.id.tvMenuEmailUsuario);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        InserirUsuarioLocal load = null;
        if (load == null) {
            load = new InserirUsuarioLocal();
        } else {
            load.cancel(true);
            load = new InserirUsuarioLocal();
        }
        load.execute();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(), InserirOcorrencia.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);

        tvNomeUsuario = (TextView) headerView.findViewById(R.id.tvMenuNomeUsuario);
        tvEmailUsuario = (TextView) headerView.findViewById(R.id.tvMenuEmailUsuario);
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
        getMenuInflater().inflate(R.menu.menu_principal, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_conta_usuario) {
            Intent intent = new Intent(getBaseContext(), UsuarioMenu.class);
            startActivity(intent);
        } else if (id == R.id.nav_listrar_ocorrencias) {
            Intent intent = new Intent(getBaseContext(), ListrarOcorrencias.class);
            startActivity(intent);

        } else if (id == R.id.nav_consultar_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), ConsultarOcorrencia.class);
            startActivity(intent);

        } else if (id == R.id.nav_atender_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), SelecionarAtenderOcorrencia.class);
            startActivity(intent);

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private class InserirUsuarioLocal extends AsyncTask<Void, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            /*load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Estamos carregando os seus dados! ;)");*/
        }

        @Override
        protected Usuario doInBackground(Void... voids) {

            try {
                Usuario usuario = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .getUserById(1L);
                System.out.println("Identificador on INSERT: " + usuario.getId());
                return usuario;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            System.out.println("Identificador on POST EXECUTE " + usuario.getNome());
            tvNomeUsuario.setText(usuario.getNome());
            tvEmailUsuario.setText(usuario.getEmail());
            //load.dismiss();
        }
    }
}
