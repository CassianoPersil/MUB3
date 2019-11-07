package com.projetos.mub;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.util.Log;
import android.view.WindowManager;

import com.projetos.mub.conexao.Utils;
import com.projetos.mub.roomDatabase.UsuarioDatabase;
import com.projetos.mub.roomDatabase.entities.Usuario;

import org.json.JSONObject;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.ParseException;

public class Splash extends AppCompatActivity {

    private final int DURACION_SPLASH = 3000;
    private AlertDialog alert;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                testeConexao();
            }

            ;
        }, DURACION_SPLASH);
    }

    private void callConsultarTask() {
        ConsultarLocalmenteTask atualizarUsuarioLocal = null;
        if (atualizarUsuarioLocal == null) {
            atualizarUsuarioLocal = new ConsultarLocalmenteTask();
        } else {
            atualizarUsuarioLocal.cancel(true);
            atualizarUsuarioLocal = new ConsultarLocalmenteTask();
        }
        atualizarUsuarioLocal.execute();
    }

    private void callErroConexao() {
        System.out.println("Sem internet disponível!");
        // Criando gerador de Alerta
        final AlertDialog.Builder builder = new AlertDialog.Builder(Splash.this);
        builder.setTitle("Nossa, tem algo errado :(");
        builder.setMessage("Não desista da gente porque está sem internet, tente novamente! ♥");

        builder.setPositiveButton("Tentar novamente", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                testeConexao();
            }
        });

        builder.setNegativeButton("Fechar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alert.dismiss();
                finish();
            }
        });
        alert = builder.create();
        alert.show();
    }

    private void testeConexao() {
        if (isInternetAvailable()) {
            callConsultarTask();
            finish();
        } else {
            callErroConexao();
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
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
