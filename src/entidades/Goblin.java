package entidades;

import java.util.Random;
import enums.ResultadoAtaque;
import jogo.Log;

public class Goblin extends Monstro {
    private int dardos;
    private Log log;

    public Goblin(String nome, int hp, int ataque, int defesa, int destreza, int velocidade, int dardos, Log log) {
        super(nome, hp, ataque, defesa, destreza, velocidade, log);
        this.dardos = dardos;
        this.log = log;
    }

    public int getDardos() {
        return dardos;
    }

    public void setDardos(int dardos) {
        this.dardos = dardos;
    }

    @Override
    public ResultadoAtaque realizarAtaque(Jogador alvo) {
        Random random = new Random();

        // Se não tiver dardos, o Goblin foge
        if (this.getDardos() <= 0) {
            log.adicionarLog(this.getNome() + " está sem dardos e tenta fugir!");
            return ResultadoAtaque.ERROU;
        }

        // Reduz o número de dardos
        this.setDardos(this.getDardos() - 1);

        // Nova fórmula de chance de acerto
        double chanceDeAcerto = (double) this.getDestreza() / (this.getDestreza() + alvo.getVelocidade());

        if (random.nextDouble() > chanceDeAcerto) {
            log.logAtaque(this.getNome(), alvo.getNome(), "ERROU", 0);
            log.logEstadoJogador(this);
            return ResultadoAtaque.ERROU;
        }

        // Cálculo do dano base
        int dano = Math.max(1, this.getAtaque() - alvo.getDefesa()); // Garante pelo menos 1 de dano
        boolean ataqueCritico = random.nextInt(100) < 10; // 10% de chance de crítico

        // Se for ataque crítico, dobra o dano
        if (ataqueCritico) {
            dano *= 2;
        }

        // Aplica dano ao alvo
        alvo.setHp(alvo.getHp() - dano);

        // Registra um único log de ataque
        log.logAtaque(this.getNome(), alvo.getNome(), ataqueCritico ? "ATAQUE CRÍTICO!" : "ATAQUE NORMAL", dano);

        // Registra o estado atualizado do Goblin e do alvo no log
        log.logEstadoJogador(this);
        log.logEstadoJogador(alvo);

        return ataqueCritico ? ResultadoAtaque.CRITICAL_HIT : ResultadoAtaque.ACERTOU;
    }

}
