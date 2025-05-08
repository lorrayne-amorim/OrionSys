package controller;

import model.dao.CategoriaDAO;
import model.dao.TransacaoDAO;
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

public class InserirTransacaoController {

    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private ComboBox<String> comboLocal;
    @FXML private ComboBox<String> comboFormaPagamento;
    @FXML private TextField txtValor;
    @FXML private DatePicker datePicker;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    private Transacao transacaoEdicao = null;

    @FXML
    public void initialize() {
        categoriaDAO.setConnection(connection);
        carregarCategorias();
        carregarLocais();
        carregarFormasPagamento();
        datePicker.setValue(LocalDate.now());
    }

    private void carregarCategorias() {
        List<Categoria> categorias = categoriaDAO.listar();
        comboCategoria.setItems(FXCollections.observableArrayList(categorias));
    }

    private void carregarLocais() {
        ObservableList<String> locais = FXCollections.observableArrayList();
        String sql = "SELECT nome FROM local_transacao ORDER BY nome";
        try (
            PreparedStatement stmt = connection.prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()
        ) {
            while (rs.next()) {
                locais.add(rs.getString("nome"));
            }
            comboLocal.setItems(locais);
        } catch (SQLException e) {
            mostrarAlerta("Erro ao carregar locais: " + e.getMessage());
        }
    }

    private void carregarFormasPagamento() {
        comboFormaPagamento.setItems(FXCollections.observableArrayList("avista", "cartao", "pix", "outros"));
        comboFormaPagamento.setValue("avista");
    }

    public void preencherFormulario(Transacao transacao) {
        this.transacaoEdicao = transacao;
        Categoria categoria = categoriaDAO.buscarPorId(transacao.getIdCategoria());
        comboCategoria.setValue(categoria);
        comboLocal.setValue(transacao.getNomeLocal());
        txtValor.setText(transacao.getValor().toString());
        datePicker.setValue(transacao.getData().toLocalDate());
        comboFormaPagamento.setValue(transacao.getFormaPagamento());
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

            Transacao transacao = new Transacao(categoria.getIdCategoria(), idLocal, valor, data);
            transacao.setFormaPagamento(formaPagamento);

            if ("avista".equals(formaPagamento)) {
                BigDecimal saldoAvista = transacaoDAO.calcularSaldoAvista();
                if (transacao.getValor().compareTo(saldoAvista) > 0) {
                    mostrarAlerta("Saldo à vista insuficiente para esta transação.");
                    return;
                }
            }

            if (transacaoDAO.ultrapassaOrcamento(transacao)) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Aviso de orçamento");
                alert.setHeaderText(null);
                alert.setContentText("Esta transação ultrapassa o limite definido no orçamento da categoria.");
                alert.showAndWait();
            }

            boolean sucesso = transacaoDAO.registrarTransacao(transacao);

            if (sucesso) {
                mostrarInfo("Transação registrada com sucesso!");
                fecharJanela();
            } else {
                mostrarAlerta("Erro ao registrar transação.");
            }

        } catch (Exception e) {
            mostrarAlerta("Erro: " + e.getMessage());
        }
    }

    private int buscarIdLocalPorNome(String nome) {
        String sql = "SELECT id FROM local_transacao WHERE nome = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
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