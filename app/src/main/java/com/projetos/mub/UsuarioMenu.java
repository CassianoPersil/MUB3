package com.projetos.mub;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UsuarioMenu extends AppCompatActivity {

    Button btSalvarUsu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario_menu);

        btSalvarUsu = (Button)findViewById(R.id.btSalvarUsuario);
        btSalvarUsu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getBaseContext(),MenuPrincipal.class );
                startActivity(intent);
            }
        });
    }
}
