package controller;

import dao.TransacaoDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import model.Transacao;

import java.io.IOException;
import java.util.List;

public class TransacaoController {

    @FXML private TableView<Transacao> tabelaTransacoes;
    @FXML private TableColumn<Transacao, String> colData;
    @FXML private TableColumn<Transacao, String> colCategoria;
    @FXML private TableColumn<Transacao, String> colLocal;
    @FXML private TableColumn<Transacao, String> colValor;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private ObservableList<Transacao> listaTransacoes;

    @FXML
    public void initialize() {
        configurarTabela();
        carregarTransacoes();
    }

    private void configurarTabela() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("nomeLocal"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    private void carregarTransacoes() {
        try {
            List<Transacao> transacoes = transacaoDAO.listarTodos(); // você deve implementar esse método depois
            listaTransacoes = FXCollections.observableArrayList(transacoes);
            tabelaTransacoes.setItems(listaTransacoes);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void abrirInserirTransacao() {
        abrirJanelaModal("/view/InserirTransacaoView.fxml", "Nova Transação");
        carregarTransacoes(); // recarrega após fechar
    }

    @FXML
    public void abrirCancelarTransacao() {
        abrirJanelaModal("/view/CancelarTransacaoView.fxml", "Cancelar Transação");
        carregarTransacoes(); // recarrega após fechar
    }

    private void abrirJanelaModal(String caminhoFXML, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(caminhoFXML));
            Parent root = loader.load();
            Stage stage = new Stage();
            stage.setTitle(titulo);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setScene(new Scene(root));
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
