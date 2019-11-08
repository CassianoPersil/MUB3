package com.projetos.mub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AtenderOcorrencia extends AppCompatActivity {
    private TextView tipoOcorrencia, dataOcorrencia, horarioOcorrencia, descOcorrencia;
    private Spinner statusOcorrencia;
    private Button btAtender;
    private Usuario usuario;
    private Bundle informacoes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_ocorrencia);
        inicializarVariaveis();
        setTitle("Atender Ocorrências");

        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        dataOcorrencia.setText(sdf1.format(d));
        horarioOcorrencia.setText(sdf2.format(d));

        this.usuario = consultarLocalmente();
        Intent intent = getIntent();
        informacoes = intent.getExtras();


        //Criar array para receber os dados da String
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusOcorrencia.setAdapter(adapter);

        statusOcorrencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getBaseContext(),sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

        });

        btAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AtenderOcorrenciasTask atenderOcorrenciasTask = null;
                if (atenderOcorrenciasTask == null) {
                    atenderOcorrenciasTask = new AtenderOcorrenciasTask();
                } else {
                    atenderOcorrenciasTask.cancel(true);
                    atenderOcorrenciasTask = new AtenderOcorrenciasTask();
                }
                atenderOcorrenciasTask.execute();
            }
        });

    }

    public void inicializarVariaveis() {
        tipoOcorrencia = findViewById(R.id.tipoOcorrenciaAtd);
        dataOcorrencia = findViewById(R.id.dataOcorrenciaAtd);
        horarioOcorrencia = findViewById(R.id.horarioOcorrenciaAtd);
        descOcorrencia = findViewById(R.id.ctObsOcorrenciaAtd);
        statusOcorrencia = findViewById(R.id.spinnerStatusOcorrenciaAtd);
        btAtender = findViewById(R.id.btAtenderOcorrenciaAtd);
    }

    private Usuario consultarLocalmente() {
        Usuario usuario = UsuarioDatabase
                .getInstance(getBaseContext())
                .getUsuarioDAO()
                .getUserById(1L);
        return usuario;
    }

    private class AtenderOcorrenciasTask extends AsyncTask<Void, Void, String> {
        private Utils util = new Utils();
        private AlertDialog alert;

        @Override
        protected String doInBackground(Void... voids) {
            String dataHora = dataOcorrencia.getText().toString() + " " + horarioOcorrencia.getText().toString();
            try {
                JSONObject json = new JSONObject();
                json.put("idStatusAtendimento", statusOcorrencia.getSelectedItemPosition() + 1);
                json.put("data", dataHora);
                json.put("obs", descOcorrencia.getText().toString());
                json.put("idOcorrencia", informacoes.getLong("idOcorrencia"));
                json.put("idAgente", usuario.getIdAgenteAPI());
                return util.postTeste("http://192.168.137.1:8080/ocorrencia/atender", json);
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
                if(json.getString("id") != null){
                    modal("Sucesso!", "Alteração de status ocorrida com sucesso", true);
                }else{
                    modal("Desculpe-nos :(", "Algo de errado aconteceu... Contate um administrador.", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void modal(String title, String message, final boolean sucess) {
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(AtenderOcorrencia.this);
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(sucess == true){
                        alert.dismiss();
                        Intent i = new Intent(getBaseContext(),ListrarOcorrencias.class);
                        startActivity(i);
                    }else{
                        alert.dismiss();
                    }
                }
            });
            alert = builder.create();
            alert.show();
        }
    }
}
