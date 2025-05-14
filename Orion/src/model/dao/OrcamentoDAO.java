package model.dao;

import java.sql.*;
import java.time.LocalDate;
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
        String sql = "INSERT INTO orcamento (titulo, id_categoria, valor_limite, data_inicio, data_fim, id_tipo, id_status, id_forma_pagamento) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, orcamento.getTitulo());
            stmt.setInt(2, orcamento.getIdCategoria());
            stmt.setDouble(3, orcamento.getValorLimite());
            stmt.setDate(4, Date.valueOf(orcamento.getDataInicio()));
            stmt.setDate(5, Date.valueOf(orcamento.getDataFim()));
            stmt.setInt(6, orcamento.getIdTipoOrcamento());
            stmt.setInt(7, orcamento.getIdStatusOrcamento());
            stmt.setInt(8, orcamento.getIdFormaPagamento());
            stmt.execute();
            return true;
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean alterar(Orcamento orcamento) {
        String sql = "UPDATE orcamento SET titulo=?, id_categoria=?, valor_limite=?, data_inicio=?, data_fim=?, id_tipo=?, id_status=?, id_forma_pagamento=? WHERE id_orcamento=?";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            stmt.setString(1, orcamento.getTitulo());
            stmt.setInt(2, orcamento.getIdCategoria());
            stmt.setDouble(3, orcamento.getValorLimite());
            stmt.setDate(4, Date.valueOf(orcamento.getDataInicio()));
            stmt.setDate(5, Date.valueOf(orcamento.getDataFim()));
            stmt.setInt(6, orcamento.getIdTipoOrcamento());
            stmt.setInt(7, orcamento.getIdStatusOrcamento());
            stmt.setInt(8, orcamento.getIdFormaPagamento());
            stmt.setInt(9, orcamento.getId());
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
                orcamento.setIdCategoria(resultado.getInt("id_categoria"));
                orcamento.setValorLimite(resultado.getDouble("valor_limite"));
                orcamento.setDataInicio(resultado.getDate("data_inicio").toLocalDate());
                orcamento.setDataFim(resultado.getDate("data_fim").toLocalDate());
                orcamento.setIdTipoOrcamento(resultado.getInt("id_tipo"));
                orcamento.setIdStatusOrcamento(resultado.getInt("id_status"));
                orcamento.setIdFormaPagamento(resultado.getInt("id_forma_pagamento"));
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
                retorno.setIdCategoria(resultado.getInt("id_categoria"));
                retorno.setValorLimite(resultado.getDouble("valor_limite"));
                retorno.setDataInicio(resultado.getDate("data_inicio").toLocalDate());
                retorno.setDataFim(resultado.getDate("data_fim").toLocalDate());
                retorno.setIdTipoOrcamento(resultado.getInt("id_tipo"));
                retorno.setIdStatusOrcamento(resultado.getInt("id_status"));
                retorno.setIdFormaPagamento(resultado.getInt("id_forma_pagamento"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(OrcamentoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return retorno;
    }
    
    public boolean existeConflitoDePeriodo(int idCategoria, LocalDate inicio, LocalDate fim, int idOrcamentoAtual) {
    String sql = "SELECT 1 FROM orcamento WHERE id_categoria = ? AND id_orcamento <> ? AND data_inicio <= ? AND data_fim >= ?";
    try {
        PreparedStatement stmt = connection.prepareStatement(sql);
        stmt.setInt(1, idCategoria);
        stmt.setInt(2, idOrcamentoAtual);
        stmt.setDate(3, Date.valueOf(fim));
        stmt.setDate(4, Date.valueOf(inicio));
        ResultSet rs = stmt.executeQuery();
        return rs.next();
    } catch (SQLException e) {
        e.printStackTrace();
        return false;
    }
}

}