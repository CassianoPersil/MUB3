package com.projetos.mub;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
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
        protected Usuario doInBackground(Void... voids) {
            Utils util = new Utils();
            try {
                /*
                 ** Dispara consulta no ROOM database, verificando se ja há um usuário logado
                 ** previamente...
                 */
                Usuario usuario = consultarLocalmente();

                /*
                 ** Verifica se o usuário deve manter-se logado e se está ATIVO.
                 */
                if (usuario.isManterLogado() == true && usuario.isStAtividade()) {

                    /*
                     ** Consulta as informações na API para atualização no banco local baseado no ID do usuário.
                     */
                    String consultaApi = util.getInfFromGET("http://192.168.137.1:8080/user/buscar/"
                            + usuario.getIdUsuarioAPI());

                    /*
                     ** Transformando em JSON OBJECT a resposta do servidor.
                     */
                    JSONObject jsonObject = new JSONObject(consultaApi);
                    JSONObject contatoObj = jsonObject.getJSONObject("contatoUsuario");
                    JSONObject nvAcessoObj = jsonObject.getJSONObject("nvAcessoUsuario");

                    /*
                     ** Atualizando no objeto usuário
                     */
                    usuario.setIdUsuarioAPI(jsonObject.getLong("id"));
                    usuario.setNome(jsonObject.getString("nome"));
                    usuario.setEmail(contatoObj.getString("email"));
                    usuario.setNvAcesso(nvAcessoObj.getInt("id"));

                    /*
                     ** Disparando atualizcão LOCAL
                     */
                    Long retornoUsuarioLocal = atualizarUsuarioLocal(usuario);
                    Log.i("ATUALIZADO COM SUCESSO", String.valueOf(retornoUsuarioLocal));
                    return usuario;
                }
                return null;
            } catch (Exception e) {
                Log.i("Error ON INSERT", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(Usuario usuario) {
            if (usuario != null && usuario.isManterLogado() == true) {
                Intent intent = new Intent(Splash.this, MenuPrincipal.class);
                startActivity(intent);
            } else {
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
            }
        }

        private Usuario consultarLocalmente() {
            Usuario usuario = UsuarioDatabase
                    .getInstance(getBaseContext())
                    .getUsuarioDAO()
                    .getUserById(1L);
            return usuario;
        }

        private Long atualizarUsuarioLocal(Usuario usuario) {
            Long id = UsuarioDatabase
                    .getInstance(getBaseContext())
                    .getUsuarioDAO()
                    .insert(usuario);
            return id;
        }
    }
}
