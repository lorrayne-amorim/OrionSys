// @auhtor Julia
package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.domain.Orcamento;

public class OrcamentoDAO {

    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public boolean inserir(Orcamento orcamento) {
        String sql = "INSERT INTO orcamento (titulo, id_categoria, valor_limite, data_inicio, data_fim) VALUES (?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, orcamento.getTitulo());
            stmt.setInt(2, orcamento.getIdCategoria()); // CORRIGIDO
            stmt.setDouble(3, orcamento.getValorLimite());
            stmt.setDate(4, Date.valueOf(orcamento.getDataInicio()));
            stmt.setDate(5, Date.valueOf(orcamento.getDataFim()));
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Orcamento orcamento) {
        String sql = "UPDATE orcamento SET titulo=?, id_categoria=?, valor_limite=?, data_inicio=?, data_fim=? WHERE id_orcamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, orcamento.getTitulo());
            stmt.setInt(2, orcamento.getIdCategoria()); // CORRIGIDO
            stmt.setDouble(3, orcamento.getValorLimite());
            stmt.setDate(4, Date.valueOf(orcamento.getDataInicio()));
            stmt.setDate(5, Date.valueOf(orcamento.getDataFim()));
            stmt.setInt(6, orcamento.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean remover(Orcamento orcamento) {
        String sql = "DELETE FROM orcamento WHERE id_orcamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, orcamento.getId());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public List<Orcamento> listar() {
        List<Orcamento> orcamentos = new ArrayList<>();
        String sql = "SELECT * FROM orcamento ORDER BY titulo";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                Orcamento orcamento = new Orcamento();
                orcamento.setId(resultado.getInt("id_orcamento"));
                orcamento.setTitulo(resultado.getString("titulo"));
                orcamento.setIdCategoria(resultado.getInt("id_categoria")); // CORRIGIDO
                orcamento.setValorLimite(resultado.getDouble("valor_limite"));
                orcamento.setDataInicio(resultado.getDate("data_inicio").toLocalDate());
                orcamento.setDataFim(resultado.getDate("data_fim").toLocalDate());
                orcamentos.add(orcamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return orcamentos;
    }

    public Orcamento buscar(int id) {
        String sql = "SELECT * FROM orcamento WHERE id_orcamento=?";
        Orcamento retorno = null;
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setInt(1, id);
            ResultSet resultado = stmt.executeQuery();
            if (resultado.next()) {
                retorno = new Orcamento();
                retorno.setId(resultado.getInt("id_orcamento"));
                retorno.setTitulo(resultado.getString("titulo"));
                retorno.setIdCategoria(resultado.getInt("id_categoria")); // CORRIGIDO
                retorno.setValorLimite(resultado.getDouble("valor_limite"));
                retorno.setDataInicio(resultado.getDate("data_inicio").toLocalDate());
                retorno.setDataFim(resultado.getDate("data_fim").toLocalDate());
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
}
