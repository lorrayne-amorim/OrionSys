package cliente;

import cliente.ContadorGrupo;
import cliente.LogGrupo;
import java.io.*;
import java.net.Socket;
import java.util.List;

public class Cliente {
    public static void main(String[] args) {
        int idGrupo = 1; // ID do grupo desejado (1-10)
        String servidor = "172.18.57.199"; // IP do servidor
        int porta = 12345;
        
        try (Socket clienteSocket = new Socket(servidor, porta)) {
            System.out.println("✅ Conectado ao servidor: " + servidor + ":" + porta);
            
            // PASSO 1: Enviar ID do grupo
            ObjectOutputStream saida = new ObjectOutputStream(clienteSocket.getOutputStream());
            saida.writeObject(idGrupo);
            System.out.println("📤 Enviado ID do grupo: " + idGrupo);
            
            // PASSO 2: Receber ranking completo
            ObjectInputStream entrada = new ObjectInputStream(clienteSocket.getInputStream());
            @SuppressWarnings("unchecked")
            List<ContadorGrupo> ranking = (List<ContadorGrupo>) entrada.readObject();
            System.out.println("📥 Recebido ranking com " + ranking.size() + " grupos");
            
            // PASSO 3: Receber logs do grupo
            @SuppressWarnings("unchecked")
            List<LogGrupo> logs = (List<LogGrupo>) entrada.readObject();
            System.out.println("📥 Recebidos " + logs.size() + " logs do grupo " + idGrupo);
            
            // PASSO 4: Processar dados recebidos
            processarRanking(ranking);
            processarLogs(logs);
            
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("❌ Erro na comunicação: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void processarRanking(List<ContadorGrupo> ranking) {
        System.out.println("\n=== RANKING DOS GRUPOS ===");
        for (ContadorGrupo grupo : ranking) {
            System.out.println("ID " + grupo.getIdGrupo() + 
                             " - " + grupo.getNomeGrupo() + 
                             ": " + grupo.getQuantidadeUtilizacoes() + " utilizações");
        }
    }
    
    private static void processarLogs(List<LogGrupo> logs) {
        System.out.println("\n=== LOGS DO GRUPO ===");
        if (logs.isEmpty()) {
            System.out.println("Nenhum log encontrado para este grupo.");
        } else {
            for (LogGrupo log : logs) {
                System.out.println("Acesso em: " + log.getTimestamp());
            }
        }
    }
}