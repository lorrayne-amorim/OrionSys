package controller;

// @author lorrayne

import dao.CategoriaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.Categoria;

import java.io.IOException;
import java.sql.SQLException;

public class CadastroCategoriaController {

    @FXML private TableView<Categoria> tableViewCategorias;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private TableColumn<Categoria, String> colTipo;

    @FXML private Label labelNome;
    @FXML private Label labelTipo;
    @FXML private Label labelDescricao;
    @FXML private Label labelAtivo;

    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private ObservableList<Categoria> listaCategorias;

    @FXML
    public void initialize() {
        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> mostrarDetalhesCategoria(newSelection)
        );

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            listaCategorias = FXCollections.observableArrayList(categoriaDAO.listarTodos());
            tableViewCategorias.setItems(listaCategorias);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void mostrarDetalhesCategoria(Categoria categoria) {
        if (categoria != null) {
            labelNome.setText(categoria.getNome());
            labelTipo.setText(categoria.getTipo());
            labelDescricao.setText(categoria.getDescricao());
            labelAtivo.setText(categoria.isAtivo() ? "Sim" : "Não");
        } else {
            labelNome.setText("");
            labelTipo.setText("");
            labelDescricao.setText("");
            labelAtivo.setText("");
        }
    }

    @FXML
    public void handleInserir() {
        Categoria nova = new Categoria();
        boolean confirmado = abrirDialogCategoria(nova);
        if (confirmado) {
            try {
                categoriaDAO.inserir(nova);
                carregarTabela();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @FXML
    public void handleAlterar() {
        Categoria selecionada = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            boolean confirmado = abrirDialogCategoria(selecionada);
            if (confirmado) {
                try {
                    categoriaDAO.atualizar(selecionada);
                    carregarTabela();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        } else {
            mostrarAlerta("Seleção necessária", "Por favor, selecione uma categoria para editar.");
        }
    }

    @FXML
    public void handleRemover() {
        Categoria selecionada = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            try {
                categoriaDAO.excluir(selecionada.getId());
                carregarTabela();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Seleção necessária", "Por favor, selecione uma categoria para remover.");
        }
    }

    private boolean abrirDialogCategoria(Categoria categoria) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CadastroCategoriaDialogView.fxml"));
            AnchorPane page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Categoria");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(tableViewCategorias.getScene().getWindow());
            dialogStage.setScene(new Scene(page));

            CadastroCategoriaDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategoria(categoria);

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
