package controller;

// @author Lorrayne

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import java.math.BigDecimal;
import javafx.scene.control.Label;
import model.dao.UsuarioDAO;

public class VBoxMainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroCategoria;
    
    @FXML
    private MenuItem menuItemTransacao;
    
    @FXML
    private MenuItem menuItemOrcamento;
    
    @FXML
    private MenuItem menuItemGraficosVendasPorMes;
    @FXML private Label labelSaldo;

    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutos;
    private int idUsuarioLogado;
    
    @FXML
    private AnchorPane anchorPane;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    @FXML
    public void handleMenuItemCadastroCategoria() throws IOException {
        AnchorPane a = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/CadastroCategoriaView.fxml"));
        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemProcessoOrcamento() throws IOException {
        AnchorPane b = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/ProcessoOrcamentoView.fxml"));
        anchorPane.getChildren().setAll(b);
    }
    
    public void atualizarSaldo() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        BigDecimal saldo = usuarioDAO.buscarSaldoPorId(idUsuarioLogado);
        if (saldo != null) {
            labelSaldo.setText(String.format("Saldo: R$ %.2f", saldo));
        } else {
            labelSaldo.setText("Saldo: erro");
        }
    }


    public void setIdUsuarioLogado(int idUsuario) {
     this.idUsuarioLogado = idUsuario;
     atualizarSaldo();
 }

    
    @FXML
    public void handleMenuItemProcessoTransacao() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ProcessoTransacaoView.fxml"));
        AnchorPane c = loader.load();

        ProcessoTransacaoController controller = loader.getController();
        controller.setIdUsuarioLogado(idUsuarioLogado);
        controller.setMainController(this); // Aqui é a conexão com VBoxMain

        anchorPane.getChildren().setAll(c);
    }

    

    
}