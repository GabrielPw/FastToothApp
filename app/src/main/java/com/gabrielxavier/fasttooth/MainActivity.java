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

import java.time.Clock;
import java.util.List;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText etCPF, etConfirmaCPF;
    Button btnAvancar;
    TextView tvErrorMessage;

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
                        tvErrorMessage.setText("Erro. certifique-se se o CPF digitado é válido.");
                    } else{
                        Intent intent = new Intent(MainActivity.this, cadastro_activity.class);
                        String cpf = etCPF.getText().toString();

                        intent.putExtra("cpf", cpf);
                        startActivity(intent);
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-api-tcc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
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
}