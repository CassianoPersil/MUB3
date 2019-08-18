package com.projetos.mub;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button btLogar;
    TextView ctcadastrar;
    TextView ctEsqueceuSenha;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btLogar = (Button) findViewById(R.id.btLogar);
        ctcadastrar = (TextView) findViewById(R.id.ctCadastrar);
        ctEsqueceuSenha = (TextView) findViewById(R.id.ctEsqueceuSenha);

        btLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(),MenuPrincipal.class);
                startActivity(intent);
            }
        });

        ctcadastrar.setOnClickListener(new View.OnClickListener() {
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

}
