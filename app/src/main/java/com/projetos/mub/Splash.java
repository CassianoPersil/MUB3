package com.projetos.mub;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONObject;

import java.text.ParseException;

public class Splash extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                //Disparando AsyncTask onClick
                ConsultarLocalmenteTask atualizarUsuarioLocal = null;
                if (atualizarUsuarioLocal == null) {
                    atualizarUsuarioLocal = new ConsultarLocalmenteTask();
                } else {
                    atualizarUsuarioLocal.cancel(true);
                    atualizarUsuarioLocal = new ConsultarLocalmenteTask();
                }
                atualizarUsuarioLocal.execute();
                finish();
            }

            ;
        }, DURACION_SPLASH);
    }

    private class ConsultarLocalmenteTask extends AsyncTask<Void, Void, Usuario> {

        @Override
        protected void onPreExecute() {
            /*load = ProgressDialog.show(MenuPrincipal.this,
                    "Por favor aguarde...", "Estamos carregando os seus dados! ;)");*/
        }

        @Override
        protected Usuario doInBackground(Void... voids) {
            Utils util = new Utils();
            try {

                // Invocando método para consultar usuário LOCAL
                Usuario usuario = consultarLocalmente();

                //Verificando se o usuário deseja manter-se logado
                if(usuario.isManterLogado() == true){
                    String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/user/buscar/"
                            + usuario.getIdUsuarioAPI());

                    //Coletando dados
                    JSONObject jsonObject = new JSONObject(consultaApi);
                    JSONObject contatoObj = jsonObject.getJSONObject("contatoUsuario");

                    Long retornoUsuarioLocal = atualizarUsuarioLocal(
                            Long.parseLong(jsonObject.getString("id")),
                            jsonObject.getString("nome"),
                            contatoObj.getString("email"));
                    Log.i("ATUALIZADO COM SUCESSO", String.valueOf(retornoUsuarioLocal));
                }
                return usuario;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario.isManterLogado() == true) {
                Intent intent = new Intent(Splash.this, MenuPrincipal.class);
                startActivity(intent);
            }else{
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
            }
        }

        private Usuario consultarLocalmente(){
            Usuario usuario = UsuarioDatabase
                    .getInstance(getBaseContext())
                    .getUsuarioDAO()
                    .getUserById(1L);
            return usuario;
        }

        private Long atualizarUsuarioLocal(Long idApi, String nome, String email){
            Long id = UsuarioDatabase
                    .getInstance(getBaseContext())
                    .getUsuarioDAO()
                    .insert(new Usuario(1L,
                            idApi,
                            nome,
                            email,
                            false, true));
            return id;
        }
    }
}
