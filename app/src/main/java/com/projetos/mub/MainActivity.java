package com.projetos.mub;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
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

        //Async Task
        final GetJson login = new GetJson();

        //Atribuição de ID
        btLogar = (Button) findViewById(R.id.btLogin);
        ctCadastrar = (TextView) findViewById(R.id.ctCadastrar);
        ctEsqueceuSenha = (TextView) findViewById(R.id.ctEsqueceuSenha);
        ctEmail = (EditText) findViewById(R.id.ctLoginEmail);
        ctSenha = (EditText) findViewById(R.id.ctLoginSenha);

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login.execute();
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

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(MainActivity.this,
                    "Por favor aguarde...", "Recuperando informações...");
        }

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
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
                    Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                    startActivity(intent);
                }else{
                    System.out.println("Erro");
                }
            } catch (JSONException e) {
                Toast.makeText(getApplicationContext(),"Usuário ou senha incorretos", Toast.LENGTH_LONG);
               e.printStackTrace();
            }
            load.dismiss();
        }
    }

}
