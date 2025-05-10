// @author lorrayne

package controller;

import model.dao.CategoriaDAO;
import model.dao.TransacaoDAO;
import model.dao.UsuarioDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.domain.Categoria;
import model.domain.Transacao;
import model.database.Database;
import model.database.DatabaseFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InserirProcessoTransacaoController {

    @FXML 
    private ComboBox<Categoria> comboCategoria;
    
    @FXML 
    private ComboBox<String> comboLocal;
    
    @FXML 
    private ComboBox<String> comboFormaPagamento;
    
    @FXML 
    private TextField txtValor;
    
    @FXML 
    private DatePicker datePicker;

    private TransacaoDAO transacaoDAO = new TransacaoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private UsuarioDAO usuarioDAO = new UsuarioDAO();
    private Database database = DatabaseFactory.getDatabase("postgresql");
    private Connection connection = database.conectar();

    private Transacao transacaoEdicao = null;
    private int idUsuarioLogado;

    @FXML
    public void initialize() {
        categoriaDAO.setConnection(connection);
        usuarioDAO.setConnection(connection);
        carregarCategorias();
        carregarLocais();
        carregarFormasPagamento();
        datePicker.setValue(LocalDate.now());
    }

    private void carregarCategorias() {
        List<Categoria> categorias = categoriaDAO.listar();
        comboCategoria.setItems(FXCollections.observableArrayList(categorias));
    }

    private void carregarFormasPagamento() {
        ObservableList<String> formas = FXCollections.observableArrayList();
        String sql = "SELECT descricao FROM forma_pagamento ORDER BY descricao";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                formas.add(rs.getString("descricao"));
            }
            comboFormaPagamento.setItems(formas);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar formas de pagamento: " + e.getMessage());
        }
    }

    private void carregarLocais() {
        ObservableList<String> locais = FXCollections.observableArrayList();
        String sql = "SELECT nome FROM local_transacao ORDER BY nome";
        try (PreparedStatement stmt = connection.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                locais.add(rs.getString("nome"));
            }
            comboLocal.setItems(locais);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar locais: " + e.getMessage());
        }
    }

    public void preencherFormulario(Transacao transacao) {
        this.transacaoEdicao = transacao;
        Categoria categoria = categoriaDAO.buscar(transacao.getIdCategoria());
        comboCategoria.setValue(categoria);
        comboLocal.setValue(transacao.getNomeLocal());
        txtValor.setText(transacao.getValor().toString());
        datePicker.setValue(transacao.getData().toLocalDate());
        comboFormaPagamento.setValue(transacao.getFormaPagamento());
    }

    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }

    @FXML
    public void salvarTransacao(ActionEvent event) {
        try {
            Categoria categoria = comboCategoria.getValue();
            String nomeLocal = comboLocal.getValue();
            String formaPagamento = comboFormaPagamento.getValue();
            String valorStr = txtValor.getText().replace(",", ".");
            LocalDateTime data = datePicker.getValue().atStartOfDay();

            if (categoria == null || nomeLocal == null || formaPagamento == null || valorStr.isEmpty() || data == null) {
                mostrarAlerta("Todos os campos devem ser preenchidos.");
                return;
            }

            BigDecimal valor = new BigDecimal(valorStr);
            int idLocal = buscarIdLocalPorNome(nomeLocal);
            if (idLocal == -1) {
                mostrarAlerta("Local não encontrado no banco.");
                return;
            }

            Transacao transacao = new Transacao();
            transacao.setIdCategoria(categoria.getIdCategoria());
            transacao.setIdLocal(idLocal);
            transacao.setValor(valor);
            transacao.setData(data);
            transacao.setFormaPagamento(formaPagamento);
            transacao.setIdUsuario(idUsuarioLogado);

            if (categoria.getTipo().equalsIgnoreCase("despesa")
                && (formaPagamento.equalsIgnoreCase("avista") ||
                    formaPagamento.equalsIgnoreCase("pix") ||
                    formaPagamento.equalsIgnoreCase("transferência"))) {

                BigDecimal saldo = usuarioDAO.buscarSaldoPorId(idUsuarioLogado);
                if (saldo == null || saldo.compareTo(valor) < 0) {
                    mostrarAlerta("Você não possui saldo suficiente para essa transação.");
                    return;
                }
            }

            if ("despesa".equalsIgnoreCase(categoria.getTipo()) && transacaoDAO.ultrapassaOrcamento(transacao)) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Orçamento estourado");
                alert.setHeaderText("Você ultrapassou o limite do orçamento para essa categoria.");
                alert.setContentText("Deseja continuar mesmo assim?");
                if (alert.showAndWait().orElse(ButtonType.CANCEL) != ButtonType.OK) {
                    return;
                }
            }

            boolean sucesso;
            if (transacaoEdicao != null) {
                transacao.setIdTransacao(transacaoEdicao.getIdTransacao());
                transacao.setIdUsuario(transacaoEdicao.getIdUsuario());
                sucesso = transacaoDAO.atualizarTransacao(transacao);
            } else {
                sucesso = transacaoDAO.registrarTransacao(transacao);
            }

            if (sucesso) {
                mostrarInfo("Transação registrada com sucesso!");
                fecharJanela();
            } else {
                mostrarAlerta("Erro ao registrar transação.");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro inesperado: " + e.getMessage());
        }
    }

    private int buscarIdLocalPorNome(String nome) {
        String sql = "SELECT id FROM local_transacao WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            mostrarAlerta("Erro ao buscar local: " + e.getMessage());
        }
        return -1;
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Atenção");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Informação");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    @FXML
    public void fecharJanela() {
        Stage stage = (Stage) comboCategoria.getScene().getWindow();
        stage.close();
    }
}
