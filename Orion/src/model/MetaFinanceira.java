package model;

import javafx.beans.property.*;
import java.time.LocalDate;

public class MetaFinanceira {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final StringProperty objetivo = new SimpleStringProperty();
    private final StringProperty categoria = new SimpleStringProperty();
    private final DoubleProperty valorLimite = new SimpleDoubleProperty();
    private final ObjectProperty<LocalDate> dataInicio = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDate> dataFim = new SimpleObjectProperty<>();

    // Construtores
    public MetaFinanceira() {}

    public MetaFinanceira(int id, String objetivo, String categoria, double valorLimite, LocalDate dataInicio, LocalDate dataFim) {
        this.id.set(id);
        this.objetivo.set(objetivo);
        this.categoria.set(categoria);
        this.valorLimite.set(valorLimite);
        this.dataInicio.set(dataInicio);
        this.dataFim.set(dataFim);
    }

    public MetaFinanceira(String objetivo, String categoria, double valorLimite, LocalDate dataInicio, LocalDate dataFim) {
        this.objetivo.set(objetivo);
        this.categoria.set(categoria);
        this.valorLimite.set(valorLimite);
        this.dataInicio.set(dataInicio);
        this.dataFim.set(dataFim);
    }

    // Getters e Setters
    public int getId() { return id.get(); }
    public void setId(int id) { this.id.set(id); }
    public IntegerProperty idProperty() { return id; }

    public String getObjetivo() { return objetivo.get(); }
    public void setObjetivo(String objetivo) { this.objetivo.set(objetivo); }
    public StringProperty objetivoProperty() { return objetivo; }

    public String getCategoria() { return categoria.get(); }
    public void setCategoria(String categoria) { this.categoria.set(categoria); }
    public StringProperty categoriaProperty() { return categoria; }

    public double getValorLimite() { return valorLimite.get(); }
    public void setValorLimite(double valorLimite) { this.valorLimite.set(valorLimite); }
    public DoubleProperty valorLimiteProperty() { return valorLimite; }

    public LocalDate getDataInicio() { return dataInicio.get(); }
    public void setDataInicio(LocalDate dataInicio) { this.dataInicio.set(dataInicio); }
    public ObjectProperty<LocalDate> dataInicioProperty() { return dataInicio; }

    public LocalDate getDataFim() { return dataFim.get(); }
    public void setDataFim(LocalDate dataFim) { this.dataFim.set(dataFim); }
    public ObjectProperty<LocalDate> dataFimProperty() { return dataFim; }

    @Override
    public String toString() {
        return "MetaFinanceira{" +
                "id=" + id.get() +
                ", objetivo='" + objetivo.get() + '\'' +
                ", categoria='" + categoria.get() + '\'' +
                ", valorLimite=" + valorLimite.get() +
                ", dataInicio=" + dataInicio.get() +
                ", dataFim=" + dataFim.get() +
                '}';
    }
}
