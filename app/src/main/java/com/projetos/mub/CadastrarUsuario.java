package com.projetos.mub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.projetos.mub.conexao.Utils;
import com.projetos.mub.conexao.pojos.LoginPojo;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastrarUsuario extends AppCompatActivity {
    TextView ctNomeUsuario, ctEmailUsuario, ctSenhaUsuario, ctCpfUsuario, ctTelefoneUsuario, ctNascimentoUsuario;
    Button btCadastrarUsuario;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        final CadastrarUsuarioPost cp = new CadastrarUsuarioPost();

        //chamando o metodo
        inicializacao();

        btCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp.execute();
            }
        });

        //Mascara do campo Telefone
        SimpleMaskFormatter simpleMasktelefone = new SimpleMaskFormatter("(NN) N NNNN-NNNN");
        MaskTextWatcher masktelefone = new MaskTextWatcher(ctTelefoneUsuario, simpleMasktelefone);
        ctTelefoneUsuario.addTextChangedListener(masktelefone);

        //Mascara do campo CPF
        SimpleMaskFormatter simpleMaskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf = new MaskTextWatcher(ctCpfUsuario, simpleMaskcpf);
        ctCpfUsuario.addTextChangedListener(maskcpf);

        //Mascara do campo Data de Nascimento
        SimpleMaskFormatter simpleMasknascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher masknascimento = new MaskTextWatcher(ctNascimentoUsuario, simpleMasknascimento);
        ctNascimentoUsuario.addTextChangedListener(masknascimento);


    }

    //metodo para inicializar as variaveis
    public void inicializacao() {
        ctNomeUsuario = (TextView) findViewById(R.id.ctCadNomeUsuario);
        ctEmailUsuario = (TextView) findViewById(R.id.ctCadEmailUsuario);
        ctSenhaUsuario = (TextView) findViewById(R.id.ctCadSenhaUsuario);
        ctCpfUsuario = (TextView) findViewById(R.id.ctCadCpfUsuario);
        btCadastrarUsuario = (Button) findViewById(R.id.btCadastrarUsuario);
        ctTelefoneUsuario = (TextView) findViewById(R.id.ctCadTelefoneUsuario);
        ctNascimentoUsuario = (TextView) findViewById(R.id.ctCadNascimentoUsuario);
    }

    private class CadastrarUsuarioPost extends AsyncTask<Void, Void, String>{

        private AlertDialog alert;

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(CadastrarUsuario.this,
                    "Por favor aguarde...", "Cadastrando usuário");
        }

        @Override
        protected void onPostExecute(String s) {
            load.dismiss();
            modalSucesso();
        }

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            try {
                JSONObject json = new JSONObject();
                json.put("email", ctEmailUsuario.getText().toString());
                json.put("telefone", ctTelefoneUsuario.getText().toString());
                json.put("nome", ctNomeUsuario.getText().toString());
                json.put("cpf", ctCpfUsuario.getText().toString());
                json.put("senha", ctSenhaUsuario.getText().toString());
                json.put("dataDeNascimento", ctNascimentoUsuario.getText().toString());
                return util.postTeste("http://192.168.1.5:8080/user/cadastrar", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void modalSucesso(){
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarUsuario.this);
            builder.setTitle("Sucesso!");
            builder.setMessage("Você se cadastrou com sucesso... Agora só precisa fazer login!");


            builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            alert = builder.create();
            alert.show();
        }
    }
}
