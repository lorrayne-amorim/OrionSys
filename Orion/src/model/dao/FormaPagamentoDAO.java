/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import model.database.Database;
import model.database.DatabaseFactory;
import model.domain.FormaPagamento;

/**
 *
 * @author lorra
 */
public class FormaPagamentoDAO {
    private Connection connection;

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

    public FormaPagamentoDAO() {
        Database database = DatabaseFactory.getDatabase("postgresql");
        this.connection = database.conectar();
    }

    public FormaPagamento buscar(int id) {
    String sql = "SELECT * FROM forma_pagamento WHERE id_formapagamento = ?";
    try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        stmt.setInt(1, id);
        ResultSet rs = stmt.executeQuery();
        if (rs.next()) {
            FormaPagamento forma = new FormaPagamento();
            forma.setIdFormaPagamento(rs.getInt("id_formapagamento"));
            forma.setDescricao(rs.getString("descricao"));
            return forma;
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

    
    public List<FormaPagamento> listar() {
        List<FormaPagamento> formaPagamentos = new ArrayList<>();
        String sql = "SELECT id_formapagamento, descricao FROM forma_pagamento ORDER BY descricao";

        try {
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet resultado = stmt.executeQuery();
            while (resultado.next()) {
                FormaPagamento formaPagamento = new FormaPagamento();
                formaPagamento.setIdFormaPagamento(resultado.getInt("id_formapagamento"));
                formaPagamento.setDescricao(resultado.getString("descricao"));

                formaPagamentos.add(formaPagamento);
            }
        } catch (SQLException ex) {
            Logger.getLogger(CategoriaDAO.class.getName()).log(Level.SEVERE, null, ex);
        }
        return formaPagamentos;
    }

}
