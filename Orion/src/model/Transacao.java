package model;

import javafx.beans.property.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Transacao {

    private final IntegerProperty id = new SimpleIntegerProperty();
    private final IntegerProperty idCategoria = new SimpleIntegerProperty();
    private final StringProperty nomeCategoria = new SimpleStringProperty();
    private final StringProperty nomeLocal = new SimpleStringProperty();
    private final IntegerProperty idLocal = new SimpleIntegerProperty();
    private final ObjectProperty<BigDecimal> valor = new SimpleObjectProperty<>();
    private final ObjectProperty<LocalDateTime> data = new SimpleObjectProperty<>();

    public Transacao() {}

    public Transacao(int id, int idCategoria, int idLocal, BigDecimal valor, LocalDateTime data) {
        this.id.set(id);
        this.idCategoria.set(idCategoria);
        this.idLocal.set(idLocal);
        this.valor.set(valor);
        this.data.set(data);
    }

    public Transacao(int idCategoria, int idLocal, BigDecimal valor, LocalDateTime data) {
        this.idCategoria.set(idCategoria);
        this.idLocal.set(idLocal);
        this.valor.set(valor);
        this.data.set(data);
    }

    
    // Getters e setters

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public int getIdCategoria() {
        return idCategoria.get();
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria.set(idCategoria);
    }

    public IntegerProperty idCategoriaProperty() {
        return idCategoria;
    }

    public int getIdLocal() {
        return idLocal.get();
    }

    public void setIdLocal(int idLocal) {
        this.idLocal.set(idLocal);
    }

    public IntegerProperty idLocalProperty() {
        return idLocal;
    }

    public BigDecimal getValor() {
        return valor.get();
    }

    public void setValor(BigDecimal valor) {
        this.valor.set(valor);
    }

    public ObjectProperty<BigDecimal> valorProperty() {
        return valor;
    }

    public LocalDateTime getData() {
        return data.get();
    }

    public void setData(LocalDateTime data) {
        this.data.set(data);
    }
    
    public String getNomeCategoria() {
    return nomeCategoria.get();
}

    public void setNomeCategoria(String nomeCategoria) {
        this.nomeCategoria.set(nomeCategoria);
    }

    public StringProperty nomeCategoriaProperty() {
        return nomeCategoria;
    }

    public String getNomeLocal() {
        return nomeLocal.get();
    }

    public void setNomeLocal(String nomeLocal) {
        this.nomeLocal.set(nomeLocal);
    }

    public StringProperty nomeLocalProperty() {
        return nomeLocal;
    }


    public ObjectProperty<LocalDateTime> dataProperty() {
        return data;
    }
}
