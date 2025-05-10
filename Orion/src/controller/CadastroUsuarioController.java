package controller;

// @author Julia

import model.dao.UsuarioDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.domain.Usuario;

import java.sql.Date;

public class CadastroUsuarioController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private DatePicker datePickerNascimento;
    @FXML private TextField txtSaldo;
    @FXML private Button btnSalvar;

    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    private void handleSalvar() {
        if (validarCampos()) {
            Usuario usuario = new Usuario();
            usuario.setNome(txtNome.getText());
            usuario.setEmail(txtEmail.getText());
            usuario.setSenha(txtSenha.getText());
            usuario.setDataNascimento(Date.valueOf(datePickerNascimento.getValue()));
            usuario.setSaldo(Double.parseDouble(txtSaldo.getText()));

            usuarioDAO.inserir(usuario);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Sucesso");
            alert.setHeaderText(null);
            alert.setContentText("Usuário cadastrado com sucesso!");
            alert.showAndWait();

            ((Stage) btnSalvar.getScene().getWindow()).close();
        }
    }

    private boolean validarCampos() {
    String email = txtEmail.getText();

    if (txtNome.getText().isEmpty() || email.isEmpty()
            || txtSenha.getText().isEmpty() || txtSaldo.getText().isEmpty()
            || datePickerNascimento.getValue() == null) {
        mostrarAlerta("Preencha todos os campos.");
        return false;
    }

    if (!email.matches("^[\\w.-]+@[\\w.-]+\\.\\w{2,}$")) {
        mostrarAlerta("E-mail inválido. Digite um e-mail válido.");
        return false;
    }

    try {
        Double.parseDouble(txtSaldo.getText());
    } catch (NumberFormatException e) {
        mostrarAlerta("Saldo inválido. Digite um número.");
        return false;
    }

    return true;
}

    private void mostrarAlerta(String mensagem) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Validação");
        alert.setHeaderText(null);
        alert.setContentText(mensagem);
        alert.showAndWait();
    }


    @FXML
    private void handleCancelar() {
        ((Stage) btnSalvar.getScene().getWindow()).close();
    }
}