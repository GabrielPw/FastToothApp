package com.gabrielxavier.fasttooth;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.gabrielxavier.fasttooth.interfaces.APICall;
import com.gabrielxavier.fasttooth.model.Usuario;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cadastro_activity extends AppCompatActivity {

    EditText etNome, etTelefone, etEmail, etDataNascimento, etSenha;
    Button btnCadastrar;
    CheckBox cbTermos;
    TextView tvErro;

    boolean camposPreenhidos = true;
    boolean termosPreenchido;

    String cpf = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        etNome = (EditText) findViewById(R.id.etNome);
        etTelefone = (EditText) findViewById(R.id.etTelefone);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etDataNascimento = (EditText) findViewById(R.id.etNascimento);
        etSenha = (EditText) findViewById(R.id.etSenha);

        btnCadastrar = (Button) findViewById(R.id.btnCadastrar);
        cbTermos = (CheckBox) findViewById(R.id.cbTermos);
        tvErro = (TextView) findViewById(R.id.tvMensagemErro);

        Bundle extras = getIntent().getExtras();
        cpf = extras.getString("cpf");

    }

    public void cadastrar(View view){

        camposPreenchidos();
        if (camposPreenhidos == true && termosPreenchido == true){

            retrofitConfigAndCreateUser().enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.code() == 200){
                        tvErro.setText("Usuário cadastrado com sucesso!");
                    } else if(response.code() == 400){

                        try {
                            String erro = response.errorBody().string();

                            if (erro.contains("cpf")){
                                tvErro.setText("CPF inválido:");
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Usuario> call, Throwable t) {

                    t.printStackTrace();
                }
            });
        } else {
            tvErro.setText("Por favor preencha todos os campos acima e verifique o campo de termos.");
        }
    }

    void camposPreenchidos(){

        String nome = etNome.getText().toString().replaceAll("\\s+","");
        String telefone = etTelefone.getText().toString().replaceAll("\\s+","");
        String email = etEmail.getText().toString().replaceAll("\\s+","");
        String nascimento = etDataNascimento.getText().toString().replaceAll("\\s+","");
        String senha = etSenha.getText().toString().replaceAll("\\s+","");

        if (nome.matches("") || telefone.matches("") || email.matches("") || nascimento.matches("") || senha.matches("")){
            camposPreenhidos = false;
        } else { camposPreenhidos = true;}

        if (cbTermos.isChecked()){
            termosPreenchido = true;
        } else {termosPreenchido = false;}
    }

    Call<Usuario> retrofitConfigAndCreateUser(){

        // Retirando possíveis espaços em brancos deixados pelo usuário nos campos preenchidos...
        String nome = etNome.getText().toString().replaceAll("\\s+$", "");;
        String telefone = etTelefone.getText().toString().replaceAll("\\s+$", "");
        String email = etEmail.getText().toString().replaceAll("\\s+$", "");
        String nascimento = etDataNascimento.getText().toString().replaceAll("\\s+$", "");
        String senha = etSenha.getText().toString().replaceAll("\\s+$", "");;

        Usuario novoUsuario = new Usuario();

        novoUsuario.setCpf(cpf);
        novoUsuario.setSenha(senha);

        //Retrofit Builder.
        // Especificando a URL base e convertendo o Gson.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-api-tcc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Instacia da interface;
        APICall apiCall = retrofit.create(APICall.class);

        Call<Usuario> usuario = apiCall.newUsuario(novoUsuario);
        return usuario;
    }
}