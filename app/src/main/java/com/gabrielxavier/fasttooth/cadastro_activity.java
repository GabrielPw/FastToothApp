package com.gabrielxavier.fasttooth;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.gabrielxavier.fasttooth.interfaces.APICall;
import com.gabrielxavier.fasttooth.model.Cliente;
import com.gabrielxavier.fasttooth.model.Usuario;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class cadastro_activity extends AppCompatActivity {

    final Calendar myCalendar= Calendar.getInstance();
    EditText etNome, etTelefone, etEmail, etDataNascimento, etSenha;
    Button btnCadastrar;
    CheckBox cbTermos;
    TextView tvErro;

    boolean camposPreenhidos = true;
    boolean termosPreenchido;

    String cpf = "";

    private APICall apiCall;

    private String nome, telefone, email, nascimento, senha;
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

        // (abaixo) Evento de clique do campo de texto para a data de nascimento. (vai abrir um pop up para selecionar a data).
        DatePickerDialog.OnDateSetListener date =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };
        etDataNascimento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(cadastro_activity.this,date,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void cadastrar(View view){

        camposPreenchidos();
        if (camposPreenhidos == true && termosPreenchido == true){

            configurarRetrofit();
            Usuario novoUsuario = new Usuario();
            novoUsuario.setCpf(cpf);
            novoUsuario.setSenha(senha);

            Call<Usuario> usuario = apiCall.newUsuario(novoUsuario);
            usuario.enqueue(new Callback<Usuario>() {
                @Override
                public void onResponse(Call<Usuario> call, Response<Usuario> response) {
                    if (response.code() == 200){
                        tvErro.setText("Usuário cadastrado com sucesso!");

                        // Após feito o cadastro do usuário (feito acima com status 200), será criado um cliente para associar a esse usuário.
                        Cliente novoCliente = new Cliente();

                        // Adaptando o formato da data para o que é requerido pelo JSON da API.
                        String data = etDataNascimento.getText().toString();

                        List totalChars = new ArrayList();

                        for(int i = 0; i < data.length(); i++){
                            totalChars.add(data.charAt(i));
                        }

                        System.out.println("\n\n");
                        for (int i = 0; i < totalChars.size(); i++){
                            System.out.print(totalChars.get(i));
                        }
                        System.out.println("\n\n");

                        String dia = totalChars.get(0).toString().concat(totalChars.get(1).toString());
                        String mes = totalChars.get(3).toString().concat(totalChars.get(4).toString());
                        String ano = totalChars.get(6).toString().concat(totalChars.get(7).toString()).concat(totalChars.get(8).toString()).concat(totalChars.get(9).toString());

                        String dataFinal = ano.concat("/"  + mes).concat("/" + dia);
                        dataFinal.replace("/", "-");

                        // -------------------------------------------------------------
                        novoCliente.setNome(nome);
                        novoCliente.setEmail(email);
                        novoCliente.setTelefone(telefone);
                        novoCliente.setDtNascimento(dataFinal.replaceAll("/", "-"));
                        novoCliente.setSenha(senha);
                        novoCliente.setUsuario(novoUsuario);


                        Call<Cliente> cliente = apiCall.newCliente(novoCliente);
                        cliente.enqueue(new Callback<Cliente>() {
                            @Override
                            public void onResponse(Call<Cliente> call, Response<Cliente> response) {
                                if (response.code() == 200) {
                                    System.out.println("Cliente "+ novoCliente.getNome() + " foi registrado com sucesso!");
                                }
                            }

                            @Override
                            public void onFailure(Call<Cliente> call, Throwable t) {

                            }
                        });
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

        nome = etNome.getText().toString().replaceAll("\\s+","");
        telefone = etTelefone.getText().toString().replaceAll("\\s+","");
        email = etEmail.getText().toString().replaceAll("\\s+","");
        nascimento = etDataNascimento.getText().toString().replaceAll("\\s+","");
        senha = etSenha.getText().toString().replaceAll("\\s+","");

        if (nome.matches("") || telefone.matches("") || email.matches("") || nascimento.matches("") || senha.matches("")){
            camposPreenhidos = false;
        } else { camposPreenhidos = true;}

        if (cbTermos.isChecked()){
            termosPreenchido = true;
        } else {termosPreenchido = false;}
    }

    void configurarRetrofit(){

        //Retrofit Builder.
        // Especificando a URL base e convertendo o Gson.

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://clinica-api-tcc.herokuapp.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Instacia da interface;
        apiCall = retrofit.create(APICall.class);
    }

    // Atualiza o campo de data de nascimento.
    private void updateLabel(){
        String myFormat="dd/MM/yyyy";
        SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat);
        etDataNascimento.setText(dateFormat.format(myCalendar.getTime()));

    }
}