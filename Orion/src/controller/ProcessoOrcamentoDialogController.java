package controller;

// julia

import model.dao.*;
import model.database.Database;
import model.database.DatabaseFactory;
import model.domain.*;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.*;

public class ProcessoOrcamentoDialogController {

    @FXML private TextField txtTitulo;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private ComboBox<String> comboTipoOrcamento;
    @FXML private ComboBox<String> comboStatus;
    @FXML private ComboBox<String> comboFormaPagamento;
    @FXML private TextField txtValorLimite;
    @FXML private DatePicker dataInicio;
    @FXML private DatePicker dataFim;

    private Stage dialogStage;
    private Orcamento orcamento;
    private boolean confirmado = false;

    private final Database database = DatabaseFactory.getDatabase("postgresql");
    private final Connection connection = database.conectar();

    private final CategoriaDAO categoriaDAO = new CategoriaDAO();
    private final TipoOrcamentoDAO tipoOrcamentoDAO = new TipoOrcamentoDAO();
    private final StatusOrcamentoDAO statusOrcamentoDAO = new StatusOrcamentoDAO();
    private final FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
    private final OrcamentoDAO orcamentoDAO = new OrcamentoDAO();

    private final Map<String, Integer> categoriaMap = new HashMap<>();
    private final Map<String, Integer> tipoMap = new HashMap<>();
    private final Map<String, Integer> statusMap = new HashMap<>();
    private final Map<String, Integer> formaMap = new HashMap<>();

    @FXML
    public void initialize() {
        categoriaDAO.setConnection(connection);
        tipoOrcamentoDAO.setConnection(connection);
        statusOrcamentoDAO.setConnection(connection);
        formaPagamentoDAO.setConnection(connection);
        orcamentoDAO.setConnection(connection);

        carregarCategorias();
        carregarTipos();
        carregarStatus();
        carregarFormasPagamento();
    }

    private void carregarCategorias() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        for (Categoria c : categoriaDAO.listar()) {
            nomes.add(c.getNome());
            categoriaMap.put(c.getNome(), c.getIdCategoria());
        }
        comboCategoria.setItems(nomes);
    }

    private void carregarTipos() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        for (TipoOrcamento tipo : tipoOrcamentoDAO.listar()) {
            nomes.add(tipo.getDescricao());
            tipoMap.put(tipo.getDescricao(), tipo.getIdTipo());
        }
        comboTipoOrcamento.setItems(nomes);
    }

    private void carregarStatus() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        for (StatusOrcamento s : statusOrcamentoDAO.listar()) {
            nomes.add(s.getDescricao());
            statusMap.put(s.getDescricao(), s.getIdStatus());
        }
        comboStatus.setItems(nomes);
    }

    private void carregarFormasPagamento() {
        ObservableList<String> nomes = FXCollections.observableArrayList();
        for (FormaPagamento f : formaPagamentoDAO.listar()) {
            nomes.add(f.getDescricao());
            formaMap.put(f.getDescricao(), f.getIdFormaPagamento());
        }
        comboFormaPagamento.setItems(nomes);
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setOrcamento(Orcamento orcamento) {
        this.orcamento = orcamento;

        txtTitulo.setText(orcamento.getTitulo());
        txtValorLimite.setText(String.valueOf(orcamento.getValorLimite()));
        dataInicio.setValue(orcamento.getDataInicio());
        dataFim.setValue(orcamento.getDataFim());

        categoriaMap.forEach((nome, id) -> {
            if (id == orcamento.getIdCategoria()) comboCategoria.setValue(nome);
        });
        tipoMap.forEach((nome, id) -> {
            if (id == orcamento.getIdTipoOrcamento()) comboTipoOrcamento.setValue(nome);
        });
        statusMap.forEach((nome, id) -> {
            if (id == orcamento.getIdStatusOrcamento()) comboStatus.setValue(nome);
        });
        formaMap.forEach((nome, id) -> {
            if (id == orcamento.getIdFormaPagamento()) comboFormaPagamento.setValue(nome);
        });
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void handleConfirmar() {
        if (validarCampos()) {
            int idCategoria = categoriaMap.get(comboCategoria.getValue());
            LocalDate inicio = dataInicio.getValue();
            LocalDate fim = dataFim.getValue();

            // Verificação de conflito
            boolean conflito = orcamentoDAO.existeConflitoDePeriodo(idCategoria, inicio, fim, orcamento.getId());
            if (conflito) {
                exibirAlerta("Já existe um orçamento para essa categoria com período sobreposto.", Alert.AlertType.WARNING);
                return;
            }

            orcamento.setTitulo(txtTitulo.getText());
            orcamento.setIdCategoria(idCategoria);
            orcamento.setValorLimite(Double.parseDouble(txtValorLimite.getText()));
            orcamento.setDataInicio(inicio);
            orcamento.setDataFim(fim);
            orcamento.setIdTipoOrcamento(tipoMap.get(comboTipoOrcamento.getValue()));
            orcamento.setIdStatusOrcamento(statusMap.get(comboStatus.getValue()));
            orcamento.setIdFormaPagamento(formaMap.get(comboFormaPagamento.getValue()));

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
            erro += "Informe o título do orçamento.\n";

        if (comboCategoria.getValue() == null)
            erro += "Selecione uma categoria.\n";

        if (comboTipoOrcamento.getValue() == null)
            erro += "Selecione um tipo de orçamento.\n";

        if (comboStatus.getValue() == null)
            erro += "Selecione um status.\n";

        if (comboFormaPagamento.getValue() == null)
            erro += "Selecione uma forma de pagamento.\n";

        if (txtValorLimite.getText().isEmpty() || !txtValorLimite.getText().matches("\\d+(\\.\\d+)?"))
            erro += "Informe um valor limite válido.\n";

        if (dataInicio.getValue() == null || dataFim.getValue() == null)
            erro += "Informe o período completo do orçamento.\n";

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
