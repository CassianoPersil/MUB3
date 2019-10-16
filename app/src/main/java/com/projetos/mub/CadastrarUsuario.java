package com.projetos.mub;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.projetos.mub.conexao.Utils;
import com.projetos.mub.conexao.pojos.LoginPojo;

import org.json.JSONException;
import org.json.JSONObject;

public class CadastrarUsuario extends AppCompatActivity {
    TextView ctNomeUsuario, ctEmailUsuario, ctSenhaUsuario, ctCpfUsuario, ctTelefoneUsuario, ctNascimentoUsuario;
    Button btCadastrarUsuario;
    private ProgressDialog load;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastrar_usuario);

        final CadastrarUsuarioPost cp = new CadastrarUsuarioPost();

        //chamando o metodo
        inicializacao();

        btCadastrarUsuario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cp.execute();
            }
        });

        //Mascara do campo Telefone
        SimpleMaskFormatter simpleMasktelefone = new SimpleMaskFormatter("(NN) N NNNN-NNNN");
        MaskTextWatcher masktelefone = new MaskTextWatcher(ctTelefoneUsuario, simpleMasktelefone);
        ctTelefoneUsuario.addTextChangedListener(masktelefone);

        //Mascara do campo CPF
        SimpleMaskFormatter simpleMaskcpf = new SimpleMaskFormatter("NNN.NNN.NNN-NN");
        MaskTextWatcher maskcpf = new MaskTextWatcher(ctCpfUsuario, simpleMaskcpf);
        ctCpfUsuario.addTextChangedListener(maskcpf);

        //Mascara do campo Data de Nascimento
        SimpleMaskFormatter simpleMasknascimento = new SimpleMaskFormatter("NN/NN/NNNN");
        MaskTextWatcher masknascimento = new MaskTextWatcher(ctNascimentoUsuario, simpleMasknascimento);
        ctNascimentoUsuario.addTextChangedListener(masknascimento);


    }

    //metodo para inicializar as variaveis
    public void inicializacao() {
        ctNomeUsuario = (TextView) findViewById(R.id.ctCadNomeUsuario);
        ctEmailUsuario = (TextView) findViewById(R.id.ctCadEmailUsuario);
        ctSenhaUsuario = (TextView) findViewById(R.id.ctCadSenhaUsuario);
        ctCpfUsuario = (TextView) findViewById(R.id.ctCadCpfUsuario);
        btCadastrarUsuario = (Button) findViewById(R.id.btCadastrarUsuario);
        ctTelefoneUsuario = (TextView) findViewById(R.id.ctCadTelefoneUsuario);
        ctNascimentoUsuario = (TextView) findViewById(R.id.ctCadNascimentoUsuario);
    }

    private class CadastrarUsuarioPost extends AsyncTask<Void, Void, String>{

        private AlertDialog alert;

        @Override
        protected void onPreExecute() {
            load = ProgressDialog.show(CadastrarUsuario.this,
                    "Por favor aguarde...", "Cadastrando usuário");
        }

        @Override
        protected void onPostExecute(String s) {
            Log.i("RESULTADO: ", s);
            load.dismiss();
            String titulo = "Sucesso";
            String texto = "Você se cadastrou com sucesso... Agora só precisa fazer login!";
            modalAlert(titulo, texto);
        }

        @Override
        protected String doInBackground(Void... voids) {
            Utils util = new Utils();
            String retornoCadastro = "";
            try {
                JSONObject json = new JSONObject();
                json.put("email", ctEmailUsuario.getText().toString());
                json.put("telefone", ctTelefoneUsuario.getText().toString());
                json.put("nome", ctNomeUsuario.getText().toString());
                json.put("cpf", ctCpfUsuario.getText().toString());
                json.put("senha", ctSenhaUsuario.getText().toString());
                json.put("dataDeNascimento", ctNascimentoUsuario.getText().toString());

                retornoCadastro = util.postTeste("http://192.168.137.1:8080/user/cadastrar", json);
                return retornoCadastro;
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void modalAlert(String titulo, String texto){
            // Criando gerador de Alerta
            final AlertDialog.Builder builder = new AlertDialog.Builder(CadastrarUsuario.this);
            builder.setTitle(titulo);
            builder.setMessage(texto);


            builder.setPositiveButton("Ok!", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    alert.dismiss();
                    Intent intent = new Intent(getBaseContext(), MainActivity.class);
                    startActivity(intent);
                }
            });
            alert = builder.create();
            alert.show();
        }
    }
    //metodo para validar o cpf
    public static boolean validaCPF(String cpf) {
        cpf = cpf.replace(".","").replace("-","").trim();
        if (cpf == null || cpf.length() != 11)
            return false;
        try {
            Long.parseLong(cpf);
        } catch (NumberFormatException e) {
            return false;
        }

        return calcDigVerif(cpf.substring(0, 9)).equals(cpf.substring(9, 11));
    }
    private static String calcDigVerif(String num) {
        Integer primDig, segDig;
        int soma = 0, peso = 10;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        if (soma % 11 == 0 | soma % 11 == 1)
            primDig = new Integer(0);
        else
            primDig = new Integer(11 - (soma % 11));

        soma = 0;
        peso = 11;
        for (int i = 0; i < num.length(); i++)
            soma += Integer.parseInt(num.substring(i, i + 1)) * peso--;

        soma += primDig.intValue() * 2;
        if (soma % 11 == 0 | soma % 11 == 1)
            segDig = new Integer(0);
        else
            segDig = new Integer(11 - (soma % 11));

        return primDig.toString() + segDig.toString();
    }

}
