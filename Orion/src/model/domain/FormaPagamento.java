package model.domain;
// @auhtor Julia
public class FormaPagamento {
    private int id;
    private String nome;

    public FormaPagamento(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() { return id; }
    public String getNome() { return nome; }

    @Override
    public String toString() {
        return nome;
    }
}
