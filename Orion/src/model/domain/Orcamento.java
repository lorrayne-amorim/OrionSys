package model.domain;

import java.time.LocalDate;
// julia
public class Orcamento {

    private int id_orcamento;
    private String titulo;
    private int idCategoria;
    private double valorLimite;
    private LocalDate dataInicio;
    private LocalDate dataFim;
    private int idTipoOrcamento;
    private int idStatusOrcamento;
    private int idFormaPagamento;

    public Orcamento() {}

    public Orcamento(int id_orcamento, String titulo, int idCategoria, double valorLimite,
                     LocalDate dataInicio, LocalDate dataFim, int idTipoOrcamento,
                     int idStatusOrcamento, int idFormaPagamento) {
        this.id_orcamento = id_orcamento;
        this.titulo = titulo;
        this.idCategoria = idCategoria;
        this.valorLimite = valorLimite;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
        this.idTipoOrcamento = idTipoOrcamento;
        this.idStatusOrcamento = idStatusOrcamento;
        this.idFormaPagamento = idFormaPagamento;
    }

    // Getters e Setters
    public int getId() {
        return id_orcamento;
    }

    public void setId(int id_orcamento) {
        this.id_orcamento = id_orcamento;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }

    public double getValorLimite() {
        return valorLimite;
    }

    public void setValorLimite(double valorLimite) {
        this.valorLimite = valorLimite;
    }

    public LocalDate getDataInicio() {
        return dataInicio;
    }

    public void setDataInicio(LocalDate dataInicio) {
        this.dataInicio = dataInicio;
    }

    public LocalDate getDataFim() {
        return dataFim;
    }

    public void setDataFim(LocalDate dataFim) {
        this.dataFim = dataFim;
    }

    public int getIdTipoOrcamento() {
        return idTipoOrcamento;
    }

    public void setIdTipoOrcamento(int idTipoOrcamento) {
        this.idTipoOrcamento = idTipoOrcamento;
    }

    public int getIdStatusOrcamento() {
        return idStatusOrcamento;
    }

    public void setIdStatusOrcamento(int idStatusOrcamento) {
        this.idStatusOrcamento = idStatusOrcamento;
    }

    public int getIdFormaPagamento() {
        return idFormaPagamento;
    }

    public void setIdFormaPagamento(int idFormaPagamento) {
        this.idFormaPagamento = idFormaPagamento;
    }
}
