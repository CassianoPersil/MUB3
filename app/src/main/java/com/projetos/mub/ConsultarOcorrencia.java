package com.projetos.mub;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

public class ConsultarOcorrencia extends AppCompatActivity {
    TextView txSelecionar, txSelecionarMeses;
    Spinner spMeses, spinnerOcorrencias;
    Button btPesquisar;
    RecyclerView recListragem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consultar_ocorrencia);

        txSelecionar = (TextView) findViewById(R.id.tvAlterarOcorrencia);
        txSelecionarMeses = (TextView) findViewById(R.id.tvSelecionarMesOcorrencia);
        spMeses = (Spinner) findViewById(R.id.spMesesOcorrencia);
        spinnerOcorrencias = (Spinner) findViewById(R.id.spinnerAtenderOcorrencias);
        btPesquisar = (Button) findViewById(R.id.btPesquisarOcorrencia);
        recListragem = (RecyclerView) findViewById(R.id.recAlterarOcorrencia);

        btPesquisar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(),SelecionarConsultarOcorrencia.class);
                startActivity(intent);
            }
        });

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerOcorrencias.setAdapter(adapter);

        spinnerOcorrencias.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(),spinnerStatus.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.spinner_meses, R.layout.support_simple_spinner_dropdown_item);

        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spMeses.setAdapter(adapter1);

        spMeses.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //Toast.makeText(getBaseContext(),spinnerStatus.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
}
