package com.gabrielxavier.fasttooth.interfaces;

import com.gabrielxavier.fasttooth.model.Cliente;
import com.gabrielxavier.fasttooth.model.Usuario;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface APICall {

    // https://clinica-api-tcc.herokuapp.com/             usuarios/getAll


    // Usuario.
    @GET("usuarios/getAll")
    Call<List<Usuario>> getUsuario();

    @GET("usuarios/getByCpf/{cpf}")
    Call<Usuario> getByCpf(@Path("cpf") String cpf);

    @POST("usuarios/newUsuario")
    Call<Usuario> newUsuario(@Body Usuario usuario);

    // Cliente

    @POST("clientes/cadastrar")
    Call<Cliente> newCliente(@Body Cliente cliente);
}
