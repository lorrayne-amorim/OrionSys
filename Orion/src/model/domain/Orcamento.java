package model.domain;

import java.time.LocalDate;

public class Orcamento {

    private int id_orcamento;
    private String titulo;
    private String categoria;
    private double valorLimite;
    private LocalDate dataInicio;
    private LocalDate dataFim;

    // Construtores
    public Orcamento() {}

    public Orcamento(int id_orcamento, String titulo, String categoria, double valorLimite, LocalDate dataInicio, LocalDate dataFim) {
        this.id_orcamento = id_orcamento;
        this.titulo = titulo;
        this.categoria = categoria;
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

    public String getCategoria() { 
        return categoria; 
    }

    public void setCategoria(String categoria) { 
        this.categoria = categoria; 
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
