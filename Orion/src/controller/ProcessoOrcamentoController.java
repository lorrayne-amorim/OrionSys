package controller;

// @author Julia

import model.dao.OrcamentoDAO;
import model.dao.CategoriaDAO;
import model.dao.FormaPagamentoDAO;
import model.dao.TipoOrcamentoDAO;
import model.dao.StatusOrcamentoDAO;
import model.database.Database;
import model.database.DatabaseFactory;
import model.domain.Categoria;
import model.domain.FormaPagamento;
import model.domain.TipoOrcamento;
import model.domain.StatusOrcamento;
import model.domain.Orcamento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import java.sql.Connection;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
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

    @FXML private ComboBox<FormaPagamento> comboFormaPagamento;
    @FXML private ComboBox<TipoOrcamento> comboTipoOrcamento;
    @FXML private ComboBox<StatusOrcamento> comboStatusOrcamento;

    private OrcamentoDAO orcamentoDAO = new OrcamentoDAO();
    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
    private TipoOrcamentoDAO tipoOrcamentoDAO = new TipoOrcamentoDAO();
    private StatusOrcamentoDAO statusOrcamentoDAO = new StatusOrcamentoDAO();
    private ObservableList<Orcamento> listaOrcamentos;

    private Database database = DatabaseFactory.getDatabase("postgresql");
    private Connection connection = database.conectar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orcamentoDAO.setConnection(connection);
        categoriaDAO.setConnection(connection);
        formaPagamentoDAO.setConnection(connection);
        tipoOrcamentoDAO.setConnection(connection);
        statusOrcamentoDAO.setConnection(connection);
        
        carregarTabela();
        carregarComboBoxes();

        colTitulo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTitulo()));
        colCategoria.setCellValueFactory(cellData -> {
            Categoria categoria = categoriaDAO.buscar(cellData.getValue().getIdCategoria());
            return new javafx.beans.property.SimpleStringProperty(categoria != null ? categoria.getNome() : "Desconhecida");
        });

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

    private void carregarComboBoxes() {
        List<FormaPagamento> formasPagamento = formaPagamentoDAO.listarTodos();
        comboFormaPagamento.getItems().addAll(formasPagamento);

        List<TipoOrcamento> tiposOrcamento = tipoOrcamentoDAO.listarTodos();
        comboTipoOrcamento.getItems().addAll(tiposOrcamento);

        List<StatusOrcamento> statusOrcamentos = statusOrcamentoDAO.listarTodos();
        comboStatusOrcamento.getItems().addAll(statusOrcamentos);
    }

    private void mostrarDetalhesOrcamento(Orcamento orcamento) {
        if (orcamento != null) {
            Categoria categoria = categoriaDAO.buscar(orcamento.getIdCategoria());
            labelTitulo.setText(orcamento.getTitulo());
            labelCategoria.setText(categoria != null ? categoria.getNome() : "Desconhecida");
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
