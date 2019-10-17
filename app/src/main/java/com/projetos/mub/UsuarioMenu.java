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

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class UsuarioMenu extends AppCompatActivity {
    private ImageView imgPerfilUsuario;
    private ImageButton btNomeUsuario, btSenhaUsuario, btEmailUsuario, btDataNasc;
    private TextView tvNomeUsuario, tvEmailUsuario, ctNomeUsuario, ctSenhaUsuario, ctCpf, ctEmailUsuario, ctDataNasc, ctTelefone;
    private Button btAtualizarUsuario;
    private Long idUsuario;

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
                System.out.println("BT OK");
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
        imgPerfilUsuario = (ImageView) findViewById(R.id.imgPerfilUsuario);
        tvNomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        ctNomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        ctSenhaUsuario = (TextView) findViewById(R.id.ctEditSenha);
        ctCpf = (TextView) findViewById(R.id.ctCpf);
        ctEmailUsuario = (TextView) findViewById(R.id.ctEditEmail);
        ctDataNasc = (TextView) findViewById(R.id.ctEditDtNascimento);
        tvNomeUsuario = (TextView) findViewById(R.id.tvEditNome);
        tvEmailUsuario = (TextView) findViewById(R.id.tvEditEmail);
        ctTelefone = (TextView) findViewById(R.id.ctEditTelefone);
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

            idUsuario = Long.parseLong(inf.getString("id"));
            try {
                return util.getInfFromGET("http://192.168.137.1:8080/user/buscar/" + idUsuario);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            try {
                JSONObject jsonObject = new JSONObject(s);
                JSONObject contatoObj = jsonObject.getJSONObject("contatoUsuario");

                //Carregando CPF e deixando campo desativado
                ctCpf.setEnabled(true);
                ctCpf.setText(jsonObject.getString("cpf"));
                ctCpf.setEnabled(false);

                ctNomeUsuario.setText(jsonObject.getString("nome"));
                tvNomeUsuario.setText(jsonObject.getString("nome"));

                ctSenhaUsuario.setText(jsonObject.getString("senha"));
                ctDataNasc.setText(jsonObject.getString("dataNascimento"));

                ctEmailUsuario.setText(contatoObj.getString("email"));
                tvEmailUsuario.setText(contatoObj.getString("email"));

                ctTelefone.setText(contatoObj.getString("telefone"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
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
            String retornoCadastro = "";
            Utils util = new Utils();
            try {
                Long id = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .insert(new Usuario(1L, ctNomeUsuario.getText().toString(), ctEmailUsuario.getText().toString(), false));
                System.out.println("INSERÇÃO DE USUÁRIO LOCAL OK ID: " + id);

                JSONObject json = new JSONObject();
                json.put("email", ctEmailUsuario.getText().toString());
                json.put("telefone", ctTelefone.getText().toString());
                json.put("nome", ctNomeUsuario.getText().toString());
                json.put("cpf", ctCpf.getText().toString());
                json.put("senha", ctSenhaUsuario.getText().toString());
                json.put("dataDeNascimento", ctDataNasc.getText().toString());
                retornoCadastro = util.putSend("http://192.168.137.1:8080/user/alterar/" + idUsuario, json);
                Log.i("RETORNO DO PUT: ", retornoCadastro);
                return id;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Long id) {
            System.out.println("USUÁRIO ATUALIZADO COM SUCESSO: ID - " + id);
            Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
            Bundle inf = new Bundle();

            inf.putString("id", inf.getString("id"));
            inf.putString("nome", ctNomeUsuario.getText().toString());
            inf.putString("email", ctEmailUsuario.getText().toString());
            inf.putString("statusLogin", "true");
            intent.putExtras(inf);
            startActivity(intent);
        }
    }
}
