// @author lorrayne

package model.domain;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {

    private int idTransacao;
    private int idUsuario;
    private int idCategoria;
    private String nomeCategoria;
    private int idLocal;
    private String nomeLocal;
    private BigDecimal valor;
    private String formaPagamento;
    private LocalDateTime data;

    public Transacao() {}

    public Transacao(int idTransacao, int idUsuario, int idCategoria, String nomeCategoria, int idLocal, String nomeLocal, BigDecimal valor,  String formaPagamento, LocalDateTime data) {
        this.idTransacao = idTransacao;
        this.idUsuario = idUsuario;
        this.idCategoria = idCategoria;
        this.nomeCategoria = nomeCategoria;
        this.idLocal = idLocal;
        this.nomeLocal = nomeLocal;
        this.valor = valor;
        this.formaPagamento = formaPagamento;
        this.data = data;
    }


    // Getters e Setters
   
    public int getIdTransacao() {
        return idTransacao;
    }
    public void setIdTransacao(int idTransacao) {
        this.idTransacao = idTransacao;
    }
    
    public int getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
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

    public String getFormaPagamento(){
        return formaPagamento;
    }
    public void setFormaPagamento(String formaPagamento){
        this.formaPagamento = formaPagamento;
    }
}
