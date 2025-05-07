package controller;

// @author lorrayne

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Categoria;

public class CadastroCategoriaDialogController {

    @FXML private TextField txtNome;
    @FXML private ComboBox<String> comboTipo;
    @FXML private TextField txtDescricao;
    @FXML private CheckBox checkAtivo;

    private Stage dialogStage;
    private Categoria categoria;
    private boolean confirmado = false;

    @FXML
    private void initialize() {
        comboTipo.getItems().addAll("receita", "despesa");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;

        txtNome.setText(categoria.getNome());
        comboTipo.setValue(categoria.getTipo());
        txtDescricao.setText(categoria.getDescricao());
        checkAtivo.setSelected(categoria.isAtivo());
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void handleConfirmar() {
        if (txtNome.getText().isEmpty() || comboTipo.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos obrigatórios");
            alerta.setHeaderText("Preencha nome e tipo.");
            alerta.showAndWait();
            return;
        }

        categoria.setNome(txtNome.getText());
        categoria.setTipo(comboTipo.getValue());
        categoria.setDescricao(txtDescricao.getText());
        categoria.setAtivo(checkAtivo.isSelected());

        confirmado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }
} 
