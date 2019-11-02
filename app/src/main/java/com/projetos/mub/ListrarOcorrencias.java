package com.projetos.mub;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
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

import java.util.ArrayList;
import java.util.List;

public class ListrarOcorrencias extends AppCompatActivity {
    List<Cards> listCard;
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
       // listrarOcorrenciaImg = (ImageView) findViewById(R.id.imgListrarOcorrencia);
       // textolista = (TextView) findViewById(R.id.textoLista);
       // listarOcorrenciaEmail = (TextView) findViewById(R.id.listarOcorrenciaEmail);
       // spListaOcorrencia = (Spinner) findViewById(R.id.spListaOcorrencia);
        reclistaOcorrencia = (RecyclerView) findViewById(R.id.recCards);



        listCard = new ArrayList<>();
        listCard.add(new Cards());
        listCard.add(new Cards());
        listCard.add(new Cards());
        listCard.add(new Cards());
        listCard.add(new Cards());
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recCards);
        RecycleViewAdapter recycleViewAdapter = new RecycleViewAdapter(this, listCard);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setAdapter(recycleViewAdapter);

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
