package model.domain;

// @author lorrayne

import java.io.Serializable;

public class Categoria implements Serializable {

    private int id_categoria;
    private String nome;
    private String tipo;
    private String descricao;
    private String prioridade; // alta, média, baixa
    private Boolean recorrente; // true ou false
 

    public Categoria() {
        this.recorrente = false;
        this.prioridade = "Média";
    }

    public Categoria(int id_categoria, String nome, String tipo, String descricao, String prioridade, boolean recorrente) {
        this.id_categoria = id_categoria;
        this.nome = nome;
        this.tipo = tipo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.recorrente = recorrente;
    }

    // Getters e Setters
    public int getIdCategoria() {
         return id_categoria; 
    }

    public void setIdCategoria(int id_categoria) { 
        this.id_categoria = id_categoria; 
    }

    public String getNome() {
        return nome;
    }
    
    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTipo() {
        return tipo;
    }
    
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getDescricao() {
        return descricao;
    }
    
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getPrioridade() {
        return prioridade;
    }
    
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }
   
    public boolean getRecorrente(){
        return recorrente != null && recorrente;
    }

    public void setRecorrente(Boolean recorrente){
        this.recorrente = recorrente;
    }
   
    @Override
    public String toString() {
        return nome;
}

}
