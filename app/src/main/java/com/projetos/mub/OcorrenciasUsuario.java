package com.projetos.mub;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OcorrenciasUsuario extends AppCompatActivity {
    private TextView tvProtocolo, tvTipo, tvStatus, tvEndereco, tvDescricao, tvData, tvHorario, textDescricao, idTextDescricao, tvObs, tvTxtObs;
    private Button btAtender;
    private Usuario usuario;
    protected Bundle informacoes;
    private String txtObs = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencias_usuario);
        setTitle("Destalhes da Ocorrência");


        usuario = consultarLocalmente();
        this.informacoes = new Bundle();
        /*
         ** Declarando VIEWS
         */
        tvProtocolo = findViewById(R.id.tvProtocoloOcorrencia);
        tvTipo = findViewById(R.id.tvTipoOcorrencia);
        tvStatus = findViewById(R.id.tvStatusOcorrencia);
        tvEndereco = findViewById(R.id.tvEnderecoOcorrencia);
        tvDescricao = findViewById(R.id.tvDescricaoOcorrencia);
        tvData = findViewById(R.id.tvDataOcorrencia);
        textDescricao = findViewById(R.id.textDescricao);
        tvHorario = findViewById(R.id.tvHorarioOcorrencia);
        btAtender = findViewById(R.id.btAtenderOcorrencia);
        tvObs = findViewById(R.id.tvObs);
        tvTxtObs = findViewById(R.id.tvTxtObs);


        if (usuario.isAgente()) {
            btAtender.setVisibility(View.VISIBLE);
        }

        BuscarOcorrenciasTask buscarOcorrenciasTask = null;
        if (buscarOcorrenciasTask == null) {
            buscarOcorrenciasTask = new BuscarOcorrenciasTask();
        } else {
            buscarOcorrenciasTask.cancel(true);
            buscarOcorrenciasTask = new BuscarOcorrenciasTask();
        }
        buscarOcorrenciasTask.execute();

        BuscarAtendimentosTask buscarAtendimentosTask = null;
        if (buscarAtendimentosTask == null) {
            buscarAtendimentosTask = new BuscarAtendimentosTask();
        } else {
            buscarAtendimentosTask.cancel(true);
            buscarAtendimentosTask = new BuscarAtendimentosTask();
        }
        buscarAtendimentosTask.execute();

        btAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AtenderOcorrencia.class);
                intent.putExtras(informacoes);
                startActivity(intent);
            }
        });
    }

    private class BuscarAtendimentosTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            Intent intent = getIntent();
            Bundle inf = intent.getExtras();
            return util.getInfFromGET("http://192.168.137.1:8080/ocorrencia/atendimentos-ocorrencia/"
                    + inf.getLong("idOcorrencia"));
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArray = new JSONArray(s);
                JSONObject jsonObject = jsonArray.getJSONObject(0);
                System.out.println(jsonObject);
                txtObs = jsonObject.getJSONObject("status").getString("status") + " - "
                        + jsonObject.getString("observacao");
            } catch (JSONException e) {
                Log.e("Resultado", "Não encontramos nenhum array...");
                e.printStackTrace();
            }
            if (txtObs == "") {
                tvTxtObs.setText("Até o momento nada consta em nossos registros... Aguarde!");
            } else {
                tvTxtObs.setText(txtObs);
            }
        }
    }

    private class BuscarOcorrenciasTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            try {
                Intent intent = getIntent();
                Bundle inf = intent.getExtras();
                /*
                 ** Consulta as informações na API para atualização no banco local baseado no ID do usuário.
                 */
                String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/ocorrencia/buscar/"
                        + inf.getLong("idOcorrencia"));
                return consultaApi;
            } catch (Exception e) {
                Log.i("Erro na consulta", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            String dataHora;

            try {
                JSONObject jsonObject = new JSONObject(s);
                Log.i("Resultado ocorrência ", jsonObject.toString());

                /*
                 **Concatenando Endereço
                 */

                String endereco = jsonObject.getJSONObject("enderecoOcorrencia").getString("cep") + ", "
                        + jsonObject.getJSONObject("enderecoOcorrencia").getString("rua") + ", "
                        + jsonObject.getJSONObject("enderecoOcorrencia").getString("numero") + " - "
                        + jsonObject.getJSONObject("enderecoOcorrencia").getString("bairro") + ", "
                        + jsonObject.getJSONObject("enderecoOcorrencia").getString("cidade") + " - "
                        + jsonObject.getJSONObject("enderecoOcorrencia").getString("uf");

                /*
                 ** Separando DATA e HORA
                 */
                String[] textoSeparado = jsonObject.getString("data").split(" ");
                String status = jsonObject.getJSONObject("statusOcorrencia").getString("nome");
                tvProtocolo.setText(jsonObject.getString("protocolo"));
                tvTipo.setText(jsonObject.getJSONObject("tipoOcorrencia").getString("nome"));
                tvStatus.setText(status);
                tvEndereco.setText(endereco);
                tvDescricao.setText(jsonObject.getString("descricao"));
                tvData.setText(textoSeparado[0]);
                tvHorario.setText(textoSeparado[1]);

                informacoes.putString("data", textoSeparado[0]);
                informacoes.putString("hora", textoSeparado[1]);
                informacoes.putLong("idOcorrencia", jsonObject.getLong("id"));

                switch (status) {
                    case "Em espera":
                        tvStatus.setTextColor(Color.DKGRAY);
                        break;
                    case "Em atendimento":
                        tvStatus.setTextColor(Color.BLUE);
                        break;
                    case "Atendida":
                        tvStatus.setTextColor(Color.GREEN);
                        break;
                    case "Recusada":
                        tvStatus.setTextColor(Color.RED);
                        break;
                    default:
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private Usuario consultarLocalmente() {
        Usuario usuario = UsuarioDatabase
                .getInstance(getBaseContext())
                .getUsuarioDAO()
                .getUserById(1L);
        return usuario;
    }
}
