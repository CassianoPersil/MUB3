package com.projetos.mub;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class InserirOcorrencia extends AppCompatActivity {

    private Object ImageViewFoto;

    Spinner sp;
    Button btEnviarOco, btInserirImagem;
    ImageButton imgOcorrencia, imgData,imgHoras;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_ocorrencia);

        sp = (Spinner) findViewById(R.id.sp);
        imgOcorrencia = (ImageButton) findViewById(R.id.imgOcorrencia);
        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        imgData = (ImageButton) findViewById(R.id.imgData);
        imgHoras = (ImageButton) findViewById(R.id.imgHoras);
        btInserirImagem = (Button) findViewById(R.id.btInserirImgOco);

        //Criar array para receber os dados da String
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(getBaseContext(),sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //verifica se foi permitido o uso da camera
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 0);
        }


        //recuperando os elementos da interface
        Button tirar = (Button) findViewById(R.id.btInserirImgOco);

        //metodo para o click do botão
        tirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tirarFoto();
            }
        });

        btEnviarOco = (Button)findViewById(R.id.btEnviarOco);
        btEnviarOco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });
    }

    //metodo para realizar a foto
    private void tirarFoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    //retornar o resultado
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");
            ImageView imageViewFoto = (ImageView) findViewById(R.id.imgOcorrencia);
            imageViewFoto.setImageBitmap(imagem);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
