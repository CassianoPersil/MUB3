package com.projetos.mub;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import java.util.Calendar;


public class InserirOcorrencia extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{
    static final int REQUEST_IMAGE_CAPTURE = 1;

    private Object ImageViewFoto;

    Spinner sp;
    Button btEnviarOco;
    TextView ctDataOco, ctHorarioOco;
    ImageButton imgOcorrencia, imgData,imgHoras;
    TimePickerDialog timePickerDialog;
    Calendar calendar;
    int currentHour;
    int currentMinute;
    String amPm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_ocorrencia);

        sp = (Spinner) findViewById(R.id.sp);
        imgOcorrencia = (ImageButton) findViewById(R.id.imgOcorrenciaConsulta);
        ctDataOco = (TextView) findViewById(R.id.ctDataOco);
        ctHorarioOco = (TextView) findViewById(R.id.ctHorarioOco);
        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        imgData = (ImageButton) findViewById(R.id.imgData);
        imgHoras = (ImageButton) findViewById(R.id.imgHoras);


        //Criar array para receber os dados da String
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.ocorrencias, R.layout.support_simple_spinner_dropdown_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        sp.setAdapter(adapter);

        sp.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
               // Toast.makeText(getBaseContext(),sp.getSelectedItem().toString(),Toast.LENGTH_LONG).show();
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
        ImageButton tirar = (ImageButton) findViewById(R.id.imgOcorrenciaConsulta);

        //metodo para o click do botão
        tirar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //tirarFoto();
                dispatchTakePictureIntent();
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

        //evento para chamar o relogio
        imgHoras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                calendar = Calendar.getInstance();
                currentHour = calendar.get(Calendar.HOUR_OF_DAY);
                currentMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = new TimePickerDialog(InserirOcorrencia.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minutes) {
                        if (hourOfDay >= 12) {
                            amPm = "PM";
                        } else {
                            amPm = "AM";
                        }
                        ctHorarioOco.setText(String.format("%02d:%02d", hourOfDay, minutes) + amPm);
                    }
                }, currentHour, currentMinute, false);

                timePickerDialog.show();

            }
        });

        //evento para chamar o calendario

        imgData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePickerDialog();
            }
        });

    }

    //metodo para realizar a foto
    private void tirarFoto() {
        if (MediaStore.ACTION_IMAGE_CAPTURE != null){
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, 1);
        }else{
            return;
        }

    }

    //retornar o resultado da camera
   /* @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1){
            Bundle extras = data.getExtras();
            Bitmap imagem = (Bitmap) extras.get("data");
            ImageView imageViewFoto = (ImageView) findViewById(R.id.imgOcorrenciaConsulta);
            imageViewFoto.setImageBitmap(imagem);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
*/
//metodo para setar a data no calendario
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String Date = dayOfMonth + "/" + month + "/" + year;
        ctDataOco.setText(Date);

    }

//metodo para capturar a data
    private void showDatePickerDialog(){
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageViewFoto = (ImageView) findViewById(R.id.imgOcorrenciaConsulta);
            imageViewFoto.setImageBitmap(imageBitmap);
        }
    }




}
