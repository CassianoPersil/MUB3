package com.projetos.mub;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

public class AlterarOcorrencia extends AppCompatActivity {
    ImageView ImgPerfilUsuario, ImgOcorrencia;
    TextView NomeUsuario, EmailUsuario, ctLocalOcorrencia, ctImagemOcorrencia,
            ctDataOcorrencia, ctHorarioOcorrencia, ctTextoStatusOcorrencia, ctStautsOcorrencia, ctObservacoes;
    Button btAlterarDados;
    Spinner spinnerStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alterar_ocorrencia);

        ImgPerfilUsuario = (ImageView) findViewById(R.id.imgPerfilUsuario);
        ImgOcorrencia = (ImageView) findViewById(R.id.imgOcorrenciaConsulta);
        NomeUsuario = (TextView) findViewById(R.id.NomeUsuario);
        EmailUsuario = (TextView) findViewById(R.id.EmailUsuario);
        ctLocalOcorrencia = (TextView) findViewById(R.id.ctLocalOcorrencia);
        ctImagemOcorrencia = (TextView) findViewById(R.id.ctImagemOcorrencia);
        ctDataOcorrencia = (TextView) findViewById(R.id.ctDataOcorrencia);
        ctHorarioOcorrencia = (TextView)findViewById(R.id.ctHorarioOcorrencia);
        ctTextoStatusOcorrencia = (TextView) findViewById(R.id.ctTextoStatusOcorrencia);
        ctStautsOcorrencia = (TextView) findViewById(R.id.ctStatusOcorrencia);
        btAlterarDados = (Button) findViewById(R.id.btAlterarDados);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        ctObservacoes = (TextView) findViewById(R.id.ctObservacoes);


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerStatus.setAdapter(adapter);

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
