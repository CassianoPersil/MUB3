package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class ListrarOcorrencias extends AppCompatActivity {
    ImageView listrarOcorrenciaImg;
    TextView textolista, listrarOcorrenciaEmail;
    Spinner spListaOcorrencia;
    RecyclerView reclistaOcorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrar_ocorrencias);

        listrarOcorrenciaImg = (ImageView) findViewById(R.id.imgListrarOcorrencia);
        textolista = (TextView) findViewById(R.id.textoLista);
        listrarOcorrenciaEmail = (TextView) findViewById(R.id.listrarOcorrenciaEmail);
        spListaOcorrencia = (Spinner) findViewById(R.id.spListaOcorrencia);
        reclistaOcorrencia = (RecyclerView) findViewById(R.id.recListaOcorencia);
    }


}
