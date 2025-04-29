package controller;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.collections.FXCollections;

import java.time.LocalDate;

public class MetaFinanceiraController {

    @FXML private TextField txtObjetivo;
    @FXML private ComboBox<String> comboCategoria;
    @FXML private TextField txtValorLimite;
    @FXML private DatePicker dataInicio;
    @FXML private DatePicker dataFim;

    @FXML
    public void initialize() {
        comboCategoria.setItems(FXCollections.observableArrayList("Alimentação", "Transporte", "Lazer", "Moradia", "Educação", "Outros"));
    }

    @FXML
    void salvarMeta() {
        if (validarCampos()) {
            String objetivo = txtObjetivo.getText();
            String categoria = comboCategoria.getValue();
            double valorLimite = Double.parseDouble(txtValorLimite.getText());
            LocalDate inicio = dataInicio.getValue();
            LocalDate fim = dataFim.getValue();

            // Aqui você salva a meta no banco
            exibirAlerta("Meta financeira salva com sucesso!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void editarMeta() {
        if (validarCampos()) {
            // Implemente a edição no banco
            exibirAlerta("Meta financeira editada com sucesso!", Alert.AlertType.INFORMATION);
        }
    }

    @FXML
    void excluirMeta() {
        // Implemente a exclusão no banco
        exibirAlerta("Meta financeira excluída com sucesso!", Alert.AlertType.INFORMATION);
    }

    private boolean validarCampos() {
        String erro = "";

        if (txtObjetivo.getText().isEmpty())
            erro += "Informe o objetivo da meta.\n";

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
