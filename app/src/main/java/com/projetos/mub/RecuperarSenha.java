package com.projetos.mub;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class RecuperarSenha extends AppCompatActivity {
    ImageView imgLogo;
    TextView tvRecuperacao;
    TextView ctEmailRecSenha;
    Button btEnviarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar_senha);

        imgLogo = (ImageView) findViewById(R.id.imgLogo);
        tvRecuperacao = (TextView) findViewById(R.id.tvRecuperacao);
        ctEmailRecSenha = (TextView) findViewById(R.id.ctEmailRecSenha);
        btEnviarDados = (Button) findViewById(R.id.btEnviarDados);


    }
}
