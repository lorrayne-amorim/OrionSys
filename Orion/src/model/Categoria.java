package model;

import javafx.beans.property.*;

public class Categoria {
    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty nome = new SimpleStringProperty();
    private final StringProperty tipo = new SimpleStringProperty();
    private final StringProperty descricao = new SimpleStringProperty();
    private final BooleanProperty ativo = new SimpleBooleanProperty();  

    public Categoria() {}

    public Categoria(int id, String nome, String tipo, String descricao, boolean ativo) {
        this.id.set(id);
        this.nome.set(nome);
        this.tipo.set(tipo);
        this.descricao.set(descricao);
        this.ativo.set(ativo);
    }

    public Categoria(String nome, String tipo, String descricao, boolean ativo) {
        this.nome.set(nome);
        this.tipo.set(tipo);
        this.descricao.set(descricao);
        this.ativo.set(ativo);
    }

    // Getters e Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getNome() { return nome.get(); }
    public void setNome(String nome) { this.nome.set(nome); }
    public StringProperty nomeProperty() { return nome; }

    public String getTipo() { return tipo.get(); }
    public void setTipo(String tipo) { this.tipo.set(tipo); }
    public StringProperty tipoProperty() { return tipo; }

    public String getDescricao() { return descricao.get(); }
    public void setDescricao(String descricao) { this.descricao.set(descricao); }
    public StringProperty descricaoProperty() { return descricao; }

    public boolean isAtivo() { return ativo.get(); }
    public void setAtivo(boolean ativo) { this.ativo.set(ativo); }
    public BooleanProperty ativoProperty() { return ativo; }
}
