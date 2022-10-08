package com.gabrielxavier.fasttooth;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.gabrielxavier.fasttooth.interfaces.APICall;
import com.gabrielxavier.fasttooth.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class LoginActivity extends AppCompatActivity {

    APICall apiCall;
    private String cpf = "";
    private String senha = "";

    boolean camposPreenhidos = false;

    EditText etLoginCPF;
    EditText etLoginSenha;

    TextView tvErrorLoging;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginCPF = (EditText) findViewById(R.id.etLoginCPF);
        etLoginSenha = (EditText) findViewById(R.id.etLoginSenha);
        tvErrorLoging = (TextView) findViewById(R.id.tvErrorLogin);
    }

    public void avancar(View view){

        configurarRetrofit();
        camposPreenchidos();

        Usuario usuario = new Usuario();

        usuario.setCpf(cpf);
        usuario.setSenha(senha);

        System.out.println("\n\n\nsenha -->" + senha + "-" + "\ncpf --> " + cpf + "-\n\n\n");


        if (camposPreenhidos){
            Call<Usuario> loginUsuario = apiCall.login(usuario);
            loginUsuario.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                    System.out.println("Mensagem da requisição: " + response.message());
                    if (response.code() == 200){

                        System.out.println("STATUS 200. OK");
                        Intent intent = new Intent(LoginActivity.this, InicioActivity.class);

                        startActivity(intent);
                    } else if (response.code() == 404){
                        System.out.println("Usuário não encontrado.");
                        tvErrorLoging.setText("*CPF ou senha inválidos.");
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {

                    System.out.println("ERRO\n\n");
                    t.printStackTrace();
                }
            });
        } else {

            tvErrorLoging.setText("*Preencha os campos acima para proceder.");
        }
        // Minimiza o teclado quando usuário clicar no botão.
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etLoginSenha.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etLoginCPF.getWindowToken(), 0);

    }

    public void telaCadastro(View view){


    }

    public void forgetPassword(View view){

    }

    void configurarRetrofit(){

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-api-tcc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);

    }

    void camposPreenchidos(){

        cpf = etLoginCPF.getText().toString().replaceAll("\\s+","");
        senha = etLoginSenha.getText().toString().replaceAll("\\s+","");

        // Verifica se o usuário deixou os campos totalmente em branco.
        if (cpf.matches("") || senha.matches("")){
            camposPreenhidos = false;
        } else { camposPreenhidos = true;}

    }
}