package model.dao;

import model.domain.StatusOrcamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class StatusOrcamentoDAO {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
    
    public StatusOrcamento buscar(int id) {
    String sql = "SELECT * FROM status_orcamento WHERE id_status = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            StatusOrcamento status = new StatusOrcamento();
            status.setIdStatus(rs.getInt("id_status"));
            status.setDescricao(rs.getString("descricao"));
            return status;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}


    public List<StatusOrcamento> listar() {
        List<StatusOrcamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM status_orcamento ORDER BY descricao";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                StatusOrcamento status = new StatusOrcamento();
                status.setIdStatus(rs.getInt("id_status"));
                status.setDescricao(rs.getString("descricao"));
                lista.add(status);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
