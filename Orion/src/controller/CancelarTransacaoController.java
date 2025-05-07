package controller;

// @author lorrayne
import dao.TransacaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Transacao;

import java.util.List;

public class CancelarTransacaoController {

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, Integer> colId;
    @FXML private TableColumn<Transacao, String> colData;
    @FXML private TableColumn<Transacao, Integer> colCategoria;
    @FXML private TableColumn<Transacao, Integer> colLocal;
    @FXML private TableColumn<Transacao, String> colValor;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private ObservableList<Transacao> listaTransacoes;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarTransacoes();
    }

    private void configurarTabela() {
        colId.setCellValueFactory(cellData -> cellData.getValue().idProperty().asObject());
        colData.setCellValueFactory(cellData -> cellData.getValue().dataProperty().asString());
        colCategoria.setCellValueFactory(cellData -> cellData.getValue().idCategoriaProperty().asObject());
        colLocal.setCellValueFactory(cellData -> cellData.getValue().idLocalProperty().asObject());
        colValor.setCellValueFactory(cellData -> cellData.getValue().valorProperty().asString());
    }

    private void carregarTransacoes() {
        try {
            List<Transacao> transacoes = transacaoDAO.listarTodos();
            listaTransacoes = FXCollections.observableArrayList(transacoes);
            tabelaTransacoes.setItems(listaTransacoes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void cancelarTransacao() {
        Transacao transacaoSelecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();

        if (transacaoSelecionada == null) {
            mostrarAlerta("Selecione uma transação para cancelar.");
            return;
        }

        boolean sucesso = transacaoDAO.cancelarTransacao(transacaoSelecionada.getId());

        if (sucesso) {
            mostrarInfo("Transação cancelada com sucesso.");
            carregarTransacoes();
        } else {
            mostrarAlerta("Erro ao cancelar transação.");
        }
    }

    @FXML
    public void fecharJanela() {
        Stage stage = (Stage) tabelaTransacoes.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

    private void mostrarInfo(String msg) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Sucesso");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}
