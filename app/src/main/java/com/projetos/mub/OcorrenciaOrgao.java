package com.projetos.mub;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;

import org.json.JSONException;
import org.json.JSONObject;

public class OcorrenciaOrgao extends AppCompatActivity {

    private TextView tvNmDepartamento, tvAlertaTitulo, tvAlertaVigencia, tvAlertaStatus, tvAlertaDescricao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencia_orgao);

        tvNmDepartamento = findViewById(R.id.tvNmDepartamentoAlerta);
        tvAlertaTitulo = findViewById(R.id.tvAlertaTitulo);
        tvAlertaVigencia = findViewById(R.id.tvAlertaVigencia);
        tvAlertaStatus = findViewById(R.id.tvAlertaStatus);
        tvAlertaDescricao = findViewById(R.id.tvAlertaDescricao);

        BuscarAlertaTask buscarAlertaTask = null;
        if (buscarAlertaTask == null) {
            buscarAlertaTask = new BuscarAlertaTask();
        } else {
            buscarAlertaTask.cancel(true);
            buscarAlertaTask = new BuscarAlertaTask();
        }
        buscarAlertaTask.execute();

    }

    private class BuscarAlertaTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            try {
                Intent intent = getIntent();
                Bundle inf = intent.getExtras();
                /*
                 ** Consulta as informações na API para atualização no banco local baseado no ID do usuário.
                 */
                String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/alertas/buscar/"
                        + inf.getLong("idAlerta"));
                return consultaApi;
            } catch (Exception e) {
                Log.i("Erro na consulta", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                String status;
                JSONObject jsonAlerta = new JSONObject(s);
                Log.i("Resultado do alerta ", jsonAlerta.toString());

                if(jsonAlerta.getBoolean("statusDeAtividade") == true){
                    status = "Ativo";
                }else{
                    status = "Inativo";
                }
                tvNmDepartamento.setText(jsonAlerta.getJSONObject("departamentoResponsavel").getString("nomeFantasia"));
                tvAlertaTitulo.setText(jsonAlerta.getString("titulo"));
                tvAlertaVigencia.setText(jsonAlerta.getString("vigencia"));
                tvAlertaStatus.setText(status);
                tvAlertaDescricao.setText(jsonAlerta.getString("descricao"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
