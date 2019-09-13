package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class CadastrarUsuario extends AppCompatActivity {
    TextView ctnomeUsu, ctemailUsu, ctsenhaUsu, ctCpfUsu, ctTelefone, ctDataNasc;
    Button btCadastrarUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        ctnomeUsu = (TextView) findViewById(R.id.ctNomeUsu);
        ctemailUsu = (TextView) findViewById(R.id.ctEmailRecSenha);
        ctsenhaUsu = (TextView) findViewById(R.id.ctSenhaUsu);
        ctCpfUsu = (TextView) findViewById(R.id.ctCpfUsu);
        btCadastrarUsu = (Button) findViewById(R.id.btEnviarDados);
        ctTelefone = (TextView) findViewById(R.id.ctTelefone);
        ctDataNasc = (TextView) findViewById(R.id.ctDataNasc);


        btCadastrarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        SimpleMaskFormatter simpleMasktelefone = new SimpleMaskFormatter("(NN) N NNNN-NNNN");
        MaskTextWatcher masktelfone = new MaskTextWatcher(ctTelefone, simpleMasktelefone);

        ctTelefone.addTextChangedListener(masktelfone);

        SimpleMaskFormatter simpleMaskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf = new MaskTextWatcher(ctCpfUsu, simpleMaskcpf);

        ctCpfUsu.addTextChangedListener(maskcpf);



    }
}
