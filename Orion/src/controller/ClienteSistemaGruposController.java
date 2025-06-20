package controller;

import sockets.thread.ContadorGrupo;
import sockets.thread.LogGrupo;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClienteSistemaGruposController {

    @FXML
    private TableView<ContadorGrupo> tableViewGeral;
    @FXML
    private TableColumn<ContadorGrupo, Integer> tableColumnPosicao;
    @FXML
    private TableColumn<ContadorGrupo, String> tableColumnGrupo;
    @FXML
    private TableColumn<ContadorGrupo, Integer> tableColumnUtilizacoes;
    @FXML
    private TextArea textAreaLog;

    private final int idGrupo = 1;
    private final String servidor = "34.41.27.130";
    private final int porta = 12345;

    @FXML
    public void initialize() {
        // Configura colunas da tabela
        tableColumnPosicao.setCellValueFactory(new PropertyValueFactory<>("idGrupo"));
        tableColumnGrupo.setCellValueFactory(new PropertyValueFactory<>("nomeGrupo"));
        tableColumnUtilizacoes.setCellValueFactory(new PropertyValueFactory<>("quantidadeUtilizacoes"));

        // Executa a conexão em thread separada
        new Thread(this::conectarComServidor).start();
    }

    private void conectarComServidor() {
        try (Socket clienteSocket = new Socket(servidor, porta)) {
            System.out.println("✅ Conectado ao servidor: " + servidor + ":" + porta);

            ObjectOutputStream saida = new ObjectOutputStream(clienteSocket.getOutputStream());
            saida.writeObject(idGrupo);

            ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());

            @SuppressWarnings("unchecked")
            List<ContadorGrupo> ranking = (List<ContadorGrupo>) entrada.readObject();

            @SuppressWarnings("unchecked")
            List<LogGrupo> logs = (List<LogGrupo>) entrada.readObject();

            // Atualiza interface na thread do JavaFX
            Platform.runLater(() -> {
                tableViewGeral.getItems().setAll(ranking);

                StringBuilder sb = new StringBuilder();
                for (LogGrupo log : logs) {
                    sb.append("Acesso em: ").append(log.getTimestamp()).append("\n");
                }
                textAreaLog.setText(sb.toString());
            });

        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> textAreaLog.setText("Erro ao conectar: " + e.getMessage()));
        }
    }
}
