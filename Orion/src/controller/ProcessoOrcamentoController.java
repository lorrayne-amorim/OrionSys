package controller;

// @author lorrayne

import model.dao.OrcamentoDAO;
import model.database.Database;
import model.database.DatabaseFactory;
import java.util.Optional;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.domain.Orcamento;
import java.sql.Connection;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.logging.Logger;

public class ProcessoOrcamentoController implements Initializable {

    @FXML private TableView<Orcamento> tableViewOrcamentos;
    @FXML private TableColumn<Orcamento, String> colTitulo;
    @FXML private TableColumn<Orcamento, String> colCategoria;

    @FXML private Label labelTitulo;
    @FXML private Label labelCategoria;
    @FXML private Label labelValorLimite;
    @FXML private Label labelDataInicio;
    @FXML private Label labelDataFim;

    private OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private ObservableList<Orcamento> listaOrcamentos;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orcamentoDAO.setConnection(connection);
        carregarTabela();

        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colCategoria.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getCategoria()));

        tableViewOrcamentos.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> mostrarDetalhesOrcamento(newSelection)
        );
    }

    private void carregarTabela() {
        try {
            List<Orcamento> orcamentos = orcamentoDAO.listar();
            listaOrcamentos = FXCollections.observableArrayList(orcamentos);
            tableViewOrcamentos.setItems(listaOrcamentos);
        } catch (Exception e) {
            Logger.getLogger(ProcessoOrcamentoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void mostrarDetalhesOrcamento(Orcamento orcamento) {
        if (orcamento != null) {
            labelTitulo.setText(orcamento.getTitulo());
            labelCategoria.setText(orcamento.getCategoria());
            labelValorLimite.setText(String.format("R$ %.2f", orcamento.getValorLimite()));
            labelDataInicio.setText(orcamento.getDataInicio().toString());
            labelDataFim.setText(orcamento.getDataFim().toString());
        } else {
            labelTitulo.setText("");
            labelCategoria.setText("");
            labelValorLimite.setText("");
            labelDataInicio.setText("");
            labelDataFim.setText("");
        }
    }

    @FXML
    public void handleInserir() {
        Orcamento novo = new Orcamento();
        boolean confirmado = abrirDialogOrcamento(novo);
        if (confirmado) {
            if (orcamentoDAO.inserir(novo)) {
                carregarTabela();
            }
        }
    }

    @FXML
    public void handleAlterar() {
        Orcamento selecionado = tableViewOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            boolean confirmado = abrirDialogOrcamento(selecionado);
            if (confirmado) {
                orcamentoDAO.alterar(selecionado);
                carregarTabela();
            }
        } else {
            mostrarAlerta("Seleção necessária", "Por favor, selecione um orçamento para editar.");
        }
    }

    @FXML
    public void handleRemover() {
        Orcamento selecionado = tableViewOrcamentos.getSelectionModel().getSelectedItem();
        if (selecionado != null) {
            Alert confirm = new Alert(Alert.AlertType.CONFIRMATION, "Deseja realmente excluir o orçamento selecionado?", ButtonType.YES, ButtonType.NO);
            Optional<ButtonType> result = confirm.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.YES) {
                orcamentoDAO.remover(selecionado);
                carregarTabela();
            }
        } else {
            mostrarAlerta("Seleção necessária", "Por favor, selecione um orçamento para remover.");
        }
    }

    private boolean abrirDialogOrcamento(Orcamento orcamento) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProcessoOrcamentoDialogView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Orçamento");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableViewOrcamentos.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            ProcessoOrcamentoDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setOrcamento(orcamento); 

            dialogStage.showAndWait();
            return controller.isConfirmado();
           
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void mostrarAlerta(String titulo, String conteudo) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(conteudo);
        alerta.showAndWait();
    }
}
