package com.projetos.mub;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListrarOcorrencias extends AppCompatActivity {
    private List<Cards> listCard = new ArrayList<>();
    private ImageView listrarOcorrenciaImg;
    private TextView textolista, listarOcorrenciaEmail;
    private Spinner spListaOcorrencia;
    private RecyclerView reclistaOcorrencia;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrar_ocorrencias);
        reclistaOcorrencia = (RecyclerView) findViewById(R.id.recCards);

        BuscarOcorrenciasTask consultarLocalmenteTask = null;
        if (consultarLocalmenteTask == null) {
            consultarLocalmenteTask = new BuscarOcorrenciasTask();
        } else {
            consultarLocalmenteTask.cancel(true);
            consultarLocalmenteTask = new BuscarOcorrenciasTask();
        }
        consultarLocalmenteTask.execute();



    }


    private class BuscarOcorrenciasTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            try {
                /*
                 ** Dispara consulta no ROOM database, verificando se ja há um usuário logado
                 ** previamente...
                 */
                Usuario usuario = consultarLocalmente();

                /*
                 ** Verifica se o usuário deve manter-se logado e se está ATIVO.
                 */
                if (usuario.isManterLogado() == true && usuario.isStAtividade() == true && !usuario.isAgente()) {

                    /*
                     ** Consulta as informações na API para atualização no banco local baseado no ID do usuário.
                     */
                    String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/ocorrencia/buscar-usuario/4");
                    //+ usuario.getIdUsuarioAPI());
                    return consultaApi;
                }
                return null;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONArray jsonArrayOcorrencias = new JSONArray(s);
                Log.i("Resultado ocorrência ", jsonArrayOcorrencias.toString());

                for (int i = 0; i < jsonArrayOcorrencias.length(); i++) {
                     Cards card = new Cards();
                    JSONObject jsonOcorrencia = jsonArrayOcorrencias.getJSONObject(i);
                    System.out.println(jsonOcorrencia.getString("protocolo"));
                    card.setTipo(jsonOcorrencia.getJSONObject("tipoOcorrencia").getString("nome"));
                    card.setStatus(jsonOcorrencia.getJSONObject("statusOcorrencia").getString("nome"));
                    listCard.add(card);
                }

                RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recCards);
                RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(getBaseContext(), listCard);
                recyclerView.setLayoutManager(new GridLayoutManager(getBaseContext(), 1));
                recyclerView.setAdapter(recycleViewAdapter);
            } catch (JSONException e) {
                e.printStackTrace();
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

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
