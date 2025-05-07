package controller;

import dao.CategoriaDAO;
import dao.TransacaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Categoria;
import model.Transacao;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class InserirTransacaoController {

    @FXML private ComboBox<Categoria> comboCategoria;
    @FXML private ComboBox<String> comboLocal;
    @FXML private TextField txtValor;
    @FXML private DatePicker datePicker;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @FXML
    public void initialize() {
        carregarCategorias();
        carregarLocais();
        datePicker.setValue(LocalDate.now());
    }

    private void carregarCategorias() {
        try {
            List<Categoria> categorias = categoriaDAO.listarTodos();
            comboCategoria.setItems(FXCollections.observableArrayList(categorias));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void carregarLocais() {
        ObservableList<String> locais = FXCollections.observableArrayList(
                "Supermercado Vitoria", "Farmácia Saúde", "Posto Shell",
                "Shopping Sul", "Restaurante Bom Sabor", "Pix Recebido",
                "Transferência Bancária", "Salário Empresa XYZ"
        );
        comboLocal.setItems(locais);
    }

    @FXML
    public void salvarTransacao(ActionEvent event) {
        try {
            Categoria categoria = comboCategoria.getValue();
            String nomeLocal = comboLocal.getValue();
            BigDecimal valor = new BigDecimal(txtValor.getText().replace(",", "."));
            LocalDateTime data = datePicker.getValue().atStartOfDay();

            if (categoria == null || nomeLocal == null || valor == null || data == null) {
                mostrarAlerta("Todos os campos devem ser preenchidos.");
                return;
            }

            int idLocal = buscarIdLocalPorNome(nomeLocal);
            if (idLocal == -1) {
                mostrarAlerta("Local não encontrado no banco.");
                return;
            }

            Transacao transacao = new Transacao(categoria.getId(), idLocal, valor, data);
            boolean sucesso = transacaoDAO.registrarTransacao(transacao);

            if (sucesso) {
                mostrarInfo("Transação registrada com sucesso!");
                fecharJanela();
            } else {
                mostrarAlerta("Erro ao registrar transação.");
            }

        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Erro: " + e.getMessage());
        }
    }

    private int buscarIdLocalPorNome(String nome) {
        String sql = "SELECT id FROM local_transacao WHERE nome = ?";
        try (
            Connection conn = DriverManager.getConnection("jdbc:postgresql://127.0.0.1/orionsys", "postgres", "123");
            PreparedStatement stmt = conn.prepareStatement(sql)
        ) {
            stmt.setString(1, nome);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
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
