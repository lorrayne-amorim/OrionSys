package model.domain;

// @author lorrayne

import java.io.Serializable;
import java.sql.Date;

public class Usuario implements Serializable {

    private int id_usuario;
    private String nome;
    private String email;
    private Date data_nascimento;
    private Double saldo; 
    private String senha;
 

    public Usuario() {
        
    }

    public Usuario(int id_usuario, String nome, String email, Date data_nascimento, Double saldo, char[] senha) {
        this.id_usuario = id_usuario;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.saldo = saldo;
        this.senha = senha;
    }

    // Getters e Setters
    public int getIdUsuario() {
         return id_usuario; 
    }

    public void setIdUsuario(int id_usuario) { 
        this.id_usuario = id_usuario; 
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }
    
    public void setEmail(String email) {
        this.email = email;
    }

     public String getSenha(){
        return senha;
    }

    public void setSenha(String senha){
        this.senha = senha;
    }
    
    public Date getDataNascimento() {
        return data_nascimento;
    }
    
    public void setDataNascimento(Date data_nascimento) {
        this.data_nascimento = data_nascimento;
    }

    public Double getSaldo() {
        return saldo;
    }
    
    public void setSaldo(Double saldo) {
        this.saldo = saldo;
    }

}
