package model.dao;
// @auhtor Julia
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.domain.StatusOrcamento;

public class StatusOrcamentoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public StatusOrcamentoDAO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<StatusOrcamento> listarTodos() {
        List<StatusOrcamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM status_orcamento";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new StatusOrcamento(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
