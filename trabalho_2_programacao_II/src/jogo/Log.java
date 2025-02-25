package jogo;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import entidades.Jogador;

public class Log {
    private static final int MAX_PARTIDAS = 10; // Máximo de 10 partidas completas no arquivo
    private static final String FILE_NAME = "historico_batalhas.txt";
    private List<String> registrosAtuais; // Registros da partida atual
    private LinkedList<List<String>> partidas; // Lista de partidas completas
    private boolean salvarLog; // Define se os logs serão salvos no arquivo

    public Log(boolean salvarLog) {
        this.registrosAtuais = new ArrayList<>();
        this.partidas = new LinkedList<>();
        this.salvarLog = salvarLog;

        if (salvarLog) {
            carregarPartidasDoArquivo();
        }
    }

    public void adicionarLog(String mensagem) {
        registrosAtuais.add(mensagem);
    }

    public void exibirLogs() {
        System.out.println("\n════════════════════════ LOG DA PARTIDA ATUAL ════════════════════════\n");
        for (String log : registrosAtuais) {
            System.out.println(log);
        }
        System.out.println("\n════════════════════════ FIM DO LOG ════════════════════════\n");
    }

    private void carregarPartidasDoArquivo() {
        if (!salvarLog) return; // Se salvarLog for falso, não carregar o arquivo

        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            List<String> partida = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("FIM DA PARTIDA")) {
                    partidas.add(new ArrayList<>(partida)); // Salva a partida completa
                    partida.clear();
                } else {
                    partida.add(line);
                }
            }

            // Se houver mais de 10 partidas, remover as mais antigas
            while (partidas.size() > MAX_PARTIDAS) {
                partidas.pollFirst(); // Remove a partida mais antiga
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao carregar logs do arquivo: " + e.getMessage());
        }
    }

    public void salvarPartidaNoArquivo() {
        if (!salvarLog) return;

        partidas.add(new ArrayList<>(registrosAtuais));

        while (partidas.size() > MAX_PARTIDAS) {
            partidas.pollFirst();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (List<String> partida : partidas) {
                for (String log : partida) {
                    writer.write(log);
                    writer.newLine();
                }
                writer.write("════════════════════════ FIM DA PARTIDA ════════════════════════\n\n");
            }
        } catch (IOException e) {
            System.err.println("❌ Erro ao salvar logs: " + e.getMessage());
        }

        registrosAtuais.clear(); // 🚀 IMPORTANTE: Limpa os registros após salvar
    }


    public void logInicioJogo(String dificuldade, List<?> herois, List<?> monstros) {
        adicionarLog("═════════════════════════════════════════════════════════════");
        adicionarLog("🛡️  INÍCIO DO JOGO  🛡️ ");
        adicionarLog("Dificuldade: " + dificuldade);
        adicionarLog("Número de Heróis: " + herois.size());
        adicionarLog("Número de Monstros: " + monstros.size());
        adicionarLog("═════════════════════════════════════════════════════════════\n");
    }

    public void logOrdemTurno(int turno) {
        adicionarLog("\n📜  TURNO " + turno + " 📜");
        adicionarLog("═════════════════════════════════════════════════════════════");
    }

    public void logAtaque(String atacante, String alvo, String resultado, int dano) {
        adicionarLog("⚔️  " + atacante + " atacou " + alvo + " → " + resultado + " (Dano: " + dano + ")");
    }

    public void logDanoStatus(String jogador, String status, int dano) {
        adicionarLog("🔥  " + jogador + " sofreu " + dano + " de dano por " + status);
    }

    public void logMorte(String jogador) {
        adicionarLog("💀  " + jogador + " foi derrotado!");
    }

    public void logMudancaAlvo(String jogador, String novoAlvo, String motivo) {
        adicionarLog("🔄  " + jogador + " mudou seu alvo para " + novoAlvo + " devido a " + motivo);
    }

    public void logFimJogo(boolean heroisVenceram) {
        adicionarLog("\n═════════════════════════════════════════════════════════════");
        if (heroisVenceram) {
            adicionarLog("🎉  FIM DO JOGO: HERÓIS VENCERAM! 🎉");
        } else {
            adicionarLog("💀  FIM DO JOGO: MONSTROS VENCERAM! 💀");
        }
        adicionarLog("═════════════════════════════════════════════════════════════\n");

        salvarPartidaNoArquivo(); // Salva a partida ao final do jogo (se permitido)
    }

    public void logEstadoJogador(Jogador jogador) {
        adicionarLog("📊  Estado de " + jogador.getNome() + " → Vida: " + jogador.getHp() + " | Status: " + jogador.getStatus());
    }
}
