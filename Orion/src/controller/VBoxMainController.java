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
    private MenuItem menuItemGraficoTransacoesPorPeriodo;

    @FXML
    private MenuItem menuItemDespesasPorCategoria;

    @FXML
    private Label labelSaldo;

    @FXML
    private MenuItem menuItemRelatorioTransacoes;
    
    @FXML
    private MenuItem menuItemRelatorioDespesas;
    private int idUsuarioLogado;
    
    @FXML
    private MenuItem menuItemLog;

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
    public void handleMenuItemLog() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ClienteSistemaGruposFXML.fxml"));
        AnchorPane a = loader.load();
     
       anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemDespesasPorCategoria() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GraficoPizzaDespesaCategoriaView.fxml"));
        AnchorPane a = loader.load();

        GraficoPizzaDespesasController controller = loader.getController();
        controller.setIdUsuarioLogado(idUsuarioLogado);

        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemGraficoTransacoesPorPeriodo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/GraficoReceitaDespesaPeriodoView.fxml"));
        AnchorPane a = loader.load();

        GraficoTransacoesController controller = loader.getController();
        controller.setIdUsuarioLogado(idUsuarioLogado);

        anchorPane.getChildren().setAll(a);
    }
    
    // Relatórios
    @FXML
    public void handleMenuItemRelatorioTransacoesPeriodo() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RelatorioTransacoesPeriodoView.fxml"));
        AnchorPane a = loader.load();

        RelatorioTransacoesPeriodoController controller = loader.getController();
        controller.setIdUsuarioLogado(idUsuarioLogado);

        anchorPane.getChildren().setAll(a);
    }
    
    @FXML
    public void handleMenuItemRelatorioDespesasCategoria () throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/RelatorioDespesasCategoriaView.fxml"));
        AnchorPane a = loader.load();
        RelatorioTransacoesCategoriaController controller = loader.getController();
        controller.setIdUsuarioLogado(idUsuarioLogado);
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
