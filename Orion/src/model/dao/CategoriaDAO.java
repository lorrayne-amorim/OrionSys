package model.dao;

// @author lorrayne

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.domain.Categoria;
import java.sql.Connection;

public class CategoriaDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Categoria categoria) {
    String sql = "INSERT INTO categoria (nome, tipo, descricao, prioridade, recorrente) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setString(4, categoria.getPrioridade());
            stmt.setBoolean(5, categoria.getRecorrente());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }


    public boolean alterar(Categoria categoria) {
        String sql = "UPDATE categoria SET nome=?, tipo=?, descricao=?, prioridade=?, recorrente=? WHERE id_categoria=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, categoria.getNome());
            stmt.setString(2, categoria.getTipo());
            stmt.setString(3, categoria.getDescricao());
            stmt.setString(4, categoria.getPrioridade());
            stmt.setBoolean(5, categoria.getRecorrente());
            stmt.setInt(6, categoria.getIdCategoria());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Categoria categoria) {
        // Primeiro verifica se a categoria está sendo usada em transações
        String sqlVerificaTransacoes = "SELECT COUNT(*) FROM transacao WHERE id_categoria=?";
        try {
            PreparedStatement stmtVerificaTransacoes = connection.prepareStatement(sqlVerificaTransacoes);
            stmtVerificaTransacoes.setInt(1, categoria.getIdCategoria());
            ResultSet rs = stmtVerificaTransacoes.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            // Verifica se a categoria está sendo usada em orçamentos
            String sqlVerificaOrcamentos = "SELECT COUNT(*) FROM orcamento WHERE id_categoria=?";
            PreparedStatement stmtVerificaOrcamentos = connection.prepareStatement(sqlVerificaOrcamentos);
            stmtVerificaOrcamentos.setInt(1, categoria.getIdCategoria());
            rs = stmtVerificaOrcamentos.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }

            // Exclui a categoria
            String sqlDelete = "DELETE FROM categoria WHERE id_categoria=?";
            PreparedStatement stmt = connection.prepareStatement(sqlDelete);
            stmt.setInt(1, categoria.getIdCategoria());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
}


    public List<Categoria> listar() {
       List<Categoria> categorias = new ArrayList<>();
       String sql = "SELECT * FROM categoria ORDER BY nome";
       
       try {
           PreparedStatement stmt = connection.prepareStatement(sql);
           ResultSet resultado = stmt.executeQuery();
           while (resultado.next()) {
               Categoria categoria = new Categoria();
               categoria.setIdCategoria(resultado.getInt("id_categoria"));
               categoria.setNome(resultado.getString("nome"));
               categoria.setTipo(resultado.getString("tipo"));
               categoria.setDescricao(resultado.getString("descricao"));

               String prioridade = resultado.getString("prioridade");
               categoria.setPrioridade(prioridade != null ? prioridade : "Média");

               boolean recorrente = resultado.getObject("recorrente") != null && resultado.getBoolean("recorrente");
               categoria.setRecorrente(recorrente);

               categorias.add(categoria);
           }
       } catch (SQLException ex) {
           Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
       }
       return categorias;
   }


    public Categoria buscar(int id) {
        String sql = "SELECT * FROM categoria WHERE id_categoria=?";
        Categoria categorias = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                categorias = new Categoria();
                categorias.setIdCategoria(resultado.getInt("id_categoria"));
                categorias.setNome(resultado.getString("nome"));
                categorias.setTipo(resultado.getString("tipo"));
                categorias.setDescricao(resultado.getString("descricao"));
                categorias.setPrioridade(resultado.getString("prioridade"));
                categorias.setRecorrente(resultado.getBoolean("recorrente"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return categorias;
    }

}
