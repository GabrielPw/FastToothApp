package com.gabrielxavier.fasttooth;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.gabrielxavier.fasttooth.interfaces.APICall;
import com.gabrielxavier.fasttooth.model.Usuario;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.time.Clock;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText etCPF, etConfirmaCPF;
    Button btnAvancar;
    TextView tvErrorMessage;

    String cpfAleatorio = "";
    GeradorCPF geradorCPF = new GeradorCPF();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etCPF = (EditText) findViewById(R.id.etCPF);
        etConfirmaCPF = (EditText) findViewById(R.id.etConfirmaCPF);
        btnAvancar = (Button) findViewById(R.id.btnAvancar);
        tvErrorMessage = (TextView) findViewById(R.id.tvErrorMessage);
    }

    public void avancar(View view){

        if (campoCpfCoincide()){

            retrofitConfigurarBuilder().enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {

                    if (response.code() == 200){
                        tvErrorMessage.setText("Seu CPF já possui um cadastro. Por favor faça login.");
                    } else if (response.code() == 400){

                        String error = null;
                        try {
                            error = response.errorBody().string();   // Pega mensagem de erro da requisição.
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                        System.out.println("\n\nError --> " + error);

                        if (error.matches("CPF INSERIDO É INVÁLIDO")){
                            etCPF.setError("CPF INSERIDO É INVÁLIDO");
                            tvErrorMessage.setText("*Cpf inserido é inválido."); // Exibe erro para o cliente (CPF inválido);
                        } else if (error.matches("CPF inserido não foi encontrado")){
                            Intent intent = new Intent(MainActivity.this, cadastro_activity.class);
                            String cpf = etCPF.getText().toString();

                            intent.putExtra("cpf", cpf);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {

                }
            });
        } else {

            tvErrorMessage.setText("Os campos informados não coincidem.");
        }

        // Minimiza o teclado quando usuário clicar no botão.
        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etConfirmaCPF.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(etCPF.getWindowToken(), 0);


    }

    Call<Usuario> retrofitConfigurarBuilder(){

        //Retrofit Builder.
        // Especificando a URL base e convertendo o Gson.

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        OkHttpClient client = new OkHttpClient();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-api-tcc.herokuapp.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        // Instacia da interface;
        APICall apiCall = retrofit.create(APICall.class);

        Call<Usuario> usuario = apiCall.getByCpf(etCPF.getText().toString());
        return usuario;
    }

    boolean campoCpfCoincide(){

        if (etCPF.getText().toString().matches(etConfirmaCPF.getText().toString())){

            return true;

        }

        return false;
    }

    public void generateCPF(View view){
        cpfAleatorio = geradorCPF.generateCPF();
    }

    public void possuiLogin(View view){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }
}