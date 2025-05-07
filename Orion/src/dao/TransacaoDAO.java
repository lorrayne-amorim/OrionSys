package dao;

import java.math.BigDecimal;
import model.Transacao;
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

            // 1. SELECT na categoria
            PreparedStatement stmtCategoria = connection.prepareStatement(
                    "SELECT * FROM categoria WHERE id = ?"
            );
            stmtCategoria.setInt(1, transacao.getIdCategoria());
            ResultSet rsCategoria = stmtCategoria.executeQuery();
            if (!rsCategoria.next()) throw new SQLException("Categoria não encontrada.");

            // 2. SELECT no local
            PreparedStatement stmtLocal = connection.prepareStatement(
                    "SELECT * FROM local_transacao WHERE id = ?"
            );
            stmtLocal.setInt(1, transacao.getIdLocal());
            ResultSet rsLocal = stmtLocal.executeQuery();
            if (!rsLocal.next()) throw new SQLException("Local não encontrado.");

            // 3. INSERT na transacao
            PreparedStatement stmtInsert = connection.prepareStatement(
                    "INSERT INTO transacao (id_categoria, id_local, valor, data) VALUES (?, ?, ?, ?)"
            );
            stmtInsert.setInt(1, transacao.getIdCategoria());
            stmtInsert.setInt(2, transacao.getIdLocal());
            stmtInsert.setBigDecimal(3, transacao.getValor());
            stmtInsert.setTimestamp(4, Timestamp.valueOf(transacao.getData()));
            stmtInsert.executeUpdate();

            // 4. UPDATE no orçamento
            String mesAno = transacao.getData().toLocalDate().toString().substring(0, 7); // yyyy-MM
            PreparedStatement stmtUpdateOrcamento = connection.prepareStatement(
                    "UPDATE orcamento SET valor_utilizado = valor_utilizado + ? " +
                    "WHERE id_categoria = ? AND mes_ano = ?"
            );
            stmtUpdateOrcamento.setBigDecimal(1, transacao.getValor());
            stmtUpdateOrcamento.setInt(2, transacao.getIdCategoria());
            stmtUpdateOrcamento.setString(3, mesAno);
            stmtUpdateOrcamento.executeUpdate();

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

        String sql = 
        "SELECT t.id, t.id_categoria, t.id_local, t.valor, t.data, " +
        "       c.nome AS nome_categoria, " +
        "       l.nome AS nome_local " +
        "FROM transacao t " +
        "JOIN categoria c ON t.id_categoria = c.id " +
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

          // 1. SELECT da transação para pegar os dados
          String sqlSelect = "SELECT * FROM transacao WHERE id = ?";
          PreparedStatement stmtSelect = connection.prepareStatement(sqlSelect);
          stmtSelect.setInt(1, idTransacao);
          ResultSet rs = stmtSelect.executeQuery();

          if (!rs.next()) throw new SQLException("Transação não encontrada.");

          int idCategoria = rs.getInt("id_categoria");
          BigDecimal valor = rs.getBigDecimal("valor");
          LocalDate data = rs.getTimestamp("data").toLocalDateTime().toLocalDate();
          String mesAno = data.toString().substring(0, 7); // yyyy-MM

          // 2. DELETE da transação
          String sqlDelete = "DELETE FROM transacao WHERE id = ?";
          PreparedStatement stmtDelete = connection.prepareStatement(sqlDelete);
          stmtDelete.setInt(1, idTransacao);
          stmtDelete.executeUpdate();

          // 3. UPDATE no orçamento (reverte valor)
          String sqlUpdate = "UPDATE orcamento SET valor_utilizado = valor_utilizado - ? " +
                             "WHERE id_categoria = ? AND mes_ano = ?";
          PreparedStatement stmtUpdate = connection.prepareStatement(sqlUpdate);
          stmtUpdate.setBigDecimal(1, valor);
          stmtUpdate.setInt(2, idCategoria);
          stmtUpdate.setString(3, mesAno);
          stmtUpdate.executeUpdate();
          
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

}
