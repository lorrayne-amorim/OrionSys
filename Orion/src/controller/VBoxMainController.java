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

public class VBoxMainController implements Initializable {

    @FXML
    private MenuItem menuItemCadastroCategoria;
    
   
    
    //ESSES MENUS AINDA IREMOS MUDAR EM 
    @FXML
    private MenuItem menuItemTransacao;
    
    @FXML
    private MenuItem menuItemOrcamento;
    
    @FXML
    private MenuItem menuItemGraficosVendasPorMes;
    
    @FXML
    private MenuItem menuItemRelatoriosQuantidadeProdutos;

    // MUDAR OS MENUS DE CIMA
    
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
    
    @FXML
    public void handleMenuItemProcessoTransacao() throws IOException {
        AnchorPane c = (AnchorPane) FXMLLoader.load(getClass().getResource("/view/ProcessoTransacaoView.fxml"));
        anchorPane.getChildren().setAll(c);

    }

    
}
