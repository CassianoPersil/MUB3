package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class CadastrarUsuario extends AppCompatActivity {
    TextView ctnomeUsu, ctemailUsu, ctsenhaUsu, ctsenhaConfUsu, ctCpfUsu;
    Button btCadastrarUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        ctnomeUsu = (TextView) findViewById(R.id.ctNomeUsu);
        ctemailUsu = (TextView) findViewById(R.id.ctEmailRecSenha);
        ctsenhaUsu = (TextView) findViewById(R.id.ctSenhaUsu);
        //ctsenhaConfUsu = (TextView) findViewById(R.id.ctConfSenhaUsu);
        ctCpfUsu = (TextView) findViewById(R.id.ctCpfUsu);
        btCadastrarUsu = (Button) findViewById(R.id.btEnviarDados);


        btCadastrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });



    }
}
