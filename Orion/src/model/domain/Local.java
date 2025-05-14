package model.domain;

/**
 *
 * @author lorrayne
 */
public class Local {
    private int id_local;
    private String nome;
    
     public Local() {
        
    }
    public Local(int id_local, String nome) {
        this.id_local = id_local;
        this.nome = nome;
    }
    // Getters e Setters
    public int getIdLocal() {
         return id_local; 
    }

    public void setIdLocal(int id_local) { 
        this.id_local = id_local; 
    }
    
    public String getNome() {
         return nome; 
    }

    public void setNome(String nome) { 
        this.nome = nome; 
    }
    
    @Override
    public String toString() {
        return nome;
}

      
}
   



