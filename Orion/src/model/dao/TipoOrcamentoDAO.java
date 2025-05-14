package model.dao;

// LORRAYNE 
import model.domain.TipoOrcamento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TipoOrcamentoDAO {
    private Connection connection;

    public void setConnection(Connection connection) {
        this.connection = connection;
    }
   
    public TipoOrcamento buscar(int id) {
    String sql = "SELECT * FROM tipo_orcamento WHERE id_tipo = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            TipoOrcamento tipo = new TipoOrcamento();
            tipo.setIdTipo(rs.getInt("id_tipo"));
            tipo.setDescricao(rs.getString("descricao"));
            return tipo;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}
    
    public List<TipoOrcamento> listar() {
        List<TipoOrcamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM tipo_orcamento ORDER BY descricao";
        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                TipoOrcamento tipo = new TipoOrcamento();
                tipo.setIdTipo(rs.getInt("id_tipo"));
                tipo.setDescricao(rs.getString("descricao"));
                lista.add(tipo);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}
