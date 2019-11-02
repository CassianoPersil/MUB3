package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

public class SelecionarAlterarOcorrencia extends AppCompatActivity {
    TextView tvAlterarOcorrencia;
    RecyclerView recAlterarOcorrencia;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selecionar_alterar_ocorrencia);

        tvAlterarOcorrencia = (TextView) findViewById(R.id.tvAlterarOcorrencia);
        recAlterarOcorrencia = (RecyclerView) findViewById(R.id.recAlterarOcorrencia);
    }
}
