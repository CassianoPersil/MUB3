package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Ocorrencias extends AppCompatActivity {

    Button btAtender;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ocorrencias);

        btAtender = (Button) findViewById(R.id.btAtender);

        btAtender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AtenderOcorrencia.class);
                startActivity(intent);
            }
        });

    }
}
