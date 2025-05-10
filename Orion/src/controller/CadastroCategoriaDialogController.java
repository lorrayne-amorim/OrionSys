package controller;

// @author lorrayne

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.domain.Categoria;

public class CadastroCategoriaDialogController {

    @FXML 
    private TextField txtNome;
    
    @FXML 
    private ComboBox<String> comboTipo;
    
    @FXML 
    private TextField txtDescricao;
    
    @FXML 
    private ComboBox<String> comboPrioridade;
    
    @FXML 
    private CheckBox checkRecorrente;

    private Stage dialogStage;
    private Categoria categoria;
    private boolean confirmado = false;

    @FXML
    private void initialize() {
        comboTipo.getItems().addAll("receita", "despesa");
        comboPrioridade.getItems().addAll("Alta", "Média", "Baixa");
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;

        txtNome.setText(categoria.getNome());
        comboTipo.setValue(categoria.getTipo());
        txtDescricao.setText(categoria.getDescricao());
        comboPrioridade.setValue(categoria.getPrioridade());
        checkRecorrente.setSelected(categoria.getRecorrente());
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    @FXML
    private void handleConfirmar() {
        if (txtNome.getText().isEmpty() || comboTipo.getValue() == null || comboPrioridade.getValue() == null) {
            Alert alerta = new Alert(Alert.AlertType.WARNING);
            alerta.setTitle("Campos obrigatórios");
            alerta.setHeaderText("Preencha nome, tipo e prioridade.");
            alerta.showAndWait();
            return;
        }

        categoria.setNome(txtNome.getText());
        categoria.setTipo(comboTipo.getValue());
        categoria.setDescricao(txtDescricao.getText());
        categoria.setPrioridade(comboPrioridade.getValue());
        categoria.setRecorrente(checkRecorrente.isSelected());

        confirmado = true;
        dialogStage.close();
    }

    @FXML
    private void handleCancelar() {
        dialogStage.close();
    }
}
