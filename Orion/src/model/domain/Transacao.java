package model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {

    private int id;
    private int idCategoria;
    private String nomeCategoria;
    private int idLocal;
    private String nomeLocal;
    private BigDecimal valor;
    private LocalDateTime data;

    public Transacao() {}

    public Transacao(int id, int idCategoria, int idLocal, BigDecimal valor, LocalDateTime data) {
        this.id = id;
        this.idCategoria = idCategoria;
        this.idLocal = idLocal;
        this.valor = valor;
        this.data = data;
    }

    public Transacao(int idCategoria, int idLocal, BigDecimal valor, LocalDateTime data) {
        this.idCategoria = idCategoria;
        this.idLocal = idLocal;
        this.valor = valor;
        this.data = data;
    }

    // Getters e Setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public String getNomeCategoria() {
        return nomeCategoria;
    }

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria = nomeCategoria;
    }

    public int getIdLocal() {
        return idLocal;
    }

    public void setIdLocal(int idLocal) {
        this.idLocal = idLocal;
    }

    public String getNomeLocal() {
        return nomeLocal;
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal = nomeLocal;
    }

    public BigDecimal getValor() {
        return valor;
    }

    public void setValor(BigDecimal valor) {
        this.valor = valor;
    }

    public LocalDateTime getData() {
        return data;
    }

    public void setData(LocalDateTime data) {
        this.data = data;
    }
}
