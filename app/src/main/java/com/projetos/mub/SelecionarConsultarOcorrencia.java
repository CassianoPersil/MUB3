package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class SelecionarConsultarOcorrencia extends AppCompatActivity {
    ImageView imgUsuario, imgOcorrencia;
    TextView idNomeUsuario, idTipoOcorrencia, idOcorrencia, idImagemOcorrencia, iddataOcorrencia, data, idHorasOcorrencia, horas;
    //Button btAtender, btRecusar, btImgOcorrencia;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_consultar_ocorrencia);
        inicializarVariaveis();
    }

    public void inicializarVariaveis() {
        imgUsuario = (ImageView) findViewById(R.id.imagemUsuario);
        imgOcorrencia = (ImageView) findViewById(R.id.imgOcorrenciaConsulta);
        idNomeUsuario = (TextView) findViewById(R.id.NomeUsuario);
        idTipoOcorrencia = (TextView) findViewById(R.id.OcorrenciaTipo);
        idOcorrencia = (TextView) findViewById(R.id.OcorrenciaTipo);
        idImagemOcorrencia = (TextView) findViewById(R.id.btImgOcorrencia);
        iddataOcorrencia = (TextView) findViewById(R.id.dataOcorrencia);
        data = (TextView) findViewById(R.id.dataOcorrencia);
        idHorasOcorrencia = (TextView) findViewById(R.id.HorasOcorrencia);
        horas = (TextView) findViewById(R.id.horasOcorrencia);

    }
}
