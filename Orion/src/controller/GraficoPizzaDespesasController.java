package controller;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.DatePicker;
import model.dao.TransacaoDAO;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Map;

public class GraficoPizzaDespesasController {

    @FXML private DatePicker dataInicio;
    @FXML private DatePicker dataFim;
    @FXML private PieChart pieChart;

    private final TransacaoDAO transacaoDAO = new TransacaoDAO();
    private int idUsuarioLogado;

    public void setIdUsuarioLogado(int idUsuario) {
        this.idUsuarioLogado = idUsuario;
    }

    @FXML
    public void handleGerarGrafico() {
        LocalDate inicio = dataInicio.getValue();
        LocalDate fim = dataFim.getValue();

        if (inicio == null || fim == null) return;

        Map<String, BigDecimal> despesas = transacaoDAO.getDespesasPorCategoria(inicio, fim, idUsuarioLogado);
        pieChart.getData().clear();

        for (Map.Entry<String, BigDecimal> entry : despesas.entrySet()) {
            pieChart.getData().add(new PieChart.Data(entry.getKey(), entry.getValue().doubleValue()));
        }

        pieChart.setTitle("Distribuição de Despesas por Categoria");
    }
}
