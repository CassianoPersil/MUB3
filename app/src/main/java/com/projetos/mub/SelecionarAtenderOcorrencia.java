package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class SelecionarAtenderOcorrencia extends AppCompatActivity {
    TextView tvSelecionarOcorrencia;
    RecyclerView recListragemOcorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_atender_ocorrencia);

        tvSelecionarOcorrencia = (TextView) findViewById(R.id.tvAlterarOcorrencia);
        recListragemOcorrencia = (RecyclerView) findViewById(R.id.recAlterarOcorrencia);


           }

}
