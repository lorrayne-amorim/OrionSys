package controller;

// @author lorrayne 

import model.dao.CategoriaDAO;
import model.database.Database;
import model.database.DatabaseFactory;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.domain.Categoria;
import java.sql.Connection;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import javafx.fxml.Initializable;
import java.util.ResourceBundle;
import java.net.URL;
import java.util.logging.Logger;

public class CadastroCategoriaController implements Initializable {

    @FXML private TableView<Categoria> tableViewCategorias;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private TableColumn<Categoria, String> colTipo;

    @FXML private Label labelNome;
    @FXML private Label labelTipo;
    @FXML private Label labelDescricao;
    @FXML private Label labelPrioridade;
    @FXML private Label labelRecorrente;

    private CategoriaDAO categoriaDAO = new CategoriaDAO();
    private ObservableList<Categoria> listaCategorias;
    
    // Atributos para manipulação de Banco de Dados
    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        categoriaDAO.setConnection(connection);
        carregarTabela();

        colNome.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getNome()));
        colTipo.setCellValueFactory(cellData -> new javafx.beans.property.SimpleStringProperty(cellData.getValue().getTipo()));

        tableViewCategorias.getSelectionModel().selectedItemProperty().addListener(
            (obs, oldSelection, newSelection) -> mostrarDetalhesCategoria(newSelection)
        );
    }

    private void carregarTabela() {
        try {
            List<Categoria> categorias = categoriaDAO.listar();
            listaCategorias = FXCollections.observableArrayList(categorias);
            tableViewCategorias.setItems(listaCategorias);
        } catch (Exception e) {
            Logger.getLogger(CadastroCategoriaController.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    private void mostrarDetalhesCategoria(Categoria categoria) {
        if (categoria != null) {
            labelNome.setText(categoria.getNome());
            labelTipo.setText(categoria.getTipo());
            labelDescricao.setText(categoria.getDescricao());
            labelPrioridade.setText(categoria.getPrioridade());
            labelRecorrente.setText(categoria.getRecorrente() ? "Sim" : "Não");
        } else {
            labelNome.setText("");
            labelTipo.setText("");
            labelDescricao.setText("");
            labelPrioridade.setText("");
            labelRecorrente.setText("");
        }
    }

    @FXML
    public void handleInserir() {
        Categoria nova = new Categoria();
        boolean confirmado = abrirDialogCategoria(nova);
        if (confirmado) {
            if (categoriaDAO.inserir(nova)) {
                carregarTabela();
            }
        }
    }

    @FXML
    public void handleAlterar() {
        Categoria selecionada = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            boolean confirmado = abrirDialogCategoria(selecionada);
            if (confirmado) {
                categoriaDAO.alterar(selecionada);
                carregarTabela();
            }
        } else {
            mostrarAlerta("Seleção necessária", "Por favor, selecione uma categoria para editar.");
        }
    }

    @FXML
    public void handleRemover() {
        Categoria selecionada = tableViewCategorias.getSelectionModel().getSelectedItem();
        if (selecionada != null) {
            categoriaDAO.remover(selecionada);
            carregarTabela();
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
