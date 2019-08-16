package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
        ctemailUsu = (TextView) findViewById(R.id.ctEmailUsu);
        ctsenhaUsu = (TextView) findViewById(R.id.ctSenhaUsu);
        ctsenhaConfUsu = (TextView) findViewById(R.id.ctConfSenhaUsu);
        ctCpfUsu = (TextView) findViewById(R.id.ctCpfUsu);
        btCadastrarUsu = (Button) findViewById(R.id.btSalvarCad);

    }
}
