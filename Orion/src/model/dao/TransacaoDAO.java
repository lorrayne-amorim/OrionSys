package model.dao;

import java.math.BigDecimal;
import model.domain.Transacao;
import model.database.Database;
import model.database.DatabaseFactory;

import java.sql.*;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private final Connection connection;

    public TransacaoDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public boolean registrarTransacao(Transacao transacao) {
        try {
            connection.setAutoCommit(false);

            // 1. Validar categoria
            PreparedStatement stmtCategoria = connection.prepareStatement(
                "SELECT * FROM categoria WHERE id_categoria = ?"
            );
            stmtCategoria.setInt(1, transacao.getIdCategoria());
            ResultSet rsCategoria = stmtCategoria.executeQuery();
            if (!rsCategoria.next()) throw new SQLException("Categoria não encontrada.");

            // 2. Validar local
            PreparedStatement stmtLocal = connection.prepareStatement(
                "SELECT * FROM local_transacao WHERE id = ?"
            );
            stmtLocal.setInt(1, transacao.getIdLocal());
            ResultSet rsLocal = stmtLocal.executeQuery();
            if (!rsLocal.next()) throw new SQLException("Local não encontrado.");

            // 3. Verificar orçamento por período
            PreparedStatement stmtOrcamento = connection.prepareStatement(
                "SELECT valor_limite, " +
                "  (SELECT COALESCE(SUM(valor), 0) FROM transacao WHERE id_categoria = o.id_categoria AND data BETWEEN o.data_inicio AND o.data_fim) AS total_utilizado " +
                "FROM orcamento o " +
                "WHERE id_categoria = ? AND ? BETWEEN o.data_inicio AND o.data_fim"
            );
            stmtOrcamento.setInt(1, transacao.getIdCategoria());
            stmtOrcamento.setTimestamp(2, Timestamp.valueOf(transacao.getData()));
            ResultSet rsOrcamento = stmtOrcamento.executeQuery();

            if (!rsOrcamento.next()) {
                throw new SQLException("Nenhum orçamento encontrado para esse período e categoria.");
            }

            BigDecimal limite = rsOrcamento.getBigDecimal("valor_limite");
            BigDecimal utilizado = rsOrcamento.getBigDecimal("total_utilizado");

            if (utilizado.add(transacao.getValor()).compareTo(limite) > 0) {
                throw new SQLException("Transação ultrapassa o limite definido no orçamento.");
            }

            // 4. Inserir transação
            PreparedStatement stmtInsert = connection.prepareStatement(
                "INSERT INTO transacao (id_categoria, id_local, valor, data) VALUES (?, ?, ?, ?)"
            );
            stmtInsert.setInt(1, transacao.getIdCategoria());
            stmtInsert.setInt(2, transacao.getIdLocal());
            stmtInsert.setBigDecimal(3, transacao.getValor());
            stmtInsert.setTimestamp(4, Timestamp.valueOf(transacao.getData()));
            stmtInsert.executeUpdate();

            connection.commit();
            return true;

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public List<Transacao> listarTodos() {
        List<Transacao> transacoes = new ArrayList<>();
        String sql = "SELECT t.id, t.id_categoria, t.id_local, t.valor, t.data, " +
                     "       c.nome AS nome_categoria, " +
                     "       l.nome AS nome_local " +
                     "FROM transacao t " +
                     "JOIN categoria c ON t.id_categoria = c.id_categoria " +
                     "JOIN local_transacao l ON t.id_local = l.id " +
                     "ORDER BY t.data DESC";

        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                Transacao transacao = new Transacao(
                    rs.getInt("id"),
                    rs.getInt("id_categoria"),
                    rs.getInt("id_local"),
                    rs.getBigDecimal("valor"),
                    rs.getTimestamp("data").toLocalDateTime()
                );
                transacao.setNomeCategoria(rs.getString("nome_categoria"));
                transacao.setNomeLocal(rs.getString("nome_local"));
                transacoes.add(transacao);
            }
        } catch (SQLException ex) {
            Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
        }

        return transacoes;
    }

    public boolean cancelarTransacao(int idTransacao) {
        try {
            connection.setAutoCommit(false);

            String sqlSelect = "SELECT * FROM transacao WHERE id = ?";
            PreparedStatement stmtSelect = connection.prepareStatement(sqlSelect);
            stmtSelect.setInt(1, idTransacao);
            ResultSet rs = stmtSelect.executeQuery();

            if (!rs.next()) throw new SQLException("Transação não encontrada.");

            int idCategoria = rs.getInt("id_categoria");
            BigDecimal valor = rs.getBigDecimal("valor");
            LocalDate data = rs.getTimestamp("data").toLocalDateTime().toLocalDate();

            String sqlDelete = "DELETE FROM transacao WHERE id = ?";
            PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete);
            stmtDelete.setInt(1, idTransacao);
            stmtDelete.executeUpdate();

            // OBS: Aqui não estamos revertendo orçamento porque a lógica depende de orçamento por período. Ajuste se necessário.

            connection.commit();
            return true;

        } catch (SQLException ex) {
            try {
                connection.rollback();
            } catch (SQLException rollbackEx) {
                Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, rollbackEx);
            }
            Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        } finally {
            try {
                connection.setAutoCommit(true);
            } catch (SQLException e) {
                Logger.getLogger(TransacaoDAO.class.getName()).log(Level.SEVERE, null, e);
            }
        }
    }

    public boolean ultrapassaOrcamento(Transacao transacao) {
        String sql = "SELECT valor_limite, (SELECT COALESCE(SUM(valor), 0) FROM transacao " +
                     "WHERE id_categoria = o.id_categoria AND data BETWEEN o.data_inicio AND o.data_fim) AS total_utilizado " +
                     "FROM orcamento o WHERE id_categoria = ? AND ? BETWEEN o.data_inicio AND o.data_fim";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transacao.getIdCategoria());
            stmt.setTimestamp(2, Timestamp.valueOf(transacao.getData()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal limite = rs.getBigDecimal("valor_limite");
                BigDecimal utilizado = rs.getBigDecimal("total_utilizado");
                return utilizado.add(transacao.getValor()).compareTo(limite) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public BigDecimal calcularSaldoAvista() {
        try {
            PreparedStatement stmtReceitas = connection.prepareStatement(
                "SELECT COALESCE(SUM(valor), 0) AS total FROM transacao " +
                "WHERE forma_pagamento = 'avista' AND id_categoria IN (" +
                "SELECT id_categoria FROM categoria WHERE tipo = 'receita')"
            );
            ResultSet rsReceitas = stmtReceitas.executeQuery();
            rsReceitas.next();
            BigDecimal receitas = rsReceitas.getBigDecimal("total");
    
            PreparedStatement stmtDespesas = connection.prepareStatement(
                "SELECT COALESCE(SUM(valor), 0) AS total FROM transacao " +
                "WHERE forma_pagamento = 'avista' AND id_categoria IN (" +
                "SELECT id_categoria FROM categoria WHERE tipo = 'despesa')"
            );
            ResultSet rsDespesas = stmtDespesas.executeQuery();
            rsDespesas.next();
            BigDecimal despesas = rsDespesas.getBigDecimal("total");
    
            return receitas.subtract(despesas);
    
        } catch (SQLException e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }
}
