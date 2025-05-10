// @author lorrayne
package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.dao.TransacaoDAO;
import model.domain.Transacao;

import java.io.IOException;
import java.util.List;
import javafx.scene.Parent;

public class ProcessoTransacaoController {

    @FXML 
    private TableView<Transacao> tabelaTransacoes;
    
    @FXML 
    private TableColumn<Transacao, String> colData;
    
    @FXML 
    private TableColumn<Transacao, String> colCategoria;
    
    @FXML 
    private TableColumn<Transacao, String> colLocal;
    
    @FXML 
    private TableColumn<Transacao, String> colFormaPagamento;
    
    @FXML 
    private TableColumn<Transacao, String> colValor;

    private TransacaoDAO transacaoDAO = new TransacaoDAO();
    private ObservableList<Transacao> listaTransacoes;

    private int idUsuarioLogado;
    
    private VBoxMainController mainController;

    public void setMainController(VBoxMainController controller) {
        this.mainController = controller;
    }

    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }

    @FXML
    public void initialize() {
        configurarTabela();
        carregarTransacoes();
    }

    private void configurarTabela() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("nomeLocal"));
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
    }

    private void carregarTransacoes() {
        List<Transacao> transacoes = transacaoDAO.listarTodos();
        listaTransacoes = FXCollections.observableArrayList(transacoes);
        tabelaTransacoes.setItems(listaTransacoes);
    }

    @FXML
    public void handleInserir() {
        abrirFormularioTransacao(null);
        carregarTransacoes();
    }

    @FXML
    public void handleEditar() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            abrirFormularioTransacao(selecionada);
            carregarTransacoes();
        } else {
            mostrarAlerta("Selecione uma transação para editar.");
        }
    }
    
    @FXML
    public void handleVoltar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VBoxMainView.fxml"));
            Parent mainView = loader.load();

            VBoxMainController controller = loader.getController();
            controller.setIdUsuarioLogado(idUsuarioLogado); // Mantém o usuário logado

            Stage stage = (Stage) tabelaTransacoes.getScene().getWindow();
            stage.setScene(new Scene(mainView));
            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleCancelar() {
        Transacao selecionada = tabelaTransacoes.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            Alert alerta = new Alert(Alert.AlertType.CONFIRMATION);
            alerta.setTitle("Cancelar Transação");
            alerta.setHeaderText(null);
            alerta.setContentText("Tem certeza que deseja cancelar esta transação?");
            if (alerta.showAndWait().get() == ButtonType.OK) {
                boolean sucesso = transacaoDAO.cancelarTransacao(selecionada.getIdTransacao());
                if (sucesso) {
                    mostrarInfo("Transação cancelada com sucesso.");
                    carregarTransacoes();
                } else {
                    mostrarAlerta("Erro ao cancelar transação.");
                }
            }
        } else {
            mostrarAlerta("Selecione uma transação para cancelar.");
        }
    }

    private void abrirFormularioTransacao(Transacao transacao) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/InserirProcessoTransacaoView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle(transacao == null ? "Inserir Transação" : "Editar Transação");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setScene(new Scene(page));

            InserirProcessoTransacaoController controller = loader.getController();
            controller.setIdUsuarioLogado(idUsuarioLogado);
            if (transacao != null) {
                controller.preencherFormulario(transacao);
            }

            dialogStage.showAndWait();

            // 👇 Atualiza saldo após salvar/editar
            if (mainController != null) {
                mainController.atualizarSaldo();
            }

            carregarTransacoes();
        } catch (IOException e) {
            e.printStackTrace();
        }
}


    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
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
}