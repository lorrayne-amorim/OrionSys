package controller;

import dao.CategoriaDAO;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.Categoria;

import java.sql.SQLException;

public class CategoriaController {

    @FXML private TextField txtNome;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtDescricao;
    @FXML private CheckBox checkAtivo;
    @FXML private TableView<Categoria> tabelaCategoria;
    @FXML private TableColumn<Categoria, String> colNome;
    @FXML private TableColumn<Categoria, String> colTipo;

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private ObservableList<Categoria> listaCategorias;
    private Categoria categoriaSelecionada = null;

    @FXML
    public void initialize() {
        comboTipo.setItems(FXCollections.observableArrayList("receita", "despesa"));

        colNome.setCellValueFactory(cellData -> cellData.getValue().nomeProperty());
        colTipo.setCellValueFactory(cellData -> cellData.getValue().tipoProperty());

        tabelaCategoria.getSelectionModel().selectedItemProperty().addListener(
            (observable, oldValue, newValue) -> selecionarCategoria(newValue)
        );

        carregarTabela();
    }

    private void carregarTabela() {
        try {
            listaCategorias = FXCollections.observableArrayList(categoriaDAO.listarTodos());
            tabelaCategoria.setItems(listaCategorias);
        } catch (SQLException e) {
            exibirAlertaErro("Erro ao carregar categorias: " + e.getMessage());
        }
    }

    @FXML
    public void salvarCategoria(ActionEvent event) {
        if (txtNome.getText().isEmpty() || comboTipo.getValue() == null) {
            exibirAlertaErro("Preencha todos os campos obrigatórios!");
            return;
        }

        try {
            if (categoriaSelecionada == null) { // Nova categoria
                Categoria c = new Categoria(
                    txtNome.getText(),
                    comboTipo.getValue(),
                    txtDescricao.getText(),
                    checkAtivo.isSelected()
                );
                categoriaDAO.inserir(c);
            } else { // Atualizar categoria existente
                categoriaSelecionada.setNome(txtNome.getText());
                categoriaSelecionada.setTipo(comboTipo.getValue());
                categoriaSelecionada.setDescricao(txtDescricao.getText());
                categoriaSelecionada.setAtivo(checkAtivo.isSelected());
                categoriaDAO.atualizar(categoriaSelecionada);
            }

            carregarTabela();
            limparCampos();

        } catch (SQLException e) {
            exibirAlertaErro("Erro ao salvar categoria: " + e.getMessage());
        }
    }

    @FXML
    public void removerCategoria(ActionEvent event) {
        Categoria c = tabelaCategoria.getSelectionModel().getSelectedItem();
        if (c == null) {
            exibirAlertaErro("Selecione uma categoria para remover!");
            return;
        }

        Alert confirmacao = new Alert(Alert.AlertType.CONFIRMATION, 
            "Deseja realmente remover a categoria: " + c.getNome() + "?", 
            ButtonType.YES, ButtonType.NO);

        confirmacao.showAndWait().ifPresent(resposta -> {
            if (resposta == ButtonType.YES) {
                try {
                    categoriaDAO.excluir(c.getId());
                    carregarTabela();
                    limparCampos();
                } catch (SQLException e) {
                    exibirAlertaErro("Erro ao remover categoria: " + e.getMessage());
                }
            }
        });
    }

    private void selecionarCategoria(Categoria categoria) {
        categoriaSelecionada = categoria;

        if (categoria != null) {
            txtNome.setText(categoria.getNome());
            comboTipo.setValue(categoria.getTipo());
            txtDescricao.setText(categoria.getDescricao());
            checkAtivo.setSelected(categoria.isAtivo());
        }
    }

    private void limparCampos() {
        txtNome.clear();
        comboTipo.setValue(null);
        txtDescricao.clear();
        checkAtivo.setSelected(false);
        categoriaSelecionada = null;
        tabelaCategoria.getSelectionModel().clearSelection();
    }

    private void exibirAlertaErro(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.ERROR, mensagem, ButtonType.OK);
        alert.showAndWait();
    }
}
