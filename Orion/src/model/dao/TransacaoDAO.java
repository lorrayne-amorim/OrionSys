//@author lorrayne

package model.dao;

import java.math.BigDecimal;
import model.domain.Transacao;
import model.database.Database;
import model.database.DatabaseFactory;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.ArrayList;
import java.util.List;

public class TransacaoDAO {

    private Connection connection;

    public TransacaoDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public boolean registrarTransacao(Transacao transacao) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement stmtCategoria = connection
                    .prepareStatement("SELECT tipo FROM categoria WHERE id_categoria = ?");
            stmtCategoria.setInt(1, transacao.getIdCategoria());
            ResultSet rsCategoria = stmtCategoria.executeQuery();
            if (!rsCategoria.next())
                throw new SQLException("Categoria não encontrada.");
            String tipoCategoria = rsCategoria.getString("tipo");

            PreparedStatement stmtLocal = connection.prepareStatement("SELECT * FROM local_transacao WHERE id = ?");
            stmtLocal.setInt(1, transacao.getIdLocal());
            ResultSet rsLocal = stmtLocal.executeQuery();
            if (!rsLocal.next())
                throw new SQLException("Local não encontrado.");

            PreparedStatement stmtInsert = connection.prepareStatement(
                    "INSERT INTO transacao (id_categoria, id_local, valor, data, forma_pagamento, id_usuario) VALUES (?, ?, ?, ?, ?, ?)");
            stmtInsert.setInt(1, transacao.getIdCategoria());
            stmtInsert.setInt(2, transacao.getIdLocal());
            stmtInsert.setBigDecimal(3, transacao.getValor());
            stmtInsert.setTimestamp(4, Timestamp.valueOf(transacao.getData()));
            stmtInsert.setString(5, transacao.getFormaPagamento());
            stmtInsert.setInt(6, transacao.getIdUsuario());
            stmtInsert.executeUpdate();

            BigDecimal valor = tipoCategoria.equalsIgnoreCase("despesa") ? transacao.getValor().negate()
                    : transacao.getValor();
            PreparedStatement stmtSaldo = connection
                    .prepareStatement("UPDATE usuario SET saldo = saldo + ? WHERE id_usuario = ?");
            stmtSaldo.setBigDecimal(1, valor);
            stmtSaldo.setInt(2, transacao.getIdUsuario());
            stmtSaldo.executeUpdate();

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

    public boolean atualizarTransacao(Transacao transacao) {
        try {
            connection.setAutoCommit(false);

            PreparedStatement stmtBusca = connection.prepareStatement(
                    "SELECT t.*, c.tipo FROM transacao t JOIN categoria c ON t.id_categoria = c.id_categoria WHERE t.id_transacao = ? AND t.id_usuario = ?");
            stmtBusca.setInt(1, transacao.getIdTransacao());
            stmtBusca.setInt(2, transacao.getIdUsuario());
            ResultSet rs = stmtBusca.executeQuery();
            if (!rs.next())
                throw new SQLException("Transação antiga não encontrada.");

            BigDecimal valorAntigo = rs.getBigDecimal("valor");
            String tipoAntigo = rs.getString("tipo");
            BigDecimal ajuste = "receita".equalsIgnoreCase(tipoAntigo) ? valorAntigo.negate() : valorAntigo;

            PreparedStatement stmtReverter = connection
                    .prepareStatement("UPDATE usuario SET saldo = saldo + ? WHERE id_usuario = ?");
            stmtReverter.setBigDecimal(1, ajuste);
            stmtReverter.setInt(2, transacao.getIdUsuario());
            stmtReverter.executeUpdate();

            PreparedStatement stmtUpdate = connection.prepareStatement(
                    "UPDATE transacao SET id_categoria = ?, id_local = ?, valor = ?, data = ?, forma_pagamento = ? WHERE id_transacao = ? AND id_usuario = ?");
            stmtUpdate.setInt(1, transacao.getIdCategoria());
            stmtUpdate.setInt(2, transacao.getIdLocal());
            stmtUpdate.setBigDecimal(3, transacao.getValor());
            stmtUpdate.setTimestamp(4, Timestamp.valueOf(transacao.getData()));
            stmtUpdate.setString(5, transacao.getFormaPagamento());
            stmtUpdate.setInt(6, transacao.getIdTransacao());
            stmtUpdate.setInt(7, transacao.getIdUsuario());
            stmtUpdate.executeUpdate();

            PreparedStatement stmtTipoNovo = connection
                    .prepareStatement("SELECT tipo FROM categoria WHERE id_categoria = ?");
            stmtTipoNovo.setInt(1, transacao.getIdCategoria());
            ResultSet rsNovo = stmtTipoNovo.executeQuery();
            if (!rsNovo.next())
                throw new SQLException("Categoria nova não encontrada.");

            BigDecimal novoValor = "despesa".equalsIgnoreCase(rsNovo.getString("tipo")) ? transacao.getValor().negate()
                    : transacao.getValor();

            PreparedStatement stmtAplicar = connection
                    .prepareStatement("UPDATE usuario SET saldo = saldo + ? WHERE id_usuario = ?");
            stmtAplicar.setBigDecimal(1, novoValor);
            stmtAplicar.setInt(2, transacao.getIdUsuario());
            stmtAplicar.executeUpdate();

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
        String sql = "SELECT t.id_transacao, t.id_usuario, t.id_categoria, c.nome AS nome_categoria, t.id_local, l.nome AS nome_local, t.valor, t.forma_pagamento, t.data FROM transacao t JOIN categoria c ON t.id_categoria = c.id_categoria JOIN local_transacao l ON t.id_local = l.id ORDER BY t.data DESC";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                Transacao transacao = new Transacao(
                        rs.getInt("id_transacao"),
                        rs.getInt("id_usuario"),
                        rs.getInt("id_categoria"),
                        rs.getString("nome_categoria"),
                        rs.getInt("id_local"),
                        rs.getString("nome_local"),
                        rs.getBigDecimal("valor"),
                        rs.getString("forma_pagamento"),
                        rs.getTimestamp("data").toLocalDateTime());
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
            String sqlSelect = "SELECT t.*, c.tipo FROM transacao t JOIN categoria c ON t.id_categoria = c.id_categoria WHERE t.id_transacao = ?";
            PreparedStatement stmtSelect = connection.prepareStatement(sqlSelect);
            stmtSelect.setInt(1, idTransacao);
            ResultSet rs = stmtSelect.executeQuery();
            if (!rs.next())
                throw new SQLException("Transação não encontrada.");

            int idUsuario = rs.getInt("id_usuario");
            BigDecimal valor = rs.getBigDecimal("valor");
            String tipo = rs.getString("tipo");
            BigDecimal ajuste = "receita".equalsIgnoreCase(tipo) ? valor.negate() : valor;

            PreparedStatement stmtUpdateSaldo = connection
                    .prepareStatement("UPDATE usuario SET saldo = saldo + ? WHERE id_usuario = ?");
            stmtUpdateSaldo.setBigDecimal(1, ajuste);
            stmtUpdateSaldo.setInt(2, idUsuario);
            stmtUpdateSaldo.executeUpdate();

            PreparedStatement stmtDelete = connection.prepareStatement("DELETE FROM transacao WHERE id_transacao = ?");
            stmtDelete.setInt(1, idTransacao);
            stmtDelete.executeUpdate();

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
        String sql = "SELECT o.valor_limite, " +
                "(SELECT COALESCE(SUM(t.valor), 0) " +
                " FROM transacao t " +
                " JOIN categoria c ON t.id_categoria = c.id_categoria " +
                " WHERE t.id_categoria = o.id_categoria " +
                " AND t.id_usuario = ? " +
                " AND c.tipo = 'despesa' " +
                " AND t.data BETWEEN o.data_inicio AND o.data_fim) AS total_utilizado " +
                "FROM orcamento o " +
                "WHERE o.id_categoria = ? " +
                "AND ? BETWEEN o.data_inicio AND o.data_fim";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, transacao.getIdUsuario());
            stmt.setInt(2, transacao.getIdCategoria());
            stmt.setTimestamp(3, Timestamp.valueOf(transacao.getData()));
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                BigDecimal limite = rs.getBigDecimal("valor_limite");
                BigDecimal utilizado = rs.getBigDecimal("total_utilizado");
                if (transacao.getValor().compareTo(BigDecimal.ZERO) > 0) {
                    return utilizado.add(transacao.getValor()).compareTo(limite) > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public BigDecimal calcularSaldoAvista(int idUsuario) {
        try {
            PreparedStatement stmtReceitas = connection.prepareStatement(
                    "SELECT COALESCE(SUM(valor), 0) AS total FROM transacao WHERE id_usuario = ? AND forma_pagamento IN ('avista', 'pix', 'transferencia') AND id_categoria IN (SELECT id_categoria FROM categoria WHERE tipo = 'receita')");
            stmtReceitas.setInt(1, idUsuario);
            ResultSet rsReceitas = stmtReceitas.executeQuery();
            rsReceitas.next();
            BigDecimal receitas = rsReceitas.getBigDecimal("total");

            PreparedStatement stmtDespesas = connection.prepareStatement(
                    "SELECT COALESCE(SUM(valor), 0) AS total FROM transacao WHERE id_usuario = ? AND forma_pagamento IN ('avista', 'pix', 'transferencia') AND id_categoria IN (SELECT id_categoria FROM categoria WHERE tipo = 'despesa')");
            stmtDespesas.setInt(1, idUsuario);
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
