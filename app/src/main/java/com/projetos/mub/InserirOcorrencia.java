package com.projetos.mub;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.location.aravind.getlocation.GeoLocator;
import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.util.Date;


public class InserirOcorrencia extends AppCompatActivity {
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Object ImageViewFoto;

    Spinner sp;
    Button btEnviarOco;
    TextView textLocal, dataOcorrencia, horarioOcorrencia;
    EditText descricaoOcorrencia;
    ImageButton imgData, imgHoras;
    private Usuario usuario;
    private String rua, numero, bairro, cidade, estado, cep;
    private Double latitude, longitude;


    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MenuPrincipal.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_ocorrencia);

        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        boolean GPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (!GPSEnabled) {
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

        sp = (Spinner) findViewById(R.id.sp);
        dataOcorrencia = (TextView) findViewById(R.id.textDataOco);
        textLocal = (TextView) findViewById(R.id.textLocal);
        descricaoOcorrencia = findViewById(R.id.textDescricaoOco);
        horarioOcorrencia = (TextView) findViewById(R.id.textHorarioOco);
        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        imgData = (ImageButton) findViewById(R.id.imgData);
        imgHoras = (ImageButton) findViewById(R.id.imgHoras);
        this.usuario = consultarLocalmente();

        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");
        dataOcorrencia.setText(sdf1.format(d));
        horarioOcorrencia.setText(sdf2.format(d));

        //Criar array para receber os dados da String
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ocorrencias, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp.setAdapter(adapter);
        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getBaseContext(),sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        btEnviarOco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InserirOcorrenciasTask inserirOcorrencia = null;
                if (inserirOcorrencia == null) {
                    inserirOcorrencia = new InserirOcorrenciasTask();
                } else {
                    inserirOcorrencia.cancel(true);
                    inserirOcorrencia = new InserirOcorrenciasTask();
                }
                inserirOcorrencia.execute();
            }
        });
    }

    private Usuario consultarLocalmente() {
        Usuario usuario = UsuarioDatabase
                .getInstance(getBaseContext())
                .getUsuarioDAO()
                .getUserById(1L);
        return usuario;
    }

    private class InserirOcorrenciasTask extends AsyncTask<Void, Void, String> {
        private Utils util = new Utils();
        private AlertDialog alert;

        @Override
        protected String doInBackground(Void... voids) {
            String dataHora = dataOcorrencia.getText().toString() + " " + horarioOcorrencia.getText().toString();
            try {
                JSONObject json = new JSONObject();
                json.put("data", dataHora);
                json.put("cep", cep);
                json.put("rua", rua);
                json.put("numero", numero);
                json.put("bairro", bairro);
                json.put("cidade", cidade);
                json.put("uf", estado);
                json.put("latitude", latitude);
                json.put("longitude", longitude);
                json.put("descricao", descricaoOcorrencia.getText());
                json.put("idUsuario", usuario.getIdUsuarioAPI());
                json.put("idTipoOcorrencia", sp.getSelectedItemPosition() + 1);
                json.put("idStatusOcorrencia", 1);
                return util.postTeste("http://192.168.137.1:8080/ocorrencia/inserir", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("Res ATEND", s);
            try {
                JSONObject json = new JSONObject(s);
                if (json.getString("id") != null) {
                    modal("Sucesso!", "Alteração de status ocorrida com sucesso", true);
                } else {
                    modal("Desculpe-nos :(", "Algo de errado aconteceu... Contate um administrador.", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void modal(String title, String message, final boolean sucess) {
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if (sucess == true) {
                        alert.dismiss();
                        Intent i = new Intent(getBaseContext(), ListrarOcorrencias.class);
                        startActivity(i);
                    } else {
                        alert.dismiss();
                    }
                }
            });
            alert = builder.create();
            alert.show();
        }
    }

    private class ConsultarCoordenadas extends AsyncTask<Void, Void, String> {
        private ProgressDialog load;
        Utils util = new Utils();

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(InserirOcorrencia.this,
                    "Por favor aguarde...", "Buscando coordenadas...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            GeoLocator geoLocator = new GeoLocator(getApplicationContext(), InserirOcorrencia.this);

            latitude = geoLocator.getLattitude();
            longitude = geoLocator.getLongitude();
            System.out.println(latitude + "\n" + longitude);
            String endereco = util.getInfFromGET("https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude +
                    "," + longitude + "&key=AIzaSyAUzj3IP_i6PCj-VcZYJaNLcr0lxj9xLnc");
            return endereco;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject jsonObject1 = jsonObject.getJSONArray("results").getJSONObject(0);
                String endereco = jsonObject1.getString("formatted_address");
                quebrarEndereco(endereco);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            load.dismiss();
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
        System.out.println("FUNFOU: " + estado);

        textLocal.setText(s);
    }
}
