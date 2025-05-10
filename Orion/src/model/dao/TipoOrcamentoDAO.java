// @auhtor Julia
package model.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.domain.TipoOrcamento;

public class TipoOrcamentoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public TipoOrcamentoDAO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<TipoOrcamento> listarTodos() {
        List<TipoOrcamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_orcamento";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new TipoOrcamento(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
