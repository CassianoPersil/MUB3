package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AtenderOcorrencia extends AppCompatActivity {
    ImageView imgUsuario;
    Spinner spinnerStatus;
    TextView nomeUsuario, textOcorrencia, Ocorrencia, textData, data, textHorario, horario , ctObservacoes, textStatus;
    Button btAtender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atender_ocorrencia);
        inicializarVariaveis();

        //Criar array para receber os dados da String
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getBaseContext(),sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }

    });

        btAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });

    }

    public void inicializarVariaveis(){
        imgUsuario = (ImageView) findViewById(R.id.imagemUsuario);
        nomeUsuario= (TextView) findViewById(R.id.nomeUsuario);
        textOcorrencia = (TextView) findViewById(R.id.textOcorrencia);
        Ocorrencia = (TextView) findViewById(R.id.Ocorrencia);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        textData = (TextView) findViewById(R.id.textData);
        data = (TextView) findViewById(R.id.data);
        textHorario = (TextView) findViewById(R.id.textHorario);
        horario = (TextView) findViewById(R.id.horario);
        ctObservacoes = (TextView) findViewById(R.id.ctObservacoes);
        textStatus = (TextView) findViewById(R.id.textStatus);
        btAtender = (Button) findViewById(R.id.btAtender);


    }
}
