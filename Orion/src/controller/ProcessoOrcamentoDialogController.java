package controller;

import model.dao.CategoriaDAO;
import model.database.Database;
import model.database.DatabaseFactory;
import model.domain.Categoria;
import model.domain.Orcamento;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

public class ProcessoOrcamentoDialogController {

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtValorLimite;
    @FXML private DatePicker dataInicio;
    @FXML private DatePicker dataFim;

    private Stage dialogStage;
    private Orcamento orcamento;
    private boolean confirmado = false;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();
    private final CategoriaDAO categoriaDAO = new CategoriaDAO();

    @FXML
    public void initialize() {
        categoriaDAO.setConnection(connection);
        carregarCategorias();
    }

    private void carregarCategorias() {
        List<Categoria> categorias = categoriaDAO.listar();
        ObservableList<String> nomes = FXCollections.observableArrayList();
        for (Categoria c : categorias) {
            nomes.add(c.getNome());
        }
        comboCategoria.setItems(nomes);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;

        txtTitulo.setText(orcamento.getTitulo());
        comboCategoria.setValue(orcamento.getCategoria());
        txtValorLimite.setText(String.valueOf(orcamento.getValorLimite()));
        dataInicio.setValue(orcamento.getDataInicio());
        dataFim.setValue(orcamento.getDataFim());
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void handleConfirmar() {
        if (validarCampos()) {
            orcamento.setTitulo(txtTitulo.getText());
            orcamento.setCategoria(comboCategoria.getValue());
            orcamento.setValorLimite(Double.parseDouble(txtValorLimite.getText()));
            orcamento.setDataInicio(dataInicio.getValue());
            orcamento.setDataFim(dataFim.getValue());

            confirmado = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }

    private boolean validarCampos() {
        String erro = "";

        if (txtTitulo.getText().isEmpty())
            erro += "Informe o título do Orçamento.\n";

        if (comboCategoria.getValue() == null)
            erro += "Selecione uma categoria.\n";

        if (txtValorLimite.getText().isEmpty() || !txtValorLimite.getText().matches("\\d+(\\.\\d+)?"))
            erro += "Informe um valor limite válido.\n";

        if (dataInicio.getValue() == null || dataFim.getValue() == null)
            erro += "Informe o período completo da meta.\n";

        if (!erro.isEmpty()) {
            exibirAlerta(erro, Alert.AlertType.ERROR);
            return false;
        }
        return true;
    }

    private void exibirAlerta(String mensagem, Alert.AlertType tipo) {
        Alert alert = new Alert(tipo, mensagem, ButtonType.OK);
        alert.showAndWait();
    }
}
