package com.projetos.mub;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.location.aravind.getlocation.GeoLocator;
import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    List<CardGeral> Card;


    TextView tvNomeUsuario, tvEmailUsuario;
    private ProgressDialog load;
    private Usuario usuario;
    private String cidade;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(!GPSEnabled){
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (ContextCompat.checkSelfPermission(getBaseContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
            startActivity(intent);
        } else {
            ConsultarCoordenadas consultarCoordenadas = null;
            if (consultarCoordenadas == null) {
                consultarCoordenadas = new ConsultarCoordenadas();
            } else {
                consultarCoordenadas.cancel(true);
                consultarCoordenadas = new ConsultarCoordenadas();
            }
            consultarCoordenadas.execute();
        }

        Card = new ArrayList<>();
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        Card.add(new CardGeral());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recGeral);
        RecycleViewAdapterGeral recycleViewAdapter = new RecycleViewAdapterGeral(this, Card);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(recycleViewAdapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ConsultarLocalmenteTask load = null;
        if (load == null) {
            load = new ConsultarLocalmenteTask();
        } else {
            load.cancel(true);
            load = new ConsultarLocalmenteTask();
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

        //Intância dos itens do menu
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Chamando views que contém informações do usuário
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
        /*
         * Handler do botão de Logout
         */
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            LogoutTask logout = null;

            if (logout == null) {
                logout = new LogoutTask();
            } else {
                logout.cancel(true);
                logout = new LogoutTask();
            }

            logout.execute();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle para navegação dos itens do menu.
        int id = item.getItemId();

        // menu para usuario comum e agente de transito
        if (id == R.id.nav_conta_usuario) {
            Intent intent = new Intent(getBaseContext(), UsuarioMenu.class);
            startActivity(intent);
        } else if (id == R.id.nav_listrar_ocorrencias) {
            Intent intent = new Intent(getBaseContext(), ListrarOcorrencias.class);
            startActivity(intent);

       /* } else if (id == R.id.nav_consultar_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), ConsultarOcorrencia.class);
            startActivity(intent); */

        } else if (id == R.id.nav_inserir_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), InserirOcorrencia.class);
            startActivity(intent);
            //fim para usuario comum

            //complemento para agente de transito
      /*  } else if (id == R.id.nav_alterar_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), AlterarOcorrencia.class);
            startActivity(intent); */

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /*
     ** Task responsável por puxar os dados armazenados no banco de dados local.
     */
    private class ConsultarLocalmenteTask extends AsyncTask<Void, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Estamos carregando os seus dados! ;)");
        }

        @Override
        protected Usuario doInBackground(Void... voids) {
            try {
                Usuario usuario = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .getUserById(1L);
                System.out.println("Usuário buscado MENU_PRINCIPAL: " + usuario.getId());
                setUsuario(usuario);
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
            load.dismiss();
        }
    }


    /*
     ** Task criada para realizar logou do usuário.
     */
    private class LogoutTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Usuario obj = getUsuario();
            try {
                obj.setManterLogado(false);
                Long id = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .insert(obj);
                System.out.println("Logout do usuário " + id +
                        " realizado com sucesso!");
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            } catch (Exception e) {
                Log.i("Erro ao fazer logout ", e.toString());
            }
            return null;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    private class ConsultarCoordenadas extends AsyncTask<Void, Void, String> {
        Utils util = new Utils();

        @Override
        protected void onPreExecute() {
            /*load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Buscando coordenadas...");*/
        }

        @Override
        protected String doInBackground(Void... voids) {
            try {
                GeoLocator geoLocator = new GeoLocator(getApplicationContext(), MenuPrincipal.this);
                cidade = geoLocator.getAddress();
                System.out.println("ENDEREÇO" + cidade);
                return String.valueOf(geoLocator.getLongitude());
            } catch (Exception e) {
                Log.e("ERRO AO BUSCAR", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                System.out.println("ENDEREÇOOOO " + s);
            }
            //load.dismiss();
        }
    }
}
