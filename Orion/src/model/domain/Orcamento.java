package model.domain;

import java.time.LocalDate;

public class Orcamento {

    private int id_orcamento;
    private String titulo;
    private int idCategoria; // Alterado de String para int
    private double valorLimite;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Construtores
    public Orcamento() {}

    public Orcamento(int id_orcamento, String titulo, int idCategoria, double valorLimite, LocalDate dataInicio, LocalDate dataFim) {
        this.id_orcamento = id_orcamento;
        this.titulo = titulo;
        this.idCategoria = idCategoria;
        this.valorLimite = valorLimite;
        this.dataInicio = dataInicio;
        this.dataFim = dataFim;
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
}
