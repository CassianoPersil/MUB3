package com.projetos.mub;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.conexao.pojos.LoginPojo;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    Button btLogar;
    TextView ctCadastrar;
    TextView ctEsqueceuSenha;
    EditText ctEmail;
    EditText ctSenha;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Chamando view
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Atribuição de ID
        btLogar = (Button) findViewById(R.id.btLogin);
        ctCadastrar = (TextView) findViewById(R.id.ctCadastrar);
        ctEsqueceuSenha = (TextView) findViewById(R.id.ctEsqueceuSenha);
        ctEmail = (EditText) findViewById(R.id.ctLoginEmail);
        ctSenha = (EditText) findViewById(R.id.ctLoginSenha);

        btLogar.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                /*//Async Task
                GetJson login = null;
                if (login == null) {
                    login = new GetJson();
                } else {
                    login.cancel(true);
                    login = new GetJson();
                }
                login.execute();*/

                Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });

        ctCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), CadastrarUsuario.class);
                startActivity(intent);
            }
        });

        ctEsqueceuSenha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), RecuperarSenha.class);
                startActivity(intent);
            }
        });


    }

    private class GetJson extends AsyncTask<Void, Void, String> {

        private AlertDialog alert;
        private Utils util = new Utils();

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this,
                    "Por favor aguarde...", "Realizando login...");
        }

        @Override
        protected String doInBackground(Void... voids) {

            try {
                JSONObject json = new JSONObject();
                json.put("email", ctEmail.getText().toString());
                json.put("senha", ctSenha.getText().toString());
                return util.postTeste("http://192.168.137.1:8080/user/login", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String string) {
            try {
                JSONObject jsonObject = new JSONObject(string);
                if (Boolean.parseBoolean(jsonObject.getString("statusLogin")) == true) {
                    try {
                        Long id = UsuarioDatabase
                                .getInstance(getBaseContext())
                                .getUsuarioDAO()
                                .insert(new Usuario(1L,
                                        Long.parseLong(jsonObject.getString("id")),
                                        jsonObject.getString("nomeUsuario"),
                                        jsonObject.getString("email"),
                                        false, true));
                        System.out.println("Identificador on INSERT: " + id);
                        Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.i("Error ON INSERT", e.toString());
                    }
                } else {
                    System.out.println("Erro");
                }
            } catch (JSONException e) {
                modalErro();
                e.printStackTrace();
            }
            load.dismiss();
        }

        protected void modalErro() {
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setTitle("Erro...");
            builder.setMessage("E-mail ou senha incorretos... :/");
            builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                }
            });
            alert = builder.create();
            alert.show();
        }
    }

}
