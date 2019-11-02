package com.projetos.mub;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

public class ListrarOcorrencias extends AppCompatActivity {
    ImageView listrarOcorrenciaImg;
    TextView textolista, listarOcorrenciaEmail;
    Spinner spListaOcorrencia;
    RecyclerView reclistaOcorrencia;
    private Usuario usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listrar_ocorrencias);
        /*
        * DESCOMENTAR ESSE TRECHO!
        ConsultarLocalmenteTask consultarLocalmenteTask = null;
        if (consultarLocalmenteTask == null) {
            consultarLocalmenteTask = new ConsultarLocalmenteTask();
        } else {
            consultarLocalmenteTask.cancel(true);
            consultarLocalmenteTask = new ConsultarLocalmenteTask();
        }
        consultarLocalmenteTask.execute();
        */
        listrarOcorrenciaImg = (ImageView) findViewById(R.id.imgListrarOcorrencia);
        textolista = (TextView) findViewById(R.id.textoLista);
        listarOcorrenciaEmail = (TextView) findViewById(R.id.listarOcorrenciaEmail);
        spListaOcorrencia = (Spinner) findViewById(R.id.spListaOcorrencia);
        reclistaOcorrencia = (RecyclerView) findViewById(R.id.recListaOcorencia);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Status_Ocorrencias, R.layout.support_simple_spinner_dropdown_item);

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

    /*
    * Task para atualização de dados locais.
     */
    private class ConsultarLocalmenteTask extends AsyncTask<Void, Void, Usuario> {

        @Override
        protected void onPreExecute() {
        }

        @Override
        protected Usuario doInBackground(Void... voids) {
            try {
                Usuario usuario = UsuarioDatabase
                        .getInstance(getBaseContext())
                        .getUsuarioDAO()
                        .getUserById(1L);
                System.out.println("BUSCA DE USUÁRIO: ID " + usuario.getNome() + " SUCESSO!");
                setUsuario(usuario);
                return usuario;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            listarOcorrenciaEmail.setText(usuario.getEmail());
            System.out.println("Identificador on POST EXECUTE " + usuario.getNome());
        }
    }

    private class BuscarOcorrenciasTask extends AsyncTask<Void, Void, String>{

        @Override
        protected String doInBackground(Void... voids) {
            return null;
        }
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
