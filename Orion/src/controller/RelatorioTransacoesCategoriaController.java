package controller;
// @author Lorrayne
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import model.dao.TransacaoDAO;
import model.domain.Transacao;
import java.math.BigDecimal;
import java.sql.Connection;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.scene.control.cell.PropertyValueFactory;
import model.database.DatabaseFactory;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.util.JRLoader;
import net.sf.jasperreports.view.JasperViewer;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


public class RelatorioTransacoesCategoriaController {

    @FXML 
    private ComboBox<String> categoriaNome;

   
    @FXML 
    private TableView<Transacao> tabelaTransacoes;
     
    @FXML 
    private TableColumn<Transacao, String> colData;
    
    @FXML 
    private TableColumn<Transacao, String> colCategoria;
    
    @FXML 
    private TableColumn<Transacao, BigDecimal> colValor;
    
    @FXML 
    private TableColumn<Transacao, String> colFormaPagamento;
    
    @FXML 
    private TableColumn<Transacao, String> colLocal;
    
    
    @FXML 
    private Button btnImprimir;
    
    @FXML 
    private Button btnBuscar;
    
    private List<Transacao> listTransacao;
    private ObservableList<Transacao> observableListTransacoes;


    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private int idUsuarioLogado;
    private final Connection connection = DatabaseFactory.getDatabase("postgresql").conectar();


    public void initialize() {
        carregarTableViewTransacoes();
        categoriaNome.setItems(FXCollections.observableArrayList(transacaoDAO.listarCategoriasDespesas()));

    }
    
    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }
    
    public void carregarTableViewTransacoes(){
        colData.setCellValueFactory(new PropertyValueFactory<> ("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<> ("nomeCategoria"));
        colValor.setCellValueFactory(new PropertyValueFactory<> ("valor"));
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<> ("formaPagamento"));
        colLocal.setCellValueFactory(new PropertyValueFactory<> ("nomeLocal"));
        
        listTransacao = transacaoDAO.listarTodos();
        
        observableListTransacoes = FXCollections.observableArrayList(listTransacao);
        tabelaTransacoes.setItems(observableListTransacoes); 
    }
    
    public void handleImprimir() throws JRException{
        URL url = getClass().getResource("/relatorios/RelatorioDespesasCategoria.jasper");
        JasperReport report = (JasperReport) JRLoader.loadObject(url);
        
        // Mapa de parâmetros
        Map<String, Object> parametros = new HashMap<>();
        if (categoriaNome.getValue() != null) {
        parametros.put("categoriaNome", categoriaNome.getValue());

    }
      
        JasperPrint print = JasperFillManager.fillReport(report, parametros, connection);
        JasperViewer viewer = new JasperViewer(print, false);
        viewer.setVisible(true);
        
    }

    @FXML
    public void handleBuscar() {
        colData.setCellValueFactory(new PropertyValueFactory<>("data"));
        colCategoria.setCellValueFactory(new PropertyValueFactory<>("nomeCategoria"));
        colValor.setCellValueFactory(new PropertyValueFactory<>("valor"));
        colFormaPagamento.setCellValueFactory(new PropertyValueFactory<>("formaPagamento"));
        colLocal.setCellValueFactory(new PropertyValueFactory<>("nomeLocal"));

        java.lang.String categoria_nome = (java.lang.String) categoriaNome.getValue();

      
        if (categoria_nome != null) {
            listTransacao = transacaoDAO.listarPorCategoria(categoria_nome);
        }else {
            listTransacao = transacaoDAO.listarTodos();
        }
        observableListTransacoes = FXCollections.observableArrayList(listTransacao);

        tabelaTransacoes.setItems(observableListTransacoes);
    }

    
}