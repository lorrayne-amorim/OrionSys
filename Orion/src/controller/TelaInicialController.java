package controller;

// @author Lorrayne 

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.dao.UsuarioDAO;
import model.domain.Usuario;

public class TelaInicialController {

    @FXML 
    private TextField textFieldEmail;
    
    @FXML 
    private PasswordField textFieldSenha;
    
    @FXML 
    private Button btnEntrar;
    
    @FXML 
    private Button btnCadastrar;

    @FXML
    private void handleCadastrar(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/CadastroUsuarioView.fxml"));
            AnchorPane page = loader.load();
            Stage dialogStage = new Stage();
            dialogStage.setTitle("Cadastro de Usuário");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.setResizable(false);
            dialogStage.setScene(new Scene(page));
            dialogStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String email = textFieldEmail.getText();
        String senha = textFieldSenha.getText();

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        Usuario usuario = usuarioDAO.buscarPorEmailESenha(email, senha);

        if (usuario != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/VBoxMainView.fxml"));
                Parent root = loader.load();

                VBoxMainController controller = loader.getController();
                controller.setIdUsuarioLogado(usuario.getIdUsuario()); // 👈 ESSENCIAL

                Stage stage = (Stage) btnEntrar.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } catch (Exception e) {
                e.printStackTrace();
                mostrarAlerta("Erro ao carregar sistema.");
            }
        } else {
            mostrarAlerta("Email ou senha incorretos.");
        }
}

    private void mostrarAlerta(String msg) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Erro de Login");
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }

}