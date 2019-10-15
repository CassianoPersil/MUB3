package com.projetos.mub;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class UsuarioMenu extends AppCompatActivity {
    ImageView imgPerfilUsuario;
    ImageButton btNomeUsuario, btEnderecoUsuario, btEmailUsuario, btDataNasc;
    TextView tvNomeUsuario, tvEmailUsuario, ctNomeUsuario, ctEnderecoUsuario, ctCpf, ctEmailUsuario, ctDataNasc;
    Button btAtualizarUsuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_menu);
        //chamando o metodo
        iniciarVariaveis();
        ctCpf.setEnabled(false);

        //Disparando AsyncTask onCreate
        RecuperarDadosPerfilTask recuperarDadosPerfilTask = null;


        if (recuperarDadosPerfilTask == null) {
            recuperarDadosPerfilTask = new RecuperarDadosPerfilTask();
        } else {
            recuperarDadosPerfilTask.cancel(true);
            recuperarDadosPerfilTask = new RecuperarDadosPerfilTask();
        }
        recuperarDadosPerfilTask.execute();

        btAtualizarUsuario = (Button) findViewById(R.id.btAlterarDados);
        btAtualizarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Disparando AsyncTask onClick
                AtualizarUsuarioLocal atualizarUsuarioLocal = null;
                if (atualizarUsuarioLocal == null) {
                    atualizarUsuarioLocal = new AtualizarUsuarioLocal();
                } else {
                    atualizarUsuarioLocal.cancel(true);
                    atualizarUsuarioLocal = new AtualizarUsuarioLocal();
                }
                atualizarUsuarioLocal.execute();
                atualizarUsuarioLocal.cancel(true);

                Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });

        //Mascara do campo CPF
        SimpleMaskFormatter simpleMaskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf = new MaskTextWatcher(ctCpf, simpleMaskcpf);
        ctCpf.addTextChangedListener(maskcpf);

        //Mascara do campo Data de Nascimento
        SimpleMaskFormatter simpleMasknascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher masknascimento = new MaskTextWatcher(ctDataNasc, simpleMasknascimento);
        ctDataNasc.addTextChangedListener(masknascimento);
    }

    //metodo para inicializar as variaveis
    public void iniciarVariaveis() {
        btNomeUsuario = (ImageButton) findViewById(R.id.btNomeUsuario);
        btEnderecoUsuario = (ImageButton) findViewById(R.id.btEnderecoUsuario);
        btEmailUsuario = (ImageButton) findViewById(R.id.btEmailUsuario);
        btDataNasc = (ImageButton) findViewById(R.id.btDataNasc);
        imgPerfilUsuario = (ImageView) findViewById(R.id.imgPerfilUsuario);
        tvNomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        tvEmailUsuario = (TextView) findViewById(R.id.ctEmailUsuario);
        ctNomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        ctEnderecoUsuario = (TextView) findViewById(R.id.ctEnderecoUsuario);
        ctCpf = (TextView) findViewById(R.id.ctCpf);
        ctEmailUsuario = (TextView) findViewById(R.id.ctEmailUsuario);
        ctDataNasc = (TextView) findViewById(R.id.ctDataNasc);
        tvNomeUsuario = (TextView) findViewById(R.id.tvPerfilNomeUsuario);
        tvEmailUsuario = (TextView) findViewById(R.id.tvPerfilEmailUsuario);
    }

    private class RecuperarDadosPerfilTask extends AsyncTask<Void, Void, String> {
        private AlertDialog alert;
        private Utils util = new Utils();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Intent intent = getIntent();
            Bundle inf = intent.getExtras();
            try {
                return util.getInfFromGET("http://192.168.137.1:8080/user/buscar/" + inf.getString("id"));
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject contatoObj = jsonObject.getJSONObject("contatoUsuario");

                ctCpf.setEnabled(true);
                ctCpf.setText(jsonObject.getString("cpf"));
                ctCpf.setEnabled(false);

                ctNomeUsuario.setText(jsonObject.getString("nome"));
                tvNomeUsuario.setText(jsonObject.getString("nome"));
                ctEmailUsuario.setText(contatoObj.getString("email"));
                tvEmailUsuario.setText(contatoObj.getString("email"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            super.onPostExecute(s);
        }
    }

    private class AtualizarUsuarioLocal extends AsyncTask<Void, Void, Long> {

        @Override
        protected void onPreExecute() {
            /*load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Estamos carregando os seus dados! ;)");*/
        }

        @Override
        protected Long doInBackground(Void... voids) {
            try {
                Long id = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .insert(new Usuario(1L, ctNomeUsuario.getText().toString(), ctEmailUsuario.getText().toString(), false));
                System.out.println("Identificador on INSERT: " + id);
                return id;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long id) {
            System.out.println("USUÁRIO ATUALIZADO COM SUCESSO: ID - " + id);
        }
    }
}
