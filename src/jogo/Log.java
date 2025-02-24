package jogo;

import java.util.ArrayList;
import java.util.List;

import entidades.Jogador;

public class Log {
    private List<String> registros;

    public Log() {
        this.registros = new ArrayList<>();
    }

    public void adicionarLog(String mensagem) {
        registros.add(mensagem);
    }

    public void exibirLogs() {
        System.out.println("\n════════════════════════ LOG DO JOGO ════════════════════════\n");
        for (String log : registros) {
            System.out.println(log);
        }
        System.out.println("\n════════════════════════ FIM DO LOG ════════════════════════\n");
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
    }

    public void logEstadoJogador(Jogador jogador) {
        adicionarLog("📊  Estado de " + jogador.getNome() + " → Vida: " + jogador.getHp() + " | Status: " + jogador.getStatus());
    }
}
