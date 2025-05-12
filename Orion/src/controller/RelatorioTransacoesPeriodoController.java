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
import model.database.Database;
import model.database.DatabaseFactory;

public class RelatorioTransacoesPeriodoController {

    @FXML 
    private DatePicker dataInicio;
    
    @FXML 
    private DatePicker dataFim;
    
    @FXML 
    private TableView<Transacao> tabelaTransacoes;
     
    @FXML 
    private TableColumn<Transacao, String> colData;
    
    @FXML 
    private TableColumn<Transacao, String> colCategoria;
    
    @FXML 
    private TableColumn<Transacao, String> colTipo;
    
    @FXML 
    private TableColumn<Transacao, BigDecimal> colValor;
    
    @FXML 
    private TableColumn<Transacao, String> colFormaPagamento;
    
    @FXML 
    private TableColumn<Transacao, String> colLocal;
    
    @FXML 
    private Label labelTotalReceitas;
    
    @FXML 
    private Label labelTotalDespesas;
    
    @FXML 
    private Label labelSaldo;
    
    @FXML 
    private Button btnImprimir;
    
    @FXML 
    private Button btnBuscar;
    
    private List<Transacao> listTransacao;
    private ObservableList<Transacao> observableListTransacoes;


    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private int idUsuarioLogado;
    

    public void initialize() {
        carregarTableViewTransacoes();
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
    
    public void handleImprimir(){
        
    }
    
    
}