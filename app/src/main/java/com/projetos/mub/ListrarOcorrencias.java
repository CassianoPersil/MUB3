package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spListaOcorrencia.setAdapter(adapter);

        spListaOcorrencia.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
