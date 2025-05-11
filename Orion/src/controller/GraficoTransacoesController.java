package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import model.dao.TransacaoDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class GraficoTransacoesController {

    @FXML
    private BarChart<String, Number> barChart;
    @FXML
    private CategoryAxis eixoX;
    @FXML
    private NumberAxis eixoY;
    @FXML
    private DatePicker dataInicio;
    @FXML
    private DatePicker dataFim;
    @FXML
    private ComboBox<String> comboAgrupamento;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private int idUsuarioLogado;

    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }
    
    @FXML
    public void initialize() {
        if (comboAgrupamento != null) {
            comboAgrupamento.setItems(FXCollections.observableArrayList(
                    "Dia", "Semana", "Mês", "Trimestre", "Semestre", "Ano"));
            comboAgrupamento.setValue("Mês");
        }

        if (barChart != null && eixoX != null && eixoY != null) {
            barChart.setTitle("Comparativo de Receita e Despesa");
            eixoX.setLabel("Período");
            eixoY.setLabel("Valor");
        }
    }


    @FXML
    public void handleGerarGrafico() {
        LocalDate inicio = dataInicio.getValue();
        LocalDate fim = dataFim.getValue();
        String agrupamento = comboAgrupamento.getValue();

        if (inicio == null || fim == null || agrupamento == null)
            return;

        Map<String, BigDecimal[]> resultados = transacaoDAO.getTotaisPorPeriodo(inicio, fim, agrupamento, "todos",
                idUsuarioLogado);

        XYChart.Series<String, Number> serieReceita = new XYChart.Series<>();
        serieReceita.setName("Receita");

        XYChart.Series<String, Number> serieDespesa = new XYChart.Series<>();
        serieDespesa.setName("Despesa");

        resultados.forEach((periodo, valores) -> {
            serieReceita.getData().add(new XYChart.Data<>(periodo, valores[0]));
            serieDespesa.getData().add(new XYChart.Data<>(periodo, valores[1]));
        });

        barChart.getData().clear();
        barChart.getData().addAll(serieReceita, serieDespesa);
    }
}
