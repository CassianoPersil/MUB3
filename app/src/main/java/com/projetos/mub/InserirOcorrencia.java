package com.projetos.mub;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;


public class InserirOcorrencia extends AppCompatActivity{
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Object ImageViewFoto;

    Spinner sp;
    Button btEnviarOco;
    TextView textLocal, dataOcorrencia, horarioOcorrencia;
    EditText descricaoOcorrencia;
    ImageButton imgData, imgHoras;
    private Usuario usuario;

    @Override
    public void onBackPressed() {
        Intent i = new Intent(getBaseContext(), MenuPrincipal.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inserir_ocorrencia);

        sp = (Spinner) findViewById(R.id.sp);
        dataOcorrencia = (TextView) findViewById(R.id.textDataOco);
        textLocal = (TextView) findViewById(R.id.textLocal);
        descricaoOcorrencia = findViewById(R.id.textDescricaoOco);
        horarioOcorrencia = (TextView) findViewById(R.id.textHorarioOco);
        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        imgData = (ImageButton) findViewById(R.id.imgData);
        imgHoras = (ImageButton) findViewById(R.id.imgHoras);


        this.usuario = consultarLocalmente();

        Date d = new Date();
        SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("HH:mm:ss");

        dataOcorrencia.setText(sdf1.format(d));
        horarioOcorrencia.setText(sdf2.format(d));

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

        btEnviarOco = (Button) findViewById(R.id.btEnviarOco);
        btEnviarOco.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(getBaseContext(), MenuPrincipal.class);
                startActivity(intent);
            }
        });
    }
    private Usuario consultarLocalmente() {
        Usuario usuario = UsuarioDatabase
                .getInstance(getBaseContext())
                .getUsuarioDAO()
                .getUserById(1L);
        return usuario;
    }

    private class InserirOcorrenciasTask extends AsyncTask<Void, Void, String> {
        private Utils util = new Utils();
        private AlertDialog alert;

        @Override
        protected String doInBackground(Void... voids) {
            String dataHora = dataOcorrencia.getText().toString() + " " + horarioOcorrencia.getText().toString();
            try {
                JSONObject json = new JSONObject();
                json.put("cep", "");
                json.put("rua", "");
                json.put("numero", "");
                json.put("bairro", "");
                json.put("cidade", "");
                json.put("uf", "");
                json.put("latitude", "");
                json.put("longitude", "");
                json.put("descricao", descricaoOcorrencia.getText());
                json.put("idUsuario", usuario.getIdUsuarioAPI());
                json.put("idTipoOcorrencia", sp.getSelectedItemPosition() + 1);
                json.put("idStatusOcorrencia", 1);
                return util.postTeste("http://192.168.137.1:8080/ocorrencia/add", json);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("Res ATEND", s);
            try {
                JSONObject json = new JSONObject(s);
                if(json.getString("id") != null){
                    modal("Sucesso!", "Alteração de status ocorrida com sucesso", true);
                }else{
                    modal("Desculpe-nos :(", "Algo de errado aconteceu... Contate um administrador.", false);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        protected void modal(String title, String message, final boolean sucess) {
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(getBaseContext());
            builder.setTitle(title);
            builder.setMessage(message);
            builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    if(sucess == true){
                        alert.dismiss();
                        Intent i = new Intent(getBaseContext(),ListrarOcorrencias.class);
                        startActivity(i);
                    }else{
                        alert.dismiss();
                    }
                }
            });
            alert = builder.create();
            alert.show();
        }
    }


}
