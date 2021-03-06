package com.projetos.mub;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MenuPrincipal extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<CardGeral> listCard = new ArrayList<>();


    TextView tvNomeUsuario, tvEmailUsuario;
    private String rua, numero, bairro, cidade, estado, cep;
    private Double latitude, longitude;
    private final static int MY_PERMISSIONS_REQUEST_INTERNET_LOCATION = 128;
    private AlertDialog alert;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);
        setTitle("Alertas Municipais!");

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!GPSEnabled) {
            startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            Log.e("TESTEE", "Permissão não concedida");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_INTERNET_LOCATION);
        } else {
            System.out.println("A LOCALIZAÇÃO ESTÁ OK");
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("ENTROU AQUIIIIIIIIII");
            executarConsultaCoodenadas();
        }

        recyclerView = (RecyclerView) findViewById(R.id.recGeral);
        recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));

        new Thread(new Runnable() {
            public void run() {
                Usuario u = consultarLocalmente();
                tvNomeUsuario.setText(u.getNome());
                tvEmailUsuario.setText(u.getEmail());
            }
        }).start();
    }

    @Override
    public void onBackPressed() {
        /*DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }*/

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

        if (id == R.id.nav_conta_usuario) {
            Intent intent = new Intent(getBaseContext(), UsuarioMenu.class);
            startActivity(intent);
        } else if (id == R.id.nav_listrar_ocorrencias) {
            Intent intent = new Intent(getBaseContext(), ListrarOcorrencias.class);
            startActivity(intent);
        } else if (id == R.id.nav_inserir_ocorrencia) {
            Intent intent = new Intent(getBaseContext(), InserirOcorrencia.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        //checkLocalizacao();
        super.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[],
                                           int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_INTERNET_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        System.out.println("Localização ATIVA");
                    }
                }
            }
        }

    }

    private void executarConsultaCoodenadas() {
        ConsultarCoordenadas consultarCoordenadas = null;
        if (consultarCoordenadas == null) {
            consultarCoordenadas = new ConsultarCoordenadas();
        } else {
            consultarCoordenadas.cancel(true);
            consultarCoordenadas = new ConsultarCoordenadas();
        }
        consultarCoordenadas.execute();
    }

    private void checkLocalizacao() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            System.out.println("ENTROU AQUIIIIIIIIII");
            executarConsultaCoodenadas();
        }
    }

    private class LogoutTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {

            Usuario obj = consultarLocalmente();
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

    private class ConsultarCoordenadas extends AsyncTask<Void, Void, String> {
        Utils util = new Utils();
        private ProgressDialog load;

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Carregando as coordenadas ;)");
        }

        @Override
        protected String doInBackground(Void... voids) {
            GeoLocator geoLocator = new GeoLocator(getApplicationContext(), MenuPrincipal.this);

            latitude = geoLocator.getLattitude();
            longitude = geoLocator.getLongitude();
            System.out.println(latitude + "\n" + longitude);
            String endereco = util.getInfFromGET("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude +
                    "," + longitude + "&key=AIzaSyAUzj3IP_i6PCj-VcZYJaNLcr0lxj9xLnc");
            if (endereco.length() > 0) {
                return endereco;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s != null) {
                try {
                    System.out.println("RETORNO AVISOS: " + s);
                    JSONObject jsonObject = new JSONObject(s);
                    JSONObject jsonObject1 = jsonObject.getJSONArray("results").getJSONObject(0);
                    String endereco = jsonObject1.getString("formatted_address");
                    quebrarEndereco(endereco);
                    load.dismiss();

                    CarregarAvisosTask carregarAvisosTask = null;
                    if (carregarAvisosTask == null) {
                        carregarAvisosTask = new CarregarAvisosTask();
                    } else {
                        carregarAvisosTask.cancel(true);
                        carregarAvisosTask = new CarregarAvisosTask();
                    }
                    carregarAvisosTask.execute();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                // Criando gerador de Alerta
                final AlertDialog.Builder builder = new AlertDialog.Builder(MenuPrincipal.this);
                builder.setTitle("Nossa, tem algo errado :(");
                builder.setMessage("Não desista da gente porque está sem internet, tente novamente! ♥");

                builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        System.exit(0);
                    }
                });
                alert = builder.create();
                alert.show();
            }

        }
    }

    private class CarregarAvisosTask extends AsyncTask<Void, Void, String> {
        private Utils util = new Utils();
        private ProgressDialog load;

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Carregando avisos municipais ;)");
        }

        @Override
        protected String doInBackground(Void... voids) {
            String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/alertas/buscar-cidade/" + cidade);
            return consultaApi;
        }

        @Override
        protected void onPostExecute(String s) {
            try {

                JSONArray jsonArrayOcorrencias = new JSONArray(s);
                Log.i("Resultado ocorrência ", jsonArrayOcorrencias.toString());
                String status;
                for (int i = 0; i < jsonArrayOcorrencias.length(); i++) {
                    JSONObject jsonOcorrencia = jsonArrayOcorrencias.getJSONObject(i);
                    CardGeral card = new CardGeral();
                    if (jsonOcorrencia.getBoolean("statusDeAtividade") == true) {
                        status = "Ativo";
                    } else {
                        status = "Inativo";
                    }
                    card.setIdAlerta(jsonOcorrencia.getLong("id"));
                    card.setAlerta(jsonOcorrencia.getString("titulo"));
                    card.setTextOrgao(jsonOcorrencia.getJSONObject("departamentoResponsavel").getString("nomeFantasia"));
                    card.setVigencia(jsonOcorrencia.getString("vigencia"));
                    card.setStatusInfo(status);
                    listCard.add(card);
                }

                RecycleViewAdapterGeral recycleViewAdapter = new RecycleViewAdapterGeral(getApplicationContext(), listCard);
                recyclerView.setAdapter(recycleViewAdapter);
                load.dismiss();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void quebrarEndereco(String s) {
        String[] vetorEndereco = s.split(", "); // Remove ", " quebrando em várias strings
        rua = vetorEndereco[0];
        cep = vetorEndereco[3];

        String[] vetorNumBairro = vetorEndereco[1].split(" - ");
        numero = vetorNumBairro[0];
        bairro = vetorNumBairro[1];

        String[] vetorCidadeEstado = vetorEndereco[2].split(" -");
        cidade = vetorCidadeEstado[0];
        estado = vetorCidadeEstado[1];
        System.out.println("FUNFOU: " + cidade + estado);
    }

    private Usuario consultarLocalmente() {
        Usuario usuario = UsuarioDatabase
                .getInstance(getBaseContext())
                .getUsuarioDAO()
                .getUserById(1L);
        return usuario;
    }
}
