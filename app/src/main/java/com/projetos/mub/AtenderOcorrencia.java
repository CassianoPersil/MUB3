package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class AtenderOcorrencia extends AppCompatActivity {
    ImageView imgUsuario, imgOcorrencia;
    TextView idNomeUsuario, idTipoOcorrencia, idOcorrencia, idImagemOcorrencia, iddataOcorrencia, data, idHorasOcorrencia, horas;
    Button btAtender, btRecusar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_ocorrencia);
    }

    public void inicializarVariaveis(){
        imgUsuario = (ImageView) findViewById(R.id.imgUsuario);
        imgOcorrencia = (ImageView) findViewById(R.id.imgOcorrencia);
        idNomeUsuario = (TextView) findViewById(R.id.idNomeUsuario);
        idTipoOcorrencia = (TextView) findViewById(R.id.idTipoOcorrencia);
        idOcorrencia = (TextView) findViewById(R.id.idOcorrencia);
        idImagemOcorrencia = (TextView) findViewById(R.id.idImgOcorrencia);
        iddataOcorrencia = (TextView) findViewById(R.id.idataOcorrencia);
        data = (TextView) findViewById(R.id.data);
        idHorasOcorrencia = (TextView) findViewById(R.id.idHorasOcorrencia);
        horas = (TextView) findViewById(R.id.horas);
        btAtender = (Button) findViewById(R.id.btAtender);
        btRecusar = (Button) findViewById(R.id.btRecusar);
    }
}
