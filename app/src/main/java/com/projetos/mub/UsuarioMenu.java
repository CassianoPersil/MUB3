package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

public class UsuarioMenu extends AppCompatActivity {
    ImageView imgPerfilUsuario;
    ImageButton btNomeUsuario,btEnderecoUsuario,btEmailUsuario, btDataNasc;
    TextView nomeUsuario, emailUsuario, ctNomeUsuario,ctEnderecoUsuario,ctCpf,ctEmailUsuario,ctDataNasc;
    Button btSalvarUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_menu);
        //chamando o metodo
        iniciarVariaveis();

        btSalvarUsu = (Button)findViewById(R.id.btAlterarDados);
        btSalvarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MenuPrincipal.class );
                startActivity(intent);
            }
        });

        //Mascara do campo CPF
        SimpleMaskFormatter simpleMaskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf = new MaskTextWatcher(ctCpf, simpleMaskcpf);
        ctCpf.addTextChangedListener(maskcpf);

        //Mascara do campo Data de Nascimento
        SimpleMaskFormatter simpleMasknascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher masknascimento = new MaskTextWatcher(ctDataNasc, simpleMasknascimento);
        ctDataNasc.addTextChangedListener(masknascimento);
    }
    //metodo para inicializar as variaveis
    public void iniciarVariaveis(){
        btNomeUsuario = (ImageButton) findViewById(R.id.btNomeUsuario);
        btEnderecoUsuario = (ImageButton) findViewById(R.id.btEnderecoUsuario);
        btEmailUsuario = (ImageButton) findViewById(R.id.btEmailUsuario);
        btDataNasc = (ImageButton) findViewById(R.id.btDataNasc);
        imgPerfilUsuario = (ImageView) findViewById(R.id.imgPerfilUsuario);
        nomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        emailUsuario = (TextView) findViewById(R.id.ctEmailUsuario);
        ctNomeUsuario = (TextView) findViewById(R.id.ctNomeUsuario);
        ctEnderecoUsuario = (TextView) findViewById(R.id.ctEnderecoUsuario);
        ctCpf = (TextView) findViewById(R.id.ctCpf);
        ctEmailUsuario = (TextView) findViewById(R.id.ctEmailUsuario);
        ctDataNasc = (TextView) findViewById(R.id.ctDataNasc);
    }
}
