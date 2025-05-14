package model.domain;

/**
 *
 * @author lorrayne
 */
public class FormaPagamento {
    private int id_formapagamento;
    private String descricao;
    
     public FormaPagamento() {
        
    }
    public FormaPagamento(int id_formaPagamento, String descricao) {
        this.id_formapagamento = id_formaPagamento;
        this.descricao = descricao;
    }
    // Getters e Setters
    public int getIdFormaPagamento() {
         return id_formapagamento; 
    }

    public void setIdFormaPagamento(int id_formapagamento) { 
        this.id_formapagamento = id_formapagamento; 
    }
    
    public String getDescricao() {
         return descricao; 
    }

    public void setDescricao(String descricao) { 
        this.descricao = descricao; 
    }
    
    @Override
    public String toString() {
        return descricao;
}

      
}
   



