package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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

        txSelecionar = (TextView) findViewById(R.id.txSelecionar);
        txSelecionarMeses = (TextView) findViewById(R.id.txSelecionarMes);
        spMeses = (Spinner) findViewById(R.id.spMeses);
        spinnerOcorrencias = (Spinner) findViewById(R.id.spinnerOcorrencias);
        btPesquisar = (Button) findViewById(R.id.btPesquisar);
        recListragem = (RecyclerView) findViewById(R.id.recListragem);

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
