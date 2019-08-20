package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AlterarOcorrencia extends AppCompatActivity {
    ImageView ImgPerfilUsuario, ImgOcorrencia;
    TextView NomeUsuario, EmailUsuario, ctLocalOcorrencia, ctImagemOcorrencia,
            ctDataOcorrencia, ctHorarioOcorrencia, ctTextoStatusOcorrencia, ctStautsOcorrencia;
    Button btAlterarDados;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_ocorrencia);

        ImgPerfilUsuario = (ImageView) findViewById(R.id.imgPerfilUsuario);
        ImgOcorrencia = (ImageView) findViewById(R.id.imgOcorrencia);
        NomeUsuario = (TextView) findViewById(R.id.NomeUsuario);
        EmailUsuario = (TextView) findViewById(R.id.EmailUsuario);
        ctLocalOcorrencia = (TextView) findViewById(R.id.ctLocalOcorrencia);
        ctImagemOcorrencia = (TextView) findViewById(R.id.ctImagemOcorrencia);
        ctDataOcorrencia = (TextView) findViewById(R.id.ctDataOcorrencia);
        ctHorarioOcorrencia = (TextView)findViewById(R.id.ctHorarioOcorrencia);
        ctTextoStatusOcorrencia = (TextView) findViewById(R.id.ctTextoStatusOcorrencia);
        ctStautsOcorrencia = (TextView) findViewById(R.id.ctStatusOcorrencia);
        btAlterarDados = (Button) findViewById(R.id.btAlterarDados);
    }
}
