package model.dao;
// @auhtor Julia
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import model.domain.FormaPagamento;

public class FormaPagamentoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public FormaPagamentoDAO() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    public List<FormaPagamento> listarTodos() {
        List<FormaPagamento> lista = new ArrayList<>();
        String sql = "SELECT * FROM forma_pagamento";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                lista.add(new FormaPagamento(rs.getInt("id"), rs.getString("nome")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return lista;
    }
}

