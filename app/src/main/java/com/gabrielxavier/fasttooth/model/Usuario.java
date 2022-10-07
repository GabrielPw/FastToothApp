package com.gabrielxavier.fasttooth.model;
import com.google.gson.annotations.SerializedName;

public class Usuario {

    private String cpf;
    private String senha;  // caso queira atribuir um nome diferente -- > @SerializedName("{nome do atributo no Json}")


    // Getters
    public String getCpf() {
        return cpf;
    }

    public String getSenha() {
        return senha;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }
}
